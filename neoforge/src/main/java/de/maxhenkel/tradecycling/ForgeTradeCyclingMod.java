package de.maxhenkel.tradecycling;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.event.EventNetworkChannel;

@Mod(TradeCyclingMod.MODID)
public class ForgeTradeCyclingMod extends TradeCyclingMod {

    public static EventNetworkChannel CYCLE_TRADES_CHANNEL;

    private ForgeTradeCyclingClientMod clientMod;

    public ForgeTradeCyclingMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        if (FMLEnvironment.dist.isClient()) {
            clientMod = new ForgeTradeCyclingClientMod();
        }
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        init();
        registerPacket();
    }

    public void registerPacket() {
        CYCLE_TRADES_CHANNEL = NetworkRegistry.ChannelBuilder.named(CYCLE_TRADES_PACKET)
                .networkProtocolVersion(() -> NetworkRegistry.ACCEPTVANILLA)
                .clientAcceptedVersions(NetworkRegistry.acceptMissingOr(NetworkRegistry.ACCEPTVANILLA))
                .serverAcceptedVersions(NetworkRegistry.acceptMissingOr(NetworkRegistry.ACCEPTVANILLA))
                .eventNetworkChannel();
        CYCLE_TRADES_CHANNEL.addListener(event -> {
            if (event.getPayload() == null) {
                return;
            }
            NetworkEvent.Context context = event.getSource();
            if (context.getDirection().equals(PlayNetworkDirection.PLAY_TO_SERVER)) {
                context.enqueueWork(() -> onCycleTrades(context.getSender()));
            }
        });
    }

}
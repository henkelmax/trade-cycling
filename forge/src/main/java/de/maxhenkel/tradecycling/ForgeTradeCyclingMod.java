package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.net.CycleTradesPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;
import net.minecraftforge.network.NetworkDirection;

@Mod(TradeCyclingMod.MODID)
public class ForgeTradeCyclingMod extends TradeCyclingMod {

    public static EventNetworkChannel CYCLE_TRADES_CHANNEL;

    private ForgeTradeCyclingClientMod clientMod;

    public ForgeTradeCyclingMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            clientMod = new ForgeTradeCyclingClientMod();
        });
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        init();
        registerPacket();
    }

    public void registerPacket() {
        CYCLE_TRADES_CHANNEL = ChannelBuilder.named(CycleTradesPacket.CYCLE_TRADES.id())
                .networkProtocolVersion(0)
                .acceptedVersions((status, version) -> true)
                .optional()
                .eventNetworkChannel();
        CYCLE_TRADES_CHANNEL.addListener(event -> {
            CustomPayloadEvent.Context context = event.getSource();
            if (context.getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                context.enqueueWork(() -> onCycleTrades(context.getSender()));
            }
        });
    }

}
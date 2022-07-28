package de.maxhenkel.tradecycling;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;

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
        CYCLE_TRADES_CHANNEL = NetworkRegistry.newEventChannel(
                TradeCyclingMod.CYCLE_TRADES_PACKET,
                () -> NetworkRegistry.ACCEPTVANILLA,
                NetworkRegistry.ACCEPTVANILLA::equals,
                NetworkRegistry.ACCEPTVANILLA::equals
        );
        CYCLE_TRADES_CHANNEL.addListener(event -> {
            NetworkEvent.Context context = event.getSource().get();
            if (context.getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                context.enqueueWork(() -> onCycleTrades(context.getSender()));
            }
        });
    }

}
package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.net.CycleTradesPacket;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;

@Mod(TradeCyclingMod.MODID)
public class ForgeTradeCyclingMod extends TradeCyclingMod {

    public static EventNetworkChannel CYCLE_TRADES_CHANNEL;

    private ForgeTradeCyclingClientMod clientMod;

    public ForgeTradeCyclingMod(FMLJavaModLoadingContext context) {
        FMLCommonSetupEvent.getBus(context.getModBusGroup()).addListener(this::commonSetup);

        if (FMLEnvironment.dist.isClient()) {
            clientMod = new ForgeTradeCyclingClientMod(context);
        }
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
            if (context.isServerSide()) {
                context.enqueueWork(() -> onCycleTrades(context.getSender()));
            }
        });
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
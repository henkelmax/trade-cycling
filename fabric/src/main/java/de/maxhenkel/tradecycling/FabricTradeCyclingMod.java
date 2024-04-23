package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.net.CycleTradesPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricTradeCyclingMod extends TradeCyclingMod implements ModInitializer {

    @Override
    public void onInitialize() {
        registerPacket();
        init();
    }

    private void registerPacket() {
        PayloadTypeRegistry.playC2S().register(CycleTradesPacket.CYCLE_TRADES, CycleTradesPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(CycleTradesPacket.CYCLE_TRADES, (payload, context) -> context.player().server.execute(() -> onCycleTrades(context.player())));
    }

}

package de.maxhenkel.tradecycling;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricTradeCyclingMod extends TradeCyclingMod implements ModInitializer {

    @Override
    public void onInitialize() {
        registerPacket();
        init();
    }

    private void registerPacket() {
        ServerPlayNetworking.registerGlobalReceiver(TradeCyclingMod.CYCLE_TRADES_PACKET, (server, player, handler, buf, responseSender) -> onCycleTrades(player));
    }

}

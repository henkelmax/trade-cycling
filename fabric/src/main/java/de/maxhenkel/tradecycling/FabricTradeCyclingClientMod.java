package de.maxhenkel.tradecycling;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.tradecycling.config.FabricTradeCyclingClientConfig;
import de.maxhenkel.tradecycling.config.TradeCyclingClientConfig;
import de.maxhenkel.tradecycling.net.CycleTradesPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.loader.api.FabricLoader;

public class FabricTradeCyclingClientMod extends TradeCyclingClientMod implements ClientModInitializer {

    private static FabricTradeCyclingClientMod instance;

    public FabricTradeCyclingClientMod() {
        instance = this;
        CONFIG = createClientConfig();
    }

    @Override
    public void onInitializeClient() {
        clientInit();
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            onOpenScreen(screen, guiEventListener -> {
                Screens.getButtons(screen).add(guiEventListener);
            });
        });
    }

    @Override
    public void registerKeyBindings() {
        KeyBindingHelper.registerKeyBinding(CYCLE_TRADES_KEY);
    }

    @Override
    public void sendCycleTradesPacket() {
        ClientPlayNetworking.send(new CycleTradesPacket());
    }

    @Override
    public TradeCyclingClientConfig createClientConfig() {
        return ConfigBuilder.builder(FabricTradeCyclingClientConfig::new)
                .path(FabricLoader.getInstance().getConfigDir().resolve(TradeCyclingMod.MODID).resolve("trade_cycling.properties"))
                .build();
    }

    public static FabricTradeCyclingClientMod instance() {
        return instance;
    }

}

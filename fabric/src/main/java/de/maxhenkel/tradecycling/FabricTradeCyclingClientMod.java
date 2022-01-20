package de.maxhenkel.tradecycling;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.tradecycling.config.FabricTradeCyclingClientConfig;
import de.maxhenkel.tradecycling.config.TradeCyclingClientConfig;
import de.maxhenkel.tradecycling.mixin.ScreenAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;

public class FabricTradeCyclingClientMod extends TradeCyclingClientMod implements ClientModInitializer {

    private static FabricTradeCyclingClientMod instance;

    public FabricTradeCyclingClientMod() {
        instance = this;
    }

    @Override
    public void onInitializeClient() {
        clientInit();
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            ScreenAccessor screenAccessor = (ScreenAccessor) screen;
            onOpenScreen(screen, screenAccessor::invokeAddRenderableWidget);
        });
    }

    @Override
    public void registerKeyBindings() {
        KeyBindingHelper.registerKeyBinding(CYCLE_TRADES_KEY);
    }

    @Override
    public TradeCyclingClientConfig createClientConfig() {
        return ConfigBuilder.build(FabricLoader.getInstance().getConfigDir().resolve(TradeCyclingMod.MODID).resolve("trade_cycling.properties"), true, FabricTradeCyclingClientConfig::new);
    }

    public static FabricTradeCyclingClientMod instance() {
        return instance;
    }

}

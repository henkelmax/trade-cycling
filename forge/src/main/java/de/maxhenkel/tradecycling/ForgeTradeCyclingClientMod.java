package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.config.ForgeTradeCyclingClientConfig;
import de.maxhenkel.tradecycling.config.TradeCyclingClientConfig;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ForgeTradeCyclingClientMod extends TradeCyclingClientMod {

    @Override
    public void clientInit() {
        super.clientInit();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerKeyBindings() {
        ClientRegistry.registerKeyBinding(CYCLE_TRADES_KEY);
    }

    @Override
    public TradeCyclingClientConfig createClientConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ForgeTradeCyclingClientConfig config = new ForgeTradeCyclingClientConfig(builder);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build());
        return config;
    }

    @SubscribeEvent
    public void onInitScreen(ScreenEvent.InitScreenEvent.Post event) {
        onOpenScreen(event.getScreen(), event::addListener);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        onCycleKeyPressed(event.getKey(), event.getScanCode(), event.getAction());
    }

}

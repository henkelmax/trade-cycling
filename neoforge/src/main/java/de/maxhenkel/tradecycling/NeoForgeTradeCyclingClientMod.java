package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.config.NeoForgeTradeCyclingClientConfig;
import de.maxhenkel.tradecycling.config.TradeCyclingClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeoForgeTradeCyclingClientMod extends TradeCyclingClientMod {

    public NeoForgeTradeCyclingClientMod(IEventBus eventBus) {
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::onRegisterKeyBinds);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        clientInit();
    }

    @Override
    public void clientInit() {
        super.clientInit();
        NeoForge.EVENT_BUS.register(this);
    }

    @Override
    public void sendCycleTradesPacket() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            PacketDistributor.SERVER.noArg().send(new NeoForgeTradeCyclingMod.WrappedCycleTradesPacket());
        }
    }

    public void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        event.register(CYCLE_TRADES_KEY);
    }

    @Override
    public TradeCyclingClientConfig createClientConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        NeoForgeTradeCyclingClientConfig config = new NeoForgeTradeCyclingClientConfig(builder);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build());
        return config;
    }

    @SubscribeEvent
    public void onInitScreen(ScreenEvent.Init.Post event) {
        onOpenScreen(event.getScreen(), event::addListener);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        onCycleKeyPressed(event.getKey(), event.getScanCode(), event.getAction());
    }

}

package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.config.NeoForgeTradeCyclingClientConfig;
import de.maxhenkel.tradecycling.config.TradeCyclingClientConfig;
import de.maxhenkel.tradecycling.net.CycleTradesPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;

public class NeoForgeTradeCyclingClientMod extends TradeCyclingClientMod {

    public NeoForgeTradeCyclingClientMod(IEventBus eventBus) {
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::onRegisterKeyBinds);
        CONFIG = createClientConfig();
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
            ClientPacketDistributor.sendToServer(new CycleTradesPacket());
        }
    }

    public void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        event.register(CYCLE_TRADES_KEY);
    }

    @Override
    public TradeCyclingClientConfig createClientConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        NeoForgeTradeCyclingClientConfig config = new NeoForgeTradeCyclingClientConfig(builder);
        ModContainer modContainer = ModList.get().getModContainerById(TradeCyclingMod.MODID).orElseThrow(() -> new RuntimeException("Could not find mod %s".formatted(TradeCyclingMod.MODID)));
        modContainer.registerConfig(ModConfig.Type.CLIENT, builder.build());
        return config;
    }

    @SubscribeEvent
    public void onInitScreen(ScreenEvent.Init.Post event) {
        onOpenScreen(event.getScreen(), event::addListener);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        onCycleKeyPressed(event.getKeyEvent(), event.getAction());
    }

}

package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.config.ForgeTradeCyclingClientConfig;
import de.maxhenkel.tradecycling.config.TradeCyclingClientConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.INetworkDirection;
import net.neoforged.neoforge.network.PlayNetworkDirection;

public class ForgeTradeCyclingClientMod extends TradeCyclingClientMod {

    public ForgeTradeCyclingClientMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKeyBinds);
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
            connection.send(PlayNetworkDirection.PLAY_TO_SERVER.buildPacket(new INetworkDirection.PacketData(new FriendlyByteBuf(Unpooled.buffer()), 0), ForgeTradeCyclingMod.CYCLE_TRADES_PACKET));
        }
    }

    @SubscribeEvent
    public void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        event.register(CYCLE_TRADES_KEY);
    }

    @Override
    public TradeCyclingClientConfig createClientConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        ForgeTradeCyclingClientConfig config = new ForgeTradeCyclingClientConfig(builder);
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

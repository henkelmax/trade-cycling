package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.config.ForgeTradeCyclingClientConfig;
import de.maxhenkel.tradecycling.config.TradeCyclingClientConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeTradeCyclingClientMod extends TradeCyclingClientMod {

    protected FMLJavaModLoadingContext context;

    public ForgeTradeCyclingClientMod(FMLJavaModLoadingContext context) {
        this.context = context;
        context.getModEventBus().addListener(this::clientSetup);
        context.getModEventBus().addListener(this::onRegisterKeyBinds);
        CONFIG = createClientConfig();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        clientInit();
    }

    @Override
    public void clientInit() {
        super.clientInit();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void sendCycleTradesPacket() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            ForgeTradeCyclingMod.CYCLE_TRADES_CHANNEL.send(new FriendlyByteBuf(Unpooled.buffer()), connection.getConnection());
        }
    }

    public void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        event.register(CYCLE_TRADES_KEY);
    }

    @Override
    public TradeCyclingClientConfig createClientConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ForgeTradeCyclingClientConfig config = new ForgeTradeCyclingClientConfig(builder);
        context.registerConfig(ModConfig.Type.CLIENT, builder.build());
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

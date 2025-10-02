package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.config.ForgeTradeCyclingClientConfig;
import de.maxhenkel.tradecycling.config.TradeCyclingClientConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeTradeCyclingClientMod extends TradeCyclingClientMod {

    protected FMLJavaModLoadingContext context;

    public ForgeTradeCyclingClientMod(FMLJavaModLoadingContext context) {
        this.context = context;
        FMLClientSetupEvent.getBus(context.getModBusGroup()).addListener(this::clientSetup);
        RegisterKeyMappingsEvent.getBus(context.getModBusGroup()).addListener(this::onRegisterKeyBinds);
        ScreenEvent.Init.Post.BUS.addListener(this::onInitScreen);
        InputEvent.Key.BUS.addListener(this::onKeyInput);
        CONFIG = createClientConfig();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        clientInit();
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

    public void onInitScreen(ScreenEvent.Init.Post event) {
        onOpenScreen(event.getScreen(), event::addListener);
    }

    public void onKeyInput(InputEvent.Key event) {
        onCycleKeyPressed(new KeyEvent(event.getKey(), event.getScanCode(), event.getModifiers()), event.getAction());
    }

}

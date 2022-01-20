package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.config.TradeCyclingClientConfig;
import de.maxhenkel.tradecycling.gui.CycleTradesButton;
import de.maxhenkel.tradecycling.mixin.AbstractContainerScreenAccessor;
import io.netty.buffer.Unpooled;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.sounds.SoundEvents;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public abstract class TradeCyclingClientMod {

    public static TradeCyclingClientConfig CONFIG;
    public static final KeyMapping CYCLE_TRADES_KEY = new KeyMapping("key.trade_cycling.cycle_trades", GLFW.GLFW_KEY_C, "key.categories.inventory");

    public TradeCyclingClientMod() {
        CONFIG = createClientConfig();
    }

    public void clientInit() {
        registerKeyBindings();
    }

    public static void sendCycleTradesPacket() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(new ServerboundCustomPayloadPacket(TradeCyclingMod.CYCLE_TRADES_PACKET, buffer));
        }
    }

    public abstract TradeCyclingClientConfig createClientConfig();

    public abstract void registerKeyBindings();

    public <T extends GuiEventListener & Widget> void onOpenScreen(Screen screen, Consumer<T> eventConsumer) {
        if (!(screen instanceof MerchantScreen merchantScreen)) {
            return;
        }

        if (!(screen instanceof AbstractContainerScreenAccessor s)) {
            return;
        }

        TradeCyclingClientConfig.CycleTradesButtonLocation loc = CONFIG.getCycleTradesButtonLocation();

        if (loc.equals(TradeCyclingClientConfig.CycleTradesButtonLocation.NONE)) {
            return;
        }

        int posX;

        switch (loc) {
            case TOP_LEFT:
            default:
                posX = s.getLeftPos() + 107;
                break;
            case TOP_RIGHT:
                posX = s.getLeftPos() + 250;
                break;
        }

        eventConsumer.accept((T) new CycleTradesButton(posX, s.getTopPos() + 8, b -> sendCycleTradesPacket(), merchantScreen));
    }

    public void onCycleKeyPressed(int key, int scanCode, int action) {
        if (!CYCLE_TRADES_KEY.matches(key, scanCode) || action != 1) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Screen currentScreen = mc.screen;

        if (!(currentScreen instanceof MerchantScreen screen)) {
            return;
        }

        if (!screen.getMenu().showProgressBar() || screen.getMenu().getTraderXp() > 0) {
            return;
        }

        sendCycleTradesPacket();
        mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
    }

}

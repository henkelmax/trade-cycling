package de.maxhenkel.tradecycling.compatibility;

import de.maxhenkel.tradecycling.TradeCyclingMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.villager.Villager;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Arrays;

public class VisibleTraders {

    @Nullable
    private static Method regenerateTrades;
    @Nullable
    private static Method requestOffers;

    public static void regenerateTrades(Villager villager) {
        if (regenerateTrades == null) {
            return;
        }
        try {
            regenerateTrades.invoke(villager);
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to regenerate visible traders trades", e);
            regenerateTrades = null;
        }
    }

    public static void requestOffers(Villager villager, ServerPlayer player) {
        if (requestOffers == null) {
            return;
        }
        try {
            requestOffers.invoke(villager, player);
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to request offers", e);
            requestOffers = null;
        }
    }

    public static void init(TradeCyclingMod mod) {
        if (!mod.isModLoaded("visibletraders")) {
            return;
        }
        regenerateTrades = getRegenerateMethod();
        requestOffers = requestOffersMethod();
    }

    private static Method getRegenerateMethod() {
        try {
            Method forceTradeGeneration = Arrays.stream(Villager.class.getDeclaredMethods()).filter(method -> method.getName().contains("visibleTrades$regenerateTrades")).findFirst().orElseThrow(() -> new RuntimeException("Could not find visible traders regenerate trades method"));
            forceTradeGeneration.setAccessible(true);
            return forceTradeGeneration;
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to initialize visible traders integration", e);
            return null;
        }
    }

    private static Method requestOffersMethod() {
        try {
            Method requestOffers = Arrays.stream(Villager.class.getDeclaredMethods()).filter(method -> method.getName().contains("visibleTraders$requestOffers")).findFirst().orElseThrow(() -> new RuntimeException("Could not find visible traders requestOffers method"));
            requestOffers.setAccessible(true);
            return requestOffers;
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to initialize visible traders integration", e);
            return null;
        }
    }

}

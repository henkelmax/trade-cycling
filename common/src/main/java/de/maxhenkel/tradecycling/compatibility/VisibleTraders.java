package de.maxhenkel.tradecycling.compatibility;

import de.maxhenkel.tradecycling.TradeCyclingMod;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.trading.MerchantOffers;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class VisibleTraders {

    @Nullable
    private static Method regenerateTrades;
    @Nullable
    private static Method getMerchantOffers;

    @Nullable
    private static Method getLevel;

    public static void forceTradeGeneration(Villager villager) {
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

    public static MerchantOffers getOffers(Villager villager) {
        if (getMerchantOffers == null) {
            return villager.getOffers();
        }
        try {
            return (MerchantOffers) getMerchantOffers.invoke(villager);
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to regenerate visible traders trades", e);
            getMerchantOffers = null;
            return villager.getOffers();
        }
    }

    public static int getLevel(Villager villager) {
        if (getLevel == null) {
            return villager.getVillagerData().level();
        }
        try {
            return (int) getLevel.invoke(villager);
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to regenerate visible traders trades", e);
            getLevel = null;
            return villager.getVillagerData().level();
        }
    }

    public static void init(TradeCyclingMod mod) {
        if (!mod.isModLoaded("visibletraders")) {
            return;
        }
        regenerateTrades = getRegenerateMethod();
        getMerchantOffers = getMerchantOffersMethod();
        getLevel = getLevelMethod();
    }

    private static Method getRegenerateMethod() {
        try {
            Method forceTradeGeneration = Villager.class.getDeclaredMethod("visibleTrades$regenerateTrades");
            forceTradeGeneration.setAccessible(true);
            return forceTradeGeneration;
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to initialize visible traders integration", e);
            return null;
        }
    }

    private static Method getMerchantOffersMethod() {
        try {
            Method getMerchantOffers = Villager.class.getDeclaredMethod("visibleTraders$getCombinedOffers");
            getMerchantOffers.setAccessible(true);
            return getMerchantOffers;
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to initialize visible traders integration", e);
            return null;
        }
    }

    private static Method getLevelMethod() {
        try {
            Method getLevel = Villager.class.getDeclaredMethod("visibleTraders$getShiftedLevel");
            getLevel.setAccessible(true);
            return getLevel;
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to initialize visible traders integration", e);
            return null;
        }
    }

}

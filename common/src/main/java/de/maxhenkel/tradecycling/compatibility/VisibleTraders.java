package de.maxhenkel.tradecycling.compatibility;

import de.maxhenkel.tradecycling.TradeCyclingMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class VisibleTraders {

    @Nullable
    private static Method regenerateTrades;
    @Nullable
    private static Method sendMerchantOffers;

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

    public static boolean sendTrades(ServerPlayer player, Merchant merchant, int containerId, MerchantOffers merchantOffers, int level, int villagerXp, boolean showProgressBar, boolean canRestock) {
        if (sendMerchantOffers == null) {
            return false;
        }
        try {
            sendMerchantOffers.invoke(player, merchant, containerId, merchantOffers, level, villagerXp, showProgressBar, canRestock);
            return true;
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to regenerate visible traders trades", e);
            sendMerchantOffers = null;
            return false;
        }
    }

    public static void init(TradeCyclingMod mod) {
        if (!mod.isModLoaded("visibletraders")) {
            return;
        }
        regenerateTrades = getRegenerateMethod();
        sendMerchantOffers = getSendMerchantOffersMethod();
    }

    private static Method getRegenerateMethod() {
        try {
            Method forceTradeGeneration = Villager.class.getDeclaredMethod("visibleTraders$forceTradeGeneration");
            forceTradeGeneration.setAccessible(true);
            return forceTradeGeneration;
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to initialize visible traders integration", e);
            return null;
        }
    }

    private static Method getSendMerchantOffersMethod() {
        try {
            Method sendMerchantOffers = ServerPlayer.class.getDeclaredMethod("visibleTraders$wrapAndSendMerchantOffers", Merchant.class, int.class, MerchantOffers.class, int.class, int.class, boolean.class, boolean.class);
            sendMerchantOffers.setAccessible(true);
            return sendMerchantOffers;
        } catch (Throwable e) {
            TradeCyclingMod.LOGGER.error("Failed to initialize visible traders integration", e);
            return null;
        }
    }

}

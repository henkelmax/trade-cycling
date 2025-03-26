package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.compatibility.VisibleTraders;
import de.maxhenkel.tradecycling.mixin.VillagerAccessor;
import de.maxhenkel.tradecycling.mixin.MerchantMenuAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public abstract class TradeCyclingMod {

    public static final String MODID = "trade_cycling";
    public static final Logger LOGGER = LogManager.getLogger(MODID);


    public void init() {
        VisibleTraders.init(this);
    }

    public abstract boolean isModLoaded(String modId);

    public static void onCycleTrades(@Nullable ServerPlayer player) {
        if (player == null) {
            return;
        }
        if (!(player.containerMenu instanceof MerchantMenuAccessor merchantAccessor)) {
            return;
        }
        if (!(player.containerMenu instanceof MerchantMenu container)) {
            return;
        }
        Merchant merchant = merchantAccessor.getTrader();

        if (container.getTraderXp() > 0 && merchantAccessor.getTradeContainer().getActiveOffer() != null) {
            return;
        }

        if (!(merchant instanceof Villager villager)) {
            return;
        }
        if (!(merchant instanceof VillagerAccessor villagerAccessor)) {
            return;
        }

        if (villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).isEmpty()) {
            return;
        }

        villager.setOffers(null);
        villager.getOffers();
        villagerAccessor.invokeUpdateSpecialPrices(player);
        villager.setTradingPlayer(player);
        VisibleTraders.forceTradeGeneration(villager);
        sendOffers(player, container.containerId, villager);
    }

    private static void sendOffers(ServerPlayer player, int containerId, Villager villager) {
        boolean success = VisibleTraders.sendTrades(player, villager, containerId, villager.getOffers(), villager.getVillagerData().level(), villager.getVillagerXp(), villager.showProgressBar(), villager.canRestock());
        if (success) {
            return;
        }
        player.sendMerchantOffers(containerId, villager.getOffers(), villager.getVillagerData().level(), villager.getVillagerXp(), villager.showProgressBar(), villager.canRestock());
    }

}

package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.mixin.VillagerAccessor;
import de.maxhenkel.tradecycling.mixin.MerchantMenuAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;

public abstract class TradeCyclingMod {

    public static final String MODID = "trade_cycling";
    public static final ResourceLocation CYCLE_TRADES_PACKET = new ResourceLocation(MODID, "cycle_trades");

    public void init() {

    }

    public static void onCycleTrades(ServerPlayer player) {
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
        villagerAccessor.invokeUpdateSpecialPrices(player);
        villager.setTradingPlayer(player);
        player.sendMerchantOffers(container.containerId, villager.getOffers(), villager.getVillagerData().getLevel(), villager.getVillagerXp(), villager.showProgressBar(), villager.canRestock());
    }

}

package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.mixin.AbstractVillagerAccessor;
import de.maxhenkel.tradecycling.mixin.MerchantMenuAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;

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

        if (merchant.getVillagerXp() > 0) {
            return;
        }

        if (!(merchant instanceof Villager villager)) {
            return;
        }
        if (!(merchant instanceof AbstractVillagerAccessor villagerAccessor)) {
            return;
        }

        villagerAccessor.setOffers(new MerchantOffers());
        villagerAccessor.invokeUpdateTrades();
        player.sendMerchantOffers(container.containerId, villager.getOffers(), villager.getVillagerData().getLevel(), villager.getVillagerXp(), villager.showProgressBar(), villager.canRestock());
    }

}

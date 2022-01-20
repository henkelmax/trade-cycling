package de.maxhenkel.tradecycling.mixin;

import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractVillager.class)
public interface AbstractVillagerAccessor {

    @Accessor
    void setOffers(MerchantOffers offers);

    @Invoker("updateTrades")
    void invokeUpdateTrades();

}

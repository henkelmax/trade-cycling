package de.maxhenkel.tradecycling;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration("assets/" + TradeCyclingMod.MODID + "/trade_cycling.mixins.json");
    }

}


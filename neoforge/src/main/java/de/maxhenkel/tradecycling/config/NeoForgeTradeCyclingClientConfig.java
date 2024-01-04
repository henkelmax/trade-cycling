package de.maxhenkel.tradecycling.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class NeoForgeTradeCyclingClientConfig extends TradeCyclingClientConfig {

    public final ModConfigSpec.EnumValue<CycleTradesButtonLocation> cycleTradesButtonLocation;

    public NeoForgeTradeCyclingClientConfig(ModConfigSpec.Builder builder) {
        cycleTradesButtonLocation = builder
                .comment("The location of the cycle trades button")
                .defineEnum("cycle_trades_button_location", CycleTradesButtonLocation.TOP_LEFT);
    }

    @Override
    public CycleTradesButtonLocation getCycleTradesButtonLocation() {
        return cycleTradesButtonLocation.get();
    }
}

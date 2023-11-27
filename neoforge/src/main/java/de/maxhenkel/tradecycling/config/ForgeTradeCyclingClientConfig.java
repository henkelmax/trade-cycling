package de.maxhenkel.tradecycling.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ForgeTradeCyclingClientConfig extends TradeCyclingClientConfig {

    public final ModConfigSpec.EnumValue<CycleTradesButtonLocation> cycleTradesButtonLocation;

    public ForgeTradeCyclingClientConfig(ModConfigSpec.Builder builder) {
        cycleTradesButtonLocation = builder
                .comment("The location of the cycle trades button")
                .defineEnum("cycle_trades_button_location", CycleTradesButtonLocation.TOP_LEFT);
    }

    @Override
    public CycleTradesButtonLocation getCycleTradesButtonLocation() {
        return cycleTradesButtonLocation.get();
    }
}

package de.maxhenkel.tradecycling.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeTradeCyclingClientConfig extends TradeCyclingClientConfig {

    public final ForgeConfigSpec.EnumValue<CycleTradesButtonLocation> cycleTradesButtonLocation;

    public ForgeTradeCyclingClientConfig(ForgeConfigSpec.Builder builder) {
        cycleTradesButtonLocation = builder
                .comment("The location of the cycle trades button")
                .defineEnum("cycle_trades_button_location", CycleTradesButtonLocation.TOP_LEFT);
    }

    @Override
    public CycleTradesButtonLocation getCycleTradesButtonLocation() {
        return cycleTradesButtonLocation.get();
    }
}

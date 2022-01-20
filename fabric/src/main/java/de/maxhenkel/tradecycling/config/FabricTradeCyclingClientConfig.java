package de.maxhenkel.tradecycling.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.ConfigEntry;

public class FabricTradeCyclingClientConfig extends TradeCyclingClientConfig {

    public final ConfigEntry<CycleTradesButtonLocation> cycleTradesButtonLocation;

    public FabricTradeCyclingClientConfig(ConfigBuilder builder) {
        cycleTradesButtonLocation = builder.enumEntry("cycle_trades_button_location", CycleTradesButtonLocation.TOP_LEFT);
    }

    @Override
    public CycleTradesButtonLocation getCycleTradesButtonLocation() {
        return cycleTradesButtonLocation.get();
    }
}

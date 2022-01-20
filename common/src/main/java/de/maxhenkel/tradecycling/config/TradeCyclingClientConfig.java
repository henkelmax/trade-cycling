package de.maxhenkel.tradecycling.config;

public abstract class TradeCyclingClientConfig {

    public abstract CycleTradesButtonLocation getCycleTradesButtonLocation();

    public static enum CycleTradesButtonLocation {
        TOP_LEFT, TOP_RIGHT, NONE
    }

}

package de.maxhenkel.tradecycling.net;

import de.maxhenkel.tradecycling.TradeCyclingMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class CycleTradesPacket implements CustomPacketPayload {

    public static final Type<CycleTradesPacket> CYCLE_TRADES = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TradeCyclingMod.MODID, "cycle_trades"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CycleTradesPacket> CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, CycleTradesPacket packet) {
        }

        @Override
        public CycleTradesPacket decode(RegistryFriendlyByteBuf buf) {
            return new CycleTradesPacket();
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return CYCLE_TRADES;
    }

}

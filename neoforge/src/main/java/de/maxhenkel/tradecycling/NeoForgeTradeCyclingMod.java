package de.maxhenkel.tradecycling;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@Mod(TradeCyclingMod.MODID)
public class NeoForgeTradeCyclingMod extends TradeCyclingMod {

    private NeoForgeTradeCyclingClientMod clientMod;

    public NeoForgeTradeCyclingMod(IEventBus eventBus) {
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::onRegisterPayloadHandler);

        if (FMLEnvironment.dist.isClient()) {
            clientMod = new NeoForgeTradeCyclingClientMod(eventBus);
        }
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        init();
    }

    public void onRegisterPayloadHandler(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(MODID).optional();

        registrar.play(CYCLE_TRADES_PACKET, buf -> new WrappedCycleTradesPacket(), (payload, context) -> {
            if (payload == null) {
                return;
            }
            if (context.flow().equals(PacketFlow.SERVERBOUND)) {
                context.workHandler().execute(() -> onCycleTrades(context.player().filter(player -> player instanceof ServerPlayer).map(ServerPlayer.class::cast).orElse(null)));
            }
        });
    }

    public static record WrappedCycleTradesPacket() implements CustomPacketPayload {

        @Override
        public void write(FriendlyByteBuf buf) {

        }

        @Override
        public ResourceLocation id() {
            return CYCLE_TRADES_PACKET;
        }
    }

}
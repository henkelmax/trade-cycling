package de.maxhenkel.tradecycling;

import de.maxhenkel.tradecycling.net.CycleTradesPacket;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

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

    public void onRegisterPayloadHandler(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).optional();

        registrar.playToServer(CycleTradesPacket.CYCLE_TRADES, CycleTradesPacket.CODEC, (payload, context) -> {
            if (context.flow().equals(PacketFlow.SERVERBOUND) && context.player() instanceof ServerPlayer player) {
                context.enqueueWork(() -> onCycleTrades(player));
            }
        });
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

}
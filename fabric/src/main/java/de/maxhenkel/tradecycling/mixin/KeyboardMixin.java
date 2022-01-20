package de.maxhenkel.tradecycling.mixin;

import de.maxhenkel.tradecycling.FabricTradeCyclingClientMod;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {

    @Inject(at = @At("HEAD"), method = "keyPress")
    private void onKey(long window, int key, int scancode, int action, int j, CallbackInfo info) {
        FabricTradeCyclingClientMod.instance().onCycleKeyPressed(key, scancode, action);
    }

}

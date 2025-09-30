package de.maxhenkel.tradecycling.mixin;

import de.maxhenkel.tradecycling.FabricTradeCyclingClientMod;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {

    @Inject(at = @At("HEAD"), method = "keyPress")
    private void onKey(long handle, int action, KeyEvent keyEvent, CallbackInfo ci) {
        FabricTradeCyclingClientMod.instance().onCycleKeyPressed(keyEvent, action);
    }

}

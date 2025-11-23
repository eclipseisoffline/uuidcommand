package xyz.eclipseisoffline.uuidcommand.mixin;

import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntitySelectorParser.class)
public class EntitySelectorReaderMixin {

    @Inject(method = "allowSelectors", at = @At("HEAD"), cancellable = true)
    private static void alwaysAllowAtSelectors(Object source, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(true);
    }
}

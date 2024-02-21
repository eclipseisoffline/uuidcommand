package xyz.eclipseisoffline.uuidcommand.mixin;

import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityArgumentType.class)
public class EntityArgumentTypeMixin {

    @Redirect(method = "listSuggestions", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/CommandSource;hasPermissionLevel(I)Z"))
    public boolean alwaysListAtSuggestions(CommandSource instance, int i) {
        return true;
    }
}

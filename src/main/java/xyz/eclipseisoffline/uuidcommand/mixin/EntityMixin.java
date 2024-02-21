package xyz.eclipseisoffline.uuidcommand.mixin;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.eclipseisoffline.uuidcommand.UUIDHolder;

@Mixin(Entity.class)
public abstract class EntityMixin implements UUIDHolder {

    @Shadow protected UUID uuid;

    @Shadow public abstract Text getDisplayName();

    @Override
    public UUID UUIDCommand$getUUID() {
        return uuid;
    }

    @Override
    public Text UUIDCommand$getName() {
        return getDisplayName();
    }
}

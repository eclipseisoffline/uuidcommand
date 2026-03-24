package xyz.eclipseisoffline.uuidcommand.mixin;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.eclipseisoffline.uuidcommand.UUIDHolder;

@Mixin(Entity.class)
public abstract class EntityMixin implements UUIDHolder {

    @Shadow protected UUID uuid;

    @Shadow public abstract Component getDisplayName();

    @Override
    public UUID UUIDCommand$getUUID() {
        return uuid;
    }

    @Override
    public Component UUIDCommand$getName() {
        return getDisplayName();
    }
}

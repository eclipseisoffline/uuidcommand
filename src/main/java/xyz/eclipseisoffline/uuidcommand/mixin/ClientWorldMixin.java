package xyz.eclipseisoffline.uuidcommand.mixin;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.eclipseisoffline.uuidcommand.ClientWorldEntityCollector;

@Mixin(ClientLevel.class)
public abstract class ClientWorldMixin implements ClientWorldEntityCollector {


    @Shadow protected abstract LevelEntityGetter<Entity> getEntities();

    @Override
    public <T extends Entity> void UUIDCommand$collectEntitiesByType(EntityTypeTest<Entity, T> filter,
            Predicate<? super T> predicate, List<? super T> result, int limit) {
        getEntities().get(filter, entity -> {
            if (predicate.test(entity)) {
                result.add(entity);
                if (result.size() >= limit) {
                    return AbortableIterationConsumer.Continuation.ABORT;
                }
            }
            return AbortableIterationConsumer.Continuation.CONTINUE;
        });
    }
}

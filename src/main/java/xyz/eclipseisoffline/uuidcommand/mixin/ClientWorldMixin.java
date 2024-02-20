package xyz.eclipseisoffline.uuidcommand.mixin;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.world.entity.EntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.eclipseisoffline.uuidcommand.ClientWorldEntityCollector;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin implements ClientWorldEntityCollector {


    @Shadow protected abstract EntityLookup<Entity> getEntityLookup();

    @Override
    public <T extends Entity> void UUIDCommand$collectEntitiesByType(TypeFilter<Entity, T> filter,
            Predicate<? super T> predicate, List<? super T> result, int limit) {
        getEntityLookup().forEach(filter, entity -> {
            if (predicate.test(entity)) {
                result.add(entity);
                if (result.size() >= limit) {
                    return LazyIterationConsumer.NextIteration.ABORT;
                }
            }
            return LazyIterationConsumer.NextIteration.CONTINUE;
        });
    }
}

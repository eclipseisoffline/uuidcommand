package xyz.eclipseisoffline.uuidcommand;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTypeTest;

public interface ClientWorldEntityCollector {

    <T extends Entity> void UUIDCommand$collectEntitiesByType(
            EntityTypeTest<Entity, T> filter, Predicate<? super T> predicate, List<? super T> result, int limit);
}

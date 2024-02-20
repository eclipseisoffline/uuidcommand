package xyz.eclipseisoffline.uuidcommand;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;

public interface ClientWorldEntityCollector {

    <T extends Entity> void UUIDCommand$collectEntitiesByType(
            TypeFilter<Entity, T> filter, Predicate<? super T> predicate, List<? super T> result, int limit);
}

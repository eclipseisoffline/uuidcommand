package xyz.eclipseisoffline.uuidcommand.mixin;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntitySelector.class)
public interface EntitySelectorAccessor {

    @Accessor("PASSTHROUGH_FILTER")
    static TypeFilter<Entity, ?> getPassthroughFilter() {
        throw new AssertionError();
    }

    @Accessor
    Predicate<Entity> getBasePredicate();

    @Accessor
    NumberRange.DoubleRange getDistance();

    @Accessor
    String getPlayerName();

    @Accessor
    UUID getUuid();

    @Accessor
    Function<Vec3d, Vec3d> getPositionOffset();

    @Accessor
    boolean getSenderOnly();

    @Accessor
    boolean getUsesAt();

    @Accessor
    Box getBox();

    @Accessor
    BiConsumer<Vec3d, List<? extends Entity>> getSorter();

    @Accessor
    TypeFilter<Entity, ?> getEntityFilter();

    @Invoker
    <T extends Entity> List<T> invokeGetEntities(Vec3d pos, List<T> entities);

    @Invoker
    Predicate<Entity> invokeGetPositionPredicate(Vec3d pos);

    @Invoker
    int invokeGetAppendLimit();
}

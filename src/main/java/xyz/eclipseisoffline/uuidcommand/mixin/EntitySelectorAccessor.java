package xyz.eclipseisoffline.uuidcommand.mixin;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntitySelector.class)
public interface EntitySelectorAccessor {

    @Accessor("ANY_TYPE")
    static EntityTypeTest<Entity, ?> getAnyType() {
        throw new AssertionError();
    }

    @Accessor
    List<Predicate<Entity>> getContextFreePredicates();

    @Accessor
    MinMaxBounds.Doubles getRange();

    @Accessor
    String getPlayerName();

    @Accessor
    UUID getEntityUUID();

    @Accessor
    Function<Vec3, Vec3> getPosition();

    @Accessor
    boolean getCurrentEntity();

    @Accessor
    boolean getUsesSelector();

    @Accessor
    AABB getAabb();

    @Accessor
    BiConsumer<Vec3, List<? extends Entity>> getOrder();

    @Accessor
    EntityTypeTest<Entity, ?> getType();

    @Invoker
    <T extends Entity> List<T> invokeSortAndLimit(Vec3 pos, List<T> entities);

    @Invoker
    AABB invokeGetAbsoluteAabb(Vec3 offset);

    @Invoker
    Predicate<Entity> invokeGetPredicate(Vec3 pos, @Nullable AABB box, @Nullable FeatureFlagSet enabledFeatures);

    @Invoker
    int invokeGetResultLimit();
}

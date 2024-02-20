package xyz.eclipseisoffline.uuidcommand.mixin;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import xyz.eclipseisoffline.uuidcommand.ClientEntitySelector;
import xyz.eclipseisoffline.uuidcommand.ClientWorldEntityCollector;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin implements ClientEntitySelector {

    @Shadow @Final private @Nullable String playerName;

    @Shadow @Final private @Nullable UUID uuid;

    @Shadow @Final private Function<Vec3d, Vec3d> positionOffset;

    @Shadow protected abstract Predicate<Entity> getPositionPredicate(Vec3d pos);

    @Shadow @Final private boolean senderOnly;

    @Shadow protected abstract int getAppendLimit();

    @Shadow @Final private @Nullable Box box;

    @Shadow @Final private TypeFilter<Entity, ?> entityFilter;

    @Shadow protected abstract <T extends Entity> List<T> getEntities(Vec3d pos, List<T> entities);

    @Override
    public Entity UUIDCommand$getEntity(FabricClientCommandSource source) throws CommandSyntaxException {
        // Not implemented from server side: permission check
        List<? extends Entity> list = UUIDCommand$getEntities(source);
        if (list.isEmpty()) {
            throw EntityArgumentType.ENTITY_NOT_FOUND_EXCEPTION.create();
        }
        if (list.size() > 1) {
            throw EntityArgumentType.TOO_MANY_ENTITIES_EXCEPTION.create();
        }
        return list.get(0);
    }

    @Override
    public List<? extends Entity> UUIDCommand$getEntities(FabricClientCommandSource source)
            throws CommandSyntaxException {
        return getUnfilteredClientEntities(source).stream().filter(entity -> entity.getType().isEnabled(source.getEnabledFeatures())).toList();
    }

    @Unique
    private List<? extends Entity> getUnfilteredClientEntities(FabricClientCommandSource source) {
        // Not implemented from server side: permission check, player-only selectors
        // Discarded local world check, since the client can never check other worlds
        if (playerName != null) {
            for (AbstractClientPlayerEntity player : source.getWorld().getPlayers()) {
                if (player.getGameProfile().getName().equals(playerName)) {
                    return Lists.newArrayList(player);
                }
            }
            return Collections.emptyList();
        }
        if (uuid != null) {
            for (Entity entity : source.getWorld().getEntities()) {
                if (entity.getUuid().equals(uuid)) {
                    return Lists.newArrayList(entity);
                }
            }
            return Collections.emptyList();
        }
        Vec3d vec3d = positionOffset.apply(source.getPosition());
        Predicate<Entity> predicate = getPositionPredicate(vec3d);
        if (senderOnly) {
            if (source.getEntity() != null && predicate.test(source.getEntity())) {
                return Lists.newArrayList(source.getEntity());
            }
            return Collections.emptyList();
        }
        ArrayList<Entity> list = Lists.newArrayList();
        appendEntitiesFromClientWorld(list, source.getWorld(), vec3d, predicate);
        return getEntities(vec3d, list);
    }

    @Unique
    private void appendEntitiesFromClientWorld(List<Entity> entities, ClientWorld world,
            Vec3d pos, Predicate<Entity> predicate) {
        int i = getAppendLimit();
        if (entities.size() >= i) {
            return;
        }
        if (box != null) {
            world.collectEntitiesByType(entityFilter, this.box.offset(pos), predicate, entities, i);
        } else {
            ((ClientWorldEntityCollector) world).UUIDCommand$collectEntitiesByType(entityFilter, predicate, entities, i);
        }
    }
}

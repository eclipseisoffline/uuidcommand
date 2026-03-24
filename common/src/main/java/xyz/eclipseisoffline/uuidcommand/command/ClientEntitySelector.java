package xyz.eclipseisoffline.uuidcommand.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import xyz.eclipseisoffline.uuidcommand.ClientWorldEntityCollector;
import xyz.eclipseisoffline.uuidcommand.UUIDHolder;
import xyz.eclipseisoffline.uuidcommand.mixin.EntitySelectorAccessor;

public class ClientEntitySelector extends EntitySelector {

    public ClientEntitySelector(EntitySelector selector) {
        super(selector.getMaxResults(), selector.includesEntities(), true,
                ((EntitySelectorAccessor) selector).getContextFreePredicates(),
                ((EntitySelectorAccessor) selector).getRange(),
                ((EntitySelectorAccessor) selector).getPosition(),
                ((EntitySelectorAccessor) selector).getAabb(),
                ((EntitySelectorAccessor) selector).getOrder(),
                ((EntitySelectorAccessor) selector).getCurrentEntity(),
                ((EntitySelectorAccessor) selector).getPlayerName(),
                ((EntitySelectorAccessor) selector).getEntityUUID(),
                ((EntitySelectorAccessor) selector).getType() == EntitySelectorAccessor.getAnyType()
                        ? null : (EntityType<?>) ((EntitySelectorAccessor) selector).getType(),
                ((EntitySelectorAccessor) selector).getUsesSelector());
    }

    public UUIDHolder getClientEntity(FabricClientCommandSource source) throws CommandSyntaxException {
        List<UUIDHolder> list = getClientEntities(source);
        if (list.isEmpty()) {
            throw EntityArgument.NO_ENTITIES_FOUND.create();
        }
        if (list.size() > 1) {
            throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
        }
        return list.getFirst();
    }

    public List<UUIDHolder> getClientEntities(FabricClientCommandSource source) {
        List<UUIDHolder> entities = new ArrayList<>(getUnfilteredClientEntities(source).stream()
                .filter(entity -> entity.getType().isEnabled(source.enabledFeatures()))
                .map(entity -> (UUIDHolder) entity)
                .toList());
        if (entities.isEmpty()) {
            entities.addAll(getClientPlayerEntries(source).stream().map(player -> (UUIDHolder) player).toList());
        }
        return entities;
    }

    private List<PlayerInfo> getClientPlayerEntries(FabricClientCommandSource source) {
        PlayerInfo player = null;

        assert source.getClient().getConnection() != null;
        if (((EntitySelectorAccessor) this).getPlayerName() != null) {
            player = source.getClient().getConnection().getPlayerInfo(((EntitySelectorAccessor) this).getPlayerName());
        } else if (((EntitySelectorAccessor) this).getEntityUUID() != null) {
            player = source.getClient().getConnection().getPlayerInfo(((EntitySelectorAccessor) this).getEntityUUID());
        }

        if (player != null) {
            return Lists.newArrayList(player);
        }

        return Collections.emptyList();
    }

    private List<? extends Entity> getUnfilteredClientEntities(FabricClientCommandSource source) {
        // Not implemented from server side: player-only selectors
        // Discarded local world check, since localWordOnly is always true
        if (((EntitySelectorAccessor) this).getPlayerName() != null) {
            for (AbstractClientPlayer player : source.getWorld().players()) {
                if (player.getGameProfile().name().equals(((EntitySelectorAccessor) this).getPlayerName())) {
                    return Lists.newArrayList(player);
                }
            }
            return Collections.emptyList();
        } else if (((EntitySelectorAccessor) this).getEntityUUID() != null) {
            for (Entity entity : source.getWorld().entitiesForRendering()) {
                if (entity.getUUID().equals(((EntitySelectorAccessor) this).getEntityUUID())) {
                    return Lists.newArrayList(entity);
                }
            }
            return Collections.emptyList();
        }

        Vec3 vec3d = ((EntitySelectorAccessor) this).getPosition().apply(source.getPosition());
        AABB box = ((EntitySelectorAccessor) this).invokeGetAbsoluteAabb(vec3d);
        Predicate<Entity> predicate;
        if (((EntitySelectorAccessor) this).getCurrentEntity()) {
            predicate = ((EntitySelectorAccessor) this).invokeGetPredicate(vec3d, box, null);
            return source.getEntity() != null && predicate.test(source.getEntity()) ? List.of(source.getEntity()) : List.of();
        }
        predicate = ((EntitySelectorAccessor) this).invokeGetPredicate(vec3d, box, source.enabledFeatures());
        List<Entity> list = new ObjectArrayList<>();
        appendEntitiesFromClientWorld(list, source.getWorld(), vec3d, predicate);
        return ((EntitySelectorAccessor) this).invokeSortAndLimit(vec3d, list);
    }

    private void appendEntitiesFromClientWorld(List<Entity> entities, ClientLevel world,
            Vec3 pos, Predicate<Entity> predicate) {
        int i = ((EntitySelectorAccessor) this).invokeGetResultLimit();
        if (entities.size() >= i) {
            return;
        }
        if (((EntitySelectorAccessor) this).getAabb() != null) {
            world.getEntities(((EntitySelectorAccessor) this).getType(), ((EntitySelectorAccessor) this).getAabb().move(pos), predicate, entities, i);
        } else {
            ((ClientWorldEntityCollector) world).UUIDCommand$collectEntitiesByType(((EntitySelectorAccessor) this).getType(), predicate, entities, i);
        }
    }
}

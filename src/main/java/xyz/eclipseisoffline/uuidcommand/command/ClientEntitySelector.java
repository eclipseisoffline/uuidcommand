package xyz.eclipseisoffline.uuidcommand.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import xyz.eclipseisoffline.uuidcommand.ClientWorldEntityCollector;
import xyz.eclipseisoffline.uuidcommand.UUIDHolder;
import xyz.eclipseisoffline.uuidcommand.mixin.EntitySelectorAccessor;

public class ClientEntitySelector extends EntitySelector {

    public ClientEntitySelector(EntitySelector selector) {
        super(selector.getLimit(), selector.includesNonPlayers(), true,
                ((EntitySelectorAccessor) selector).getBasePredicate(),
                ((EntitySelectorAccessor) selector).getDistance(),
                ((EntitySelectorAccessor) selector).getPositionOffset(),
                ((EntitySelectorAccessor) selector).getBox(),
                ((EntitySelectorAccessor) selector).getSorter(),
                ((EntitySelectorAccessor) selector).getSenderOnly(),
                ((EntitySelectorAccessor) selector).getPlayerName(),
                ((EntitySelectorAccessor) selector).getUuid(),
                ((EntitySelectorAccessor) selector).getEntityFilter() == EntitySelectorAccessor.getPassthroughFilter()
                        ? null : (EntityType<?>) ((EntitySelectorAccessor) selector).getEntityFilter(),
                ((EntitySelectorAccessor) selector).getUsesAt());
    }

    public UUIDHolder getClientEntity(FabricClientCommandSource source) throws CommandSyntaxException {
        List<UUIDHolder> list = getClientEntities(source);
        if (list.isEmpty()) {
            throw EntityArgumentType.ENTITY_NOT_FOUND_EXCEPTION.create();
        }
        if (list.size() > 1) {
            throw EntityArgumentType.TOO_MANY_ENTITIES_EXCEPTION.create();
        }
        return list.get(0);
    }

    public List<UUIDHolder> getClientEntities(FabricClientCommandSource source) throws CommandSyntaxException {
        List<UUIDHolder> entities = new ArrayList<>(getUnfilteredClientEntities(source).stream()
                .filter(entity -> entity.getType().isEnabled(source.getEnabledFeatures()))
                .map(entity -> (UUIDHolder) entity)
                .toList());
        if (entities.isEmpty()) {
            entities.addAll(getClientPlayerEntries(source).stream().map(player -> (UUIDHolder) player).toList());
        }
        return entities;
    }

    private List<PlayerListEntry> getClientPlayerEntries(FabricClientCommandSource source) {
        PlayerListEntry player = null;

        assert source.getClient().getNetworkHandler() != null;
        if (((EntitySelectorAccessor) this).getPlayerName() != null) {
            player = source.getClient().getNetworkHandler().getPlayerListEntry(((EntitySelectorAccessor) this).getPlayerName());
        } else if (((EntitySelectorAccessor) this).getUuid() != null) {
            player = source.getClient().getNetworkHandler().getPlayerListEntry(((EntitySelectorAccessor) this).getUuid());
        }

        if (player != null) {
            return Lists.newArrayList(player);
        }

        return Collections.emptyList();
    }

    private List<? extends Entity> getUnfilteredClientEntities(FabricClientCommandSource source)
            throws CommandSyntaxException {
        // Not implemented from server side: player-only selectors
        // Discarded local world check, since localWordOnly is always true
        checkSourcePermission(source);
        if (((EntitySelectorAccessor) this).getPlayerName() != null) {
            for (AbstractClientPlayerEntity player : source.getWorld().getPlayers()) {
                if (player.getGameProfile().getName().equals(((EntitySelectorAccessor) this).getPlayerName())) {
                    return Lists.newArrayList(player);
                }
            }
            return Collections.emptyList();
        } else if (((EntitySelectorAccessor) this).getUuid() != null) {
            for (Entity entity : source.getWorld().getEntities()) {
                if (entity.getUuid().equals(((EntitySelectorAccessor) this).getUuid())) {
                    return Lists.newArrayList(entity);
                }
            }
            return Collections.emptyList();
        }

        Vec3d vec3d = ((EntitySelectorAccessor) this).getPositionOffset().apply(source.getPosition());
        Predicate<Entity> predicate = ((EntitySelectorAccessor) this).invokeGetPositionPredicate(vec3d);
        if (((EntitySelectorAccessor) this).getSenderOnly()) {
            if (source.getEntity() != null && predicate.test(source.getEntity())) {
                return Lists.newArrayList(source.getEntity());
            }
            return Collections.emptyList();
        }
        ArrayList<Entity> list = Lists.newArrayList();
        appendEntitiesFromClientWorld(list, source.getWorld(), vec3d, predicate);
        return ((EntitySelectorAccessor) this).invokeGetEntities(vec3d, list);
    }

    private void checkSourcePermission(FabricClientCommandSource source) throws CommandSyntaxException {
        // Always able to use @ selectors
        if (((EntitySelectorAccessor) this).getUsesAt() && !source.hasPermissionLevel(0)) {
            throw EntityArgumentType.NOT_ALLOWED_EXCEPTION.create();
        }
    }

    private void appendEntitiesFromClientWorld(List<Entity> entities, ClientWorld world,
            Vec3d pos, Predicate<Entity> predicate) {
        int i = ((EntitySelectorAccessor) this).invokeGetAppendLimit();
        if (entities.size() >= i) {
            return;
        }
        if (((EntitySelectorAccessor) this).getBox() != null) {
            world.collectEntitiesByType(((EntitySelectorAccessor) this).getEntityFilter(), ((EntitySelectorAccessor) this).getBox().offset(pos), predicate, entities, i);
        } else {
            ((ClientWorldEntityCollector) world).UUIDCommand$collectEntitiesByType(((EntitySelectorAccessor) this).getEntityFilter(), predicate, entities, i);
        }
    }
}

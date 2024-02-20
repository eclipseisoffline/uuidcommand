package xyz.eclipseisoffline.uuidcommand;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.entity.Entity;

public interface ClientEntitySelector {

    Entity UUIDCommand$getEntity(FabricClientCommandSource source) throws CommandSyntaxException;
    List<? extends Entity> UUIDCommand$getEntities(FabricClientCommandSource source) throws CommandSyntaxException;
}

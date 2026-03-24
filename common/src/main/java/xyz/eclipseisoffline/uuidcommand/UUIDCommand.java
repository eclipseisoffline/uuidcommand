package xyz.eclipseisoffline.uuidcommand;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import xyz.eclipseisoffline.uuidcommand.command.ClientEntitySelector;

public class UUIDCommand implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("uuid")
                        .then(ClientCommandManager.argument("entity", EntityArgument.entity())
                                .executes(context -> {
                                    ClientEntitySelector selector = new ClientEntitySelector(context.getArgument("entity", EntitySelector.class));

                                    UUIDHolder entity = selector.getClientEntity(context.getSource());

                                    Component feedback = uuidCommand(entity);
                                    context.getSource().sendFeedback(feedback);
                                    return 0;
                                }))
        )));
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                Commands.literal("uuid")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.argument("entity", EntityArgument.entity())
                                .executes(context -> {
                                    UUIDHolder entity = (UUIDHolder) EntityArgument.getEntity(context, "entity");

                                    context.getSource().sendSuccess(() -> uuidCommand(entity), false);
                                    return 0;
                                })
                        )
        ));
    }

    private Component uuidCommand(UUIDHolder uuid) {
        Tag uuidNbt = UUIDUtil.CODEC.encodeStart(NbtOps.INSTANCE, uuid.UUIDCommand$getUUID()).getOrThrow();

        MutableComponent feedback = Component.literal("The uuid of ");
        feedback.append(uuid.UUIDCommand$getName());
        feedback.append(Component.literal(" is "));
        feedback.append(Component.literal(uuid.UUIDCommand$getUUID().toString())
                .withStyle(style -> style
                        .withUnderlined(true)
                        .withHoverEvent(new HoverEvent.ShowText(Component.nullToEmpty("Click to copy the UUID string to your clipboard")))
                        .withClickEvent(new ClickEvent.CopyToClipboard(uuid.UUIDCommand$getUUID().toString()))));
        feedback.append(Component.literal(" [copy data]").withStyle(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Component.nullToEmpty("Click to copy the UUID data element to your clipboard")))
                .withClickEvent(new ClickEvent.CopyToClipboard(NbtUtils.toPrettyComponent(uuidNbt).getString()))));

        return feedback;
    }
}

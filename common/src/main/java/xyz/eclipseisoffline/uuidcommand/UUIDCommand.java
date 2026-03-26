package xyz.eclipseisoffline.uuidcommand;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
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

import java.util.function.Consumer;

public abstract class UUIDCommand {
    public static final String MOD_ID = "uuidcommand";

    protected void initialize() {
        registerCommands(dispatcher -> dispatcher.register(
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

    protected void initializeClient() {
        registerClientCommands(dispatcher -> dispatcher.register(
                clientLiteral("uuid")
                        .then(clientArgument("entity", EntityArgument.entity())
                                .executes(context -> {
                                    ClientEntitySelector selector = new ClientEntitySelector(context.getArgument("entity", EntitySelector.class));

                                    UUIDHolder entity = selector.getClientEntity(context.getSource());

                                    ClientCommandUtil.sendFeedback(context.getSource(), uuidCommand(entity));
                                    return 0;
                                }))
        ));
    }

    protected abstract void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> registerer);

    protected abstract void registerClientCommands(Consumer<CommandDispatcher<SharedSuggestionProvider>> registerer);

    private LiteralArgumentBuilder<SharedSuggestionProvider> clientLiteral(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    private <T> RequiredArgumentBuilder<SharedSuggestionProvider, ?> clientArgument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
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

package xyz.eclipseisoffline.uuidcommand;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Uuids;
import xyz.eclipseisoffline.uuidcommand.command.ClientEntitySelector;

public class UUIDCommand implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("uuid")
                        .then(ClientCommandManager.argument("entity", EntityArgumentType.entity())
                                .executes(context -> {
                                    ClientEntitySelector selector = new ClientEntitySelector(context.getArgument("entity", EntitySelector.class));

                                    UUIDHolder entity = selector.getClientEntity(context.getSource());

                                    Text feedback = uuidCommand(entity);
                                    context.getSource().sendFeedback(feedback);
                                    return 0;
                                }))
        )));
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                CommandManager.literal("uuid")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("entity", EntityArgumentType.entity())
                                .executes(context -> {
                                    UUIDHolder entity = (UUIDHolder) EntityArgumentType.getEntity(context, "entity");

                                    context.getSource().sendFeedback(() -> uuidCommand(entity), false);
                                    return 0;
                                })
                        )
        ));
    }

    private Text uuidCommand(UUIDHolder uuid) {
        NbtElement uuidNbt = Uuids.INT_STREAM_CODEC.encodeStart(NbtOps.INSTANCE, uuid.UUIDCommand$getUUID()).getOrThrow();

        MutableText feedback = Text.literal("The uuid of ");
        feedback.append(uuid.UUIDCommand$getName());
        feedback.append(Text.literal(" is "));
        feedback.append(Text.literal(uuid.UUIDCommand$getUUID().toString())
                .styled(style -> style
                        .withUnderline(true)
                        .withHoverEvent(new HoverEvent.ShowText(Text.of("Click to copy the UUID string to your clipboard")))
                        .withClickEvent(new ClickEvent.CopyToClipboard(uuid.UUIDCommand$getUUID().toString()))));
        feedback.append(Text.literal(" [copy data]").styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.of("Click to copy the UUID data element to your clipboard")))
                .withClickEvent(new ClickEvent.CopyToClipboard(NbtHelper.toPrettyPrintedText(uuidNbt).getString()))));

        return feedback;
    }
}

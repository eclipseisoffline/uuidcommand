package xyz.eclipseisoffline.uuidcommand;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import xyz.eclipseisoffline.uuidcommand.command.ClientEntitySelector;

public class UUIDCommand implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("uuid")
                        .then(ClientCommandManager.argument("entity", EntityArgumentType.entity())
                                .executes(context -> {
                                    ClientEntitySelector selector = new ClientEntitySelector(context.getArgument("entity", EntitySelector.class));

                                    UUIDHolder entity = selector.getClientEntity(context.getSource());

                                    MutableText result = Text.literal("The uuid of ");
                                    result.append(entity.UUIDCommand$getName());
                                    result.append(Text.literal(" is "));
                                    result.append(Text.literal(entity.UUIDCommand$getUUID().toString())
                                            .styled(style -> {
                                                return style
                                                        .withUnderline(true)
                                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                                Text.of("Click to copy the UUID string to your clipboard")))
                                                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, entity.UUIDCommand$getUUID().toString()));
                                            }));
                                    result.append(Text.literal(" [copy data]").styled(style -> {
                                        return style
                                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                        Text.of("Click to copy the UUID data element to your clipboard")))
                                                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,
                                                        NbtHelper.toPrettyPrintedText(NbtHelper.fromUuid(entity.UUIDCommand$getUUID())).getString()));
                                    }));

                                    context.getSource().sendFeedback(result);
                                    return 0;
                                }))
        )));
    }
}

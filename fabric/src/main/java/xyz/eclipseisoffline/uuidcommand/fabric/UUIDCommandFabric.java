package xyz.eclipseisoffline.uuidcommand.fabric;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import xyz.eclipseisoffline.uuidcommand.UUIDCommand;

import java.util.function.Consumer;

public class UUIDCommandFabric extends UUIDCommand implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        initialize();
    }

    @Override
    public void onInitializeClient() {
        initializeClient();
    }

    @Override
    protected void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> registerer) {
        CommandRegistrationCallback.EVENT.register((dispatcher, _, _) -> registerer.accept(dispatcher));
    }

    @Override
    protected void registerClientCommands(Consumer<CommandDispatcher<SharedSuggestionProvider>> registerer) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            //noinspection rawtypes,unchecked - FabricClientCommandSource is implemented on ClientSuggestionProvider
            registerer.accept((CommandDispatcher) dispatcher);
        });
    }
}

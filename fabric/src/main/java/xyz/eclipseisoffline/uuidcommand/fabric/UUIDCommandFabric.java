package xyz.eclipseisoffline.uuidcommand.fabric;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
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
    protected void registerClientCommands(Consumer<CommandDispatcher<ClientSuggestionProvider>> registerer) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            //noinspection rawtypes,unchecked - FabricClientCommandSource is implemented on ClientSuggestionProvider
            registerer.accept((CommandDispatcher) dispatcher);
        });
    }

    @Override
    protected LiteralArgumentBuilder<ClientSuggestionProvider> clientLiteral(String literal) {
        //noinspection rawtypes,unchecked - same as above
        return (LiteralArgumentBuilder) ClientCommands.literal(literal);
    }

    @Override
    protected <T> RequiredArgumentBuilder<ClientSuggestionProvider, ?> clientArgument(String name, ArgumentType<T> type) {
        //noinspection rawtypes,unchecked - same as above
        return (RequiredArgumentBuilder) ClientCommands.argument(name, type);
    }
}

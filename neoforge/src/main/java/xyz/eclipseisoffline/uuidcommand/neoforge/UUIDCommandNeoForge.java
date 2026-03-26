package xyz.eclipseisoffline.uuidcommand.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import xyz.eclipseisoffline.uuidcommand.UUIDCommand;

import java.util.function.Consumer;

@Mod(UUIDCommand.MOD_ID)
public class UUIDCommandNeoForge extends UUIDCommand {

    public UUIDCommandNeoForge() {
        initialize();
        initializeClient();
    }

    @Override
    protected void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> registerer) {
        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> registerer.accept(event.getDispatcher()));
    }

    @Override
    protected void registerClientCommands(Consumer<CommandDispatcher<SharedSuggestionProvider>> registerer) {
        NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class, event -> {
            //noinspection rawtypes,unchecked - CommandSourceStack is a SharedSuggestionProvider
            registerer.accept((CommandDispatcher) event.getDispatcher());
        });
    }
}

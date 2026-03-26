package xyz.eclipseisoffline.uuidcommand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import xyz.eclipseisoffline.uuidcommand.mixin.ClientSuggestionProviderAccessor;

import java.util.Objects;

public interface ClientCommandUtil {

    static void sendFeedback(SharedSuggestionProvider suggestionProvider, Component message) {
        getMinecraft(suggestionProvider).gui.getChat().addClientSystemMessage(message);
        getMinecraft(suggestionProvider).getNarrator().saySystemChatQueued(message);
    }

    static LocalPlayer getPlayer(SharedSuggestionProvider suggestionProvider) {
        return Objects.requireNonNull(getMinecraft(suggestionProvider).player);
    }

    static ClientLevel getLevel(SharedSuggestionProvider suggestionProvider) {
        return Objects.requireNonNull(getMinecraft(suggestionProvider).level);
    }

    static ClientPacketListener getConnection(SharedSuggestionProvider suggestionProvider) {
        if (suggestionProvider instanceof ClientSuggestionProvider) {
            return ((ClientSuggestionProviderAccessor) suggestionProvider).getConnection();
        }
        // On NeoForge
        return Objects.requireNonNull(Minecraft.getInstance().getConnection());
    }

    static Minecraft getMinecraft(SharedSuggestionProvider suggestionProvider) {
        if (suggestionProvider instanceof ClientSuggestionProvider) {
            return ((ClientSuggestionProviderAccessor) suggestionProvider).getMinecraft();
        }
        // On NeoForge
        return Minecraft.getInstance();
    }
}

package xyz.eclipseisoffline.uuidcommand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import xyz.eclipseisoffline.uuidcommand.mixin.ClientSuggestionProviderAccessor;

import java.util.Objects;

public interface ClientCommandUtil {

    static void sendFeedback(ClientSuggestionProvider suggestionProvider, Component message) {
        getMinecraft(suggestionProvider).gui.getChat().addClientSystemMessage(message);
        getMinecraft(suggestionProvider).getNarrator().saySystemChatQueued(message);
    }

    static LocalPlayer getPlayer(ClientSuggestionProvider suggestionProvider) {
        return Objects.requireNonNull(getMinecraft(suggestionProvider).player);
    }

    static ClientLevel getLevel(ClientSuggestionProvider suggestionProvider) {
        return Objects.requireNonNull(getMinecraft(suggestionProvider).level);
    }

    static ClientPacketListener getConnection(ClientSuggestionProvider suggestionProvider) {
        return ((ClientSuggestionProviderAccessor) suggestionProvider).getConnection();
    }

    static Minecraft getMinecraft(ClientSuggestionProvider suggestionProvider) {
        return ((ClientSuggestionProviderAccessor) suggestionProvider).getMinecraft();
    }
}

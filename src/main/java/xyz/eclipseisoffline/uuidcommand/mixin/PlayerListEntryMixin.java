package xyz.eclipseisoffline.uuidcommand.mixin;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.eclipseisoffline.uuidcommand.UUIDHolder;

@Mixin(PlayerInfo.class)
public abstract class PlayerListEntryMixin implements UUIDHolder {

    @Shadow public abstract GameProfile getProfile();

    @Shadow public abstract @Nullable Component getTabListDisplayName();

    @Override
    public UUID UUIDCommand$getUUID() {
        return getProfile().id();
    }

    @Override
    public Component UUIDCommand$getName() {
        Component displayName = getTabListDisplayName();
        if (displayName != null) {
            return getTabListDisplayName();
        }

        return Component.nullToEmpty(getProfile().name());
    }
}

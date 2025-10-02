package xyz.eclipseisoffline.uuidcommand.mixin;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.eclipseisoffline.uuidcommand.UUIDHolder;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin implements UUIDHolder {

    @Shadow public abstract GameProfile getProfile();

    @Shadow public abstract @Nullable Text getDisplayName();

    @Override
    public UUID UUIDCommand$getUUID() {
        return getProfile().id();
    }

    @Override
    public Text UUIDCommand$getName() {
        Text displayName = getDisplayName();
        if (displayName != null) {
            return getDisplayName();
        }

        return Text.of(getProfile().name());
    }
}

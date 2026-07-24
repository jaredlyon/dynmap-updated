package org.dynmap.fabric_26_2.mixin;

import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.FilterMask;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.dynmap.fabric_26_2.event.BlockEvents;
import org.dynmap.fabric_26_2.event.ServerChatEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayer player;

    @Inject(
            method = "broadcastChatMessage",
            at = @At(
                    value = "HEAD"
            )
    )
    public void onGameMessage(PlayerChatMessage signedMessage, CallbackInfo ci) {
        ServerChatEvents.EVENT.invoker().onChatMessage(player, signedMessage.decoratedContent().getString());
    }

    @Inject(
            method = "updateSignText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/SignBlockEntity;updateSignText(Lnet/minecraft/world/entity/player/Player;ZLjava/util/List;)V",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    public void onSignUpdate(ServerboundSignUpdatePacket packet, List<FilteredText> signText, CallbackInfo ci,
                             ServerLevel serverWorld, BlockPos blockPos, BlockEntity blockEntity, SignBlockEntity signBlockEntity)
    {
        // Pull the raw text from the input.
        String[] rawTexts = new String[4];
        for (int i=0; i<signText.size(); i++)
            rawTexts[i] = signText.get(i).raw();

        // Fire the event.
        BlockEvents.SIGN_CHANGE_EVENT.invoker().onSignChange(serverWorld, blockPos, rawTexts, player, packet.isFrontText());

        // Rebuild the signText list with the new values.
        List<FilteredText> newSignText = Arrays.stream(rawTexts).map((raw) -> new FilteredText(raw, FilterMask.PASS_THROUGH)).toList();

        // Execute the setting of the texts with the edited values.
        signBlockEntity.updateSignText(this.player, packet.isFrontText(), newSignText);

        // Cancel the original tryChangeText() since we're calling it ourselves above.
        ci.cancel();
    }
}

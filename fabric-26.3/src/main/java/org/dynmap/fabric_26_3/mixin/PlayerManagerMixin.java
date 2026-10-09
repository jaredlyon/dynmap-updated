package org.dynmap.fabric_26_3.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.dynmap.fabric_26_3.event.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void onPlayerConnect(Connection connection, ServerPlayer player, CommonListenerCookie ccd, CallbackInfo info) {
        PlayerEvents.PLAYER_LOGGED_IN.invoker().onPlayerLoggedIn(player);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    public void remove(ServerPlayer player, CallbackInfo info) {
        PlayerEvents.PLAYER_LOGGED_OUT.invoker().onPlayerLoggedOut(player);
    }

    @Inject(method = "respawn", at = @At("RETURN"))
    public void respawnPlayer(ServerPlayer player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> info) {
        PlayerEvents.PLAYER_RESPAWN.invoker().onPlayerRespawn(info.getReturnValue());
    }
}

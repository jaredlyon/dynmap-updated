package org.dynmap.fabric_26_2.permissions;

import java.util.Set;
import net.minecraft.world.entity.player.Player;

public interface PermissionProvider {
    boolean has(Player sender, String permission);

    boolean hasPermissionNode(Player sender, String permission);

    Set<String> hasOfflinePermissions(String player, Set<String> perms);

    boolean hasOfflinePermission(String player, String perm);

}

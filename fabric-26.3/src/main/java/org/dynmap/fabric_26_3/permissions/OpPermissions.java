package org.dynmap.fabric_26_3.permissions;

import org.dynmap.Log;
import org.dynmap.fabric_26_3.DynmapPlugin;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.entity.player.Player;

public class OpPermissions implements PermissionProvider {
    public HashSet<String> usrCommands = new HashSet<String>();

    public OpPermissions(String[] usrCommands) {
        for (String usrCommand : usrCommands) {
            this.usrCommands.add(usrCommand);
        }
        Log.info("Using ops.txt for access control");
    }

    @Override
    public Set<String> hasOfflinePermissions(String player, Set<String> perms) {
        HashSet<String> rslt = new HashSet<String>();
        if (DynmapPlugin.plugin.isOp(player)) {
            rslt.addAll(perms);
        }
        return rslt;
    }

    @Override
    public boolean hasOfflinePermission(String player, String perm) {
        return DynmapPlugin.plugin.isOp(player);
    }

    @Override
    public boolean has(Player psender, String permission) {
        if (psender != null) {
            if (usrCommands.contains(permission)) {
                return true;
            }
            return DynmapPlugin.plugin.isOp(psender.getName().getString());
        }
        return true;
    }

    @Override
    public boolean hasPermissionNode(Player psender, String permission) {
        if (psender != null) {
            return DynmapPlugin.plugin.isOp(psender.getName().getString());
        }
        return true;
    }
}

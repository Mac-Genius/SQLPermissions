package io.github.mac_genius.sqlpermissions.Permissions;

import io.github.mac_genius.sqlpermissions.PluginSettings;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

/**
 * Created by Mac on 10/26/2015.
 */
public class PermUser {
    private PermGroup group;
    private PermissionAttachment attachment;
    private Player player;
    private PluginSettings settings;

    public PermUser(PermGroup group, Player player, PluginSettings settings) {
        this.group = group;
        this.player = player;
        this.settings = settings;
        setPermissions();
    }

    public PermGroup getGroup() {
        return group;
    }

    public PermissionAttachment getAttachment() {
        return attachment;
    }

    public void refreshPermissions() {
        attachment.remove();
        setPermissions();
    }

    private void setPermissions() {
        attachment = player.addAttachment(settings.getPlugin());
        group.getPlayers().put(player, this);
        for (String s : group.getPermissions()) {
            attachment.setPermission(s, true);
        }
        //player.setDisplayName(ChatColor.translateAlternateColorCodes('&', group.getPrefix() + player.getName()));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER));

    }

    public void changeGroup(PermGroup newGroup) {
        group.getPlayers().remove(player);
        attachment.remove();
        this.group = newGroup;
        setPermissions();
    }

    public void unregister() {
        attachment.remove();
    }
}

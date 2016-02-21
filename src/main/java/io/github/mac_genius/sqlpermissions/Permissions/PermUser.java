package io.github.mac_genius.sqlpermissions.Permissions;

import com.mojang.authlib.GameProfile;
import io.github.mac_genius.sqlpermissions.PluginSettings;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.List;

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
        //alterName(ChatColor.translateAlternateColorCodes('&', group.getPrefix()) + player.getName());
        nameSetup();
    }

    public void changeGroup(PermGroup newGroup) {
        group.getPlayers().remove(player);
        attachment.remove();
        this.group = newGroup;
        setPermissions();
        updateScoreboard();
    }

    public void unregister() {
        attachment.remove();
    }

    private void alterName(String newName) {
        EntityPlayer alteredPlayer = ((CraftPlayer) player).getHandle();
        alteredPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, alteredPlayer));
        try {
            GameProfile gameProfile = alteredPlayer.getProfile();
            Class<?> profile = gameProfile.getClass();
            Field name = profile.getDeclaredField("name");
            name.setAccessible(true);
            name.set(gameProfile, "Notch");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        alteredPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, alteredPlayer));
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player) {
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(alteredPlayer.getId()));
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(alteredPlayer));
            }
        }
    }

    private void alterPacket(String newName) {
        EntityPlayer alteredPlayer = ((CraftPlayer) player).getHandle();
        alteredPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, alteredPlayer));
        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, alteredPlayer);
        try {
            Class<?> profile = info.getClass();
            Field name = profile.getDeclaredField("b");
            name.setAccessible(true);
            List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List) name.get(info);
            Class<?> data = players.get(0).getClass();
            Field infoData = data.getDeclaredField("");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        alteredPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, alteredPlayer));
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player) {
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(alteredPlayer.getId()));
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(alteredPlayer));
            }
        }
    }

    public void nameSetup() {
        for (PermGroup g : settings.getGroups()) {
            Team team;
            if (player.getScoreboard().getTeam(g.getName()) == null) {
                team = player.getScoreboard().registerNewTeam(g.getName());
            } else {
                team = player.getScoreboard().getTeam(g.getName());
            }
            //team.setSuffix(ChatColor.translateAlternateColorCodes('&', g.getSuffix()));
            team.setPrefix(ChatColor.translateAlternateColorCodes('&', g.getPrefix()));
        }
        settings.getPlayerScoreboards().put(player, player.getScoreboard());
        player.getScoreboard().getTeam(group.getName()).addEntry(player.getName());
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player) {
                p.getScoreboard().getTeam(group.getName()).addEntry(player.getName());
                player.getScoreboard().getTeam(settings.getPlayerPerms().get(p).getGroup().getName()).addEntry(p.getName());
            }
        }
        player.setDisplayName(ChatColor.translateAlternateColorCodes('&', group.getPrefix() + player.getName()) + ChatColor.WHITE);
    }

    public void updateScoreboard() {
        player.getScoreboard().getTeam(group.getName()).addEntry(player.getName());
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player) {
                p.getScoreboard().getTeam(group.getName()).addEntry(player.getName());
            }
        }
        player.setDisplayName(ChatColor.translateAlternateColorCodes('&', group.getPrefix() + player.getName()) + ChatColor.WHITE);
    }
}

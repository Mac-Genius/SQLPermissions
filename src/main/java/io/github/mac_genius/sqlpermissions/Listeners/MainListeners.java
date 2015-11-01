package io.github.mac_genius.sqlpermissions.Listeners;

import io.github.mac_genius.sqlpermissions.Database.SQLUser;
import io.github.mac_genius.sqlpermissions.Permissions.PermGroup;
import io.github.mac_genius.sqlpermissions.Permissions.PermUser;
import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Mac on 10/26/2015.
 */
public class MainListeners implements Listener {
    private PluginSettings settings;

    public MainListeners(PluginSettings settings) {
        this.settings = settings;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        String group = new SQLUser(settings).getGroup(event.getPlayer());
        for (PermGroup g : settings.getGroups()) {
            if (g.getName().equals(group)) {
                PermUser user = new PermUser(g, event.getPlayer(), settings);
                settings.getPlayerPerms().put(event.getPlayer(), user);
            }
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != event.getPlayer()) {
                p.getScoreboard().getTeam(settings.getPlayerPerms().get(event.getPlayer()).getGroup().getName()).removeEntry(event.getPlayer().getName());
            }
        }
        settings.getPlayerPerms().get(event.getPlayer()).getGroup().getPlayers().remove(event.getPlayer());
        settings.getPlayerPerms().get(event.getPlayer()).unregister();
        settings.getPlayerPerms().remove(event.getPlayer());
    }

    @EventHandler
    public void playerKick(PlayerKickEvent event) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != event.getPlayer()) {
                p.getScoreboard().getTeam(settings.getPlayerPerms().get(event.getPlayer()).getGroup().getName()).removeEntry(event.getPlayer().getName());
            }
        }
        settings.getPlayerPerms().get(event.getPlayer()).getGroup().getPlayers().remove(event.getPlayer());
        settings.getPlayerPerms().get(event.getPlayer()).unregister();
        settings.getPlayerPerms().remove(event.getPlayer());
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        event.setFormat("%s -> %s");
    }
}

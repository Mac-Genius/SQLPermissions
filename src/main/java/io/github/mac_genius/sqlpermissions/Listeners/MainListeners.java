package io.github.mac_genius.sqlpermissions.Listeners;

import io.github.mac_genius.sqlpermissions.Database.SQLUser;
import io.github.mac_genius.sqlpermissions.Permissions.PermGroup;
import io.github.mac_genius.sqlpermissions.Permissions.PermUser;
import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        settings.getPlayerPerms().get(event.getPlayer()).getGroup().getPlayers().remove(event.getPlayer());
        settings.getPlayerPerms().get(event.getPlayer()).unregister();
        settings.getPlayerPerms().remove(event.getPlayer());
    }

    @EventHandler
    public void playerKick(PlayerKickEvent event) {
        settings.getPlayerPerms().get(event.getPlayer()).getGroup().getPlayers().remove(event.getPlayer());
        settings.getPlayerPerms().get(event.getPlayer()).unregister();
        settings.getPlayerPerms().remove(event.getPlayer());
    }
}

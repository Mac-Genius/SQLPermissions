package io.github.mac_genius.sqlpermissions;

import io.github.mac_genius.sqlpermissions.Commands.PermCommand;
import io.github.mac_genius.sqlpermissions.Listeners.MainListeners;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Mac on 10/26/2015.
 */
public class SQLPermissions extends JavaPlugin {
    private Plugin plugin = this;
    private PluginSettings settings;

    @Override
    public void onEnable() {
        settings = new PluginSettings(plugin);
        settings.getPlugin().getServer().getPluginManager().registerEvents(new MainListeners(settings), settings.getPlugin());
        this.getCommand("perm").setExecutor(new PermCommand(settings));
        plugin.getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            settings.getPlayerPerms().get(p).unregister();
        }
        plugin.getLogger().info("Plugin disabled");
    }
}

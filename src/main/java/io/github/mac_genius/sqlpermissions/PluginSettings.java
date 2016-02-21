package io.github.mac_genius.sqlpermissions;

import io.github.mac_genius.sqlpermissions.Database.SQLConnect;
import io.github.mac_genius.sqlpermissions.Database.SQLGroup;
import io.github.mac_genius.sqlpermissions.Permissions.PermGroup;
import io.github.mac_genius.sqlpermissions.Permissions.PermUser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mac on 10/26/2015.
 */
public class PluginSettings {
    private Plugin plugin;
    private SQLConnect connect;
    private ArrayList<PermGroup> groups;
    private Map<Player, PermUser> playerPerms;
    private Map<Player, Scoreboard> playerScoreboards;

    public PluginSettings(Plugin plugin) {
        this.plugin = plugin;
        setupConfig();
        setupDatabase();
        setupGroups();
        setupPerms();
        setupScoreboards();
    }

    public Plugin getPlugin() {
        return plugin;
    }

    private void setupDatabase() {
        connect = new SQLConnect(this);
        if (connect.testConnection()) {
            connect.updateDatabase();
            connect.databaseSetup();
            plugin.getLogger().info(Ansi.ansi().fg(Ansi.Color.GREEN) + "Connected to the database!" + Ansi.ansi().fg(Ansi.Color.WHITE));
        } else {
            plugin.getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not connect to the database!" + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    public SQLConnect getConnect() {
        return connect;
    }

    private void setupGroups() {
        SQLGroup group = new SQLGroup(this);
        groups = group.getGroups();
    }

    private void setupConfig() {
        plugin.saveDefaultConfig();
    }

    private void setupPerms() {
        playerPerms = Collections.synchronizedMap(new HashMap<Player, PermUser>());
    }

    private void setupScoreboards() {
        playerScoreboards = Collections.synchronizedMap(new HashMap<>());
    }

    public ArrayList<PermGroup> getGroups() {
        return groups;
    }

    public Map<Player, PermUser> getPlayerPerms() {
        return playerPerms;
    }

    public Map<Player, Scoreboard> getPlayerScoreboards() {
        return playerScoreboards;
    }
}

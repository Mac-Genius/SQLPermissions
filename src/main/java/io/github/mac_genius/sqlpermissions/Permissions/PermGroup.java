package io.github.mac_genius.sqlpermissions.Permissions;

import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mac on 10/26/2015.
 */
public class PermGroup {
    private String name;
    private String prefix;
    private String suffix;
    private boolean defaultGroup;
    private ArrayList<String> permissions;
    private Map<Player, PermUser> players;
    private int rank;

    public PermGroup(String name, String prefix, String suffix, boolean defaultGroup, ArrayList<String> permissions) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.defaultGroup = defaultGroup;
        this.permissions = permissions;
        players = Collections.synchronizedMap(new HashMap<Player, PermUser>());
    }

    public PermGroup() {}

    public boolean isDefaultGroup() {
        return defaultGroup;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getRank() { return rank; }

    public void setRank(int rank) { this.rank = rank; }

    public void setDefaultGroup(boolean defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void addPermission(String permission) {
        permissions.add(permission);
        for (PermUser p : players.values()) {
            p.refreshPermissions();
        }
    }

    public void removePermission(String permission) {
        if (permissions.contains(permission)) {
            permissions.remove(permission);
        }
        for (PermUser p : players.values()) {
            p.refreshPermissions();
        }
    }

    public Map<Player, PermUser> getPlayers() {
        return players;
    }

    public void addPlayer(Player player, PluginSettings settings) {
        players.put(player, settings.getPlayerPerms().get(player));
        settings.getPlayerPerms().get(player).changeGroup(this);
        settings.getPlayerPerms().get(player).refreshPermissions();
    }
}

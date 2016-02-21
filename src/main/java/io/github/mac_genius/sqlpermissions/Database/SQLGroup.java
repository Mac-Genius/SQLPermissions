package io.github.mac_genius.sqlpermissions.Database;

import io.github.mac_genius.sqlpermissions.Permissions.PermGroup;
import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.fusesource.jansi.Ansi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mac on 10/26/2015.
 */
public class SQLGroup {
    private PluginSettings settings;
    private final String groupHeader = "SQLPerm_Group_";
    private final String groupList = "SQLPerm_GroupList";

    public SQLGroup(PluginSettings settings) {
        this.settings = settings;
    }

    public ArrayList<PermGroup> getGroups() {
        Connection connection = settings.getConnect().getConnection();
        ArrayList<PermGroup> groups = new ArrayList<>();
        try {
            PreparedStatement fetch = connection.prepareStatement("SELECT * FROM " + groupList);
            ResultSet results = fetch.executeQuery();
            while (results.next()) {
                PermGroup group = new PermGroup(results.getString(2), results.getString(3), results.getString(4), results.getBoolean(5), getPermissions(results.getString(2)));
                groups.add(group);
            }
            connection.close();
            return groups;
        } catch (SQLException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error getting the groups." + Ansi.ansi().fg(Ansi.Color.WHITE));
            return groups;
        }
    }

    public void addGroup(PermGroup permGroup) {
        Connection connection = settings.getConnect().getConnection();
        try {
            PreparedStatement add = connection.prepareStatement("INSERT INTO " + groupList + "(Name, Prefix, Suffix, DefaultG, Rank) VALUES(?, ?, ?, ?, ?)");
            add.setString(1, permGroup.getName());
            add.setString(2, permGroup.getPrefix());
            add.setString(3, permGroup.getSuffix());
            add.setBoolean(4, permGroup.isDefaultGroup());
            add.setInt(5, permGroup.getRank());
            add.executeUpdate();
            addGroupPerms(permGroup.getName());
            settings.getGroups().add(permGroup);
            connection.close();
        } catch (SQLException c) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not add the player to the whitelist." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    private void addGroupPerms(String group) {
        Connection connection = settings.getConnect().getConnection();
        String table = groupHeader + group;
        try {
            PreparedStatement tokoins = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table + "(Id INT PRIMARY KEY AUTO_INCREMENT, Permission VARCHAR(60))");
            tokoins.executeUpdate();
            tokoins.close();
            connection.close();
        } catch (SQLException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not create the " + table + " table." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    public void removeGroup(String name) {
        Connection connection = settings.getConnect().getConnection();
        try {
            PreparedStatement add = connection.prepareStatement("DELETE FROM " + groupList + " WHERE Name='" + name + "'");
            removeGroupTable(name);
            connection.close();
        } catch (SQLException c) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not add the player to the whitelist." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    private void removeGroupTable(String group) {
        Connection connection = settings.getConnect().getConnection();
        String table = groupHeader + group;
        try {
            PreparedStatement tokoins = connection.prepareStatement("DROP TABLE " + table);
            tokoins.executeUpdate();
            tokoins.close();
            connection.close();
        } catch (SQLException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not delete the " + table + " table." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    private ArrayList<String> getPermissions(String group) {
        Connection connection = settings.getConnect().getConnection();
        String table = groupHeader + group;
        ArrayList<String> permissions = new ArrayList<>();
        try {
            PreparedStatement fetch = connection.prepareStatement("SELECT * FROM " + table);
            ResultSet results = fetch.executeQuery();
            while (results.next()) {
                permissions.add(results.getString(2));
            }
            connection.close();
            return permissions;
        } catch (SQLException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error getting the permissions." + Ansi.ansi().fg(Ansi.Color.WHITE));
            return permissions;
        }
    }

    public void addPermission(String group, String permission) {
        Connection connection = settings.getConnect().getConnection();
        String table = groupHeader + group;
        try {
            PreparedStatement fetch = connection.prepareStatement("INSERT INTO " + table + "(Permission) VALUES(?)");
            fetch.setString(1, permission);
            fetch.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error addind the permissions." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    public void deletePermission(String group, String permission) {
        Connection connection = settings.getConnect().getConnection();
        String table = groupHeader + group;
        try {
            PreparedStatement fetch = connection.prepareStatement("DELETE FROM " + table + " WHERE Permission='" + permission + "'");
            fetch.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error deleting the permissions." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    public void renamePrefix(String prefix, String group) {
        Connection connection = settings.getConnect().getConnection();
        try {
            PreparedStatement change = connection.prepareStatement("UPDATE "+ groupList + " SET Prefix='" + prefix + "' WHERE Name='" + group + "'");
            change.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error deleting the permissions." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }
}

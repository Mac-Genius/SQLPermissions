package io.github.mac_genius.sqlpermissions.Database;

import io.github.mac_genius.sqlpermissions.Permissions.PermGroup;
import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.Bukkit;
import org.fusesource.jansi.Ansi;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Mac on 10/26/2015.
 */
public class SQLConnect {
    private PluginSettings settings;

    public SQLConnect(PluginSettings settings) {
        this.settings = settings;
    }

    public void databaseSetup() {
        Connection connection = getConnection();
        if (connection != null) {
            for (String s : tables()) {
                try {
                    PreparedStatement tokoins = connection.prepareStatement(s);
                    tokoins.executeUpdate();
                    tokoins.close();
                } catch (SQLException e) {
                    settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not create the PermUsers table." + Ansi.ansi().fg(Ansi.Color.WHITE));
                }
            }
            try {
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not close the connection." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not find the connection driver. Make sure it is installed and try again." + Ansi.ansi().fg(Ansi.Color.WHITE));
            return null;
        }
        Connection con = null;

        String url = "jdbc:mysql://" + settings.getPlugin().getConfig().getString("ip address") + "/" + settings.getPlugin().getConfig().getString("database");
        String user = settings.getPlugin().getConfig().getString("user");
        String password = settings.getPlugin().getConfig().getString("password");

        try {
            con = DriverManager.getConnection(url, user, password);

        } catch (SQLException ex) {
            Bukkit.getLogger().warning("Error brah");

        }
        return con;
    }

    public boolean testConnection() {
        Connection connection = getConnection();
        if (connection != null) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> tables() {
        ArrayList<String> tables = new ArrayList<>();
        tables.add("CREATE TABLE IF NOT EXISTS SQLPerm_DatabaseInfo(Id INT PRIMARY KEY AUTO_INCREMENT, Version VARCHAR(15))");
        tables.add("CREATE TABLE IF NOT EXISTS SQLPerm_PlayerList(Id INT PRIMARY KEY AUTO_INCREMENT, Uuid VARCHAR(36), Groups VARCHAR(25))");
        tables.add("CREATE TABLE IF NOT EXISTS SQLPerm_GroupList(Id INT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(25), Prefix VARCHAR(25), Suffix VARCHAR(25), DefaultG BOOLEAN, Rank INTEGER)");
        return tables;
    }

    public void updateDatabase() {
        Connection connection = getConnection();
        if (connection != null) {
            double version = getDatabaseVersion(connection);
            if (version == 1.0) {
                from1_0(connection);
            }
            else {
                settings.getPlugin().getLogger().info(Ansi.ansi().fg(Ansi.Color.GREEN) + "Database is up to date." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
            try {
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not close the connection." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
    }

    private double getDatabaseVersion(Connection connection) {
        double version = -1.0;
        try {
            PreparedStatement tokoins = connection.prepareStatement("SELECT * FROM SQLPerm_DatabaseInfo WHERE Id='1'");
            ResultSet query = tokoins.executeQuery();
            while (query.next()) {
                version = Double.parseDouble(query.getString(2));
            }
            tokoins.close();
            return version;
        } catch (SQLException e) {
            //settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Nothing to update." + Ansi.ansi().fg(Ansi.Color.WHITE));
            return version;
        }
    }

    private void from1_0(Connection connection) {
        settings.getPlugin().getLogger().info("Updating database from version 1.0");
        ArrayList<String> statements = new ArrayList<>();
        for (PermGroup g : new SQLGroup(settings).getGroups()) {
            statements.add("ALTER TABLE Perm" + g.getName() + " RENAME TO SLQPerm_Group_" + g.getName());
        }
        statements.add("ALTER TABLE PermGroups RENAME TO SQLPerm_GroupList");
        statements.add("ALTER TABLE PermUsers RENAME TO SQLPerm_PlayerList");
        statements.add("UPDATE SQLPerm_DatabaseInfo SET Version='" + settings.getPlugin().getDescription().getVersion() + "' WHERE Id='1'");
        for (String s : statements) {
            try {
                PreparedStatement tokoins = connection.prepareStatement(s);
                tokoins.executeUpdate();
                tokoins.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not update the tables." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        settings.getPlugin().getLogger().info("Update complete!");
    }
}

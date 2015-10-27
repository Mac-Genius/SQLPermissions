package io.github.mac_genius.sqlpermissions.Database;

import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.Bukkit;
import org.fusesource.jansi.Ansi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            try {
                PreparedStatement tokoins = connection.prepareStatement("CREATE TABLE IF NOT EXISTS PermUsers(Id INT PRIMARY KEY AUTO_INCREMENT, Uuid VARCHAR(36), Groups VARCHAR(25))");
                tokoins.executeUpdate();
                tokoins.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not create the PermUsers table." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
            try {
                PreparedStatement whitelist = connection.prepareStatement("CREATE TABLE IF NOT EXISTS PermGroups(Id INT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(25), Prefix VARCHAR(25), Suffix VARCHAR(25), DefaultG BOOLEAN, Rank INTEGER)");
                whitelist.executeUpdate();
                whitelist.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not create the PermGroups table." + Ansi.ansi().fg(Ansi.Color.WHITE));
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
}

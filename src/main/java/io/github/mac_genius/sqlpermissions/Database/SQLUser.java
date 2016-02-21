package io.github.mac_genius.sqlpermissions.Database;

import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.entity.Player;
import org.fusesource.jansi.Ansi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Mac on 10/26/2015.
 */
public class SQLUser {
    private PluginSettings settings;
    private final String playerList = "SQLPerm_PlayerList";

    public SQLUser(PluginSettings settings) {
        this.settings = settings;
    }

    public String getGroup(Player player) {
        Connection connection = settings.getConnect().getConnection();
        String uuid = player.getUniqueId().toString();
        try {
            PreparedStatement fetch = connection.prepareStatement("SELECT * FROM " + playerList + " WHERE Uuid='" + uuid + "'");
            ResultSet results = fetch.executeQuery();
            String fetcheduuid = "";
            String group = "";
            while (results.next()) {
                fetcheduuid = results.getString(2);
                group = results.getString(3);
            }
            if (fetcheduuid.equals("")) {
                addToTable(player);
                group = "Default";
            }
            connection.close();
            return group;
        } catch (SQLException e) {
            addToTable(player);
            return "Default";
        }
    }

    private void addToTable(Player player) {
        Connection connection = settings.getConnect().getConnection();
        try {
            PreparedStatement add = connection.prepareStatement("INSERT INTO " + playerList + "(Uuid, Groups) VALUES(?, ?)");
            add.setString(1, player.getUniqueId().toString());
            add.setString(2, "Default");
            add.executeUpdate();
            connection.close();
        } catch (SQLException c) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not add the player to " + playerList + "." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    public void setGroup(Player player, String group) {
        Connection connection = settings.getConnect().getConnection();
        try {
            PreparedStatement add = connection.prepareStatement("UPDATE " + playerList + " SET Groups='" + group + "' WHERE Uuid='" + player.getUniqueId() + "'");
            add.executeUpdate();
            connection.close();
        } catch (SQLException c) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not update the player's group." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }
}

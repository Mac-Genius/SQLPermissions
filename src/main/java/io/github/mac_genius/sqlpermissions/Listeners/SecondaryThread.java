package io.github.mac_genius.sqlpermissions.Listeners;

import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Mac on 10/28/2015.
 */
public class SecondaryThread implements Runnable {
    private PluginSettings settings;

    public SecondaryThread(PluginSettings settings) {
        this.settings = settings;
    }

    @Override
    public void run() {
        for (Player p : new ArrayList<>(Bukkit.getOnlinePlayers())) {
            if (!p.getScoreboard().equals(settings.getPlayerScoreboards().get(p))) {
                settings.getPlayerPerms().get(p).nameSetup();
            }
        }
    }
}

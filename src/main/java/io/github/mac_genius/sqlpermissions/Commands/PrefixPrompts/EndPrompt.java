package io.github.mac_genius.sqlpermissions.Commands.PrefixPrompts;

import io.github.mac_genius.sqlpermissions.Database.SQLGroup;
import io.github.mac_genius.sqlpermissions.Permissions.PermGroup;
import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/2/2016.
 */
public class EndPrompt extends MessagePrompt {
    private String name;
    private String prefix;
    private PluginSettings settings;

    public EndPrompt(String name, String prefix, PluginSettings settings) {
        this.name = name;
        this.prefix = prefix;
        this.settings = settings;
    }

    @Override
    protected Prompt getNextPrompt(ConversationContext conversationContext) {
        return null;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        SQLGroup group = new SQLGroup(settings);
        group.renamePrefix(prefix, name);
        for (PermGroup g: settings.getGroups()) {
            if (g.getName().equals(name)) {
                g.setPrefix(prefix);
                settings.getPlugin().getLogger().info("found");
                break;
            }
        }
        return ChatColor.BLUE + "[SQLPermissions]" + ChatColor.WHITE + " The prefix has been changed.";
    }
}

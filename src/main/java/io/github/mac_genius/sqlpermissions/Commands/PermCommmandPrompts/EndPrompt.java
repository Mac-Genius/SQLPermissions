package io.github.mac_genius.sqlpermissions.Commands.PermCommmandPrompts;

import io.github.mac_genius.sqlpermissions.Database.SQLGroup;
import io.github.mac_genius.sqlpermissions.Permissions.PermGroup;
import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

import java.util.ArrayList;

/**
 * Created by Mac on 1/2/2016.
 */
public class EndPrompt extends MessagePrompt {
    private PermGroup permGroup;
    private PluginSettings settings;

    public EndPrompt(PermGroup permGroup, PluginSettings settings) {
        this.permGroup = permGroup;
        this.settings = settings;
    }

    @Override
    protected Prompt getNextPrompt(ConversationContext conversationContext) {
        return null;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        SQLGroup group = new SQLGroup(settings);
        group.addGroup(permGroup);
        return ChatColor.BLUE + "[SQLPermissions]" + ChatColor.WHITE + " It was added to the database.";
    }
}

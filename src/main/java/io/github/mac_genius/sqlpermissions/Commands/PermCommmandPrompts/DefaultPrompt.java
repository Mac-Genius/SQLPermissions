package io.github.mac_genius.sqlpermissions.Commands.PermCommmandPrompts;

import io.github.mac_genius.sqlpermissions.Permissions.PermGroup;
import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

/**
 * Created by Mac on 1/2/2016.
 */
public class DefaultPrompt extends StringPrompt {
    private PermGroup permGroup;
    private PluginSettings settings;

    public DefaultPrompt(PermGroup permGroup, PluginSettings settings) {
        this.permGroup = permGroup;
        this.settings = settings;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.BLUE + "[SQLPermissions]" + ChatColor.WHITE + " Is this the default group?";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        permGroup.setDefaultGroup(s.equalsIgnoreCase("true"));
        return new RankPrompt(permGroup, settings);
    }
}

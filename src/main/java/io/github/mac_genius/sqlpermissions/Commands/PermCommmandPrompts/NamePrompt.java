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
public class NamePrompt extends StringPrompt {
    private PermGroup permGroup;
    private PluginSettings settings;

    public NamePrompt(PermGroup permGroup, PluginSettings settings) {
        this.permGroup = permGroup;
        this.settings = settings;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.BLUE + "[SQLPermissions]" + ChatColor.WHITE + " What is the name of the group?";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        permGroup.setName(s);
        return new PrefixPrompt(permGroup, settings);
    }
}

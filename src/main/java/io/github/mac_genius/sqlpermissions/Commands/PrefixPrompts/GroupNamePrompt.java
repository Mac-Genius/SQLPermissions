package io.github.mac_genius.sqlpermissions.Commands.PrefixPrompts;

import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

/**
 * Created by Mac on 1/2/2016.
 */
public class GroupNamePrompt extends StringPrompt {
    private PluginSettings settings;

    public GroupNamePrompt(PluginSettings settings) {
        this.settings = settings;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.BLUE + "[SQLPermissions]" + ChatColor.WHITE + " What is the name of the group?";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        return new PrefixPrompt(s, settings);
    }
}

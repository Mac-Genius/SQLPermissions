package io.github.mac_genius.sqlpermissions.Commands.PrefixPrompts;

import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

/**
 * Created by Mac on 1/2/2016.
 */
public class PrefixPrompt extends StringPrompt {
    private String name;
    private PluginSettings settings;

    public PrefixPrompt(String name, PluginSettings settings) {
        this.name = name;
        this.settings = settings;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.BLUE + "[SQLPermissions]" + ChatColor.WHITE + " What are you changing the group prefix to?";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        return new EndPrompt(name, s, settings);
    }
}

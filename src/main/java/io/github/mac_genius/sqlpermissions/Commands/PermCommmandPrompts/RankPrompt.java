package io.github.mac_genius.sqlpermissions.Commands.PermCommmandPrompts;

import io.github.mac_genius.sqlpermissions.Permissions.PermGroup;
import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

/**
 * Created by Mac on 1/2/2016.
 */
public class RankPrompt extends NumericPrompt {
    private PermGroup permGroup;
    private PluginSettings settings;

    public RankPrompt(PermGroup permGroup, PluginSettings settings) {
        this.permGroup = permGroup;
        this.settings = settings;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number) {
        permGroup.setRank(number.intValue());
        return new EndPrompt(permGroup, settings);
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.BLUE + "[SQLPermissions]" + ChatColor.WHITE + " What is the rank?";
    }
}

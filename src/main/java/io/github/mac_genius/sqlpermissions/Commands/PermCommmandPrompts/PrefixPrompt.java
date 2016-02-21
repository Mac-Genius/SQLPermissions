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
public class PrefixPrompt extends StringPrompt {
    private PermGroup permGroup;
    private PluginSettings settings;

    public PrefixPrompt(PermGroup permGroup, PluginSettings settings) {
        this.permGroup = permGroup;
        this.settings = settings;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.BLUE + "[SQLPermissions]" + ChatColor.WHITE + " What will the prefix for this group be? "
                + ChatColor.BLACK + "&0 " + ChatColor.DARK_BLUE + "&1 " + ChatColor.DARK_GREEN + "&2 " + ChatColor.DARK_AQUA + "&3 "
                + ChatColor.DARK_RED + "&4 " + ChatColor.DARK_PURPLE + "&5 " + ChatColor.GOLD + "&6 " + ChatColor.GRAY + "&7 "
                + ChatColor.DARK_GRAY + "&8 " + ChatColor.BLUE + "&9 " + ChatColor.GREEN + "&a " + ChatColor.AQUA + "&b "
                + ChatColor.RED + "&c " + ChatColor.LIGHT_PURPLE + "&d " + ChatColor.YELLOW + "&e " + ChatColor.WHITE + "&f";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        permGroup.setPrefix(s);
        return new SuffixPrompt(permGroup, settings);
    }
}

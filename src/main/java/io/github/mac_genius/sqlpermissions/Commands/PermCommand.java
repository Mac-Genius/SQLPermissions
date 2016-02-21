package io.github.mac_genius.sqlpermissions.Commands;

import io.github.mac_genius.sqlpermissions.Commands.PermCommmandPrompts.NamePrompt;
import io.github.mac_genius.sqlpermissions.Commands.PrefixPrompts.GroupNamePrompt;
import io.github.mac_genius.sqlpermissions.Database.SQLGroup;
import io.github.mac_genius.sqlpermissions.Database.SQLUser;
import io.github.mac_genius.sqlpermissions.Permissions.PermGroup;
import io.github.mac_genius.sqlpermissions.PluginSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Mac on 10/26/2015.
 */
public class PermCommand implements CommandExecutor {
    private PluginSettings settings;

    public PermCommand(PluginSettings settings) {
        this.settings = settings;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("perm")) {
            if (strings.length > 0) {
                if (strings[0] != null) {
                    if (strings[0].equalsIgnoreCase("add")) {
                        if (strings[1] != null) {
                            if (strings[1].equalsIgnoreCase("group")) {
                                ConversationFactory factory = new ConversationFactory(settings.getPlugin());
                                Conversation conversation = factory.withFirstPrompt(new NamePrompt(new PermGroup(), settings)).withLocalEcho(true).buildConversation((Player) commandSender);
                                conversation.begin();
                                return true;
                            } else if (strings[1].equalsIgnoreCase("perm")) {
                                if (strings[2] != null && strings[3] != null) {
                                    SQLGroup group = new SQLGroup(settings);
                                    group.addPermission(strings[2], strings[3]);
                                    for (PermGroup g : settings.getGroups()) {
                                        if (g.getName().equals(strings[2])) {
                                            g.addPermission(strings[3]);
                                            break;
                                        }
                                    }
                                    commandSender.sendMessage("Added!");
                                    return true;
                                }
                            }
                        }
                    } else if (strings[0].equalsIgnoreCase("remove")) {
                        if (strings[1] != null) {
                            if (strings[1].equalsIgnoreCase("group")) {
                                if (strings[2] != null) {
                                    SQLGroup group = new SQLGroup(settings);
                                    group.removeGroup(strings[2]);
                                    commandSender.sendMessage("Removed!");
                                    return true;
                                }
                            } else if (strings[1].equalsIgnoreCase("perm")) {
                                if (strings[2] != null && strings[3] != null) {
                                    SQLGroup group = new SQLGroup(settings);
                                    group.deletePermission(strings[2], strings[3]);
                                    for (PermGroup g : settings.getGroups()) {
                                        if (g.getName().equals(strings[2])) {
                                            g.removePermission(strings[3]);
                                            break;
                                        }
                                    }
                                    commandSender.sendMessage("Removed!");
                                    return true;
                                }
                            }
                        }
                    } else if (strings[0].equalsIgnoreCase("set")) {
                        if (strings[1] != null) {
                            if (strings[1].equalsIgnoreCase("player")) {
                                if (strings[2] != null && strings[3] != null) {
                                    SQLUser user = new SQLUser(settings);

                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        if (strings[3].equals(p.getName())) {
                                            for (PermGroup g : settings.getGroups()) {
                                                if (g.getName().equals(strings[2])) {
                                                    g.addPlayer(p, settings);
                                                    settings.getPlayerPerms().get(p).changeGroup(g);
                                                    user.setGroup(p, strings[2]);
                                                    commandSender.sendMessage("Set!");
                                                    return true;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else if (strings[1].equalsIgnoreCase("prefix")) {
                                ConversationFactory factory = new ConversationFactory(settings.getPlugin());
                                Conversation conversation = factory.withFirstPrompt(new GroupNamePrompt(settings)).withLocalEcho(true).buildConversation((Player) commandSender);
                                conversation.begin();
                                return true;
                            }
                        }
                    } else if (strings[0].equalsIgnoreCase("list")) {
                        if (strings[1] != null) {
                            for (PermGroup p : settings.getGroups()) {
                                if (p.getName().equals(strings[1])) {
                                    for (String st : p.getPermissions()) {
                                        commandSender.sendMessage(st);
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            commandSender.sendMessage(ChatColor.GREEN + "<-------------- Permissions Help -------------->");
            commandSender.sendMessage(ChatColor.GOLD + "/perm add group" + ChatColor.RESET + "" + ChatColor.WHITE + " adds a group.");
            commandSender.sendMessage(ChatColor.GOLD + "/perm add perm <group> <permission>" + ChatColor.RESET + "" + ChatColor.WHITE + " adds a permission to a group.");
            commandSender.sendMessage(ChatColor.GOLD + "/perm remove group <name> " + ChatColor.RESET + "" + ChatColor.WHITE + " removes a group.");
            commandSender.sendMessage(ChatColor.GOLD + "/perm remove perm <group> <permission>" + ChatColor.RESET + "" + ChatColor.WHITE + " removes a permission to a group.");
            commandSender.sendMessage(ChatColor.GOLD + "/perm set player <group> <player>" + ChatColor.RESET + "" + ChatColor.WHITE + " sets a player to a group.");
            commandSender.sendMessage(ChatColor.GOLD + "/perm set prefix" + ChatColor.RESET + "" + ChatColor.WHITE + " changes the prefix of a group.");
            commandSender.sendMessage(ChatColor.GOLD + "/perm set suffix" + ChatColor.RESET + "" + ChatColor.WHITE + " changes the suffix of a group.");
            commandSender.sendMessage(ChatColor.GOLD + "/perm list <group>" + ChatColor.RESET + "" + ChatColor.WHITE + " lists the permissions for a group.");
            return true;
        }
        return false;
    }
}

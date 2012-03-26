package de.codeinfection.quickwango.ItemRepair.CommandActions;

import de.codeinfection.quickwango.ItemRepair.ItemRepair;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

/**
 * Reloads the plugin
 *
 * @author Phillip Schichtel
 */
public class ReloadAction implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        final ItemRepair plugin = ItemRepair.getInstance();
        final PluginManager pm = plugin.getServer().getPluginManager();

        pm.disablePlugin(plugin);
        pm.enablePlugin(plugin);

        sender.sendMessage(ChatColor.GREEN + "ItemRepair successfully reloaded!");
        return true;
    }
}


package de.codeinfection.quickwango.ItemRepair.CommandActions;

import de.codeinfection.quickwango.ItemRepair.ItemRepair;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Adds a repair block
 *
 * @author Phillip Schichtel
 */
public class AddAction implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player)sender;
            if (!ItemRepair.addBlockChoiceRequests.contains(player))
            {
                if (!ItemRepair.removeBlockChoiceRequests.contains(player))
                {
                    ItemRepair.addBlockChoiceRequests.add(player);
                    player.sendMessage(ChatColor.YELLOW + "Rightclick the block.");
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "You can't add a repair block while you're removing one!");
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You are already adding a repair block!");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Only players can add repair blocks!");
        }

        return true;
    }
}

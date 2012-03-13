package de.codeinfection.quickwango.ItemRepair.CommandActions;

import de.codeinfection.quickwango.ItemRepair.ItemRepair;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class RemoveAction implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player)sender;
            if (!ItemRepair.removeBlockChoiceRequests.contains(player))
            {
                if (!ItemRepair.addBlockChoiceRequests.contains(player))
                {
                    ItemRepair.removeBlockChoiceRequests.add(player);
                    player.sendMessage(ChatColor.YELLOW + "Rightclick the block.");
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "You can't remove a repair block while you're adding one!");
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You are already removing a repair block!");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Only players can add repair blocks!");
        }

        return true;
    }
}

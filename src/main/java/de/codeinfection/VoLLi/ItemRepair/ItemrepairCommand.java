package de.codeinfection.VoLLi.ItemRepair;

import de.codeinfection.VoLLi.ItemRepair.CommandActions.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author CodeInfection
 */
public class ItemrepairCommand implements CommandExecutor
{
    private Map<String, CommandExecutor> actions;

    public ItemrepairCommand()
    {
        this.actions = new HashMap<String, CommandExecutor>();
        this.actions.put("add", new AddAction());
        this.actions.put("remove", new RemoveAction());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length > 0)
        {
            String actionName = args[0].trim();
            CommandExecutor action = this.actions.get(actionName);
            if (action != null)
            {
                if (sender.hasPermission("ItemRepair.commands." + actionName))
                {
                    return action.onCommand(sender, null, actionName, Arrays.copyOfRange(args, 0, args.length - 1));
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "You don't have the permission to use this command!");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "The requested action is not available!");
            }
        }
        else
        {
            return false;
        }
        return true;
    }
}

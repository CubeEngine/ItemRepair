package de.codeinfection.quickwango.ItemRepair;

import de.codeinfection.quickwango.ItemRepair.CommandActions.AddAction;
import de.codeinfection.quickwango.ItemRepair.CommandActions.RemoveAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author CodeInfection
 */
public class ItemrepairCommand implements CommandExecutor
{

    private Map<String, CommandExecutor> actions;
    private final Permission parentPermission;
    private final PluginManager pm;
    private static final String PERMISSION_BASE = "itemrepair.commands.";

    public ItemrepairCommand(Plugin plugin)
    {
        this.pm = plugin.getServer().getPluginManager();
        this.parentPermission = new Permission(PERMISSION_BASE + "*", PermissionDefault.OP);
        this.pm.addPermission(this.parentPermission);

        this.actions = new HashMap<String, CommandExecutor>();
        this.registerAction("add", new AddAction());
        this.registerAction("remove", new RemoveAction());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length > 0)
        {
            String actionName = args[0].trim();
            CommandExecutor action = this.actions.get(actionName);
            if (action != null)
            {
                if (sender.hasPermission(PERMISSION_BASE + actionName))
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

    public final ItemrepairCommand registerAction(String name, CommandExecutor action)
    {
        Permission actionPermission = new Permission(PERMISSION_BASE + name, PermissionDefault.OP);
        this.pm.addPermission(actionPermission);
        actionPermission.addParent(this.parentPermission, true);
        this.actions.put(name, action);

        return this;
    }
}

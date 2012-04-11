package de.cubeisland.ItemRepair;

import de.cubeisland.ItemRepair.CommandActions.RemoveAction;
import de.cubeisland.ItemRepair.CommandActions.AddAction;
import de.cubeisland.ItemRepair.CommandActions.ReloadAction;
import static de.cubeisland.Translation.Translator.t;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * The base command
 *
 * @author Phillip Schichtel
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
        try
        {
            this.pm.addPermission(this.parentPermission);
        }
        catch (IllegalArgumentException e)
        {}

        this.actions = new HashMap<String, CommandExecutor>();

        final Set<Player> addRequests = new HashSet<Player>();
        final Set<Player> removeRequests = new HashSet<Player>();
        this.registerAction("add", new AddAction(plugin, addRequests, removeRequests));
        this.registerAction("remove", new RemoveAction(plugin, addRequests, removeRequests));
        this.registerAction("reload", new ReloadAction());
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
                    sender.sendMessage(t("permissionDeniedCmd"));
                }
            }
            else
            {
                sender.sendMessage(t("actionNotFound"));
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
        try
        {
            this.pm.addPermission(actionPermission);
        }
        catch (IllegalArgumentException e)
        {}
        actionPermission.addParent(this.parentPermission, true);
        this.actions.put(name, action);

        return this;
    }
}

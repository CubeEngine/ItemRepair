package de.cubeisland.ItemRepair;

import static de.cubeisland.ItemRepair.ItemRepair._;
import de.cubeisland.ItemRepair.repair.RepairBlockManager;
import de.cubeisland.libMinecraft.command.Command;
import de.cubeisland.libMinecraft.command.CommandArgs;
import de.cubeisland.libMinecraft.command.CommandPermission;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author CodeInfection
 */
public class ItemRepairCommands implements Listener
{
    private final Set<Player> removeRequests;
    private final Set<Player> addRequests;
    private final RepairBlockManager rbm;

    public ItemRepairCommands(RepairPlugin plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.rbm = RepairBlockManager.getInstance();
        this.addRequests = new HashSet<Player>();
        this.removeRequests = new HashSet<Player>();
    }

    @Command(desc = "Reloads the plugin")
    @CommandPermission
    public boolean reload(CommandSender sender, CommandArgs args)
    {
        final ItemRepair plugin = ItemRepair.getInstance();
        final PluginManager pm = plugin.getServer().getPluginManager();

        pm.disablePlugin(plugin);
        pm.enablePlugin(plugin);

        sender.sendMessage(_("reloadSuccessful"));
        return true;
    }

    @Command(desc = "Adds a repair block")
    @CommandPermission
    public void add(CommandSender sender, CommandArgs args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player)sender;
            if (!this.addRequests.contains(player))
            {
                if (!this.removeRequests.contains(player))
                {
                    this.addRequests.add(player);
                    player.sendMessage(_("rightclickBlock"));
                }
                else
                {
                    player.sendMessage(_("alreadyRemoving"));
                }
            }
            else
            {
                player.sendMessage(_("alreadyAdding"));
            }
        }
        else
        {
            sender.sendMessage(_("onlyPlayersAdd"));
        }
    }

    @Command(desc = "Removes a repair block")
    @CommandPermission
    public void remove(CommandSender sender, CommandArgs args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player)sender;
            if (!this.removeRequests.contains(player))
            {
                if (!this.addRequests.contains(player))
                {
                    this.removeRequests.add(player);
                    player.sendMessage(_("rightclickBlock"));
                }
                else
                {
                    player.sendMessage(_("alreadyAdding"));
                }
            }
            else
            {
                player.sendMessage(_("alreadyRemoving"));
            }
        }
        else
        {
            sender.sendMessage(_("onlyPlayersRemove"));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAdd(PlayerInteractEvent event)
    {
        final Player player = event.getPlayer();
        if (this.addRequests.contains(player))
        {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                final Block block = event.getClickedBlock();
                if (!this.rbm.isRepairBlock(block))
                {
                    if (this.rbm.attachRepairBlock(block))
                    {
                        player.sendMessage(_("addSuccess"));
                    }
                    else
                    {
                        player.sendMessage(_("cantBeUsed"));
                    }
                }
                else
                {
                    player.sendMessage(_("alreadyARepairBlock"));
                }
            }
            if (event.getAction() != Action.PHYSICAL)
            {
                this.addRequests.remove(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRemove(PlayerInteractEvent event)
    {
        final Player player = event.getPlayer();
        if (this.removeRequests.contains(player))
        {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                if (this.rbm.detachRepairBlock(event.getClickedBlock()))
                {
                    player.sendMessage(_("removeSuccess"));
                }
                else
                {
                    player.sendMessage(_("notARepairBlock"));
                }
            }
            if (event.getAction() != Action.PHYSICAL)
            {
                this.removeRequests.remove(player);
                event.setCancelled(true);
            }
        }
    }
}

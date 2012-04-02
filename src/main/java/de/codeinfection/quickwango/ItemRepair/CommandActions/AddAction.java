package de.codeinfection.quickwango.ItemRepair.CommandActions;

import de.codeinfection.quickwango.ItemRepair.ItemRepair;
import de.codeinfection.quickwango.ItemRepair.RepairBlockManager;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 * Adds a repair block
 *
 * @author Phillip Schichtel
 */
public class AddAction implements CommandExecutor, Listener
{
    private final Set<Player> removeRequests;
    private final Set<Player> addRequests;
    private final RepairBlockManager rbm;

    public AddAction(Plugin plugin, Set<Player> addRequests, Set<Player> removeRequests)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.addRequests = addRequests;
        this.removeRequests = removeRequests;
        this.rbm = RepairBlockManager.getInstance();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player)sender;
            if (!this.addRequests.contains(player))
            {
                if (!this.removeRequests.contains(player))
                {
                    this.addRequests.add(player);
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

    @EventHandler
    public void onInteraction(PlayerInteractEvent event)
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
                        player.sendMessage(ChatColor.GREEN + "Repair block successfully added!");
                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "This block can't be used as a repair block!");
                    }
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "This block is already a repair block!");
                }
            }
            if (event.getAction() != Action.PHYSICAL)
            {
                this.addRequests.remove(player);
                event.setCancelled(true);
            }
        }
    }
}

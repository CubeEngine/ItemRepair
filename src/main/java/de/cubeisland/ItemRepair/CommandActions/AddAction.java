package de.cubeisland.ItemRepair.CommandActions;

import de.cubeisland.ItemRepair.RepairBlockManager;
import static de.cubeisland.Translation.Translator.t;
import java.util.Set;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
                    player.sendMessage(t("rightclickBlock"));
                }
                else
                {
                    player.sendMessage(t("alreadyRemoving"));
                }
            }
            else
            {
                player.sendMessage(t("alreadyAdding"));
            }
        }
        else
        {
            sender.sendMessage(t("onlyPlayersAdd"));
        }

        return true;
    }

    @EventHandler(priority = EventPriority.LOWEST)
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
                        player.sendMessage(t("addSuccess"));
                    }
                    else
                    {
                        player.sendMessage(t("cantBeUsed"));
                    }
                }
                else
                {
                    player.sendMessage(t("alreadyARepairBlock"));
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

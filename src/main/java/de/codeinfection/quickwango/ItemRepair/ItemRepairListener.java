package de.codeinfection.quickwango.ItemRepair;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listens for a few player related events
 *
 * @author Phillip Schichtel
 */
public class ItemRepairListener implements Listener
{
    private final RepairBlockManager rbm;
    private final Map<Player, RepairRequest> repairRequests;

    public ItemRepairListener()
    {
        this.rbm = RepairBlockManager.getInstance();
        this.repairRequests = new HashMap<Player, RepairRequest>();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        this.repairRequests.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();
        if (block == null)
        {
            return;
        }
        RepairBlock repairBlock = this.rbm.getRepairBlock(block);
        if (repairBlock == null)
        {
            return;
        }
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            player.openInventory(repairBlock.getInventory(player));
        }
        else if (event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            event.setCancelled(true);
            if (this.repairRequests.containsKey(player))
            {
                RepairRequest request = this.repairRequests.get(player);
                if (request.getRepairBlock() == repairBlock)
                {
                    repairBlock.repair(request);

                    this.repairRequests.remove(player);
                }
            }
            else
            {
                if (!this.repairRequests.containsKey(player))
                {
                    RepairRequest request = repairBlock.requestRepair(player);
                    if (request != null)
                    {
                        this.repairRequests.put(player, request);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCancelRepair(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.PHYSICAL)
        {
            final Player player = event.getPlayer();
            if (this.repairRequests.containsKey(player))
            {
                player.sendMessage(ChatColor.YELLOW + "The repair has been cancelled!");
                this.repairRequests.remove(player);
                event.setCancelled(true);
            }
        }
    }
}

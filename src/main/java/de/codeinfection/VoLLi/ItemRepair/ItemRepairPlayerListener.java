package de.codeinfection.VoLLi.ItemRepair;

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

/**
 *
 * @author VoLLi
 */
public class ItemRepairPlayerListener implements Listener
{

    private final RepairBlockManager rbm;
    private final Map<Player, RepairRequest> repairRequests;

    public ItemRepairPlayerListener()
    {
        this.rbm = RepairBlockManager.getInstance();
        this.repairRequests = new HashMap<Player, RepairRequest>();
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Block block = event.getClickedBlock();
            if (block == null)
            {
                return;
            }

            if (ItemRepair.addBlockChoiceRequests.contains(player))
            {
                ItemRepair.addBlockChoiceRequests.remove(player);
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
                event.setCancelled(true);
            }
            else if (ItemRepair.removeBlockChoiceRequests.contains(player))
            {
                ItemRepair.removeBlockChoiceRequests.remove(player);
                if (this.rbm.detachRepairBlock(block))
                {
                    player.sendMessage(ChatColor.GREEN + "Repair block successfully removed!");
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "That block is not a repair block!");
                }
                event.setCancelled(true);
            }
            else
            {
                RepairBlock repairBlock = this.rbm.getRepairBlock(block);
                if (repairBlock != null)
                {
                    event.setCancelled(true);
                    ItemRepair.debug("Repair block found! - " + repairBlock.getClass().getSimpleName());
                    if (this.hasPlayerRequestedRepair(player))
                    {
                        RepairRequest request = this.getRepairRequest(player);
                        if (request.getRepairBlock() == repairBlock)
                        {
                            repairBlock.repair(request);

                            player.updateInventory();

                            this.removeRepairRequest(player);
                            return;
                        }
                    }
                    else
                    {
                        RepairRequest request = repairBlock.requestRepair(player);
                        if (request != null)
                        {
                            this.requestRepair(player, request);
                            return;
                        }
                    }
                }
            }
        }
        if (this.hasPlayerRequestedRepair(player))
        {
            player.sendMessage(ChatColor.YELLOW + "The repair has been cancelled!");
            this.removeRepairRequest(player);
            event.setCancelled(true);
        }
    }

    private boolean hasPlayerRequestedRepair(Player player)
    {
        return repairRequests.containsKey(player);
    }

    private void requestRepair(Player player, RepairRequest request)
    {
        if (!hasPlayerRequestedRepair(player))
        {
            repairRequests.put(player, request);
        }
    }

    private void removeRepairRequest(Player player)
    {
        repairRequests.remove(player);
    }

    private RepairRequest getRepairRequest(Player player)
    {
        return repairRequests.get(player);
    }
}

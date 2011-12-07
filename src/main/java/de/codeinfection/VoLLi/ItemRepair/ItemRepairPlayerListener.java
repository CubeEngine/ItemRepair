package de.codeinfection.VoLLi.ItemRepair;

import com.iCo6.iConomy;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author VoLLi
 */
public class ItemRepairPlayerListener extends PlayerListener
{
    private final RepairBlockManager rbm;
    
    public ItemRepairPlayerListener()
    {
        this.rbm = RepairBlockManager.getInstance();
    }
    
    @Override
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
                ItemRepair.debug("Player " + player.getName() + " has to choose a block to add!");
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
            }
            else if (ItemRepair.removeBlockChoiceRequests.contains(player))
            {
                ItemRepair.debug("Player " + player.getName() + " has to choose a block to remove!");
                ItemRepair.removeBlockChoiceRequests.remove(player);
                if (this.rbm.detachRepairBlock(block))
                {
                    player.sendMessage(ChatColor.GREEN + "Repair block successfully removed!");
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "That block is not a repair block!");
                }
            }
            else
            {
                ItemRepair.debug("Player " + player.getName() + " rightclicked a block!");
                RepairBlock repairBlock = this.rbm.getRepairBlock(block);
                if (repairBlock != null)
                {
                    ItemRepair.debug("Repair block found! - " + repairBlock.getClass().getSimpleName());
                    if (RepairRequest.hasPlayerRequestedRepair(player))
                    {
                        repairBlock.repair(RepairRequest.getRepairRequest(player));

                        player.updateInventory();

                        RepairRequest.removeRepairRequest(player);
                    }
                    else
                    {
                        RepairRequest request = repairBlock.requestRepair(player);
                        if (request != null)
                        {
                            player.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_RED + "ItemRepair" + ChatColor.GREEN + "]");
                            player.sendMessage(ChatColor.AQUA + "Rechtsklicke" + ChatColor.WHITE + " noch einmal um die Reparatur auszuführen");
                            player.sendMessage("Die Reparatur würde " + ChatColor.AQUA + iConomy.format(request.getPrice()) + ChatColor.WHITE + " kosten.");
                            player.sendMessage("Du hast aktuell " + ChatColor.AQUA + iConomy.format(request.getHoldings().getBalance()));
                            RepairRequest.requestRepair(player, request);
                        }
                    }
                    return;
                }
            }
        }
        if (RepairRequest.hasPlayerRequestedRepair(player))
        {
            player.sendMessage(ChatColor.YELLOW + "Die Reparatur wurde abgebrochen!");
            RepairRequest.removeRepairRequest(player);
        }
    }
}

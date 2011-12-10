package de.codeinfection.VoLLi.ItemRepair;

import com.iCo6.iConomy;
import java.util.HashMap;
import java.util.Map;
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

    private final Map<Player, RepairRequest> repairRequests;
    
    public ItemRepairPlayerListener()
    {
        this.rbm = RepairBlockManager.getInstance();
        this.repairRequests = new HashMap<Player, RepairRequest>();
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
                            player.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_RED + "ItemRepair" + ChatColor.GREEN + "]");
                            player.sendMessage(ChatColor.AQUA + "Rechtsklicke" + ChatColor.WHITE + " noch einmal um die Reparatur auszuführen");
                            player.sendMessage("Die Reparatur würde " + ChatColor.AQUA + iConomy.format(request.getPrice()) + ChatColor.WHITE + " kosten.");
                            player.sendMessage("Du hast aktuell " + ChatColor.AQUA + iConomy.format(request.getHoldings().getBalance()));
                            this.requestRepair(player, request);
                            return;
                        }
                    }
                }
            }
        }
        if (this.hasPlayerRequestedRepair(player))
        {
            player.sendMessage(ChatColor.YELLOW + "Die Reparatur wurde abgebrochen!");
            this.removeRepairRequest(player);
        }
    }

    public boolean hasPlayerRequestedRepair(Player player)
    {
        return repairRequests.containsKey(player);
    }

    public void requestRepair(Player player, RepairRequest request)
    {
        if (!hasPlayerRequestedRepair(player))
        {
            repairRequests.put(player, request);
        }
    }

    public void removeRepairRequest(Player player)
    {
        repairRequests.remove(player);
    }

    public RepairRequest getRepairRequest(Player player)
    {
        return repairRequests.get(player);
    }
}

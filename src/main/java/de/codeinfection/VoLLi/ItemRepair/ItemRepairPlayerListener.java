package de.codeinfection.VoLLi.ItemRepair;

import com.iCo6.iConomy;
import com.iCo6.system.Accounts;
import com.iCo6.system.Holdings;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author VoLLi
 */
public class ItemRepairPlayerListener extends PlayerListener
{
    private final ItemRepairConfiguration config;
    private final RepairBlockManager rbm;
    
    protected final Accounts accounts;
    
    public ItemRepairPlayerListener(ItemRepairConfiguration config, iConomy iconomy)
    {
        this.config = config;
        this.rbm = RepairBlockManager.getInstance();
        
        this.accounts = new Accounts();
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Block block = event.getClickedBlock();

            int blockId = block.getType().getId();
            RepairBlock repairBlock = this.rbm.getBlock(blockId);
            if (repairBlock != null)
            {
                String permission = repairBlock.getPermission();
                if (permission != null)
                {
                    if (!player.hasPermission("itemrepair.all") && !player.hasPermission("itemrepair.block." + permission))
                    {
                        return;
                    }
                }
                
                List<ItemStack> items = repairBlock.getItems(player);
                if (!items.isEmpty())
                {
                    double price = repairBlock.getPrice();
                    Holdings holding = this.accounts.get(player.getName()).getHoldings();

                    if (RepairBlock.hasPlayerStartedRepairing(player))
                    {
                        ItemRepair.debug("Price: " + price);

                        if (holding.hasEnough(price))
                        {
                            for (ItemStack item : items)
                            {
                                item.setDurability(repairBlock.getDurability());
                            }
                            player.updateInventory();
                            holding.subtract(price);
                            holding.showBalance(player);
                            
                            repairBlock.afterRepair(player);

                            String successMessage = repairBlock.getSuccessMessage();
                            if (successMessage != null)
                            {
                                player.sendMessage(successMessage);
                            }
                        }
                        else
                        {
                            player.sendMessage(ChatColor.RED + "Du hast nicht genug Geld!");
                        }
                        
                        RepairBlock.removeRepairingPlayer(player);
                    }
                    else
                    {
                        RepairBlock.addRepairingPlayer(player, repairBlock);
                        player.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_RED + "ItemRepair" + ChatColor.GREEN + "]");
                        player.sendMessage(ChatColor.AQUA + "Rechtsklicke" + ChatColor.WHITE + " noch einmal um die Reparatur auszuführen");
                        player.sendMessage("Die Reparatur würde " + ChatColor.AQUA + iConomy.format(repairBlock.price) + ChatColor.WHITE + " kosten.");
                        player.sendMessage("Du hast aktuell " + ChatColor.AQUA + iConomy.format(holding.getBalance()));
                    }
                }
                else
                {
                    String failMessage = repairBlock.getFailMessage();
                    if (failMessage != null)
                    {
                        player.sendMessage(failMessage);
                    }
                }
                return;
            }
        }
        if (RepairBlock.hasPlayerStartedRepairing(player))
        {
            player.sendMessage(ChatColor.YELLOW + "Die Reparatur wurde abgebrochen!");
            RepairBlock.removeRepairingPlayer(player);
        }
    }
}

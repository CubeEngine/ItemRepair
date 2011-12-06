package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

import com.iCo6.iConomy;
import com.iCo6.system.Holdings;
import de.codeinfection.VoLLi.ItemRepair.RepairBlock;
import de.codeinfection.VoLLi.ItemRepair.RepairRequest;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author VoLLi
 */
public class GenericRepairBlock extends RepairBlock
{
    protected final double pricePerDamage;
    
    public GenericRepairBlock(int blockId, double basePrice)
    {
        super(blockId);
        this.pricePerDamage = basePrice;
    }

    public RepairRequest requestRepair(Player player)
    {
        if (hasRepairPermission(player, "singleRepair"))
        {
            ItemStack itemInHand = player.getItemInHand();
            if (itemInHand != null) // -> hat ein item in der hand?
            {
                int currentDurability = itemInHand.getDurability();
                if (itemInHand.getType().getMaxDurability() > -1) // -> ist reparierbar?
                {
                    if (currentDurability > 0) // -> ist beschädigt?
                    {
                        double price = itemInHand.getDurability() * this.pricePerDamage * itemInHand.getAmount() * this.getEnchantmentMultiplier(itemInHand);
                        return new RepairRequest(player, Arrays.asList(itemInHand), price);
                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "Das Item ist nicht beschädigt!");
                    }
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "Dieses Item kann man nicht reparieren!");
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "Du hast kein Item in der Hand!");
            }
        }
        else
        {
            player.sendMessage("You don't have the permission to repair a single item!");
        }
        return null;
    }

    @Override
    public void repair(RepairRequest request)
    {
        double price = request.getPrice();
        Holdings holdings = request.getHoldings();
        Player player = request.getPlayer();
        
        if (holdings.hasEnough(price))
        {
            holdings.subtract(price);
            repairItems(request.getItems());
            player.sendMessage(ChatColor.GREEN + "Your item has been repaired for " + ChatColor.AQUA + iConomy.format(price));
        }
    }
}

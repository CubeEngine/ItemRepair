package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

import com.iCo6.iConomy;
import com.iCo6.system.Holdings;
import de.codeinfection.VoLLi.ItemRepair.RepairBlock;
import de.codeinfection.VoLLi.ItemRepair.RepairRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author VoLLi
 */
public class GenericMultiRepairBlock extends RepairBlock
{
    private final double perDamagePrice;

    public GenericMultiRepairBlock(int blockId, double basePrice)
    {
        super(blockId);
        this.perDamagePrice = basePrice;
    }

    @Override
    public RepairRequest requestRepair(Player player)
    {
        if (hasRepairPermission(player, "multiRepair"))
        {
            double price = 0;
            ArrayList<ItemStack> allItems = new ArrayList<ItemStack>();
            Collections.addAll(allItems, player.getInventory().getArmorContents());
            Collections.addAll(allItems, player.getInventory().getContents());
            List<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemInHand = player.getItemInHand();

            if (itemInHand != null && isRepairable(itemInHand))
            {
                for (ItemStack item : allItems)
                {
                    if (item != null && item.getDurability() > 0)
                    {
                        price += item.getDurability() * this.perDamagePrice * item.getAmount() * this.getEnchantmentMultiplier(item);
                        items.add(item);
                    }
                }

                if (items.size() > 0)
                {
                    return new RepairRequest(player, items, price);
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "Du hast keine reparierbaren Items!");
                }
            }
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
            request.getHoldings().subtract(price);
            repairItems(request.getItems());
            player.sendMessage(ChatColor.GREEN + "Your items have been repaired for " + ChatColor.AQUA + iConomy.format(price));
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You don't have enough money to repair all your items!");
        }
    }
}

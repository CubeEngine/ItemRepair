package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

import com.iCo6.iConomy;
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
public class GenericMultiRepairBlock extends GenericRepairBlock
{
    public GenericMultiRepairBlock(int blockId, double basePrice)
    {
        super(blockId, basePrice);
        this.failMessage = ChatColor.RED + "Du hast keine reparierbaren Items!";
        this.permission = "multiRepair";
    }

    @Override
    public List<ItemStack> getItems(Player player)
    {
        this.price = 0;
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
                    this.price += item.getDurability() * this.pricePerDamage * item.getAmount();
                    items.add(item);
                }
            }
        }
        else
        {
            this.failMessage = null;
        }
        
        this.successMessage = ChatColor.GREEN + "Deine Items wurden für " + ChatColor.AQUA + iConomy.format(this.price) + ChatColor.GREEN + " repariert!";
        
        return items;
    }
}

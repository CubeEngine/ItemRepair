package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

import com.iCo6.iConomy;
import de.codeinfection.VoLLi.ItemRepair.ItemRepair;
import de.codeinfection.VoLLi.ItemRepair.RepairBlock;
import java.util.ArrayList;
import java.util.List;
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
        this.permission = "singleRepair";
    }

    @Override
    public List<ItemStack> getItems(Player player)
    {
        List<ItemStack> items = new ArrayList<ItemStack>();
        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand != null) // -> hat ein item in der hand?
        {
            int currentDurability = itemInHand.getDurability();
            if (itemInHand.getType().getMaxDurability() > -1) // -> ist reparierbar?
            {
                if (currentDurability > 0) // -> ist beschädigt?
                {
                    this.price = itemInHand.getDurability() * this.pricePerDamage;
                    items.add(itemInHand);
                    
                    this.successMessage = ChatColor.GREEN + "Dein Item wurde für " + ChatColor.AQUA + iConomy.format(this.price) + ChatColor.GREEN + " repariert!";
                }
                else
                {
                    this.failMessage = ChatColor.RED + "Das Item ist nicht beschädigt!";
                }
            }
            else
            {
                this.failMessage = ChatColor.RED + "Dieses Item kann man nicht reparieren!";
            }
        }
        else
        {
            this.failMessage = ChatColor.RED + "Du hast kein Item in der Hand!";
        }
        return items;
    }
}

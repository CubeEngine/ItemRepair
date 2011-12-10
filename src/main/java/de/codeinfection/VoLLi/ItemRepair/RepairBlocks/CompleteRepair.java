package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

import com.iCo6.iConomy;
import com.iCo6.system.Holdings;
import de.codeinfection.VoLLi.ItemRepair.ItemRepairConfiguration;
import de.codeinfection.VoLLi.ItemRepair.RepairBlock;
import de.codeinfection.VoLLi.ItemRepair.RepairRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author VoLLi
 */
public class CompleteRepair extends RepairBlock
{
    private final ItemRepairConfiguration config;

    public CompleteRepair(Material material, ItemRepairConfiguration config)
    {
        super(material);
        this.config = config;
    }

    public CompleteRepair(int blockId, ItemRepairConfiguration config)
    {
        this(Material.getMaterial(blockId), config);
    }

    public CompleteRepair(String blockName, ItemRepairConfiguration config)
    {
        this(Material.getMaterial(blockName), config);
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
                        price += (
                                item.getDurability()
                              * this.config.price_perDamage
                              * item.getAmount()
                              * getEnchantmentMultiplier(item, this.config.price_enchantMultiplier_factor, this.config.price_enchantMultiplier_base)
                        );
                        items.add(item);
                    }
                }

                if (items.size() > 0)
                {
                    return new RepairRequest(this, player, items, price);
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "You don't have any items to repair!");
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

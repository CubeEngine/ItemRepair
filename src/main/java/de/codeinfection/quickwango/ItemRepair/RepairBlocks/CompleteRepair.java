package de.codeinfection.quickwango.ItemRepair.RepairBlocks;

import de.codeinfection.quickwango.ItemRepair.Item;
import de.codeinfection.quickwango.ItemRepair.ItemRepairConfiguration;
import de.codeinfection.quickwango.ItemRepair.RepairBlock;
import de.codeinfection.quickwango.ItemRepair.RepairRequest;
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
        super("complete", material);
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
        if (hasPermission(player))
        {
            double price = 0;
            ArrayList<ItemStack> allItems = new ArrayList<ItemStack>();
            Collections.addAll(allItems, player.getInventory().getArmorContents());
            Collections.addAll(allItems, player.getInventory().getContents());
            List<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemInHand = player.getItemInHand();
            Item item = Item.getByMaterial(itemInHand.getType());

            if (itemInHand != null && item != null)
            {
                Item currentItem;
                for (ItemStack itemStack : allItems)
                {
                    currentItem = Item.getByMaterial(itemStack.getType());
                    if (itemStack != null && currentItem != null && itemStack.getDurability() > 0)
                    {
                        price += (itemStack.getDurability()
                                * this.config.price_perDamage
                                * itemStack.getAmount()
                                * getEnchantmentMultiplier(itemStack, this.config.price_enchantMultiplier_factor, this.config.price_enchantMultiplier_base));
                        items.add(itemStack);
                    }
                }

                if (items.size() > 0)
                {

                    player.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_RED + "ItemRepair" + ChatColor.GREEN + "]");
                    player.sendMessage(ChatColor.AQUA + "Rightclick" + ChatColor.WHITE + " again to repair all your damaged items.");
                    player.sendMessage("The repair would cost " + ChatColor.AQUA + getEconomy().format(price) + ChatColor.WHITE + ".");
                    player.sendMessage("You have currently " + ChatColor.AQUA + getEconomy().format(getEconomy().getBalance(player.getName())));

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
        Player player = request.getPlayer();

        if (getEconomy().getBalance(player.getName()) >= price)
        {
            List<ItemStack> items = request.getItems();
            ItemStack itemInHand = player.getItemInHand();
            if (Item.getByMaterial(itemInHand.getType()) != null)
            {
                items.add(itemInHand);
            }
            if (getEconomy().withdrawPlayer(player.getName(), price).transactionSuccess())
            {
                repairItems(items);
                player.sendMessage(ChatColor.GREEN + "Your items have been repaired for " + ChatColor.AQUA + getEconomy().format(price));
            }
            else
            {
                player.sendMessage(ChatColor.RED + "Something went wrong, report this failure to your administrator!");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You don't have enough money to repair all your items!");
        }
    }
}

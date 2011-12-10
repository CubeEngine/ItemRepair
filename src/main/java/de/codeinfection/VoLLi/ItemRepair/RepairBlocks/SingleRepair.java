package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

import com.iCo6.iConomy;
import com.iCo6.system.Holdings;
import de.codeinfection.VoLLi.ItemRepair.ItemRepair;
import de.codeinfection.VoLLi.ItemRepair.ItemRepairConfiguration;
import de.codeinfection.VoLLi.ItemRepair.RepairBlock;
import de.codeinfection.VoLLi.ItemRepair.RepairRequest;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author VoLLi
 */
public class SingleRepair extends RepairBlock
{
    private final ItemRepairConfiguration config;
    
    public SingleRepair(Material material, ItemRepairConfiguration config)
    {
        super(material);
        this.config = config;
    }

    public SingleRepair(int blockId, ItemRepairConfiguration config)
    {
        this(Material.getMaterial(blockId), config);
    }

    public SingleRepair(String blockName, ItemRepairConfiguration config)
    {
        this(Material.getMaterial(blockName), config);
    }

    public RepairRequest requestRepair(Player player)
    {
        if (hasRepairPermission(player, "singleRepair"))
        {
            ItemStack itemInHand = player.getItemInHand();
            Material itemType = itemInHand.getType();
            if (itemType != Material.AIR) // -> hat ein item in der hand?
            {
                int currentDurability = itemInHand.getDurability();
                if (itemInHand.getType().getMaxDurability() > -1) // -> ist reparierbar?
                {
                    if (currentDurability > 0) // -> ist beschädigt?
                    {
                        double price = itemInHand.getDurability();
                        price *= this.config.price_perDamage;
                        price *= itemInHand.getAmount();
                        price *= getEnchantmentMultiplier(itemInHand, this.config.price_enchantMultiplier_factor, this.config.price_enchantMultiplier_base);

                        RepairRequest request = new RepairRequest(this, player, Arrays.asList(itemInHand), price);

                        return request;
                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "This item isn't damaged!");
                    }
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "This item can't be repaired!");
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You don't have a item in your hand!");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You don't have the permission to repair a single item!");
        }
        return null;
    }

    @Override
    public void repair(RepairRequest request)
    {
        double price = request.getPrice();
        Holdings holdings = request.getHoldings();
        Player player = request.getPlayer();

        if (player.getInventory().getHeldItemSlot() == request.getHeldItemSlot())
        {
            ItemStack itemInHand = player.getItemInHand();
            if (itemInHand != null)
            {
                if (itemInHand.equals(request.getHeldItem()))
                {
                    if (holdings.hasEnough(price))
                    {
                        holdings.subtract(price);
                        ItemRepair.debug("ItemInHand pointer: " + (player.getItemInHand() == request.getHeldItem() ? "OK" : "FUCK!!"));

                        request.getHeldItem().setDurability((short)0);
                        request.getHeldItem().setAmount(request.getHeldItem().getAmount() + 1);
                        //itemInHand.setDurability((short)0);
                        //request.getItems().get(0).setDurability((short)0);
                        player.sendMessage(ChatColor.GREEN + "Your item has been repaired for " + ChatColor.AQUA + iConomy.format(price));
                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "You don't have enough money!");
                    }
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "The item in your hand has changed, repair has been cancelled!");
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "Your hands are suddenly empty, repair has been cancelled!");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You switched to anoter item slot, repair has been cancelled!");
        }
    }
}

package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

import com.iCo6.iConomy;
import com.iCo6.system.Holdings;
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
public class SinglecRepair extends RepairBlock
{
    private final ItemRepairConfiguration config;
    
    public SinglecRepair(Material material, ItemRepairConfiguration config)
    {
        super(material);
        this.config = config;
    }

    public SinglecRepair(int blockId, ItemRepairConfiguration config)
    {
        this(Material.getMaterial(blockId), config);
    }

    public SinglecRepair(String blockName, ItemRepairConfiguration config)
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
                        //itemInHand.setDurability((short)0);
                        request.getItems().get(0).setDurability((short)0);
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

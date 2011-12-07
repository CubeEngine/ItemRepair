package de.codeinfection.VoLLi.ItemRepair;

import com.iCo6.system.Accounts;
import com.iCo6.system.Holdings;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author VoLLi
 */
public abstract class RepairBlock
{
    private final static Accounts accounts = new Accounts();
    
    public final Material material;
    
    public RepairBlock(Material material)
    {
        if (material != null)
        {
            if (material.isBlock())
            {
                this.material = material;
            }
            else
            {
                throw new IllegalArgumentException("material must be block!");
            }
        }
        else
        {
            throw new IllegalArgumentException("material must not be null!");
        }
    }

    public RepairBlock(String material)
    {
        this(Material.getMaterial(material));
    }

    public RepairBlock(int material)
    {
        this(Material.getMaterial(material));
    }

    public abstract RepairRequest requestRepair(Player player);
    
    public abstract void repair(RepairRequest request);


    /*
     * Utilities
     */
    
    public static boolean hasRepairPermission(Player player, String permission)
    {
        return (player.hasPermission("itemrepair.allblocks") || player.hasPermission("itemrepair.block." + permission));
    }
    
    public static boolean isRepairable(ItemStack item)
    {
        return (item.getType().getMaxDurability() > -1);
    }

    public static double getEnchantmentMultiplier(ItemStack item, double factor, double base)
    {
        double enchantmentLevel = 0;
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet())
        {
            enchantmentLevel += entry.getValue();
        }

        if (enchantmentLevel > 0)
        {
            double enchantmentMultiplier = factor * Math.pow(base, enchantmentLevel);

            enchantmentMultiplier = enchantmentMultiplier / 100D + 1D;

            return enchantmentMultiplier;
        }
        else
        {
            return 1D;
        }
    }

    public static void repairItems(List<ItemStack> items)
    {
        repairItems(items, (short)0);
    }

    public static void repairItems(List<ItemStack> items, short durability)
    {
        ItemRepair.debug("Items to repair: " + items.size());
        for (ItemStack item : items)
        {
            item.setDurability(durability);
            ItemRepair.debug("Repaired itemstack: " + item.getType().toString());
        }
    }

    public static Holdings getHoldings(Player player)
    {
        return accounts.get(player.getName()).getHoldings();
    }

    public static void removeHeldItem(Player player)
    {
        PlayerInventory inventory = player.getInventory();
        inventory.clear(inventory.getHeldItemSlot());
    }
}

package de.codeinfection.quickwango.ItemRepair;

import java.util.List;
import java.util.Map;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author VoLLi
 */
public abstract class RepairBlock
{

    public static final String PERMISSION_BASE = "itemrepair.block.";
    private static final Economy economy = ItemRepair.getInstance().getEconomy();
    public final String name;
    public final Material material;
    public final Permission permission;

    public RepairBlock(String name, Material material)
    {
        this.name = name;
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
        this.permission = new Permission(PERMISSION_BASE + name, PermissionDefault.OP);
    }

    public Economy getEconomy()
    {
        return economy;
    }

    public RepairBlock(String name, String material)
    {
        this(name, Material.getMaterial(material));
    }

    public RepairBlock(String name, int material)
    {
        this(name, Material.getMaterial(material));
    }

    public abstract RepairRequest requestRepair(Player player);

    public abstract void repair(RepairRequest request);


    /*
     * Utilities
     */
    public boolean hasPermission(Player player)
    {
        return player.hasPermission(this.permission);
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

    public static void repairItems(RepairRequest request)
    {
        repairItems(request.getItems());
    }

    public static void repairItems(List<ItemStack> items)
    {
        repairItems(items, (short) 0);
    }

    public static void repairItems(List<ItemStack> items, short durability)
    {
        for (ItemStack item : items)
        {
            item.setDurability(durability);
        }
    }

    public static void removeHeldItem(Player player)
    {
        PlayerInventory inventory = player.getInventory();
        inventory.clear(inventory.getHeldItemSlot());
    }
}

package de.cubeisland.ItemRepair;

import java.util.HashMap;
import java.util.Map;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * Represents a repair block
 *
 * @author Phillip Schichtel
 */
public abstract class RepairBlock
{
    private final String permissionBase;
    private final RepairPlugin plugin;
    private final Economy economy;
    private final Server server;
    private final MaterialPriceProvider priceProvider;

    private final String title;
    private final Material material;
    private final Permission permission;

    private final Map<Player, Inventory> inventoryMap;

    public RepairBlock(RepairPlugin plugin, String name, String material)
    {
        this(plugin, name, Material.matchMaterial(material));
    }

    public RepairBlock(RepairPlugin plugin, String name, int material)
    {
        this(plugin, name, Material.getMaterial(material));
    }

    public RepairBlock(RepairPlugin plugin, String title, Material material)
    {
        this.plugin = plugin;
        this.economy = plugin.getEconomy();
        this.server = plugin.getServer();
        this.priceProvider = plugin.getMaterialPriceProvider();
        this.permissionBase = this.plugin.getName() + ".block.";
        this.title = title;
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
        this.permission = new Permission(this.permissionBase + title, PermissionDefault.OP);
        this.inventoryMap = new HashMap<Player, Inventory>();
    }

    public final Economy getEconomy()
    {
        return economy;
    }

    public final String getTitle()
    {
        return this.title;
    }

    public final Permission getPermission()
    {
        return this.permission;
    }

    public final Material getMaterial()
    {
        return this.material;
    }

    public final Server getServer()
    {
        return this.server;
    }
    
    public boolean hasPermission(Player player)
    {
        return player.hasPermission(this.permission);
    }

    public double calculatePrice(Iterable<ItemStack> items)
    {
        return this.calculatePrice(items, 1, 1);
    }

    public double calculatePrice(Iterable<ItemStack> items, double enchantmentFactor, double enchantmentBase)
    {
        double price = 0.0;

        Material type;
        Item item;
        BaseMaterial baseMaterial;
        double currentPrice;
        for (ItemStack itemStack : items)
        {
            type = itemStack.getType();
            item = Item.getByMaterial(type);
            baseMaterial = item.getBaseMaterial();

            currentPrice = item.getBaseMaterialCount() * this.priceProvider.getPrice(baseMaterial);
            currentPrice *= (double)Math.min(itemStack.getDurability(), type.getMaxDurability()) / (double)type.getMaxDurability();
            currentPrice *= getEnchantmentMultiplier(itemStack, enchantmentFactor, enchantmentBase);

            price += currentPrice;
        }

        return price;
    }

    public Inventory getInventory(final Player player)
    {
        if (player == null)
        {
            return null;
        }
        Inventory inventory = this.inventoryMap.get(player);
        if (inventory == null)
        {
            inventory = this.server.createInventory(player, 9 * 4, this.title);
            this.inventoryMap.put(player, inventory);
        }
        return inventory;
    }

    public abstract RepairRequest requestRepair(Inventory inventory);

    public abstract void repair(RepairRequest request);


    /*
     * Utilities
     */

    public static double getEnchantmentMultiplier(ItemStack item, double factor, double base)
    {
        double enchantmentLevel = 0;
        for (Integer level : item.getEnchantments().values())
        {
            enchantmentLevel += level;
        }

        if (enchantmentLevel > 0)
        {
            double enchantmentMultiplier = factor * Math.pow(base, enchantmentLevel);

            enchantmentMultiplier = enchantmentMultiplier / 100.0 + 1.0;

            return enchantmentMultiplier;
        }
        else
        {
            return 1.0;
        }
    }

    public static void repairItems(RepairRequest request)
    {
        repairItems(request.getItems().values());
    }

    public static void repairItems(Iterable<ItemStack> items)
    {
        repairItems(items, (short)0);
    }

    public static void repairItems(Iterable<ItemStack> items, short durability)
    {
        for (ItemStack item : items)
        {
            repairItem(item, durability);
        }
    }
    
    public static void repairItem(ItemStack item)
    {
        repairItem(item, (short)0);
    }
    
    public static void repairItem(ItemStack item, short durability)
    {
        if (item != null)
        {
            item.setDurability(durability);
        }
    }

    public static Map<Integer, ItemStack> getRepairableItems(Inventory inventory)
    {
        Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();

        ItemStack item;
        for (int i = 0; i < inventory.getSize(); ++i)
        {
            item = inventory.getItem(i);
            if (item != null && Item.getByMaterial(item.getType()) != null && item.getDurability() > 0)
            {
                items.put(i, item);
            }
        }

        return items;
    }

    public static void removeHeldItem(Player player)
    {
        PlayerInventory inventory = player.getInventory();
        inventory.clear(inventory.getHeldItemSlot());
    }
}

package de.codeinfection.VoLLi.ItemRepair;

import com.iCo6.system.Accounts;
import com.iCo6.system.Holdings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final static Map<Player, RepairBlock> repairingPlayers = new HashMap<Player, RepairBlock>();
    private final static Accounts accounts = new Accounts();
    
    public final int blockId;
    
    public RepairBlock(int blockId)
    {
        this.blockId = blockId;
    }

    public abstract RepairRequest requestRepair(Player player);
    
    public abstract void repair(RepairRequest request);


    /*
     * Utilities
     */
    
    public static boolean hasRepairPermission(Player player, String permission)
    {
        return (!player.hasPermission("itemrepair.all") && !player.hasPermission("itemrepair.block." + permission));
    }
    
    public static boolean isRepairable(ItemStack item)
    {
        return (item.getType().getMaxDurability() > -1);
    }

    public static double getEnchantmentMultiplier(ItemStack item)
    {
        double enchantmentLevel = 0;
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet())
        {
            enchantmentLevel += entry.getValue();
        }

        double enchantmentMultiplier = 6D * Math.pow(2, enchantmentLevel);

        enchantmentMultiplier = enchantmentMultiplier / 100D + 1D;

        return enchantmentMultiplier;
    }

    public static void repairItems(List<ItemStack> items)
    {
        repairItems(items, (short)0);
    }

    public static void repairItems(List<ItemStack> items, short durability)
    {
        for (ItemStack item : items)
        {
            item.setDurability(durability);
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

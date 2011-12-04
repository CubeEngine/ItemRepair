package de.codeinfection.VoLLi.ItemRepair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author VoLLi
 */
public abstract class RepairBlock
{
    private final static Map<Player, RepairBlock> repairingPlayers = new HashMap<Player, RepairBlock>();
    
    public final int blockId;
    protected double price;
    protected short durability;
    protected String successMessage;
    protected String failMessage;
    protected String permission;
    
    public RepairBlock(int blockId)
    {
        this.blockId = blockId;
        this.price = 0;
        this.durability = 0;
        this.successMessage = null;
        this.failMessage = null;
        this.permission = null;
    }
    
    public static boolean hasPlayerStartedRepairing(Player player)
    {
        return repairingPlayers.containsKey(player);
    }
    
    public static void addRepairingPlayer(Player player, RepairBlock repairBlock)
    {
        repairingPlayers.put(player, repairBlock);
    }
    
    public static void removeRepairingPlayer(Player player)
    {
        repairingPlayers.remove(player);
    }
    
    public abstract List<ItemStack> getItems(Player player);
    
    public final String getSuccessMessage()
    {
        return this.successMessage;
    }
    
    public final String getFailMessage()
    {
        return this.failMessage;
    }
    
    public final double getPrice()
    {
        return this.price;
    }
    
    public final short getDurability()
    {
        return this.durability;
    }
    
    public final String getPermission()
    {
        return this.permission;
    }
    
    public void afterRepair(Player player)
    {}
    
    public static boolean isRepairable(ItemStack item)
    {
        return (item.getType().getMaxDurability() > -1);
    }
}

package de.codeinfection.quickwango.ItemRepair;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a repair request
 *
 * @author Phillip Schichtel
 */
public class RepairRequest
{
    private final RepairBlock repairBlock;
    private final Player player;
    private final List<ItemStack> items;
    private final double price;
    private final int heldItemSlot;
    private final ItemStack heldItem;

    public RepairRequest(RepairBlock repairBlock, Player player, List<ItemStack> items, double price)
    {
        if (repairBlock == null)
        {
            throw new IllegalArgumentException("repairBlock must not be null!");
        }
        this.repairBlock = repairBlock;
        this.player = player;
        this.heldItem = player.getItemInHand();
        this.heldItemSlot = player.getInventory().getHeldItemSlot();
        this.items = items;
        this.price = price;
    }

    public RepairBlock getRepairBlock()
    {
        return this.repairBlock;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public int getHeldItemSlot()
    {
        return this.heldItemSlot;
    }

    public ItemStack getHeldItem()
    {
        return this.heldItem;
    }

    public List<ItemStack> getItems()
    {
        return this.items;
    }

    public double getPrice()
    {
        return this.price;
    }
}

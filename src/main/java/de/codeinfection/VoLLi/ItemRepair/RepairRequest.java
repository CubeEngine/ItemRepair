package de.codeinfection.VoLLi.ItemRepair;

import com.iCo6.system.Holdings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author CodeInfection
 */
public class RepairRequest
{
    private final RepairBlock repairBlock;
    private final Player player;
    private final Holdings holdings;
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
        this.holdings = RepairBlock.getHoldings(player);
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

    public Holdings getHoldings()
    {
        return this.holdings;
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

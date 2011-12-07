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
    private final Player player;
    private final Holdings holdings;
    private final List<ItemStack> items;
    private final double price;
    private final int heldItemSlot;
    private final ItemStack heldItem;

    private final static Map<Player, RepairRequest> repairRequests = new HashMap<Player, RepairRequest>();

    public RepairRequest(Player player, List<ItemStack> items, double price)
    {
        this.player = player;
        this.heldItem = player.getItemInHand();
        this.heldItemSlot = player.getInventory().getHeldItemSlot();
        this.holdings = RepairBlock.getHoldings(player);
        this.items = items;
        this.price = price;
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

    public static boolean hasPlayerRequestedRepair(Player player)
    {
        return repairRequests.containsKey(player);
    }

    public static void requestRepair(Player player, RepairRequest request)
    {
        if (!hasPlayerRequestedRepair(player))
        {
            repairRequests.put(player, request);
        }
    }

    public static void removeRepairRequest(Player player)
    {
        repairRequests.remove(player);
    }

    public static RepairRequest getRepairRequest(Player player)
    {
        return repairRequests.get(player);
    }
}

package de.cubeisland.ItemRepair.RepairBlocks;

import de.cubeisland.ItemRepair.ItemRepair;
import static de.cubeisland.ItemRepair.ItemRepair._;
import de.cubeisland.ItemRepair.ItemRepairConfiguration;
import de.cubeisland.ItemRepair.RepairBlock;
import de.cubeisland.ItemRepair.RepairRequest;
import java.util.Map;
import java.util.Random;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Repairs blocks cheaper
 *
 * @author Phillip Schichtel
 */
public class CheapRepair extends RepairBlock
{

    private final ItemRepairConfiguration config;
    private final Random rand;

    public CheapRepair(ItemRepair plugin)
    {
        super(ItemRepair.getInstance(), "cheap", _("cheapRepair"), plugin.getConfiguration().repairBlocks_cheap_block);
        this.config = plugin.getConfiguration();
        this.rand = new Random(System.currentTimeMillis());
    }

    @Override
    public RepairRequest requestRepair(Inventory inventory)
    {
        final Player player = (Player)inventory.getHolder();
        Map<Integer, ItemStack> items = getRepairableItems(inventory);
        if (items.size() > 0)
        {
            double price = calculatePrice(items.values()) * (this.config.repairBlocks_cheap_costPercentage / 100.0);

            player.sendMessage(_("headline"));
            player.sendMessage(_("rightClickAgain"));
            player.sendMessage(_("repairWouldCost", getEconomy().format(price)));
            player.sendMessage(_("youCurrentlyHave", getEconomy().format(getEconomy().getBalance(player.getName()))));

            return new RepairRequest(this, inventory, items, price);
        }
        else
        {
            player.sendMessage(_("noItems"));
        }
        return null;
    }

    @Override
    public void repair(RepairRequest request)
    {
        final double price = request.getPrice();
        final Inventory inventory = request.getInventory();
        final Player player = (Player)inventory.getHolder();

        if (checkBalance(player, price))
        {
            if (withdrawPlayer(player, price))
            {
                boolean itemsBroken = false;
                ItemStack item;
                int amount;
                for (Map.Entry<Integer, ItemStack> entry : request.getItems().entrySet())
                {
                    item = entry.getValue();
                    if (this.rand.nextInt(100) > this.config.repairBlocks_cheap_breakPercentage)
                    {
                        repairItem(entry.getValue());
                    }
                    else
                    {
                        itemsBroken = true;
                        amount = item.getAmount();
                        if (amount == 1)
                        {
                            inventory.clear(entry.getKey());
                        }
                        else
                        {
                            item.setAmount(amount - 1);
                            repairItem(item);
                        }
                    }
                }
                if (itemsBroken)
                {
                    player.sendMessage(_("someItemsBroke"));
                    player.playEffect(player.getLocation(), Effect.GHAST_SHRIEK, 0);
                }
                player.sendMessage(_("itemsRepairedCheap", getEconomy().format(price), this.config.repairBlocks_cheap_costPercentage));
            }
            else
            {
                player.sendMessage(_("somethingWentWrong"));
            }
        }
        else
        {
            player.sendMessage(_("notEnoughMoney"));
        }
    }
}

package de.codeinfection.quickwango.ItemRepair.RepairBlocks;

import de.codeinfection.quickwango.ItemRepair.ItemRepair;
import de.codeinfection.quickwango.ItemRepair.ItemRepairConfiguration;
import de.codeinfection.quickwango.ItemRepair.RepairBlock;
import de.codeinfection.quickwango.ItemRepair.RepairRequest;
import static de.codeinfection.quickwango.Translation.Translator.t;
import java.util.Map;
import java.util.Random;
import org.bukkit.Effect;
import org.bukkit.Material;
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

    public CheapRepair(Material material, ItemRepairConfiguration config)
    {
        super(ItemRepair.getInstance(), t("cheapRepair"), material);
        this.config = config;
        this.rand = new Random(System.currentTimeMillis());
    }

    public CheapRepair(int blockId, ItemRepairConfiguration config)
    {
        this(Material.getMaterial(blockId), config);
    }

    public CheapRepair(String blockName, ItemRepairConfiguration config)
    {
        this(Material.getMaterial(blockName), config);
    }

    @Override
    public RepairRequest requestRepair(Inventory inventory)
    {
        final Player player = (Player)inventory.getHolder();
        Map<Integer, ItemStack> items = getRepairableItems(inventory);
        if (items.size() > 0)
        {
            double price = calculatePrice(items.values()) * (this.config.repairBlocks_cheap_costPercentage / 100.0);

            player.sendMessage(t("headline"));
            player.sendMessage(t("rightClickAgain"));
            player.sendMessage(t("repairWouldCost", getEconomy().format(price)));
            player.sendMessage(t("youCurrentlyHave", getEconomy().format(getEconomy().getBalance(player.getName()))));

            return new RepairRequest(this, inventory, items, price);
        }
        else
        {
            player.sendMessage(t("noItems"));
        }
        return null;
    }

    @Override
    public void repair(RepairRequest request)
    {
        final double price = request.getPrice();
        final Inventory inventory = request.getInventory();
        final Player player = (Player)inventory.getHolder();

        if (getEconomy().getBalance(player.getName()) >= price)
        {
            if (getEconomy().withdrawPlayer(player.getName(), price).transactionSuccess())
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
                    player.sendMessage(t("someItemsBroke"));
                    player.playEffect(player.getLocation(), Effect.GHAST_SHRIEK, 0);
                }
                player.sendMessage(t("itemsRepairedCheap", getEconomy().format(price), this.config.repairBlocks_cheap_costPercentage));
            }
            else
            {
                player.sendMessage(t("somethingWentWrong"));
            }
        }
        else
        {
            player.sendMessage(t("notEnoughMoney"));
        }
    }
}

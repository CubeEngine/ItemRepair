package de.codeinfection.quickwango.ItemRepair.RepairBlocks;

import de.codeinfection.quickwango.ItemRepair.ItemRepair;
import de.codeinfection.quickwango.ItemRepair.RepairBlock;
import de.codeinfection.quickwango.ItemRepair.RepairRequest;
import static de.codeinfection.quickwango.Translation.Translator.t;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Repairs all blocks
 *
 * @author Phillip Schichtel
 */
public class NormalRepair extends RepairBlock
{

    public NormalRepair(Material material)
    {
        super(ItemRepair.getInstance(), t("repair"), material);
    }

    public NormalRepair(int blockId)
    {
        this(Material.getMaterial(blockId));
    }

    public NormalRepair(String blockName)
    {
        this(Material.getMaterial(blockName));
    }

    @Override
    public RepairRequest requestRepair(Inventory inventory)
    {
        final Player player = (Player)inventory.getHolder();
        Map<Integer, ItemStack> items = getRepairableItems(inventory);
        if (items.size() > 0)
        {
            double price = calculatePrice(items.values());

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
        final Player player = (Player)request.getInventory().getHolder();

        if (getEconomy().getBalance(player.getName()) >= price)
        {
            if (getEconomy().withdrawPlayer(player.getName(), price).transactionSuccess())
            {
                repairItems(request);
                player.sendMessage(t("itemsRepaired", getEconomy().format(price)));
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

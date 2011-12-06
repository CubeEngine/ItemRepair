package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

import com.iCo6.iConomy;
import com.iCo6.system.Holdings;
import de.codeinfection.VoLLi.ItemRepair.RepairRequest;
import java.util.Arrays;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author VoLLi
 */
public class CheapRepair extends GenericRepairBlock
{
    private final int breakPercentage;
    private final int costPercentage;
    
    private final Random rand;
    
    public CheapRepair(int blockId, double basePrice, int breakPercentage, int costPercentage)
    {
        super(blockId, basePrice);
        this.breakPercentage = breakPercentage;
        this.costPercentage = costPercentage;
        this.rand = new Random(System.currentTimeMillis());
    }
    
    @Override
    public RepairRequest requestRepair(Player player)
    {
        ItemStack itemInHand = player.getItemInHand();
        
        if (itemInHand != null) // -> hat ein item in der hand?
        {
            int currentDurability = itemInHand.getDurability();
            if (itemInHand.getType().getMaxDurability() > -1) // -> ist reparierbar?
            {
                if (currentDurability > 0) // -> ist beschädigt?
                {
                    double price = (double)itemInHand.getDurability() * this.pricePerDamage * ((double)this.costPercentage / 100.0D);
                    return new RepairRequest(player, Arrays.asList(itemInHand), price);
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "Das Item ist nicht beschädigt!");
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "Dieses Item kann man nicht reparieren!");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Du hast kein Item in der Hand!");
        }
        return null;
    }

    @Override
    public void repair(RepairRequest request)
    {
        double price = request.getPrice();
        Holdings holdings = request.getHoldings();
        Player player = request.getPlayer();

        if (holdings.hasEnough(price))
        {
            if (this.rand.nextInt(100) > this.breakPercentage)
            {
                player.sendMessage(ChatColor.GREEN + "Dein Item wurde für " + ChatColor.AQUA + iConomy.format(price) + ChatColor.GREEN + " (" + ChatColor.RED + this.costPercentage + "% " + ChatColor.GREEN + "des regulären Preises) repariert!");
                repairItems(request.getItems());
            }
            else
            {
                player.sendMessage("Dein Item ist leider bei der Reparatur zerbrochen.. " + ChatColor.RED + ">>:->");
                player.playEffect(player.getLocation(), Effect.GHAST_SHRIEK, 0);
                removeHeldItem(player);
            }
        }
    }
}

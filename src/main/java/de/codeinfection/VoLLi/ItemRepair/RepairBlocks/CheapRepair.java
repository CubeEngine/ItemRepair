package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

import com.iCo6.iConomy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author VoLLi
 */
public class CheapRepair extends GenericRepairBlock
{
    private final int breakPercentage;
    private final int costPercentage;
    
    private List<Player> breakQueue;
    
    private final Random rand;
    
    public CheapRepair(int blockId, double basePrice, int breakPercentage, int costPercentage)
    {
        super(blockId, basePrice);
        this.breakPercentage = breakPercentage;
        this.costPercentage = costPercentage;
        this.rand = new Random(System.currentTimeMillis());
        this.breakQueue = Collections.synchronizedList(new ArrayList<Player>());
    }
    
    @Override
    synchronized public List<ItemStack> getItems(Player player)
    {
        List<ItemStack> items = new ArrayList<ItemStack>();
        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand != null) // -> hat ein item in der hand?
        {
            int currentDurability = itemInHand.getDurability();
            if (itemInHand.getType().getMaxDurability() > -1) // -> ist reparierbar?
            {
                if (currentDurability > 0) // -> ist beschädigt?
                {
                    this.price = (double)itemInHand.getDurability() * this.pricePerDamage * ((double)this.costPercentage / 100.0D);
                    if (this.rand.nextInt(100) > this.breakPercentage)
                    {
                        this.durability = 0;
                        this.successMessage = ChatColor.GREEN + "Dein Item wurde für " + ChatColor.AQUA + iConomy.format(this.price) + ChatColor.GREEN + " (" + ChatColor.RED + this.costPercentage + "% " + ChatColor.GREEN + "des regulären Preises) repariert!";
                    }
                    else
                    {
                        this.successMessage = "Dein Item ist leider bei der Reparatur zerbrochen.. " + ChatColor.RED + ">>:->";
                        this.durability = itemInHand.getType().getMaxDurability();
                        this.breakQueue.add(player);
                    }
                    
                    items.add(itemInHand);
                }
                else
                {
                    this.failMessage = ChatColor.RED + "Das Item ist nicht beschädigt!";
                }
            }
            else
            {
                this.failMessage = ChatColor.RED + "Dieses Item kann man nicht reparieren!";
            }
        }
        else
        {
            this.failMessage = ChatColor.RED + "Du hast kein Item in der Hand!";
        }
        return items;
    }
    
    @Override
    public void afterRepair(Player player)
    {
        if (this.breakQueue.contains(player))
        {
            player.playEffect(player.getLocation(), Effect.GHAST_SHRIEK, 0);
            
            PlayerInventory inventory = player.getInventory();
            inventory.clear(inventory.getHeldItemSlot());
            
            this.breakQueue.remove(player);
        }
    }
}

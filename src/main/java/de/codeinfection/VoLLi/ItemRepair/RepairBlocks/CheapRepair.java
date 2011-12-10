package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

import com.iCo6.iConomy;
import com.iCo6.system.Holdings;
import de.codeinfection.VoLLi.ItemRepair.ItemRepairConfiguration;
import de.codeinfection.VoLLi.ItemRepair.RepairBlock;
import de.codeinfection.VoLLi.ItemRepair.RepairRequest;
import java.util.Arrays;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author VoLLi
 */
public class CheapRepair extends RepairBlock
{
    private final ItemRepairConfiguration config;
    
    private final Random rand;
    
    public CheapRepair(Material material, ItemRepairConfiguration config)
    {
        super(material);
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
    public RepairRequest requestRepair(Player player)
    {
        if (hasRepairPermission(player, "cheapRepair"))
        {
            ItemStack itemInHand = player.getItemInHand();
            Material itemType = itemInHand.getType();
            if (itemType != Material.AIR) // -> hat ein item in der hand?
            {
                int currentDurability = itemInHand.getDurability();
                if (itemInHand.getType().getMaxDurability() > -1) // -> ist reparierbar?
                {
                    if (currentDurability > 0) // -> ist beschädigt?
                    {
                        double price = itemInHand.getDurability();
                        price *= this.config.price_perDamage;
                        price *= itemInHand.getAmount();
                        price *= getEnchantmentMultiplier(itemInHand, this.config.price_enchantMultiplier_factor, this.config.price_enchantMultiplier_base);
                        price *= (this.config.repairBlocks_cheapRepair_costPercentage / 100.0D);
                        return new RepairRequest(this, player, Arrays.asList(itemInHand), price);
                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "This item isn't damaged!");
                    }
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "This item can't be repaired!");
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You don't have a item in your hand!");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You don't have the permission to repair your item cheap!");
        }
        return null;
    }

    @Override
    public void repair(RepairRequest request)
    {
        double price = request.getPrice();
        Holdings holdings = request.getHoldings();
        Player player = request.getPlayer();

        if (player.getInventory().getHeldItemSlot() == request.getHeldItemSlot())
        {
            ItemStack itemInHand = player.getItemInHand();
            if (itemInHand != null)
            {
                if (itemInHand.equals(request.getHeldItem()))
                {
                    if (holdings.hasEnough(price))
                    {
                        if (this.rand.nextInt(100) > this.config.repairBlocks_cheapRepair_breakPercentage)
                        {
                            player.sendMessage(ChatColor.GREEN + "Dein Item wurde für " + ChatColor.AQUA + iConomy.format(price) + ChatColor.GREEN + " (" + ChatColor.RED + this.config.repairBlocks_cheapRepair_costPercentage + "% " + ChatColor.GREEN + "des regulären Preises) repariert!");
                            repairItems(request.getItems());
                        }
                        else
                        {
                            player.sendMessage("Dein Item ist leider bei der Reparatur zerbrochen.. " + ChatColor.RED + ">>:->");
                            player.playEffect(player.getLocation(), Effect.GHAST_SHRIEK, 0);
                            removeHeldItem(player);
                        }
                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "You don't have enough money!");
                    }
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "The item in your hand has changed, repair has been cancelled!");
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "Your hands are suddenly empty, repair has been cancelled!");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You switched to anoter item slot, repair has been cancelled!");
        }
    }
}

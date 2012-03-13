package de.codeinfection.VoLLi.ItemRepair.RepairBlocks;

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

                        player.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_RED + "ItemRepair" + ChatColor.GREEN + "]");
                        player.sendMessage(ChatColor.AQUA + "Rightclick" + ChatColor.WHITE + " again to repair your item, with a chance of breaking it.");
                        player.sendMessage("The repair would cost " + ChatColor.AQUA + getEconomy().format(price) + ChatColor.WHITE + ".");
                        player.sendMessage("You have currently " + ChatColor.AQUA + getEconomy().format(getEconomy().getBalance(player.getName())));

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
        Player player = request.getPlayer();

        if (player.getInventory().getHeldItemSlot() == request.getHeldItemSlot())
        {
            ItemStack itemInHand = player.getItemInHand();
            if (itemInHand != null)
            {
                if (itemInHand.equals(request.getHeldItem()))
                {
                    if (getEconomy().getBalance(player.getName()) >= price)
                    {
                        if (this.rand.nextInt(100) > this.config.repairBlocks_cheapRepair_breakPercentage)
                        {
                            player.sendMessage(ChatColor.GREEN + "Your item has been repaired for " + ChatColor.AQUA + getEconomy().format(price) + ChatColor.GREEN + " (" + ChatColor.RED + this.config.repairBlocks_cheapRepair_costPercentage + "% " + ChatColor.GREEN + "of the regular price)!");
                            player.getItemInHand().setDurability((short) 0);
                            getEconomy().depositPlayer(player.getName(), -price);
                        }
                        else
                        {
                            player.sendMessage("Sorry, but your item broke... " + ChatColor.RED + ">>:->");
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
            player.sendMessage(ChatColor.RED + "You switched to another item slot, repair has been cancelled!");
        }
    }
}

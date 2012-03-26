package de.codeinfection.quickwango.ItemRepair.RepairBlocks;

import de.codeinfection.quickwango.ItemRepair.ItemRepairConfiguration;
import de.codeinfection.quickwango.ItemRepair.RepairBlock;
import de.codeinfection.quickwango.ItemRepair.RepairRequest;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author VoLLi
 * @und Baeume Faithcaio
 */
public class SingleRepair extends RepairBlock
{

    private final ItemRepairConfiguration config;

    public SingleRepair(Material material, ItemRepairConfiguration config)
    {
        super("single", material);
        this.config = config;
    }

    public SingleRepair(int blockId, ItemRepairConfiguration config)
    {
        this(Material.getMaterial(blockId), config);
    }

    public SingleRepair(String blockName, ItemRepairConfiguration config)
    {
        this(Material.getMaterial(blockName), config);
    }

    //Auszulagernde Funktion
    public RepairRequest requestPrice(Player player)
    {
        if (hasRepairPermission(player, "singleRepair"))
        {
            ItemStack itemInHand = player.getItemInHand();
            Material itemType = itemInHand.getType();
            if (itemType != Material.AIR) // -> has a item in hand?
            {
                int currentDurability = itemInHand.getDurability();
                if (isRepairable(itemInHand)) // -> item is repairable?
                {
                    if (currentDurability > 0) // -> item is damaged?
                    {
                        double price = itemInHand.getDurability()/iteminHand.getMaxDurability(); //Percentage
                        switch (iteminHand.getItemID()) //gibts die Funktion ueberhaupt?
                        {
                         case 256: price *= 1, this.config.price_iron; //Shovel
                         case 257: price *= 3, this.config.price_iron; //Pickaxe
                         case 259: price *= 3, this.config.price_iron; //Axe
                         case 259: price *= 1, this.config.price_iron; //Feuerzeug
                         case 261: price *= 2, this.config.price_wood; //Bogen
                         case 267: price *= 2, this.config.price_iron; //Schwert
                         case 268: price *= 2, this.config.price_wood; //Schwert
                         case 269: price *= 1, this.config.price_wood; //Shovel
                         case 270: price *= 3, this.config.price_wood; //Pickaxe
                         case 271: price *= 3, this.config.price_wood; //Axe
                         case 272: price *= 2, this.config.price_stone; //Schwert
                         case 273: price *= 1, this.config.price_stone; //Shovel
                         case 274: price *= 3, this.config.price_stone; //Pickaxe
                         case 275: price *= 3, this.config.price_stone; //Axe
                         case 276: price *= 2, this.config.price_diamond; //Schwert
                         case 277: price *= 1, this.config.price_diamond; //Shovel
                         case 278: price *= 3, this.config.price_diamond; //Pickaxe
                         case 279: price *= 3, this.config.price_diamond; //Axe
                         case 283: price *= 2, this.config.price_gold; //Schwert
                         case 284: price *= 2, this.config.price_gold; //Shovel
                         case 285: price *= 2, this.config.price_gold; //Pickaxe
                         case 286: price *= 2, this.config.price_gold; //Axe
                         case 290: price *= 3, this.config.price_wood; //Hoe
                         case 291: price *= 2, this.config.price_stone; //Hoe
                         case 292: price *= 2, this.config.price_iron; //Hoe
                         case 293: price *= 2, this.config.price_diamond; //Hoe
                         case 294: price *= 2, this.config.price_gold; //Hoe
                         case 298: price *= 5, this.config.price_leather; //Helmet
                         case 299: price *= 8, this.config.price_leather; //Chestplate
                         case 300: price *= 7, this.config.price_leather; //Leggings
                         case 301: price *= 4, this.config.price_leather; //Boots
                         case 302: price *= 5, this.config.price_fire; //Helmet
                         case 303: price *= 8, this.config.price_fire; //Chestplate
                         case 304: price *= 7, this.config.price_fire; //Leggings
                         case 305: price *= 4, this.config.price_fire; //Boots
                         case 306: price *= 5, this.config.price_fire; //Helmet
                         case 307: price *= 8, this.config.price_fire; //Chestplate
                         case 308: price *= 7, this.config.price_fire; //Leggings
                         case 309: price *= 4, this.config.price_fire; //Boots
                         case 310: price *= 5, this.config.price_diamond; //Helmet
                         case 311: price *= 8, this.config.price_diamond; //Chestplate
                         case 312: price *= 7, this.config.price_diamond; //Leggings
                         case 313: price *= 4, this.config.price_diamond; //Boots
                         case 314: price *= 5, this.config.price_diamond; //Helmet
                         case 315: price *= 8, this.config.price_diamond; //Chestplate
                         case 316: price *= 7, this.config.price_diamond; //Leggings
                         case 317: price *= 4, this.config.price_diamond; //Boots
                         case 346: price *= 2, this.config.price_wood; //Fishing Rod
                         case 359: price *= 2, this.config.price_iron; //Shears
                         default:  price *= 2, this.config.price_iron; //Default
                        }
                        //in der ItemRepairConfig PerUseDmg loeschen wird nimmer gebraucht
                        price *= itemInHand.getAmount();  //Was macht das??? @philipp bei mehreren Items gestackt muesste nur das erste repariert werden
                        price *= getEnchantmentMultiplier(itemInHand, this.config.price_enchantMultiplier_factor, this.config.price_enchantMultiplier_base);

                        ArrayList<ItemStack> items = new ArrayList<ItemStack>(1);
                        items.add(itemInHand);

                        player.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_RED + "ItemRepair" + ChatColor.GREEN + "]");
                        player.sendMessage(ChatColor.AQUA + "Rightclick" + ChatColor.WHITE + " again to repair your item.");
                        player.sendMessage("The repair would cost " + ChatColor.AQUA + getEconomy().format(price) + ChatColor.WHITE + ".");
                        player.sendMessage("You have currently " + ChatColor.AQUA + getEconomy().format(getEconomy().getBalance(player.getName())));

                        RepairRequest request = new RepairRequest(this, player, items, price);

                        return request;
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
            player.sendMessage(ChatColor.RED + "You don't have the permission to repair a single item!");
        }
        return null;
    }
    
    //alte Funktion...

    public RepairRequest requestRepair(Player player)
    {
        if (hasRepairPermission(player, "singleRepair"))
        {
            ItemStack itemInHand = player.getItemInHand();
            Material itemType = itemInHand.getType();
            if (itemType != Material.AIR) // -> has a item in hand?
            {
                int currentDurability = itemInHand.getDurability();
                if (isRepairable(itemInHand)) // -> item is repairable?
                {
                    if (currentDurability > 0) // -> item is damaged?
                    {
                        double price = itemInHand.getDurability();
                        price *= this.config.price_perDamage;
                        price *= itemInHand.getAmount();
                        price *= getEnchantmentMultiplier(itemInHand, this.config.price_enchantMultiplier_factor, this.config.price_enchantMultiplier_base);

                        ArrayList<ItemStack> items = new ArrayList<ItemStack>(1);
                        items.add(itemInHand);

                        player.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_RED + "ItemRepair" + ChatColor.GREEN + "]");
                        player.sendMessage(ChatColor.AQUA + "Rightclick" + ChatColor.WHITE + " again to repair your item.");
                        player.sendMessage("The repair would cost " + ChatColor.AQUA + getEconomy().format(price) + ChatColor.WHITE + ".");
                        player.sendMessage("You have currently " + ChatColor.AQUA + getEconomy().format(getEconomy().getBalance(player.getName())));

                        RepairRequest request = new RepairRequest(this, player, items, price);

                        return request;
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
            player.sendMessage(ChatColor.RED + "You don't have the permission to repair a single item!");
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
                        if (getEconomy().withdrawPlayer(player.getName(), price).transactionSuccess())
                        {
                            player.getItemInHand().setDurability((short) 0);

                            player.sendMessage(ChatColor.GREEN + "Your item has been repaired for " + ChatColor.AQUA + ChatColor.AQUA + getEconomy().format(price));
                        }
                        else
                        {
                            player.sendMessage(ChatColor.RED + "Something went wrong, report this failure to your administrator!");
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

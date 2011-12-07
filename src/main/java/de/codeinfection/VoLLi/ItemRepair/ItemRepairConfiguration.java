package de.codeinfection.VoLLi.ItemRepair;

import java.lang.reflect.Field;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;

/**
 *
 * @author VoLLi
 */
public class ItemRepairConfiguration
{
    public final double price_perDamage;
    public final double price_enchantMultiplier_base;
    public final double price_enchantMultiplier_factor;

    public final Material repairBlocks_singleRepair_block;
    public final Material repairBlocks_completeRepair_block;
    
    public final Material repairBlocks_cheapRepair_block;
    public final int repairBlocks_cheapRepair_breakPercentage;
    public final int repairBlocks_cheapRepair_costPercentage;
    
    public ItemRepairConfiguration(Configuration config)
    {
        this.price_perDamage                            = config.getDouble("price.perDamage");
        this.price_enchantMultiplier_base               = config.getDouble("price.enchantMultiplier.base");
        this.price_enchantMultiplier_factor             = config.getDouble("price.enchantMultiplier.factor");

        this.repairBlocks_singleRepair_block            = Material.getMaterial(config.getInt("repairBlocks.singleRepair.block"));
        this.repairBlocks_completeRepair_block          = Material.getMaterial(config.getInt("repairBlocks.completeRepair.block"));


        this.repairBlocks_cheapRepair_block             = Material.getMaterial(config.getInt("repairBlocks.cheapRepair.block"));
        this.repairBlocks_cheapRepair_breakPercentage   = config.getInt("repairBlocks.cheapRepair.breakPercentage");
        this.repairBlocks_cheapRepair_costPercentage    = config.getInt("repairBlocks.cheapRepair.costPercentage");
    }

    public void dump()
    {
        if (ItemRepair.debugMode)
        {
            ItemRepair.debug(" ");
            ItemRepair.debug("Configuration dump:");
            for (Field field : this.getClass().getFields())
            {
                try
                {
                    ItemRepair.debug("Field: " + field.getName());
                    ItemRepair.debug("Value: " + String.valueOf(field.get(this)));
                }
                catch (Exception e)
                {
                    ItemRepair.debug("Failed to read the value: " + e.getClass().getSimpleName());
                }
            }
        }
    }
}

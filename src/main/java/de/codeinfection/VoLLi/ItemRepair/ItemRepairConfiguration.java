package de.codeinfection.VoLLi.ItemRepair;

import org.bukkit.configuration.Configuration;

/**
 *
 * @author VoLLi
 */
public class ItemRepairConfiguration
{
    public final double price_perDamage;
    public final double price_multiplicator_base;
    public final double price_multiplicator_factor;

    public final int repairBlocks_singleRepair_block;
    public final int repairBlocks_completeRepair_block;
    
    public final int repairBlocks_cheapRepair_block;
    public final int repairBlocks_cheapRepair_breakPercentage;
    public final int repairBlocks_cheapRepair_costPercentage;
    
    public ItemRepairConfiguration(Configuration config)
    {
        this.price_perDamage                            = config.getDouble("price.perDamage");
        this.price_multiplicator_base                   = config.getDouble("price.multiplicator.base");
        this.price_multiplicator_factor                 = config.getDouble("price.multiplicator.factor");

        this.repairBlocks_singleRepair_block            = config.getInt("repairBlocks_singleRepair_block");
        this.repairBlocks_completeRepair_block          = config.getInt("repairBlocks_completeRepair_block");


        this.repairBlocks_cheapRepair_block             = config.getInt("repairBlocks_cheapRepair_block");
        this.repairBlocks_cheapRepair_breakPercentage   = config.getInt("repairBlocks_cheapRepair_breakPercentage");
        this.repairBlocks_cheapRepair_costPercentage    = config.getInt("repairBlocks_cheapRepair_costPercentage");
    }
}

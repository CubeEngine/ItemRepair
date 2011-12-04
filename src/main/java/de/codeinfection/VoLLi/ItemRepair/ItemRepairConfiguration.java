package de.codeinfection.VoLLi.ItemRepair;

import org.bukkit.configuration.Configuration;

/**
 *
 * @author VoLLi
 */
public class ItemRepairConfiguration
{
    public final double pricePerDamage;
    public final int singleRepairBlock;
    public final int allRepairBlock;
    
    public final int cheapRepairBlock;
    public final int cheapRepairBreakPercentage;
    public final int cheapRepairCostPercentage;
    
    public ItemRepairConfiguration(Configuration config)
    {
        this.pricePerDamage                 = config.getDouble("pricePerDamage");
        this.singleRepairBlock              = config.getInt("singleRepairBlock");
        this.allRepairBlock                 = config.getInt("allRepairBlock");
        
        this.cheapRepairBlock               = config.getInt("cheapRepair.block");
        this.cheapRepairBreakPercentage     = config.getInt("cheapRepair.breakPercentage");
        this.cheapRepairCostPercentage      = config.getInt("cheapRepair.costPercentage");
    }
}

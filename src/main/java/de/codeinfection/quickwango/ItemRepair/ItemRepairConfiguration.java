package de.codeinfection.quickwango.ItemRepair;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;

/**
 * Hols the configuration
 *
 * @author Phillip Schichtel
 */
public class ItemRepairConfiguration
{
    public final double price_enchantMultiplier_base;
    public final double price_enchantMultiplier_factor;
    
    public final Map<BaseMaterial, Double> materialPrices;

    public final Material repairBlocks_normal_block;
    
    public final Material repairBlocks_cheap_block;
    public final int repairBlocks_cheap_breakPercentage;
    public final int repairBlocks_cheap_costPercentage;
    
    public ItemRepairConfiguration(Configuration config)
    {
        for (BaseMaterial baseMaterial : BaseMaterial.values())
        {
            config.addDefault("price.materials." + baseMaterial.getName(), baseMaterial.getPrice());
        }

        EnumMap<BaseMaterial, Double> tempMap = new EnumMap<BaseMaterial, Double>(BaseMaterial.class);
        for (BaseMaterial baseMaterial : BaseMaterial.values())
        {
            tempMap.put(baseMaterial, config.getDouble("price.materials." + baseMaterial.getName()));
        }
        this.materialPrices = Collections.unmodifiableMap(tempMap);

        this.price_enchantMultiplier_base = config.getDouble("price.enchantMultiplier.base");
        this.price_enchantMultiplier_factor = config.getDouble("price.enchantMultiplier.factor");

        this.repairBlocks_normal_block = Material.getMaterial(config.getInt("repairBlocks.normalRepair.block"));

        this.repairBlocks_cheap_block = Material.getMaterial(config.getInt("repairBlocks.cheap.block"));
        this.repairBlocks_cheap_breakPercentage = config.getInt("repairBlocks.cheap.breakPercentage");
        this.repairBlocks_cheap_costPercentage = config.getInt("repairBlocks.cheap.costPercentage");
    }
}

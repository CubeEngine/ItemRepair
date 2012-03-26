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
    public final double price_perDamage;
    public final double price_enchantMultiplier_base;
    public final double price_enchantMultiplier_factor;
    
    public final Map<BaseMaterial, Double> materialPrices;

    public final Material repairBlocks_singleRepair_block;
    public final Material repairBlocks_completeRepair_block;
    
    public final Material repairBlocks_cheapRepair_block;
    public final int repairBlocks_cheapRepair_breakPercentage;
    public final int repairBlocks_cheapRepair_costPercentage;
    
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

        this.price_perDamage = config.getDouble("price.perDamage");
        this.price_enchantMultiplier_base = config.getDouble("price.enchantMultiplier.base");
        this.price_enchantMultiplier_factor = config.getDouble("price.enchantMultiplier.factor");

        this.repairBlocks_singleRepair_block = Material.getMaterial(config.getInt("repairBlocks.singleRepair.block"));
        this.repairBlocks_completeRepair_block = Material.getMaterial(config.getInt("repairBlocks.completeRepair.block"));

        this.repairBlocks_cheapRepair_block = Material.getMaterial(config.getInt("repairBlocks.cheapRepair.block"));
        this.repairBlocks_cheapRepair_breakPercentage = config.getInt("repairBlocks.cheapRepair.breakPercentage");
        this.repairBlocks_cheapRepair_costPercentage = config.getInt("repairBlocks.cheapRepair.costPercentage");
    }
}

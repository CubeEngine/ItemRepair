package de.cubeisland.ItemRepair;

import java.util.Map;

/**
 *
 * @author CodeInfection
 */
public class ItemrepairMaterialPriceProvider implements MaterialPriceProvider
{
    private final Map<BaseMaterial, Double> prices;

    public ItemrepairMaterialPriceProvider(ItemRepairConfiguration config)
    {
        this.prices = config.materialPrices;
    }

    public double getPrice(BaseMaterial material)
    {
        if (material != null)
        {
            Double price = this.prices.get(material);
            if (price != null)
            {
                return price.doubleValue();
            }
        }
        return 0;
    }
}

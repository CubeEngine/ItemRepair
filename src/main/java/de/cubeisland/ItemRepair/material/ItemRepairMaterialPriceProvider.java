package de.cubeisland.ItemRepair.material;

import de.cubeisland.ItemRepair.ItemRepairConfiguration;
import java.util.Map;

/**
 *
 * @author CodeInfection
 */
public class ItemRepairMaterialPriceProvider implements MaterialPriceProvider
{
    private final Map<BaseMaterial, Double> prices;

    public ItemRepairMaterialPriceProvider(ItemRepairConfiguration config)
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

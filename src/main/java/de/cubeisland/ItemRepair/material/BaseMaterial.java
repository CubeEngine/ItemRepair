package de.cubeisland.ItemRepair.material;

/**
 * Base materials for items
 *
 * @author Phillip Schichtel
 */
public enum BaseMaterial
{
    WOOD(0.1),
    STONE(0.32),
    IRON(1.4),
    GOLD(1.6),
    DIAMOND(82.0),
    LEATHER(0.2),
    FIRE(50.0);
    
    private final double price;

    private BaseMaterial(final double price)
    {
        this.price = price;
    }

    public String getName()
    {
        return this.name().toLowerCase();
    }

    public double getPrice()
    {
        return this.price;
    }
}

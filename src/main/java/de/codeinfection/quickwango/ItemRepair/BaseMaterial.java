package de.codeinfection.quickwango.ItemRepair;

/**
 * Base materials for items
 *
 * @author Phillip Schichtel
 */
public enum BaseMaterial
{
    WOOD,
    STONE,
    IRON,
    GOLD,
    DIAMOND,
    LEATHER,
    FIRE;

    public String getName()
    {
        return this.name().toLowerCase();
    }
}

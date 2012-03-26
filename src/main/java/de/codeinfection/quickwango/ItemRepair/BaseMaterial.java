package de.codeinfection.quickwango.ItemRepair;

/**
 *
 * @author CodeInfection
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

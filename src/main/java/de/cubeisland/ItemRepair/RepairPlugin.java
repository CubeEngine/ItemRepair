package de.cubeisland.ItemRepair;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author CodeInfection
 */
public interface RepairPlugin extends Plugin
{
    /**
     * Returns the economy API
     *
     * @return the economy API
     */
    public Economy getEconomy();

    /**
     * Returns the material price provider
     *
     * @return the material price provider
     */
    public MaterialPriceProvider getMaterialPriceProvider();

    /**
     * Returns the name of the server player
     *
     * @return the name of the server player
     */
    public String getServerPlayer();

    /**
     * Returns the name of the server bank
     *
     * @return the name of the server bank
     */
    public String getServerBank();
}

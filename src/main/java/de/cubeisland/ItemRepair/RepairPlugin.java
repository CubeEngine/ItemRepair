package de.cubeisland.ItemRepair;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author CodeInfection
 */
public interface RepairPlugin extends Plugin
{
    public Economy getEconomy();
    public MaterialPriceProvider getMaterialPriceProvider();
}

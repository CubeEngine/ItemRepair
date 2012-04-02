package de.codeinfection.quickwango.ItemRepair;

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

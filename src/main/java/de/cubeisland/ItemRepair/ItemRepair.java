package de.cubeisland.ItemRepair;

import de.cubeisland.ItemRepair.RepairBlocks.CheapRepair;
import de.cubeisland.ItemRepair.RepairBlocks.NormalRepair;
import de.cubeisland.Translation.Translator;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemRepair extends JavaPlugin implements RepairPlugin
{
    private static ItemRepair instance = null;
    private static Logger logger = null;
    public static boolean debugMode = false;
    
    private Server server;
    private PluginManager pm;
    private ItemRepairConfiguration config;
    private File dataFolder;
    private Economy economy = null;
    private MaterialPriceProvider priceProvider;

    public ItemRepair()
    {
        instance = this;
    }

    public static ItemRepair getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        logger = this.getLogger();
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        
        this.dataFolder = this.getDataFolder();

        this.dataFolder.mkdirs();

        this.reloadConfig();
        Configuration configuration = this.getConfig();
        configuration.options().copyDefaults(true);
        this.config = new ItemRepairConfiguration(configuration);
        debugMode = configuration.getBoolean("debug");
        if (!Translator.loadTranslation(config.language))
        {
            Translator.loadTranslation("en");
        }
        this.saveConfig();

        this.economy = this.setupEconomy();
        this.priceProvider = new ItemrepairMaterialPriceProvider(this.config);

        RepairBlockManager rbm = RepairBlockManager.initialize(this);
                rbm.setPersister(new RepairBlockPersister(new File(dataFolder, "blocks.yml")))
                .addRepairBlock(new NormalRepair(
                        this.config.repairBlocks_normal_block
                ))
                .addRepairBlock(new CheapRepair(
                        this.config.repairBlocks_cheap_block,
                        this.config
                ))
                .loadBlocks();

        this.pm.registerEvents(new ItemRepairListener(), this);

        this.getCommand("itemrepair").setExecutor(new ItemrepairCommand(this));
    }

    @Override
    public void onDisable()
    {
        RepairBlockManager.getInstance().clearBlocks();
    }

    private Economy setupEconomy()
    {
        if (this.pm.getPlugin("Vault") != null)
        {
            RegisteredServiceProvider<Economy> rsp = this.server.getServicesManager().getRegistration(Economy.class);
            if (rsp != null)
            {
                Economy eco = rsp.getProvider();
                if (eco != null)
                {
                    return eco;
                }
            }
        }
        throw new IllegalStateException("Failed to initialize with Vault!");
    }

    /**
     * Returns the economy API
     *
     * @return the economy API
     */
    public Economy getEconomy()
    {
        return this.economy;
    }

    public MaterialPriceProvider getMaterialPriceProvider()
    {
        return this.priceProvider;
    }

    public ItemRepairConfiguration getConfiguration()
    {
        return this.config;
    }

    public static void log(String msg)
    {
        logger.log(Level.INFO, msg);
    }

    public static void error(String msg)
    {
        logger.log(Level.SEVERE, msg);
    }

    public static void error(String msg, Throwable t)
    {
        logger.log(Level.SEVERE, msg, t);
    }

    public static void debug(String msg)
    {
        if (debugMode)
        {
            log("[debug] " + msg);
        }
    }
}

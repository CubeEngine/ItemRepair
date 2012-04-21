package de.cubeisland.ItemRepair;

import de.cubeisland.ItemRepair.RepairBlocks.CheapRepair;
import de.cubeisland.ItemRepair.RepairBlocks.NormalRepair;
import de.cubeisland.libMinecraft.Translation;
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
    private static Translation translation = null;
    
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
        configuration.addDefault("language", System.getProperty("user.language", "en"));
        configuration.options().copyDefaults(true);
        this.config = new ItemRepairConfiguration(configuration);
        debugMode = configuration.getBoolean("debug");
        this.saveConfig();

        translation = Translation.get(this.getClass(), config.language);
        if (translation == null)
        {
            translation = Translation.get(this.getClass(), "en");
        }

        this.economy = this.setupEconomy();
        this.priceProvider = new ItemrepairMaterialPriceProvider(this.config);

        RepairBlockManager.initialize(this)
                .setPersister(new RepairBlockPersister(new File(dataFolder, "blocks.yml")))
                .addRepairBlock(new NormalRepair(this))
                .addRepairBlock(new CheapRepair(this))
                .loadBlocks();

        this.pm.registerEvents(new ItemRepairListener(), this);

        this.getCommand("itemrepair").setExecutor(new ItemrepairCommand(this));
    }

    @Override
    public void onDisable()
    {
        translation = null;
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
    public Economy getEconomy()
    {
        return this.economy;
    }

    public MaterialPriceProvider getMaterialPriceProvider()
    {
        return this.priceProvider;
    }

    public String getServerPlayer()
    {
        return this.config.server_player;
    }

    public String getServerBank()
    {
        return this.config.server_bank;
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

    public static String _(String key, Object... params)
    {
        return translation.translate(key, params);
    }
}

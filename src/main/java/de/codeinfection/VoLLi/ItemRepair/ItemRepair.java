package de.codeinfection.VoLLi.ItemRepair;

import com.iCo6.iConomy;
import de.codeinfection.VoLLi.ItemRepair.RepairBlocks.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;

public class ItemRepair extends JavaPlugin
{
    protected static final Logger log = Logger.getLogger("Minecraft");
    public static boolean debugMode = false;

    public final static List<Player> addBlockChoiceRequests = new ArrayList<Player>();
    public final static List<Player> removeBlockChoiceRequests = new ArrayList<Player>();
    
    protected Server server;
    protected PluginManager pm;
    protected ItemRepairConfiguration config;
    protected File dataFolder;
    

    public ItemRepair()
    {}

    public void onEnable()
    {
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        
        this.dataFolder = this.getDataFolder();

        this.dataFolder.mkdirs();

        iConomy iconomy = null;
        Plugin iconomyPlugin = this.pm.getPlugin("iConomy");
        if (iconomyPlugin != null)
        {
            if (iconomyPlugin instanceof iConomy)
            {
                String version = iconomyPlugin.getDescription().getVersion();
                if (version != null && version.length() > 0)
                {
                    String firstChar = version.substring(0, 1);
                    try
                    {
                        int majorVersion = Integer.valueOf(firstChar);
                        if (majorVersion >= 6)
                        {
                            iconomy = (iConomy)iconomy;
                        }
                    }
                    catch (NumberFormatException e)
                    {}
                }
            }
        }
        if (iconomy != null)
        {
            error("Could not hook into iConomy 6");
            error("Staying in a zombie state...");
            return;
        }
        
        Configuration configuration = this.getConfig();
        configuration.options().copyDefaults(true);
        this.config = new ItemRepairConfiguration(configuration);
        debugMode = configuration.getBoolean("debug");
        this.saveConfig();

        RepairBlockManager rbm = RepairBlockManager.getInstance();
                rbm.setPersister(new RepairBlockPersister(new File(dataFolder, "blocks.yml")))
                .addRepairBlock(new SingleRepair(
                        this.config.repairBlocks_singleRepair_block,
                        this.config
                ))
                .addRepairBlock(new CompleteRepair(
                        this.config.repairBlocks_completeRepair_block,
                        this.config
                ))
                .addRepairBlock(new CheapRepair(
                        this.config.repairBlocks_cheapRepair_block,
                        this.config
                ))
                .loadBlocks();

        this.pm.registerEvent(Type.PLAYER_INTERACT, new ItemRepairPlayerListener(), Priority.Low, this);

        this.getCommand("itemrepair").setExecutor(new ItemrepairCommand());

        log("Version " + this.getDescription().getVersion() + " enabled");
    }

    public void onDisable()
    {
        log("Version " + this.getDescription().getVersion() + " disabled");
    }

    public static void log(String msg)
    {
        log.log(Level.INFO, "[ItemRepair] " + msg);
    }

    public static void error(String msg)
    {
        log.log(Level.SEVERE, "[ItemRepair] " + msg);
    }

    public static void error(String msg, Throwable t)
    {
        log.log(Level.SEVERE, "[ItemRepair] " + msg, t);
    }

    public static void debug(String msg)
    {
        if (debugMode)
        {
            log("[debug] " + msg);
        }
    }
}

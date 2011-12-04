package de.codeinfection.VoLLi.ItemRepair;

import com.iCo6.iConomy;
import de.codeinfection.VoLLi.ItemRepair.RepairBlocks.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

public class ItemRepair extends JavaPlugin
{
    protected static final Logger log = Logger.getLogger("Minecraft");
    public static boolean debugMode = true;
    
    protected Server server;
    protected PluginManager pm;
    protected ItemRepairConfiguration config;
    protected File dataFolder;
    

    public ItemRepair()
    {
    }

    public void onEnable()
    {
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        
        this.dataFolder = this.getDataFolder();

        this.dataFolder.mkdirs();
        
        iConomy iconomy = (iConomy)this.pm.getPlugin("iConomy");
        if (iconomy == null)
        {
            error("Could not hook into iConomy 6");
            error("Staying in a zombie state...");
            error("Install iConomy 6... Sucker!");            
            return;
        }
        
        Configuration configuration = this.getConfig();
        configuration.options().copyDefaults(true);
        this.config = new ItemRepairConfiguration(configuration);
        this.saveConfig();
        
        RepairBlockManager.getInstance()
                .addBlock(new GenericRepairBlock(this.config.singleRepairBlock, this.config.pricePerDamage))
                .addBlock(new GenericMultiRepairBlock(this.config.allRepairBlock, this.config.pricePerDamage))
                .addBlock(new CheapRepair(this.config.cheapRepairBlock, this.config.pricePerDamage, this.config.cheapRepairBreakPercentage, this.config.cheapRepairCostPercentage));
        
        this.pm.registerEvent(Type.PLAYER_INTERACT, new ItemRepairPlayerListener(config, iconomy), Priority.Low, this);

        System.out.println(this.getDescription().getName() + " (v" + this.getDescription().getVersion() + ") enabled");
    }

    public void onDisable()
    {
        System.out.println(this.getDescription().getName() + " Disabled");
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

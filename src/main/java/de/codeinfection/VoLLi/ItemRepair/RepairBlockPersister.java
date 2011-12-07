package de.codeinfection.VoLLi.ItemRepair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author CodeInfection
 */
public class RepairBlockPersister
{
    private final File configFile;
    private final FileConfiguration config;

    public RepairBlockPersister(File configFile)
    {
        this.configFile = configFile;
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public List<Block> load()
    {
        List<Block> blocks = new ArrayList<Block>();
        List<Object> tmp;
        World world;
        Block block;
        for (Object entry : this.config.getList("locations", new ArrayList<List<Object>>()))
        {
            if (entry instanceof List)
            {
                tmp = (List<Object>)entry;
                if (tmp.size() > 3)
                {
                    world = Bukkit.getWorld(String.valueOf(tmp.get(0)));
                    if (world != null)
                    {
                        try
                        {
                            block = world.getBlockAt(new Location(
                                world,
                                Integer.valueOf(String.valueOf(tmp.get(1))),
                                Integer.valueOf(String.valueOf(tmp.get(2))),
                                Integer.valueOf(String.valueOf(tmp.get(3)))
                            ));
                            if (block != null)
                            {
                                blocks.add(block);
                            }
                        }
                        catch (NumberFormatException e)
                        {}
                    }
                }
            }
        }

        return blocks;
    }

    public RepairBlockPersister persist(Collection<Block> blocks)
    {
        List<List<Object>> entries = new ArrayList<List<Object>>(blocks.size());
        List<Object> tmp = null;
        for (Block block : blocks)
        {
            tmp = new ArrayList<Object>(4);
            tmp.add(block.getWorld().getName());
            tmp.add(block.getX());
            tmp.add(block.getY());
            tmp.add(block.getZ());

            entries.add(tmp);
        }
        this.config.set("locations", entries);
        try
        {
            this.config.save(this.configFile);
        }
        catch (IOException e)
        {}
        return this;
    }
}

package de.cubeisland.ItemRepair;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Manages the repair blocks
 *
 * @author Phillip Schichtel
 */
public class RepairBlockManager
{

    private static RepairBlockManager instance = null;
    private Map<Material, RepairBlock> repairBlocks;
    private Map<Block, Material> blockMap;
    private RepairBlockPersister persister;
    private final PluginManager pm;
    private final Permission parentPermission;

    private RepairBlockManager()
    {
        this.repairBlocks = new EnumMap<Material, RepairBlock>(Material.class);
        this.blockMap = new HashMap<Block, Material>();
        this.persister = null;
        this.pm = Bukkit.getPluginManager();
        this.parentPermission = new Permission("itemrepair.allblocks", PermissionDefault.OP);
        try
        {
            this.pm.addPermission(this.parentPermission);
        }
        catch (IllegalArgumentException e)
        {}
    }
    public static RepairBlockManager getInstance()
    {
        if (instance == null)
        {
            instance = new RepairBlockManager();
        }
        return instance;
    }

    public RepairBlockManager setPersister(RepairBlockPersister persister)
    {
        this.persister = persister;
        return this;
    }

    /**
     * Loads the blocks from a persister
     *
     * @return fluent interface
     */
    public RepairBlockManager loadBlocks()
    {
        if (this.persister != null)
        {
            for (Block block : this.persister.load())
            {
                this.attachRepairBlock(block);
            }
        }
        return this;
    }

    /**
     * Adds a repair block
     *
     * @param block the repair block
     * @return fluent interface
     */
    public RepairBlockManager addRepairBlock(RepairBlock block)
    {
        try
        {
            this.pm.addPermission(block.getPermission());
        }
        catch (IllegalArgumentException e)
        {}
        block.getPermission().addParent(this.parentPermission, true);
        this.repairBlocks.put(block.getMaterial(), block);
        ItemRepair.debug("Added a repair block: " + block.getClass().getSimpleName() + " on ID: " + block.getMaterial());
        return this;
    }

    /**
     * Returns a repair block by its material's ID
     *
     * @param materialId the material ID
     * @return the repair block
     */
    public RepairBlock getRepairBlock(int materialId)
    {
        return this.getRepairBlock(Material.getMaterial(materialId));
    }

    /**
     * Returns a repair block by its material's name
     *
     * @param materialName the name of the material
     * @return the repair block
     */
    public RepairBlock getRepairBlock(String materialName)
    {
        return this.getRepairBlock(Material.getMaterial(materialName));
    }

    /**
     * Returns a repair block by it's materials
     *
     * @param material the material
     * @return the repair block
     */
    public RepairBlock getRepairBlock(Material material)
    {
        return this.repairBlocks.get(material);
    }

    /**
     * Returns the attached repair block of a block
     * 
     * @param block the block
     * @return the attached repair block
     */
    public RepairBlock getRepairBlock(Block block)
    {
        Material repairBlockMaterial = this.blockMap.get(block);
        if (repairBlockMaterial != null)
        {
            return this.getRepairBlock(repairBlockMaterial);
        }
        return null;
    }

    /**
     * Checks whether the given block is a repair block
     *
     * @param block the block to check
     * @return true if it is one
     */
    public boolean isRepairBlock(Block block)
    {
        return this.blockMap.containsKey(block);
    }

    /**
     * Attaches a repair block to a block
     *
     * @param block the block to attach to
     * @return true on success
     */
    public boolean attachRepairBlock(Block block)
    {
        Material material = block.getType();
        if (!this.isRepairBlock(block))
        {
            if (this.repairBlocks.containsKey(material))
            {
                this.blockMap.put(block, material);
                if (this.persister != null)
                {
                    this.persister.persist(this.blockMap.keySet());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Detaches a repair block from a block
     *
     * @param block the block to detach from
     * @return true on success
     */
    public boolean detachRepairBlock(Block block)
    {
        if (this.isRepairBlock(block))
        {
            this.blockMap.remove(block);
            if (this.persister != null)
            {
                this.persister.persist(this.blockMap.keySet());
            }
            return true;
        }
        return false;
    }

    public RepairBlockManager clearBlocks()
    {
        this.repairBlocks.clear();
        return this;
    }
}

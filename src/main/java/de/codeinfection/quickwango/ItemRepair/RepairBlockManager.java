package de.codeinfection.quickwango.ItemRepair;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author VoLLi
 */
public class RepairBlockManager
{

    private static RepairBlockManager instance = null;
    private Map<Material, RepairBlock> repairBlocks;
    private Map<Block, Material> blockMap;
    private RepairBlockPersister persister;
    private final PluginManager pm;
    private final Permission parentPermission;

    private RepairBlockManager(Plugin plugin)
    {
        this.repairBlocks = new EnumMap<Material, RepairBlock>(Material.class);
        this.blockMap = new HashMap<Block, Material>();
        this.persister = null;
        this.pm = plugin.getServer().getPluginManager();
        this.parentPermission = new Permission("itemrepair.allblocks", PermissionDefault.OP);
        this.pm.addPermission(this.parentPermission);
    }

    public static RepairBlockManager initialize(Plugin plugin)
    {
        if (instance == null)
        {
            instance = new RepairBlockManager(plugin);
        }
        return instance;
    }

    public static RepairBlockManager getInstance()
    {
        return instance;
    }

    public RepairBlockManager setPersister(RepairBlockPersister persister)
    {
        this.persister = persister;
        return this;
    }

    public void loadBlocks()
    {
        if (this.persister != null)
        {
            for (Block block : this.persister.load())
            {
                this.attachRepairBlock(block);
            }
        }
    }

    public RepairBlockManager addRepairBlock(RepairBlock block)
    {
        this.pm.addPermission(block.permission);
        block.permission.addParent(this.parentPermission, true);
        this.repairBlocks.put(block.material, block);
        ItemRepair.debug("Added a repair block: " + block.getClass().getSimpleName() + " on ID: " + block.material);
        return this;
    }

    public RepairBlock getRepairBlock(int materialId)
    {
        return this.getRepairBlock(Material.getMaterial(materialId));
    }

    public RepairBlock getRepairBlock(String materialName)
    {
        return this.getRepairBlock(Material.getMaterial(materialName));
    }

    public RepairBlock getRepairBlock(Material material)
    {
        return this.repairBlocks.get(material);
    }

    public RepairBlock getRepairBlock(Block block)
    {
        Material repairBlockMaterial = this.blockMap.get(block);
        if (repairBlockMaterial != null)
        {
            return this.getRepairBlock(repairBlockMaterial);
        }
        return null;
    }

    public boolean isRepairBlock(Block block)
    {
        return this.blockMap.containsKey(block);
    }

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
}

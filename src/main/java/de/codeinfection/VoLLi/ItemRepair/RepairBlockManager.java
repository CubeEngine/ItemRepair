package de.codeinfection.VoLLi.ItemRepair;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.block.Block;

/**
 *
 * @author VoLLi
 */
public class RepairBlockManager
{
    private static RepairBlockManager instance = null;
    
    private Map<Integer, RepairBlock> repairBlocks;
    private Map<Block, Integer> blockMap;
    
    private RepairBlockManager()
    {
        this.repairBlocks = new HashMap<Integer, RepairBlock>();
        this.blockMap = new HashMap<Block, Integer>();
    }
    
    public static RepairBlockManager getInstance()
    {
        if (instance == null)
        {
            instance = new RepairBlockManager();
        }
        return instance;
    }
    
    public RepairBlockManager addRepairBlock(RepairBlock block)
    {
        this.repairBlocks.put(block.blockId, block);
        return this;
    }
    
    public RepairBlock getRepairBlock(int blockId)
    {
        return this.repairBlocks.get(blockId);
    }

    public RepairBlock getRepairBlock(Block block)
    {
        Integer repairBlockId = this.blockMap.get(block);
        if (repairBlockId != null)
        {
            return this.getRepairBlock(repairBlockId);
        }
        return null;
    }

    public boolean attachRepairBlock(Block block)
    {
        int blockId = block.getTypeId();
        if (this.repairBlocks.containsKey(blockId))
        {
            this.blockMap.put(block, blockId);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean detachRepairBlock(Block block)
    {
        if (this.blockMap.containsKey(block))
        {
            this.blockMap.remove(block);
            return true;
        }
        else
        {
            return false;
        }
    }
}

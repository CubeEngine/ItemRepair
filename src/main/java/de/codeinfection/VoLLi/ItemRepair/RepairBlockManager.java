package de.codeinfection.VoLLi.ItemRepair;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author VoLLi
 */
public class RepairBlockManager
{
    private static RepairBlockManager instance = null;
    
    private Map<Integer, RepairBlock> repairBlocks;
    
    private RepairBlockManager()
    {
        this.repairBlocks = new HashMap<Integer, RepairBlock>();
    }
    
    public static RepairBlockManager getInstance()
    {
        if (instance == null)
        {
            instance = new RepairBlockManager();
        }
        return instance;
    }
    
    public RepairBlockManager addBlock(RepairBlock block)
    {
        this.repairBlocks.put(block.blockId, block);
        return this;
    }
    
    public RepairBlock getBlock(int blockId)
    {
        return this.repairBlocks.get(blockId);
    }
}

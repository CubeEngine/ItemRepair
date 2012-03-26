package de.codeinfection.quickwango.ItemRepair;

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;

/**
 *
 * @author CodeInfection
 */
public enum Item
{
    WOOD_AXE(Material.WOOD_AXE, BaseMaterial.WOOD, 3),
    WOOD_PICKAXE(Material.WOOD_PICKAXE, BaseMaterial.WOOD, 3),
    WOOD_SPADE(Material.WOOD_SPADE, BaseMaterial.WOOD, 2),
    WOOD_SWORD(Material.WOOD_SWORD, BaseMaterial.WOOD, 2),
    WOOD_HOE(Material.WOOD_HOE, BaseMaterial.WOOD, 2),
    STONE_AXE(Material.STONE_AXE, BaseMaterial.STONE, 3),
    STONE_PICKAXE(Material.STONE_PICKAXE, BaseMaterial.STONE, 3),
    STONE_SPADE(Material.STONE_SPADE, BaseMaterial.STONE, 2),
    STONE_SWORD(Material.STONE_SWORD, BaseMaterial.STONE, 2),
    STONE_HOE(Material.STONE_HOE, BaseMaterial.STONE, 2),
    GOLD_AXE(Material.GOLD_AXE, BaseMaterial.GOLD, 3),
    GOLD_PICKAXE(Material.GOLD_PICKAXE, BaseMaterial.GOLD, 3),
    GOLD_SPADE(Material.GOLD_SPADE, BaseMaterial.GOLD, 2),
    GOLD_SWORD(Material.GOLD_SWORD, BaseMaterial.GOLD, 2),
    GOLD_HOE(Material.GOLD_HOE, BaseMaterial.GOLD, 2);

    private static final Map<Material, Item> BY_MATERIAL = new EnumMap<Material, Item>(Material.class);

    private final Material material;
    private final BaseMaterial baseMaterial;
    private final int baseMaterialCount;

    private Item(final Material material, final BaseMaterial baseMaterial, final int baseMaterialCount)
    {
        this.material = material;
        this.baseMaterial = baseMaterial;
        this.baseMaterialCount = baseMaterialCount;
    }

    public Material getMaterial()
    {
        return this.material;
    }

    public BaseMaterial getBaseMaterial()
    {
        return this.baseMaterial;
    }

    public int getbaseMaterialCount()
    {
        return this.baseMaterialCount;
    }

    public static Item getByMaterial(Material material)
    {
        return BY_MATERIAL.get(material);
    }

    static
    {
        for (Item item : values())
        {
            BY_MATERIAL.put(item.getMaterial(), item);
        }
    }
}

package com.franchino961.constructionwands.models;

import org.bukkit.Material;
import java.util.List;

public class Wand {

    private final String id;
    private final String name;
    private final int modelData;
    private final List<String> lore;
    private final int range;
    private final Material type;
    private final int maxUses;
    private final boolean infinite;

    public Wand(String id, String name, int modelData, List<String> lore,
                int range, Material type, int maxUses, boolean infinite) {
        this.id = id;
        this.name = name;
        this.modelData = modelData;
        this.lore = lore;
        this.range = range;
        this.type = type;
        this.maxUses = maxUses;
        this.infinite = infinite;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getModelData() { return modelData; }
    public List<String> getLore() { return lore; }
    public int getRange() { return range; }
    public Material getType() { return type; }
    public int getMaxUses() { return maxUses; }
    public boolean isInfinite() { return infinite; }
}

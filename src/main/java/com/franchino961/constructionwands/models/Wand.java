package com.franchino961.constructionwands.models;

import org.bukkit.Material;
import java.util.List;

public class Wand {

    public enum LeftClickAction {
        NONE,
        UNDO,
        MODE
    }

    public enum PlacementMode {
        AUTO,
        VERTICAL,
        HORIZONTAL;

        public PlacementMode next() {
            return values()[(ordinal() + 1) % values().length];
        }
    }

    private final String id;
    private final String name;
    private final int modelData;
    private final List<String> lore;
    private final int range;
    private final int length;
    private final long delay;
    private final String source;
    private final Material type;
    private final int maxUses;
    private final boolean infinite;
    private final LeftClickAction leftClickAction;

    public Wand(String id, String name, int modelData, List<String> lore,
                int range, int length, long delay, String source, Material type, int maxUses, boolean infinite, LeftClickAction leftClickAction) {
        this.id = id;
        this.name = name;
        this.modelData = modelData;
        this.lore = lore;
        this.range = range;
        this.length = length;
        this.delay = delay;
        this.source = source;
        this.type = type;
        this.maxUses = maxUses;
        this.infinite = infinite;
        this.leftClickAction = leftClickAction;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getModelData() { return modelData; }
    public List<String> getLore() { return lore; }
    public int getRange() { return range; }
    public int getLength() { return length; }
    public long getDelay() { return delay; }
    public String getSource() { return source; }
    public Material getType() { return type; }
    public int getMaxUses() { return maxUses; }
    public boolean isInfinite() { return infinite; }
    public LeftClickAction getLeftClickAction() { return leftClickAction; }
}

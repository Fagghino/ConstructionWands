package com.franchino961.constructionwands.managers;

import com.franchino961.constructionwands.ConstructionWands;
import com.franchino961.constructionwands.models.Wand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class WandManager {

    private final ConstructionWands plugin;
    private final Map<String, Wand> wands;
    private final NamespacedKey wandIdKey;
    private final NamespacedKey wandUsesKey;

    public WandManager(ConstructionWands plugin) {
        this.plugin = plugin;
        this.wands = new HashMap<>();
        this.wandIdKey = new NamespacedKey(plugin, "wand_id");
        this.wandUsesKey = new NamespacedKey(plugin, "wand_uses");
    }

    public void loadWandsFromConfig() {
        wands.clear();
        ConfigurationSection wandsSection = plugin.getConfig().getConfigurationSection("wands");
        if (wandsSection == null) return;

        for (String wandId : wandsSection.getKeys(false)) {
            ConfigurationSection wandSection = wandsSection.getConfigurationSection(wandId);
            if (wandSection == null) continue;

            String name = ChatColor.translateAlternateColorCodes('&',
                    wandSection.getString("name", "&fBacchetta"));
            int modelData = wandSection.getInt("model-data", 0);
            List<String> lore = wandSection.getStringList("lore");
            lore.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
            int range = wandSection.getInt("range", 1);
            Material type = Material.getMaterial(wandSection.getString("type", "STICK"));
            int uses = wandSection.getInt("uses", 100);
            boolean infinite = wandSection.getBoolean("infinite", false);

            if (type == null) continue;

            Wand wand = new Wand(wandId, name, modelData, lore, range, type, uses, infinite);
            wands.put(wandId, wand);
        }
    }

    public Wand getWand(String id) { return wands.get(id); }
    public Map<String, Wand> getAllWands() { return new HashMap<>(wands); }

    public ItemStack createWandItem(String wandId) {
        Wand wand = wands.get(wandId);
        if (wand == null) return null;
        ItemStack item = new ItemStack(wand.getType());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(wand.getName());
            List<String> lore = new ArrayList<>();
            int currentUses = wand.isInfinite() ? -1 : wand.getMaxUses();
            for (String line : wand.getLore()) {
                lore.add(line.replace("{uses}", wand.isInfinite() ? "∞" : String.valueOf(currentUses)));
            }
            meta.setLore(lore);
            if (wand.getModelData() > 0) meta.setCustomModelData(wand.getModelData());
            meta.getPersistentDataContainer().set(wandIdKey, PersistentDataType.STRING, wandId);
            meta.getPersistentDataContainer().set(wandUsesKey, PersistentDataType.INTEGER, currentUses);
            item.setItemMeta(meta);
        }
        return item;
    }

    public boolean isWand(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(wandIdKey, PersistentDataType.STRING);
    }

    public String getWandId(ItemStack item) {
        if (!isWand(item)) return null;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(wandIdKey, PersistentDataType.STRING);
    }

    public int getWandUses(ItemStack item) {
        if (!isWand(item)) return 0;
        ItemMeta meta = item.getItemMeta();
        Integer uses = meta.getPersistentDataContainer().get(wandUsesKey, PersistentDataType.INTEGER);
        return (uses != null) ? uses : 0;
    }

    public boolean decrementUses(ItemStack item) {
        if (!isWand(item)) return false;
        String wandId = getWandId(item);
        Wand wand = getWand(wandId);
        if (wand != null && wand.isInfinite()) return true;

        int currentUses = getWandUses(item);
        if (currentUses <= 0) return false;
        currentUses--;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(wandUsesKey, PersistentDataType.INTEGER, currentUses);

        if (wand != null) {
            List<String> lore = new ArrayList<>();
            for (String line : wand.getLore()) {
                lore.add(line.replace("{uses}", String.valueOf(currentUses)));
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);

        return currentUses > 0;
    }

    public NamespacedKey getWandIdKey() { return wandIdKey; }
    public NamespacedKey getWandUsesKey() { return wandUsesKey; }
}

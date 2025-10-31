package com.franchino961.constructionwands;

import com.franchino961.constructionwands.commands.WandCommand;
import com.franchino961.constructionwands.listeners.WandInteractListener;
import com.franchino961.constructionwands.managers.WandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ConstructionWands extends JavaPlugin {

    private WandManager wandManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        wandManager = new WandManager(this);
        wandManager.loadWandsFromConfig();

        getCommand("wand").setExecutor(new WandCommand(this, wandManager));
        getServer().getPluginManager().registerEvents(new WandInteractListener(this, wandManager), this);

        getLogger().info("Construction Wands plugin abilitato!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Construction Wands plugin disabilitato!");
    }

    public WandManager getWandManager() {
        return wandManager;
    }
}

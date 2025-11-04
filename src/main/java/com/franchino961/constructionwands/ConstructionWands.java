package com.franchino961.constructionwands;

import com.franchino961.constructionwands.commands.WandCommand;
import com.franchino961.constructionwands.hooks.Protections;
import com.franchino961.constructionwands.listeners.WandInteractListener;
import com.franchino961.constructionwands.managers.WandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ConstructionWands extends JavaPlugin {

    private WandManager wandManager;
    private Protections protections;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        wandManager = new WandManager(this);
        wandManager.loadWandsFromConfig();

        protections = new Protections();
        if (protections.isSsb2Present()) {
            getLogger().info("Hook SuperiorSkyblock2 attivato!");
        }

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

    public Protections getProtections() {
        return protections;
    }
}

package com.franchino961.constructionwands;

import com.franchino961.constructionwands.commands.WandCommand;
import com.franchino961.constructionwands.hooks.Protections;
import com.franchino961.constructionwands.listeners.WandInteractListener;
import com.franchino961.constructionwands.managers.WandManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConstructionWands extends JavaPlugin {

    private WandManager wandManager;
    private Protections protections;
    private FileConfiguration wandsConfig;
    private FileConfiguration langConfig;
    private String currentLang;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadWandsConfig();
        loadLangConfig();

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

    private void loadWandsConfig() {
        File wandsFile = new File(getDataFolder(), "wands.yml");
        if (!wandsFile.exists()) {
            try (InputStream in = getResource("wands.yml")) {
                if (in != null) {
                    Files.copy(in, wandsFile.toPath());
                }
            } catch (IOException e) {
                getLogger().severe("Impossibile creare wands.yml: " + e.getMessage());
            }
        }
        wandsConfig = YamlConfiguration.loadConfiguration(wandsFile);
    }

    public FileConfiguration getWandsConfig() {
        return wandsConfig;
    }

    private void loadLangConfig() {
        File langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            try (InputStream in = getResource("lang.yml")) {
                if (in != null) {
                    Files.copy(in, langFile.toPath());
                }
            } catch (IOException e) {
                getLogger().severe("Impossibile creare lang.yml: " + e.getMessage());
            }
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);
        currentLang = getConfig().getString("lang", "it_IT");
        getLogger().info("Lingua impostata: " + currentLang);
    }

    public String getMessage(String key) {
        String path = currentLang + "." + key;
        String message = langConfig.getString(path);
        if (message == null) {
            // Fallback to Italian if message not found
            message = langConfig.getString("it_IT." + key, "&cMessaggio non trovato: " + key);
        }
        return message;
    }
}

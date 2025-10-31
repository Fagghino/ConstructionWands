package com.franchino961.constructionwands.commands;

import com.franchino961.constructionwands.ConstructionWands;
import com.franchino961.constructionwands.managers.WandManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class WandCommand implements CommandExecutor, TabCompleter {

    private final ConstructionWands plugin;
    private final WandManager wandManager;

    public WandCommand(ConstructionWands plugin, WandManager wandManager) {
        this.plugin = plugin;
        this.wandManager = wandManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Solo i giocatori possono eseguire questo comando!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("constructionwands.give")) {
            String noPermMsg = plugin.getConfig().getString("messages.no-permission", "&cNon hai il permesso per usare questo comando!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermMsg));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Uso: /wand <nome_bacchetta>");
            player.sendMessage(ChatColor.YELLOW + "Bacchette disponibili: " +
                    String.join(", ", wandManager.getAllWands().keySet()));
            return true;
        }
        String wandId = args[0];
        ItemStack wandItem = wandManager.createWandItem(wandId);

        if (wandItem == null) {
            String notFoundMsg = plugin.getConfig().getString("messages.wand-not-found", "&cBacchetta non trovata!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', notFoundMsg));
            return true;
        }

        player.getInventory().addItem(wandItem);

        String givenMsg = plugin.getConfig().getString("messages.wand-given", "&aHai ricevuto una {wand}!");
        givenMsg = givenMsg.replace("{wand}", wandManager.getWand(wandId).getName());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', givenMsg));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(wandManager.getAllWands().keySet());
        }
        return completions;
    }
}

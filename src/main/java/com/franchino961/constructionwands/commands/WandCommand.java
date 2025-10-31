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
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Uso: /wand <nome_bacchetta> [nome_player] [quantità]");
            sender.sendMessage(ChatColor.YELLOW + "Bacchette disponibili: " +
                    String.join(", ", wandManager.getAllWands().keySet()));
            return true;
        }

        String wandId = args[0];
        String playerName = null;
        int quantity = 1;

        if (args.length >= 2) {
            playerName = args[1];
        }
        if (args.length >= 3) {
            try {
                quantity = Integer.parseInt(args[2]);
                if (quantity < 1) quantity = 1;
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Quantità non valida!");
                return true;
            }
        }

        Player targetPlayer;
        if (playerName != null) {
            targetPlayer = plugin.getServer().getPlayer(playerName);
            if (targetPlayer == null) {
                sender.sendMessage(ChatColor.RED + "Giocatore non trovato!");
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Devi specificare un nome giocatore quando esegui da console!");
                return true;
            }
            targetPlayer = (Player) sender;
        }

        if (!sender.hasPermission("constructionwands.give")) {
            String noPermMsg = plugin.getConfig().getString("messages.no-permission", "&cNon hai il permesso per usare questo comando!");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermMsg));
            return true;
        }

        ItemStack wandItem = wandManager.createWandItem(wandId);
        if (wandItem == null) {
            String notFoundMsg = plugin.getConfig().getString("messages.wand-not-found", "&cBacchetta non trovata!");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', notFoundMsg));
            return true;
        }

        for (int i = 0; i < quantity; i++) {
            targetPlayer.getInventory().addItem(wandItem.clone());
        }

        String givenMsg = plugin.getConfig().getString("messages.wand-given", "&aHai ricevuto una {wand}!");
        givenMsg = givenMsg.replace("{wand}", wandManager.getWand(wandId).getName());
        if (quantity > 1) {
            givenMsg += " x" + quantity;
        }
        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', givenMsg));

        if (!sender.equals(targetPlayer)) {
            sender.sendMessage(ChatColor.GREEN + "Hai dato " + wandManager.getWand(wandId).getName() + " a " + targetPlayer.getName() + (quantity > 1 ? " x" + quantity : ""));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(wandManager.getAllWands().keySet());
        } else if (args.length == 2) {
            // Suggerisci giocatori online
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 3) {
            // Suggerisci quantità
            completions.add("1");
            completions.add("5");
            completions.add("10");
        }
        return completions;
    }
}

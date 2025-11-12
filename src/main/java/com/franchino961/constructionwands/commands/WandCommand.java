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
        // Controlla permesso admin
        if (!sender.hasPermission("constructionwands.admin")) {
            String noPermMsg = plugin.getConfig().getString("messages.no-permission", "&cNon hai il permesso per usare questo comando!");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermMsg));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Uso: /wand <give|reload|list>");
            sender.sendMessage(ChatColor.YELLOW + "  /wand give <nome_bacchetta> <player> [quantità]");
            sender.sendMessage(ChatColor.YELLOW + "  /wand reload - Ricarica la configurazione");
            sender.sendMessage(ChatColor.YELLOW + "  /wand list - Mostra le bacchette disponibili");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "give":
                return handleGiveCommand(sender, args);
            case "reload":
                return handleReloadCommand(sender);
            case "list":
                return handleListCommand(sender);
            default:
                sender.sendMessage(ChatColor.RED + "Sottocomando non valido!");
                sender.sendMessage(ChatColor.YELLOW + "Usa: /wand <give|reload|list>");
                return true;
        }
    }

    private boolean handleGiveCommand(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Uso: /wand give <nome_bacchetta> <player> [quantità]");
            return true;
        }

        String wandId = args[1];
        String playerName = args[2];
        int quantity = 1;

        if (args.length >= 4) {
            try {
                quantity = Integer.parseInt(args[3]);
                if (quantity < 1) quantity = 1;
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Quantità non valida!");
                return true;
            }
        }

        Player targetPlayer = plugin.getServer().getPlayer(playerName);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Giocatore non trovato!");
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

    private boolean handleReloadCommand(CommandSender sender) {
        try {
            plugin.reloadConfig();
            wandManager.loadWandsFromConfig();
            sender.sendMessage(ChatColor.GREEN + "Configurazione ricaricata con successo!");
            sender.sendMessage(ChatColor.YELLOW + "Bacchette caricate: " + wandManager.getAllWands().size());
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Errore durante il reload della configurazione!");
            plugin.getLogger().severe("Errore reload config: " + e.getMessage());
        }
        return true;
    }

    private boolean handleListCommand(CommandSender sender) {
        Map<String, com.franchino961.constructionwands.models.Wand> wands = wandManager.getAllWands();
        
        if (wands.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Nessuna bacchetta configurata!");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "=== Bacchette Disponibili ===");
        for (Map.Entry<String, com.franchino961.constructionwands.models.Wand> entry : wands.entrySet()) {
            com.franchino961.constructionwands.models.Wand wand = entry.getValue();
            sender.sendMessage(ChatColor.YELLOW + "• " + ChatColor.WHITE + entry.getKey() + 
                    ChatColor.GRAY + " - " + wand.getName() + 
                    ChatColor.DARK_GRAY + " (Range: " + wand.getRange() + "x" + wand.getRange() + 
                    ", Length: " + wand.getLength() + ")");
        }
        sender.sendMessage(ChatColor.GREEN + "Totale: " + wands.size() + " bacchette");
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (!sender.hasPermission("constructionwands.admin")) {
            return completions;
        }

        if (args.length == 1) {
            completions.add("give");
            completions.add("reload");
            completions.add("list");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            completions.addAll(wandManager.getAllWands().keySet());
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
            completions.add("1");
            completions.add("5");
            completions.add("10");
        }
        
        return completions;
    }
}

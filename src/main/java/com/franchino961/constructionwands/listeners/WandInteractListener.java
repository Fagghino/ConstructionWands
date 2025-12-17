package com.franchino961.constructionwands.listeners;

import com.franchino961.constructionwands.ConstructionWands;
import com.franchino961.constructionwands.managers.WandManager;
import com.franchino961.constructionwands.models.Wand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WandInteractListener implements Listener {

    private final ConstructionWands plugin;
    private final WandManager wandManager;
    private final Map<Player, Long> lastUse = new HashMap<>();
    private final Map<Player, Long> lastClick = new HashMap<>();
    private final Map<Player, PlacementRecord> lastPlacement = new HashMap<>();
    private final Map<String, Wand.PlacementMode> playerWandModes = new HashMap<>(); // Key: playerUUID_wandUUID
    private final List<Material> blockedBlocks = new ArrayList<>();
    
    // Record per tracciare l'ultimo piazzamento
    private static class PlacementRecord {
        final List<Block> blocks;
        final Material material;
        final long timestamp;
        
        PlacementRecord(List<Block> blocks, Material material) {
            this.blocks = blocks;
            this.material = material;
            this.timestamp = System.currentTimeMillis();
        }
    }

    public WandInteractListener(ConstructionWands plugin, WandManager wandManager) {
        this.plugin = plugin;
        this.wandManager = wandManager;
        loadBlockedBlocks();
    }
    
    private void loadBlockedBlocks() {
        List<String> blockedList = plugin.getConfig().getStringList("blocked-blocks");
        for (String materialName : blockedList) {
            try {
                Material material = Material.valueOf(materialName.toUpperCase());
                blockedBlocks.add(material);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid material in blocked-blocks: " + materialName);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();

        // Evita doppio click entro 100ms
        long currentTime = System.currentTimeMillis();
        Long lastClickTime = lastClick.get(player);
        if (lastClickTime != null && currentTime - lastClickTime < 100) {
            return;
        }
        lastClick.put(player, currentTime);

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!wandManager.isWand(mainHand)) return;

        event.setCancelled(true);

        if (!player.hasPermission("constructionwands.use")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage("no-permission")));
            return;
        }

        String wandId = wandManager.getWandId(mainHand);
        Wand wand = wandManager.getWand(wandId);
        if (wand == null) return;

        // Gestione click sinistro in base alla configurazione left-click-action
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            
            switch (wand.getLeftClickAction()) {
                case NONE:
                    // Non fare nulla
                    break;
                    
                case UNDO:
                    handleUndo(player);
                    break;
                    
                case MODE:
                    handleModeSwitch(player, mainHand);
                    break;
            }
            return;
        }

        long delay = wand.getDelay();
        if (delay > 0) {
            Long lastUseTime = lastUse.get(player);
            if (lastUseTime != null && currentTime - lastUseTime < delay) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage("cooldown")));
                return;
            }
            lastUse.put(player, currentTime);
        }

        Block clickedBlock = event.getClickedBlock();
        BlockFace blockFace = event.getBlockFace();
        if (clickedBlock == null || blockFace == null) return;
        
        // Controlla se il blocco cliccato è nella lista dei blocchi bloccati
        if (blockedBlocks.contains(clickedBlock.getType())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage("blocked-block")));
            return;
        }

        int range = wand.getRange();
        int length = wand.getLength();

        Material material;
        int availableAmount;
        if ("inventory".equalsIgnoreCase(wand.getSource())) {
            material = clickedBlock.getType();
            availableAmount = getTotalAmountInInventory(player, material);
            if (availableAmount == 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage("no-blocks-inventory")));
                return;
            }
        } else {
            ItemStack offHand = player.getInventory().getItemInOffHand();
            if (offHand == null || offHand.getType().isAir() || !offHand.getType().isBlock()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage("no-blocks")));
                return;
            }
            material = offHand.getType();
            availableAmount = offHand.getAmount();
        }

        List<Block> placedBlocksList = new ArrayList<>();
        Wand.PlacementMode mode = getPlayerWandMode(player, mainHand);
        int blocksPlaced = placeBlocks(player, clickedBlock, blockFace, material, range, length, availableAmount, placedBlocksList, mode);

        if (blocksPlaced == 0 && plugin.getProtections().isSsb2Present()) {
            // Se nessun blocco è stato piazzato e SS2 è attivo, controlla se è per mancanza di permessi
            Block testBlock = clickedBlock.getRelative(blockFace);
            if (!plugin.getProtections().canPlace(player, testBlock, clickedBlock, player.getInventory().getItemInMainHand(), org.bukkit.inventory.EquipmentSlot.HAND)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage("no-island-permission")));
            }
            return;
        }

        if (blocksPlaced > 0) {
            if (player.getGameMode() != org.bukkit.GameMode.CREATIVE) {
                if ("inventory".equalsIgnoreCase(wand.getSource())) {
                    removeFromInventory(player, material, blocksPlaced);
                } else {
                    ItemStack offHand = player.getInventory().getItemInOffHand();
                    int toRemove = Math.min(blocksPlaced, offHand.getAmount());
                    offHand.setAmount(offHand.getAmount() - toRemove);
                }
            }

            if (!wandManager.decrementUses(mainHand)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage("uses-depleted")));
                player.getInventory().setItemInMainHand(null);
            }
            
            // Salva il record del piazzamento per undo
            if (blocksPlaced > 0) {
                lastPlacement.put(player, new PlacementRecord(placedBlocksList, material));
            }
        }
    }

    private int placeBlocks(Player player, Block clickedBlock, BlockFace face, Material material, int range, int length, int amount, List<Block> placedBlocksList, Wand.PlacementMode mode) {
        int blocksPlaced = 0;
        
        // Determina il blocco di partenza e la direzione in base alla modalità
        Block startBlock;
        BlockFace effectiveFace;
        
        if (mode == Wand.PlacementMode.VERTICAL) {
            // Modalità VERTICALE: Piazza sulla faccia cliccata, griglia estesa verticalmente (asse Y)
            startBlock = clickedBlock.getRelative(face);
            effectiveFace = BlockFace.UP; // Estensione verticale
        } else if (mode == Wand.PlacementMode.HORIZONTAL) {
            // Modalità ORIZZONTALE: Piazza sulla faccia cliccata, griglia estesa orizzontalmente
            startBlock = clickedBlock.getRelative(face);
            // Usa una direzione orizzontale per l'estensione
            effectiveFace = (face == BlockFace.UP || face == BlockFace.DOWN) ? BlockFace.NORTH : face;
        } else {
            // Modalità AUTO: Comportamento originale basato sulla faccia cliccata
            startBlock = clickedBlock.getRelative(face);
            effectiveFace = face;
        }
        
        int offset = (range - 1) / 2;
        int lengthOffset = (length - 1) / 2;
        BlockFace[] perpendiculars = getPerpendicularFaces(effectiveFace);
        BlockFace perp1 = perpendiculars[0];
        BlockFace perp2 = perpendiculars[1];

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemStack blockItem = new ItemStack(material, 1);

        for (int k = -lengthOffset; k <= lengthOffset; k++) {
            Block layerBlock = startBlock.getRelative(effectiveFace, k);
            for (int i = -offset; i <= offset; i++) {
                for (int j = -offset; j <= offset; j++) {
                    if (blocksPlaced >= amount) return blocksPlaced;
                    Block targetBlock = layerBlock.getRelative(perp1, i).getRelative(perp2, j);
                    
                    // Verifica protezioni (world border, eventi, SS2)
                    if (!plugin.getProtections().canPlace(player, targetBlock, clickedBlock, itemInHand, EquipmentSlot.HAND)) {
                        continue;
                    }
                    
                    if (canPlaceBlock(targetBlock, player)) {
                        // Crea evento BlockPlaceEvent per compatibilità con SS2 e altri plugin
                        BlockPlaceEvent placeEvent = new BlockPlaceEvent(
                            targetBlock,
                            targetBlock.getState(),
                            clickedBlock,
                            blockItem,
                            player,
                            true,
                            EquipmentSlot.HAND
                        );
                        
                        // Chiama l'evento per notificare altri plugin (es. SuperiorSkyblock2)
                        Bukkit.getPluginManager().callEvent(placeEvent);
                        
                        // Se l'evento è cancellato, salta questo blocco
                        if (!placeEvent.isCancelled()) {
                            targetBlock.setType(material);
                            placedBlocksList.add(targetBlock);
                            blocksPlaced++;
                        }
                    }
                }
            }
        }
        return blocksPlaced;
    }

    private BlockFace[] getPerpendicularFaces(BlockFace face) {
        switch (face) {
            case UP:
            case DOWN:
                return new BlockFace[]{BlockFace.NORTH, BlockFace.EAST};
            case NORTH:
            case SOUTH:
                return new BlockFace[]{BlockFace.UP, BlockFace.EAST};
            case EAST:
            case WEST:
                return new BlockFace[]{BlockFace.UP, BlockFace.NORTH};
            default:
                return new BlockFace[]{BlockFace.NORTH, BlockFace.EAST};
        }
    }

    private boolean canPlaceBlock(Block block, Player player) {
        if (!block.getType().isAir() && block.getType() != Material.WATER && block.getType() != Material.LAVA
                && block.getType() != Material.GRASS && block.getType() != Material.TALL_GRASS) {
            return false;
        }
        return true;
    }

    private int getTotalAmountInInventory(Player player, Material material) {
        int total = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                total += item.getAmount();
            }
        }
        return total;
    }

    private void removeFromInventory(Player player, Material material, int amount) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                int toRemove = Math.min(amount, item.getAmount());
                item.setAmount(item.getAmount() - toRemove);
                amount -= toRemove;
                if (amount <= 0) break;
            }
        }
    }

    private void handleUndo(Player player) {
        PlacementRecord record = lastPlacement.get(player);

        if (record == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage("undo-no-placement")));
            return;
        }

        long undoTimeout = plugin.getConfig().getLong("undo-timeout", 60000);
        long currentTime = System.currentTimeMillis();

        if (currentTime - record.timestamp > undoTimeout) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage("undo-timeout-expired")));
            lastPlacement.remove(player);
            return;
        }

        int blocksRemoved = 0;
        int blocksChanged = 0;

        for (Block block : record.blocks) {
            if (block.getType() == record.material) {
                block.setType(Material.AIR);
                blocksRemoved++;
            } else {
                blocksChanged++;
            }
        }

        // Give items back to player
        ItemStack itemsToReturn = new ItemStack(record.material, blocksRemoved);
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(itemsToReturn);

        // Drop items that don't fit
        if (!leftover.isEmpty()) {
            for (ItemStack item : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }

        lastPlacement.remove(player);

        String message = plugin.getMessage("undo-success").replace("%blocks%", String.valueOf(blocksRemoved));

        if (blocksChanged > 0) {
            message += ChatColor.translateAlternateColorCodes('&',
                    plugin.getMessage("undo-blocks-changed").replace("%changed%", String.valueOf(blocksChanged)));
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    
    private void handleModeSwitch(Player player, ItemStack wandItem) {
        String wandUUID = getWandUUID(wandItem);
        if (wandUUID == null) return;
        
        String key = player.getUniqueId().toString() + "_" + wandUUID;
        Wand.PlacementMode currentMode = playerWandModes.getOrDefault(key, Wand.PlacementMode.AUTO);
        Wand.PlacementMode newMode = currentMode.next();
        playerWandModes.put(key, newMode);
        
        // Aggiorna la lore della bacchetta con la nuova modalità
        wandManager.updateWandLore(wandItem, newMode);
        
        String messageKey = "mode-" + newMode.name().toLowerCase();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage(messageKey)));
    }
    
    private String getWandUUID(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer()
                .get(wandManager.getWandUuidKey(), org.bukkit.persistence.PersistentDataType.STRING);
    }
    
    private Wand.PlacementMode getPlayerWandMode(Player player, ItemStack wandItem) {
        String wandUUID = getWandUUID(wandItem);
        if (wandUUID == null) return Wand.PlacementMode.AUTO;
        
        String key = player.getUniqueId().toString() + "_" + wandUUID;
        return playerWandModes.getOrDefault(key, Wand.PlacementMode.AUTO);
    }
}

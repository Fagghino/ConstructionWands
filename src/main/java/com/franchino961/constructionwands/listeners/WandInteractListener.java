package com.franchino961.constructionwands.listeners;

import com.franchino961.constructionwands.ConstructionWands;
import com.franchino961.constructionwands.managers.WandManager;
import com.franchino961.constructionwands.models.Wand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;

public class WandInteractListener implements Listener {

    private final ConstructionWands plugin;
    private final WandManager wandManager;
    private final Map<Player, Long> lastUse = new HashMap<>();
    private final Map<Player, Long> lastClick = new HashMap<>();

    public WandInteractListener(ConstructionWands plugin, WandManager wandManager) {
        this.plugin = plugin;
        this.wandManager = wandManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
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
            String noPermMsg = plugin.getConfig().getString("messages.no-permission", "&cNon hai il permesso per usare le bacchette!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermMsg));
            return;
        }

        String wandId = wandManager.getWandId(mainHand);
        Wand wand = wandManager.getWand(wandId);
        if (wand == null) return;

        long delay = wand.getDelay();
        if (delay > 0) {
            Long lastUseTime = lastUse.get(player);
            if (lastUseTime != null && currentTime - lastUseTime < delay) {
                String cooldownMsg = plugin.getConfig().getString("messages.cooldown", "&cDevi aspettare prima di usare nuovamente la bacchetta!");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMsg));
                return;
            }
            lastUse.put(player, currentTime);
        }

        Block clickedBlock = event.getClickedBlock();
        BlockFace blockFace = event.getBlockFace();
        if (clickedBlock == null || blockFace == null) return;

        int range = wand.getRange();
        int length = wand.getLength();

        Material material;
        int availableAmount;
        if ("inventory".equalsIgnoreCase(wand.getSource())) {
            material = clickedBlock.getType();
            availableAmount = getTotalAmountInInventory(player, material);
            if (availableAmount == 0) {
                String noBlocksMsg = plugin.getConfig().getString("messages.no-blocks-inventory", "&cNon hai abbastanza blocchi nell'inventario!");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noBlocksMsg));
                return;
            }
        } else {
            ItemStack offHand = player.getInventory().getItemInOffHand();
            if (offHand == null || offHand.getType().isAir() || !offHand.getType().isBlock()) {
                String noBlocksMsg = plugin.getConfig().getString("messages.no-blocks", "&cNon hai abbastanza blocchi nella mano secondaria!");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noBlocksMsg));
                return;
            }
            material = offHand.getType();
            availableAmount = offHand.getAmount();
        }

        int blocksPlaced = placeBlocks(player, clickedBlock, blockFace, material, range, length, availableAmount);

        if (blocksPlaced == 0 && plugin.getProtections().isSsb2Present()) {
            // Se nessun blocco è stato piazzato e SS2 è attivo, controlla se è per mancanza di permessi
            Block testBlock = clickedBlock.getRelative(blockFace);
            if (!plugin.getProtections().canPlace(player, testBlock, clickedBlock, player.getInventory().getItemInMainHand(), org.bukkit.inventory.EquipmentSlot.HAND)) {
                String noPermMsg = plugin.getConfig().getString("messages.no-island-permission", "&cNon hai il permesso per costruire su questa isola!");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermMsg));
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
                String depletedMsg = plugin.getConfig().getString("messages.uses-depleted", "&cLa bacchetta ha esaurito gli usi!");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', depletedMsg));
                player.getInventory().setItemInMainHand(null);
            }
        }
    }

    private int placeBlocks(Player player, Block clickedBlock, BlockFace face, Material material, int range, int length, int amount) {
        int blocksPlaced = 0;
        Block startBlock = clickedBlock.getRelative(face);
        int offset = (range - 1) / 2;
        int lengthOffset = (length - 1) / 2;
        BlockFace[] perpendiculars = getPerpendicularFaces(face);
        BlockFace perp1 = perpendiculars[0];
        BlockFace perp2 = perpendiculars[1];

        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        for (int k = -lengthOffset; k <= lengthOffset; k++) {
            Block layerBlock = startBlock.getRelative(face, k);
            for (int i = -offset; i <= offset; i++) {
                for (int j = -offset; j <= offset; j++) {
                    if (blocksPlaced >= amount) return blocksPlaced;
                    Block targetBlock = layerBlock.getRelative(perp1, i).getRelative(perp2, j);
                    
                    // Verifica protezioni (world border, eventi, SS2)
                    if (!plugin.getProtections().canPlace(player, targetBlock, clickedBlock, itemInHand, EquipmentSlot.HAND)) {
                        continue;
                    }
                    
                    if (canPlaceBlock(targetBlock, player)) {
                        targetBlock.setType(material);
                        blocksPlaced++;
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
}

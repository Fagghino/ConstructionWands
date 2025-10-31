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

public class WandInteractListener implements Listener {

    private final ConstructionWands plugin;
    private final WandManager wandManager;

    public WandInteractListener(ConstructionWands plugin, WandManager wandManager) {
        this.plugin = plugin;
        this.wandManager = wandManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
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

        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (offHand == null || offHand.getType().isAir() || !offHand.getType().isBlock()) {
            String noBlocksMsg = plugin.getConfig().getString("messages.no-blocks", "&cNon hai abbastanza blocchi nella mano secondaria!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noBlocksMsg));
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        BlockFace blockFace = event.getBlockFace();
        if (clickedBlock == null || blockFace == null) return;

        int range = wand.getRange();

        int blocksPlaced = placeBlocks(player, clickedBlock, blockFace, offHand.getType(), range);

        if (blocksPlaced > 0) {
            if (player.getGameMode() != org.bukkit.GameMode.CREATIVE) {
                int toRemove = Math.min(blocksPlaced, offHand.getAmount());
                offHand.setAmount(offHand.getAmount() - toRemove);
            }

            if (!wandManager.decrementUses(mainHand)) {
                String depletedMsg = plugin.getConfig().getString("messages.uses-depleted", "&cLa bacchetta ha esaurito gli usi!");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', depletedMsg));
                player.getInventory().setItemInMainHand(null);
            }
        }
    }

    private int placeBlocks(Player player, Block clickedBlock, BlockFace face, Material material, int range) {
        int blocksPlaced = 0;
        Block startBlock = clickedBlock.getRelative(face);
        int offset = (range - 1) / 2;
        BlockFace[] perpendiculars = getPerpendicularFaces(face);
        BlockFace perp1 = perpendiculars[0];
        BlockFace perp2 = perpendiculars[1];

        for (int i = -offset; i <= offset; i++) {
            for (int j = -offset; j <= offset; j++) {
                Block targetBlock = startBlock.getRelative(perp1, i).getRelative(perp2, j);
                if (canPlaceBlock(targetBlock, player)) {
                    targetBlock.setType(material);
                    blocksPlaced++;
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
}

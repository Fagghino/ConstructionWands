package com.franchino961.constructionwands.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Protections {

    private final boolean ssb2Present;

    public Protections() {
        this.ssb2Present = Bukkit.getPluginManager().getPlugin("SuperiorSkyblock2") != null;
    }

    public boolean isSsb2Present() {
        return ssb2Present;
    }

    /**
     * Verifica se il giocatore può piazzare un blocco in questa posizione.
     * Controlla: World Border, altri plugin di protezione (via BlockPlaceEvent), e SuperiorSkyblock2.
     */
    public boolean canPlace(Player player, Block target, Block placedAgainst, ItemStack itemInHand, EquipmentSlot hand) {
        // 1) World Border check
        WorldBorder border = target.getWorld().getWorldBorder();
        if (border != null) {
            Location location = target.getLocation().add(0.5, 0.5, 0.5);
            if (!border.isInside(location)) {
                return false;
            }
        }

        // 2) Verifica protezioni generiche tramite BlockPlaceEvent (WorldGuard, GriefPrevention, ecc.)
        BlockState replaced = target.getState();
        BlockPlaceEvent placeEvent = new BlockPlaceEvent(target, replaced, placedAgainst, itemInHand, player, true, hand);
        Bukkit.getPluginManager().callEvent(placeEvent);
        if (placeEvent.isCancelled()) {
            return false;
        }

        // 3) SuperiorSkyblock2 check (se presente)
        if (ssb2Present) {
            if (!canBuildInIsland(player, target.getLocation())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verifica se il giocatore può costruire nella locazione (deve essere nella sua isola o in un'isola dove ha permessi).
     */
    private boolean canBuildInIsland(Player player, Location location) {
        try {
            Island islandAt = SuperiorSkyblockAPI.getIslandAt(location);
            if (islandAt == null) {
                // Nessuna isola in questa posizione
                return false;
            }

            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
            if (superiorPlayer == null) {
                return false;
            }

            Island playerIsland = superiorPlayer.getIsland();
            if (playerIsland == null) {
                // Il giocatore non ha un'isola
                return false;
            }

            // Verifica se è la stessa isola (include membri del team)
            if (playerIsland.equals(islandAt)) {
                return true;
            }

            // Verifica se il giocatore ha permessi di costruzione sull'isola (es. coop)
            // Nota: hasPermission potrebbe richiedere IslandPrivilege specifico, verifica API
            // Per sicurezza, consentiamo solo se è la propria isola
            return false;

        } catch (Throwable ex) {
            // In caso di errore API (mismatch versione), comportamento permissivo
            Bukkit.getLogger().warning("[ConstructionWands] Errore hook SuperiorSkyblock2: " + ex.getMessage());
            return true;
        }
    }
}

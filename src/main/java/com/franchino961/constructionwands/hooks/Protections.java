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
     * Controlla: World Border (sempre), altri plugin di protezione (via BlockPlaceEvent), e SuperiorSkyblock2.
     */
    public boolean canPlace(Player player, Block target, Block placedAgainst, ItemStack itemInHand, EquipmentSlot hand) {
        // 1) World Border check (SEMPRE controllato, anche per admin bypass)
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
     * Verifica se il giocatore può costruire nella locazione.
     * Permette: proprietario, membri, coop e giocatori con permessi personalizzati di costruzione.
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

            // Verifica se lo staffer ha bypass admin attivo
            if (superiorPlayer.hasBypassModeEnabled()) {
                return true;
            }

            // Verifica se il giocatore è il proprietario
            if (islandAt.getOwner().equals(superiorPlayer)) {
                return true;
            }

            // Verifica se è un membro del team
            if (islandAt.isMember(superiorPlayer)) {
                return true;
            }

            // Verifica se è un coop player
            if (islandAt.isCoop(superiorPlayer)) {
                return true;
            }

            // Verifica se ha permessi personalizzati di piazzare blocchi
            // Questo copre i player con permessi dati tramite /is permission
            try {
                // Prova a usare hasPermission con reflection per evitare problemi di API
                java.lang.reflect.Method hasPermMethod = islandAt.getClass().getMethod("hasPermission", 
                    com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer.class, 
                    com.bgsoftware.superiorskyblock.api.island.IslandPrivilege.class);
                
                // Cerca il privilegio BLOCK_PLACE
                Class<?> privilegeClass = Class.forName("com.bgsoftware.superiorskyblock.api.island.IslandPrivileges");
                Object blockPlacePrivilege = privilegeClass.getField("BLOCK_PLACE").get(null);
                
                Boolean hasPermission = (Boolean) hasPermMethod.invoke(islandAt, superiorPlayer, blockPlacePrivilege);
                return hasPermission != null && hasPermission;
            } catch (Exception e) {
                // Se la reflection fallisce, nega l'accesso per sicurezza
                return false;
            }

        } catch (Throwable ex) {
            // In caso di errore API (mismatch versione), comportamento permissivo
            Bukkit.getLogger().warning("[ConstructionWands] Errore hook SuperiorSkyblock2: " + ex.getMessage());
            return true;
        }
    }
}

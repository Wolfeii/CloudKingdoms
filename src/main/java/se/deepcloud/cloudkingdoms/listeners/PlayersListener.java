package se.deepcloud.cloudkingdoms.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.events.PlayerEnterChunkEvent;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.kingdom.claim.ClaimPosition;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.player.chat.PlayerChat;
import se.deepcloud.cloudkingdoms.scheduler.BukkitExecutor;
import se.deepcloud.cloudkingdoms.utilities.logging.Debug;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;
import se.deepcloud.cloudkingdoms.utilities.sorting.Mutable;

import java.util.*;

public class PlayersListener implements Listener {

    private final CloudKingdoms plugin;
    private final HashSet<KingdomPlayer> previouslyDetected = new HashSet<>();

    public PlayersListener(@NotNull CloudKingdoms plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayerLogin(PlayerLoginEvent e) {
        KingdomPlayer kingdomPlayer = plugin.getPlayerManager().getKingdomPlayer(e.getPlayer());
        List<KingdomPlayer> duplicatedPlayers = plugin.getPlayerManager().matchAllPlayers(_kingdomPlayer ->
                _kingdomPlayer != kingdomPlayer && _kingdomPlayer.getName().equalsIgnoreCase(e.getPlayer().getName()));
        if (!duplicatedPlayers.isEmpty()) {
            Log.info("Changing UUID of " + kingdomPlayer.getName() + " to " + kingdomPlayer.getUniqueId());
            for (KingdomPlayer duplicatePlayer : duplicatedPlayers) {
                plugin.getPlayerManager().replacePlayers(duplicatePlayer, kingdomPlayer);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent e) {
        KingdomPlayer kingdomPlayer = plugin.getPlayerManager().getKingdomPlayer(e.getPlayer());

        // Updating the name of the player.
        if (!kingdomPlayer.getName().equals(e.getPlayer().getName())) {
            kingdomPlayer.updateName();
        }

        kingdomPlayer.updateLastOnlineStatus();

        BukkitExecutor.sync(() -> {
            if (!e.getPlayer().isOnline())
                return;

            // Updating skin of the player
            //TODO: if (!plugin.getProviders().notifySkinsListeners(superiorPlayer))
            //TODO:    plugin.getNMSPlayers().setSkinTexture(superiorPlayer);

        }, 5L);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onPlayerAsyncChatLowest(AsyncPlayerChatEvent e) {
        // PlayerChat should be on LOWEST priority so other chat plugins don't conflict.
        PlayerChat playerChat = PlayerChat.getChatListener(e.getPlayer());
        if (playerChat != null && playerChat.supply(e.getMessage())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerMove(PlayerMoveEvent e) {
        if (e != null && !e.isCancelled() && e.getTo() != null) {
            Chunk prev = e.getFrom().getChunk();
            Chunk to = e.getTo().getChunk();

            KingdomPlayer kingdomPlayer = plugin.getPlayerManager().getKingdomPlayer(e.getPlayer());
            ClaimPosition prevData = plugin.getKingdomManager().getClaimData(prev);
            ClaimPosition toData = plugin.getKingdomManager().getClaimData(to);

            if (prev.getX() != to.getX() || prev.getZ() != to.getZ()) {
                Bukkit.getPluginManager().callEvent(
                        new PlayerEnterChunkEvent(kingdomPlayer, prev, to, prevData, toData)
                );
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerEnterChunk(PlayerEnterChunkEvent e) {
        Log.debug(Debug.ENTER_KINGDOM, "PlayersListener", "onPlayerEnterChunk", e.getPlayer().getName());

        if (!e.isCancelled()) {
            if (e.getNextPosition().isClaimed()) {
                if (e.getPreviousPosition().isClaimed()) {
                    if (!e.doesChunksHaveSameOwner()) {
                        e.getPlayer().sendTitle("&a→ Går in i " + e.getNextPosition().getOwner().getName(), "&7Skriv /chunk för info", 20, 100, 20);
                    }
                } else {
                    e.getPlayer().sendTitle("&a→ Går in i " + e.getNextPosition().getOwner().getName(), "&7Skriv /chunk för info", 20, 100, 20);
                }
            } else {
                if (e.getPreviousPosition().isClaimed()) {
                    e.getPlayer().sendTitle("&c→ Lämnar " + e.getPreviousPosition().getOwner().getName(), "&7Skriv /chunk för info", 20, 100, 20);
                }
            }
        }
    }

}

package se.deepcloud.cloudkingdoms.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.player.chat.PlayerChat;
import se.deepcloud.cloudkingdoms.scheduler.BukkitExecutor;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;
import se.deepcloud.cloudkingdoms.utilities.sorting.Mutable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayersListener implements Listener {

    private final CloudKingdoms plugin;

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
}

package se.deepcloud.cloudkingdoms.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.player.builder.KingdomPlayerBuilder;
import se.deepcloud.cloudkingdoms.storage.bridge.PlayersDatabaseBridge;
import se.deepcloud.cloudkingdoms.utilities.sorting.SequentialListBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class PlayerManager {

    private final Map<UUID, KingdomPlayer> players = new ConcurrentHashMap<>();
    private final Map<String, KingdomPlayer> playersByNames = new ConcurrentHashMap<>();

    private final CloudKingdoms plugin;

    public PlayerManager(@NotNull CloudKingdoms plugin) {
        this.plugin = plugin;
    }

    public @Nullable KingdomPlayer getKingdomPlayer(String name) {
        KingdomPlayer kingdomPlayer = this.playersByNames.get(name.toLowerCase(Locale.ENGLISH));

        if (kingdomPlayer == null) {
            kingdomPlayer = players.values().stream()
                    .filter(player -> player.getName().equalsIgnoreCase(name))
                    .findFirst().orElse(null);
            if (kingdomPlayer != null)
                this.playersByNames.put(name.toLowerCase(Locale.ENGLISH), kingdomPlayer);
        }

        return kingdomPlayer;
    }

    public KingdomPlayer getKingdomPlayer(CommandSender commandSender) {
        return getKingdomPlayer((Player) commandSender);
    }

    public KingdomPlayer getKingdomPlayer(Player player) {
        return getKingdomPlayer(player.getUniqueId());
    }

    public KingdomPlayer getKingdomPlayer(UUID playerUUID) {
        return getKingdomPlayer(playerUUID, true);
    }

    public KingdomPlayer getKingdomPlayer(UUID playerUUID, boolean createNew) {
        KingdomPlayer kingdomPlayer = this.players.get(playerUUID);

        if (kingdomPlayer == null && createNew) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            KingdomPlayerBuilder builder = new KingdomPlayerBuilder()
                    .setUniqueId(playerUUID);

            if (offlinePlayer.getName() != null)
                builder.setName(offlinePlayer.getName());

            kingdomPlayer = builder.build();
            addPlayer(kingdomPlayer);
            PlayersDatabaseBridge.insertPlayer(kingdomPlayer);
        }

        return kingdomPlayer;
    }

    public List<KingdomPlayer> getAllPlayers() {
        return new ArrayList<>(players.values());
    }

    public List<KingdomPlayer> matchAllPlayers(Predicate<? super KingdomPlayer> predicate) {
        return new SequentialListBuilder<KingdomPlayer>()
                .filter(predicate)
                .build(getAllPlayers());
    }

    public void replacePlayers(KingdomPlayer originPlayer, KingdomPlayer newPlayer) {
        removePlayer(originPlayer);

        newPlayer.merge(originPlayer);

        for (Kingdom kingdom : plugin.getKingdomManager().getKingdoms())
            kingdom.replacePlayers(originPlayer, newPlayer);
    }

    public void addPlayer(KingdomPlayer kingdomPlayer) {
        this.players.put(kingdomPlayer.getUniqueId(), kingdomPlayer);
        String playerName = kingdomPlayer.getName();
        if (!playerName.equals("null"))
            this.playersByNames.put(playerName.toLowerCase(Locale.ENGLISH), kingdomPlayer);
    }

    public void removePlayer(KingdomPlayer kingdomPlayer) {
        this.players.remove(kingdomPlayer.getUniqueId());
        this.playersByNames.remove(kingdomPlayer.getName().toLowerCase(Locale.ENGLISH));
    }
}


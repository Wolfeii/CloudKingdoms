package se.deepcloud.cloudkingdoms.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.kingdom.builder.KingdomBuilder;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRole;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRoles;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.player.builder.KingdomPlayerBuilder;
import se.deepcloud.cloudkingdoms.storage.bridge.DatabaseBridge;
import se.deepcloud.cloudkingdoms.storage.cache.DatabaseCache;
import se.deepcloud.cloudkingdoms.storage.loader.DatabaseLoader;
import se.deepcloud.cloudkingdoms.storage.loader.sql.SQLDatabaseLoader;
import se.deepcloud.cloudkingdoms.storage.serialization.KingdomsDeserializer;
import se.deepcloud.cloudkingdoms.storage.serialization.PlayersDeserializer;
import se.deepcloud.cloudkingdoms.storage.sql.SQLDatabaseBridge;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class DataManager {

    private final CloudKingdoms plugin;
    private final List<DatabaseLoader> databaseLoaders = new LinkedList<>();

    public DataManager(@NotNull CloudKingdoms plugin) {
        this.plugin = plugin;
    }

    public void load() throws RuntimeException {
        loadDatabaseLoaders();

        runState(DatabaseLoader.State.INITIALIZE);

        runState(DatabaseLoader.State.POST_INITIALIZE);

        runState(DatabaseLoader.State.PRE_LOAD_DATA);

        loadPlayers();
        loadKingdoms();

        runState(DatabaseLoader.State.POST_LOAD_DATA);

        /*
         *  Because of a bug caused leaders to be guests, I am looping through all the players and trying to fix it here.
         */
        for (KingdomPlayer prisonPlayer : plugin.getPlayerManager().getAllPlayers()) {
            if (prisonPlayer.getKingdomOwner().getUniqueId().equals(prisonPlayer.getUniqueId()) && prisonPlayer.getKingdom() != null && !prisonPlayer.getKingdomRole().isLastRole()) {
                Log.warn("Seems like " + prisonPlayer.getName() + " äger ett Kingdom, men har en gäst roll - fixar det...");
                prisonPlayer.setKingdomRole(KingdomRoles.LEADER);
            }
        }
    }

    public void addDatabaseLoader(DatabaseLoader databaseLoader) {
        this.databaseLoaders.add(databaseLoader);
    }


    public void closeConnection() {
        for (DatabaseLoader databaseLoader : databaseLoaders) {
            try {
                databaseLoader.setState(DatabaseLoader.State.SHUTDOWN);
            } catch (Throwable ignored) {
            }
        }
    }

    private void loadDatabaseLoaders() {
        addDatabaseLoader(new SQLDatabaseLoader(plugin));
    }

    private void loadPlayers() {
        Log.info("Starting to load players...");

        DatabaseBridge playersLoader = new SQLDatabaseBridge();

        DatabaseCache<KingdomPlayerBuilder> databaseCache = new DatabaseCache<>();
        AtomicInteger playersCount = new AtomicInteger();
        long startTime = System.currentTimeMillis();

        // PlayersDeserializer.deserializePersistentDataContainer(playersLoader, databaseCache);

        playersLoader.loadAllObjects("players", resultSetRaw -> {
            DatabaseResult databaseResult = new DatabaseResult(resultSetRaw);

            Optional<UUID> uuid = databaseResult.getUUID("uuid");
            if (uuid.isEmpty()) {
                Log.warn("Cannot load player with null uuid, skipping...");
                return;
            }

            plugin.getPlayerManager().addPlayer(databaseCache.computeIfAbsentInfo(uuid.get(), KingdomPlayer::newBuilder)
                    .setUniqueId(uuid.get())
                    .setName(databaseResult.getString("last_used_name").orElse("null"))
                    .setTextureValue(databaseResult.getString("last_used_skin").orElse(""))
                    .build());

            playersCount.incrementAndGet();
        });

        long endTime = System.currentTimeMillis();

        Log.info("Finished loading " + playersCount.get() + " players (Took " + (endTime - startTime) + "ms)");
    }

    private void loadKingdoms() {
        DatabaseBridge kingdomsLoader = new SQLDatabaseBridge();
        DatabaseCache<KingdomBuilder> databaseCache = new DatabaseCache<>();
        AtomicInteger kingdomsCount = new AtomicInteger();

        long startTime = System.currentTimeMillis();

        KingdomsDeserializer.deserializeMembers(kingdomsLoader, databaseCache);

        kingdomsLoader.loadAllObjects("kingdoms", resultSetRaw -> {
            DatabaseResult databaseResult = new DatabaseResult(resultSetRaw);

            Optional<UUID> uuid = databaseResult.getUUID("uuid");
            if (uuid.isEmpty()) {
                Log.warn("Hittade Kingdom med ogiltligt UUID, hoppar över...");
                return;
            }

            Optional<KingdomPlayer> owner = databaseResult.getUUID("owner").map(plugin.getPlayerManager()::getKingdomPlayer);
            if (owner.isEmpty()) {
                Log.warn("Hittade Kingdom med ogiltlig ägare, hoppar över...");
                return;
            }

            Optional<String> name = databaseResult.getString("name");
            if (name.isEmpty()) {
                Log.warn("Hittade Kingdom med ogiltligt namn, hoppar över...");
                return;
            }

            KingdomBuilder kingdomBuilder = databaseCache.computeIfAbsentInfo(uuid.get(), KingdomBuilder::new)
                    .setOwner(owner.get())
                    .setUniqueId(uuid.get())
                    .setKingdomName(name.get())
                    .setCreationTime(databaseResult.getLong("creation_time").orElse(System.currentTimeMillis() / 1000));

            plugin.getKingdomManager().addKingdom(kingdomBuilder.build());
            kingdomsCount.incrementAndGet();
        });

        long endTime = System.currentTimeMillis();
        Log.info("Laddade totalt " + kingdomsCount.get() + " olika Kingdoms på " + (endTime - startTime) + "ms");
    }

    private void runState(DatabaseLoader.State state) throws RuntimeException {
        for (DatabaseLoader databaseLoader : databaseLoaders) {
            databaseLoader.setState(state);
        }
    }
}

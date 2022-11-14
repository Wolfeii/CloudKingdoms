package se.deepcloud.cloudkingdoms.storage.serialization;

import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.player.builder.KingdomPlayerBuilder;
import se.deepcloud.cloudkingdoms.storage.DatabaseResult;
import se.deepcloud.cloudkingdoms.storage.bridge.DatabaseBridge;
import se.deepcloud.cloudkingdoms.storage.cache.DatabaseCache;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;

import java.util.Optional;
import java.util.UUID;

public class PlayersDeserializer {

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    public static void deserializePersistentDataContainer(@NotNull DatabaseBridge databaseBridge, @NotNull DatabaseCache<KingdomPlayerBuilder> databaseCache) {
        databaseBridge.loadAllObjects("players_custom_data", customDataRow -> {
            DatabaseResult customData = new DatabaseResult(customDataRow);

            Optional<UUID> uuid = customData.getUUID("player");
            if (uuid.isEmpty()) {
                Log.warn("&cCannot load custom data for null players, skipping...");
                return;
            }

            byte[] persistentData = customData.getBlob("data").orElse(new byte[0]);

            if (persistentData.length == 0)
                return;

            KingdomPlayerBuilder builder = databaseCache.computeIfAbsentInfo(uuid.get(), KingdomPlayer::newBuilder);
            builder.setPersistentData(persistentData);
        });
    }
}

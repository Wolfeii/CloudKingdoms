package se.deepcloud.cloudkingdoms.kingdom;

import org.bukkit.Location;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.builder.KingdomBuilder;
import se.deepcloud.cloudkingdoms.message.Message;
import se.deepcloud.cloudkingdoms.scheduler.BukkitExecutor;
import se.deepcloud.cloudkingdoms.storage.bridge.DatabaseBridgeMode;
import se.deepcloud.cloudkingdoms.storage.bridge.KingdomDatabaseBridge;
import se.deepcloud.cloudkingdoms.utilities.formatting.Formatters;
import se.deepcloud.cloudkingdoms.utilities.logging.Debug;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;
import se.deepcloud.cloudkingdoms.utilities.sorting.SequentialListBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class KingdomManager {

    private final Map<UUID, Kingdom> kingdomsByUUID = new ConcurrentHashMap<>();

    private final CloudKingdoms plugin;

    public KingdomManager(@NotNull CloudKingdoms plugin) {
        this.plugin = plugin;
    }

    public void addKingdom(Kingdom kingdom) {
        this.kingdomsByUUID.put(kingdom.getUniqueId(), kingdom);
    }

    public void removeKingdom(Kingdom kingdom) {
        kingdomsByUUID.remove(kingdom.getUniqueId());
    }

    @Nullable
    public Kingdom getKingdomByUUID(UUID uuid) {
        return this.kingdomsByUUID.get(uuid);
    }

    public int getKingdomsAmount() {
        return this.kingdomsByUUID.size();
    }

    @Nullable
    public Kingdom getKingdomAt(@Nullable Location location) {
        if (location == null) {
            return null;
        }

        return kingdomsByUUID.values().stream()
                .filter(kingdom -> kingdom.isInside(location))
                .findAny().orElse(null);
    }

    public Kingdom getKingdom(@NotNull String kingdomName) {
        String inputName = Formatters.STRIP_COLOR_FORMATTER.format(kingdomName);
        return getKingdoms().stream().filter(kingdom -> kingdom.getName().equalsIgnoreCase(inputName)).findFirst().orElse(null);
    }

    public Kingdom createKingdom(@NotNull KingdomBuilder kingdomBuilder) {
        assert kingdomBuilder.owner != null;

        Log.debug(Debug.CREATE_KINGDOM, "KingdomManager", "createKingdom",
                kingdomBuilder.owner.getName(), kingdomBuilder.kingdomName);

        Kingdom kingdom = kingdomBuilder.setUniqueId(generateKingdomUUID()).build();
        addKingdom(kingdom);

        KingdomDatabaseBridge.insertKingdom(kingdom);
        BukkitExecutor.sync(() -> Message.CREATE_KINGDOM.send(kingdomBuilder.owner), 1L);

        return kingdom;
    }

    private UUID generateKingdomUUID() {
        UUID uuid;

        do {
            uuid = UUID.randomUUID();
        } while (getKingdomByUUID(uuid) != null || plugin.getPlayerManager().getKingdomPlayer(uuid, false) != null);

        return uuid;
    }

    public List<Kingdom> getKingdoms() {
        return new SequentialListBuilder<Kingdom>().build(this.kingdomsByUUID.values());
    }

}

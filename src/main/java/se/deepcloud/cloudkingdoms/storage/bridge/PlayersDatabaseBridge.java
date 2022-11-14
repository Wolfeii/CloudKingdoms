package se.deepcloud.cloudkingdoms.storage.bridge;


import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.storage.filter.DatabaseFilter;
import se.deepcloud.cloudkingdoms.utilities.sorting.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class PlayersDatabaseBridge {

    private static final Map<UUID, Map<FutureSave, Set<Object>>> SAVE_METHODS_TO_BE_EXECUTED = new ConcurrentHashMap<>();

    private PlayersDatabaseBridge() {
    }

    public static void saveTextureValue(KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdomPlayer.getDatabaseBridge(), databaseBridge -> databaseBridge.updateObject("players",
                createFilter("uuid", kingdomPlayer),
                new Pair<>("last_used_skin", kingdomPlayer.getTextureValue())
        ));
    }

    public static void savePlayerName(KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdomPlayer.getDatabaseBridge(), databaseBridge -> databaseBridge.updateObject("players",
                createFilter("uuid", kingdomPlayer),
                new Pair<>("last_used_name", kingdomPlayer.getName())
        ));
    }

    public static void saveClaimBonus(KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdomPlayer.getDatabaseBridge(), databaseBridge -> databaseBridge.updateObject("players",
                createFilter("uuid", kingdomPlayer),
                new Pair<>("claim_bonus", kingdomPlayer.getClaims())
        ));
    }

    public static void saveMemberBonus(KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdomPlayer.getDatabaseBridge(), databaseBridge -> databaseBridge.updateObject("players",
                createFilter("uuid", kingdomPlayer),
                new Pair<>("member_bonus", kingdomPlayer.getMembers())
        ));
    }

    public static void saveLastOnlineStatus(KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdomPlayer.getDatabaseBridge(), databaseBridge -> databaseBridge.updateObject("players",
                createFilter("uuid", kingdomPlayer),
                new Pair<>("last_online_status", kingdomPlayer.getLastOnlineStatus())
        ));
    }

    public static void insertPlayer(@NotNull KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdomPlayer.getDatabaseBridge(), databaseBridge -> {
            databaseBridge.insertObject("players",
                    new Pair<>("uuid", kingdomPlayer.getUniqueId().toString()),
                    new Pair<>("last_used_name", kingdomPlayer.getName()),
                    new Pair<>("last_used_skin", kingdomPlayer.getTextureValue()),
                    new Pair<>("member_bonus", kingdomPlayer.getMembers()),
                    new Pair<>("claim_bonus", kingdomPlayer.getClaims()),
                    new Pair<>("last_online_status", kingdomPlayer.getLastOnlineStatus()));
        });
    }

    public static void updatePlayer(@NotNull KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdomPlayer.getDatabaseBridge(), databaseBridge -> {
            databaseBridge.updateObject("players",
                    createFilter("uuid", kingdomPlayer),
                    new Pair<>("last_used_name", kingdomPlayer.getName()),
                    new Pair<>("last_used_skin", kingdomPlayer.getTextureValue())
            );
        });
    }

    public static void deletePlayer(KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdomPlayer.getDatabaseBridge(), databaseBridge -> {
            DatabaseFilter playerFilter = createFilter("player", kingdomPlayer);
            databaseBridge.deleteObject("players", createFilter("uuid", kingdomPlayer));
            databaseBridge.deleteObject("players_custom_data", playerFilter);
        });
    }

    public static boolean isModified(@NotNull KingdomPlayer kingdomPlayer) {
        return SAVE_METHODS_TO_BE_EXECUTED.containsKey(kingdomPlayer.getUniqueId());
    }

    public static void executeFutureSaves(@NotNull KingdomPlayer prisonPlayer) {
        Map<FutureSave, Set<Object>> futureSaves = SAVE_METHODS_TO_BE_EXECUTED.remove(prisonPlayer.getUniqueId());
        if (futureSaves != null) {
            for (Map.Entry<FutureSave, Set<Object>> futureSaveEntry : futureSaves.entrySet()) {
                switch (futureSaveEntry.getKey()) {
                    /*
                    case PERSISTENT_DATA: {
                        if (prisonPlayer.isPersistentDataContainerEmpty())
                            removePersistentDataContainer(prisonPlayer);
                        else
                            savePersistentDataContainer(prisonPlayer);
                        break;
                    }

                     */
                }
            }
        }
    }

    public static void executeFutureSaves(KingdomPlayer prisonPlayer, FutureSave futureSave) {
        Map<FutureSave, Set<Object>> futureSaves = SAVE_METHODS_TO_BE_EXECUTED.get(prisonPlayer.getUniqueId());

        if (futureSaves == null)
            return;

        Set<Object> values = futureSaves.remove(futureSave);

        if (values == null)
            return;

        if (futureSaves.isEmpty())
            SAVE_METHODS_TO_BE_EXECUTED.remove(prisonPlayer.getUniqueId());
    }

    private static DatabaseFilter createFilter(String id, KingdomPlayer kingdomPlayer, Pair<String, Object>... others) {
        List<Pair<String, Object>> filters = new LinkedList<>();

        filters.add(new Pair<>(id, kingdomPlayer.getUniqueId().toString()));
        if (others != null) {
            filters.addAll(Arrays.asList(others));
        }

        return DatabaseFilter.fromFilters(filters);
    }

    private static void runOperationIfRunning(DatabaseBridge databaseBridge, Consumer<DatabaseBridge> databaseBridgeConsumer) {
        if (databaseBridge.getDatabaseBridgeMode() == DatabaseBridgeMode.SAVE_DATA)
            databaseBridgeConsumer.accept(databaseBridge);
    }

    public enum FutureSave {

        PERSISTENT_DATA

    }

}
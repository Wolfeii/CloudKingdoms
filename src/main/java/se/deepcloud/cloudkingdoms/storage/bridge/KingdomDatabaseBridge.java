package se.deepcloud.cloudkingdoms.storage.bridge;

import lombok.NonNull;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.kingdom.claim.ClaimPosition;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivilege;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRole;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.storage.filter.DatabaseFilter;
import se.deepcloud.cloudkingdoms.utilities.sorting.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class KingdomDatabaseBridge {

    private static final Map<UUID, Map<FutureSave, Set<Object>>> SAVE_METHODS_TO_BE_EXECUTED = new ConcurrentHashMap<>();

    private KingdomDatabaseBridge() {
    }

    public static void addMember(@NonNull Kingdom kingdom, @NotNull KingdomPlayer kingdomPlayer, long addTime) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> databaseBridge.insertObject("kingdom_members",
                new Pair<>("island", kingdom.getUniqueId().toString()),
                new Pair<>("player", kingdom.getUniqueId().toString()),
                new Pair<>("role", kingdomPlayer.getKingdomRole().getName()),
                new Pair<>("join_time", addTime)
        ));
    }

    public static void saveMemberRole(@NonNull Kingdom kingdom, @NotNull KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> databaseBridge.updateObject("kingdom_members",
                createFilter("kingdom", kingdom, new Pair<>("player", kingdomPlayer.getUniqueId().toString())),
                new Pair<>("role", kingdomPlayer.getKingdomRole().getName())
        ));
    }

    public static void addClaim(@NotNull Kingdom kingdom, @NotNull ClaimPosition claimPosition) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> databaseBridge.insertObject("kingdom_claims",
                new Pair<>("uuid", kingdom.getUniqueId().toString()),
                new Pair<>("claimed_by", claimPosition.getClaimedBy() == null ? null : claimPosition.getClaimedBy().toString()),
                new Pair<>("world_name", claimPosition.getWorld().getName()),
                new Pair<>("chunk_x_pos", claimPosition.getX()),
                new Pair<>("chunk_z_pos", claimPosition.getZ()),
                new Pair<>("claimed_on", claimPosition.getClaimedOn())
        ));
    }

    public static void addBannedPlayer(@NonNull Kingdom kingdom, @NotNull KingdomPlayer kingdomPlayer, UUID banner, long banTime) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> databaseBridge.insertObject("kingdom_bans",
                new Pair<>("kingdom", kingdom.getUniqueId().toString()),
                new Pair<>("player", kingdomPlayer.getUniqueId().toString()),
                new Pair<>("banned_by", banner.toString()),
                new Pair<>("banned_time", banTime)
        ));
    }

    public static void removeBannedPlayer(@NonNull Kingdom kingdom, @NotNull KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> databaseBridge.deleteObject("kingdom_bans",
                createFilter("kingdom", kingdom, new Pair<>("player", kingdomPlayer.getUniqueId().toString()))
        ));
    }

    public static void removeMember(@NotNull Kingdom kingdom, @NotNull KingdomPlayer kingdomPlayer) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> databaseBridge.deleteObject("kingdom_members",
                createFilter("kingdom", kingdom, new Pair<>("player", kingdomPlayer.getUniqueId().toString()))
        ));
    }

    public static void saveRolePermission(@NotNull Kingdom kingdom, @NotNull KingdomRole kingdomRole, @NotNull KingdomPrivilege privilege, @NotNull KingdomPrivilege.Status status) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> databaseBridge.insertObject("kingdom_permissions",
                new Pair<>("kingdom", kingdom.getUniqueId().toString()),
                new Pair<>("role", kingdom.getName()),
                new Pair<>("permission", privilege.getName()),
                new Pair<>("status", status.name())
        ));
    }

    public static void clearRolePermissions(@NotNull Kingdom kingdom) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> databaseBridge.deleteObject("kingdom_permissions",
                createFilter("kingdom", kingdom)));
    }

    public static void saveName(@NotNull Kingdom kingdom) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> databaseBridge.updateObject("kingdoms",
                createFilter("uuid", kingdom),
                new Pair<>("name", kingdom.getName())
        ));
    }

    public static void saveKingdomLeader(@NotNull Kingdom kingdom) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> databaseBridge.updateObject("kingdoms",
                createFilter("uuid", kingdom),
                new Pair<>("owner", kingdom.getOwner().getUniqueId().toString())
        ));
    }

    public static void insertKingdom(@NotNull Kingdom kingdom) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> {
            databaseBridge.insertObject("kingdoms",
                    new Pair<>("uuid", kingdom.getUniqueId().toString()),
                    new Pair<>("owner", kingdom.getOwner().getUniqueId().toString()),
                    new Pair<>("creation_time", kingdom.getCreationTime()),
                    new Pair<>("name", kingdom.getName()),
                    new Pair<>("level", kingdom.getLevel())
            );
        });
    }

    public static void deleteKingdom(@NotNull Kingdom kingdom) {
        runOperationIfRunning(kingdom.getDatabaseBridge(), databaseBridge -> {
            DatabaseFilter kingdomFilter = createFilter("kingdom", kingdom);
            databaseBridge.deleteObject("kingdoms", createFilter("uuid", kingdom));
            databaseBridge.deleteObject("kingdoms_banks", kingdomFilter);
            databaseBridge.deleteObject("kingdoms_bans", kingdomFilter);
            databaseBridge.deleteObject("kingdoms_custom_data", kingdomFilter);
            databaseBridge.deleteObject("kingdoms_members", kingdomFilter);
            databaseBridge.deleteObject("kingdoms_permissions", kingdomFilter);
            databaseBridge.deleteObject("kingdoms_upgrades", kingdomFilter);
        });
    }

    private static DatabaseFilter createFilter(String id, @NotNull Kingdom kingdom, Pair<String, Object>... others) {
        List<Pair<String, Object>> filters = new LinkedList<>();
        filters.add(new Pair<>(id, kingdom.getUniqueId().toString()));
        if (others != null)
            filters.addAll(Arrays.asList(others));
        return DatabaseFilter.fromFilters(filters);
    }

    private static void runOperationIfRunning(DatabaseBridge databaseBridge, Consumer<DatabaseBridge> databaseBridgeConsumer) {
        if (databaseBridge.getDatabaseBridgeMode() == DatabaseBridgeMode.SAVE_DATA)
            databaseBridgeConsumer.accept(databaseBridge);
    }

    public enum FutureSave {

        BLOCK_COUNTS,
        ISLAND_CHESTS,
        PERSISTENT_DATA

    }

}

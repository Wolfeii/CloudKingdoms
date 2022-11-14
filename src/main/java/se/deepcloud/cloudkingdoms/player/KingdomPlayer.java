package se.deepcloud.cloudkingdoms.player;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivilege;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRole;
import se.deepcloud.cloudkingdoms.menus.KingdomMenu;
import se.deepcloud.cloudkingdoms.player.builder.KingdomPlayerBuilder;
import se.deepcloud.cloudkingdoms.scheduler.BukkitExecutor;
import se.deepcloud.cloudkingdoms.storage.bridge.DatabaseBridge;
import se.deepcloud.cloudkingdoms.storage.bridge.DatabaseBridgeMode;
import se.deepcloud.cloudkingdoms.storage.bridge.PlayersDatabaseBridge;
import se.deepcloud.cloudkingdoms.storage.sql.SQLDatabaseBridge;
import se.deepcloud.cloudkingdoms.utilities.formatting.Formatters;
import se.deepcloud.cloudkingdoms.utilities.logging.Debug;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class KingdomPlayer {

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    private final DatabaseBridge databaseBridge;
    private final List<UUID> pendingInvites = new LinkedList<>();

    private final UUID uuid;

    private KingdomRole kingdomRole;
    private Kingdom playerKingdom = null;
    private String textureValue;
    private String name;

    private boolean bypassModeEnabled = false;
    private long lastOnlineStatus;
    private int membersBonus;
    private int claimsBonus;

    private BukkitTask teleportTask = null;

    public KingdomPlayer(@NotNull KingdomPlayerBuilder playerBuilder) {
        this.uuid = playerBuilder.uuid;
        this.kingdomRole = playerBuilder.playerRole;
        this.name = playerBuilder.name;
        this.textureValue = playerBuilder.textureValue;
        this.databaseBridge = new SQLDatabaseBridge();

        databaseBridge.setDatabaseBridgeMode(DatabaseBridgeMode.SAVE_DATA);
    }

    public @NotNull UUID getUniqueId() {
        return uuid;
    }

    public @NotNull String getName() {
        return name;
    }

    public void updateLastOnlineStatus() {
        setLastOnlineStatus(System.currentTimeMillis() / 1000);
    }

    public void setLastOnlineStatus(long lastOnline) {
        Log.debug(Debug.SET_PLAYER_LAST_TIME_UPDATED, "KingdomPlayer", "setLastTimeStatus", getName(), lastOnline);

        this.lastOnlineStatus = lastOnline;

        PlayersDatabaseBridge.saveLastOnlineStatus(this);
    }

    public long getLastOnlineStatus() {
        return this.lastOnlineStatus;
    }

    public void updateName() {
        Player player = asPlayer();
        if (player != null) {
            setName(player.getName());
        }
    }

    public void setName(@NotNull String name) {
        if (!this.name.equals(name)) {
            try {
                plugin.getPlayerManager().removePlayer(this);
                this.name = name;
                PlayersDatabaseBridge.savePlayerName(this);
            } finally {
                plugin.getPlayerManager().addPlayer(this);
            }
        }
    }

    public boolean isOnline() {
        return asPlayer() != null;
    }

    public @Nullable Player asPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public @NotNull OfflinePlayer asOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public void runIfOnline(@NotNull Consumer<Player> toRun) {
        Player player = asPlayer();
        if (player != null) {
            toRun.accept(player);
        }
    }

    public @Nullable World getWorld() {
        Location location = getLocation();
        return location == null ? null : location.getWorld();
    }

    public @Nullable Location getLocation() {
        Player player = asPlayer();
        return player == null ? null : player.getLocation();
    }


    public void teleport(@NotNull Location location) {
        Player player = asPlayer();
        if (player != null) {
            player.teleport(location);
        }
    }

    public void teleport(@NotNull Kingdom kingdom) {
        Location homeLocation = kingdom.getHomeLocation();
        if (homeLocation != null) {
            teleport(homeLocation);
        }
    }

    public KingdomPlayer getKingdomOwner() {
        Kingdom kingdom = getKingdom();
        return kingdom == null ? this : kingdom.getOwner();
    }

    public void setKingdom(@Nullable Kingdom kingdom) {
        Log.debug(Debug.SET_PLAYER_KINGDOM, "KingdomPlayer", "setKingdom", getName(),
                kingdom == null ? "null" : kingdom.getName());

        this.playerKingdom = kingdom;
    }

    public boolean hasPermission(@NotNull KingdomPrivilege privilege) {
        Kingdom kingdom = getKingdom();
        return kingdom != null && kingdom.hasPermission(this, privilege);
    }

    public boolean hasPermission(@NotNull String permission) {
        Player player = asPlayer();
        return permission.isEmpty() || (player != null && player.hasPermission(permission));
    }

    public boolean hasPermissionWithoutOP(@NotNull String permission) {
        return hasPermission(permission);
    }

    public void setKingdomRole(KingdomRole kingdomRole) {
        this.kingdomRole = kingdomRole;
    }

    public KingdomRole getKingdomRole() {
        return kingdomRole;
    }

    public boolean hasKingdom() {
        return getKingdom() != null;
    }

    public @Nullable Kingdom getKingdom() {
        return playerKingdom;
    }

    public void addInvite(@NotNull Kingdom kingdom) {
        this.pendingInvites.add(kingdom.getUniqueId());
    }

    public void removeInvite(@NotNull Kingdom kingdom) {
        this.pendingInvites.remove(kingdom.getUniqueId());
    }

    public @NotNull List<Kingdom> getInvites() {
        return new ArrayList<>(pendingInvites).stream()
                .map(uuid -> plugin.getKingdomManager().getKingdomByUUID(uuid))
                .toList();
    }

    public void toggleBypassMode() {
        setBypassMode(!bypassModeEnabled);
    }

    public boolean hasBypassModeEnabled() {
        return bypassModeEnabled;
    }

    public void setBypassMode(boolean enabled) {
        Log.debug(Debug.SET_ADMIN_BYPASS, "KingdomPlayer", "setBypassMode", getName(), enabled);
        bypassModeEnabled = enabled;
    }

    public @Nullable BukkitTask getTeleportTask() {
        return teleportTask;
    }

    public void setTeleportTask(@Nullable BukkitTask bukkitTask) {
        if (this.teleportTask != null)
            this.teleportTask.cancel();
        this.teleportTask = bukkitTask;
    }

    public int getClaims() {
        return claimsBonus;
    }

    public void setClaims(int claims) {
        Log.debug(Debug.SET_CLAIMS_BONUS, "KingdomPlayer", "setClaims", getName(), claims);

        this.claimsBonus = claims;

        PlayersDatabaseBridge.saveClaimBonus(this);
    }

    public int getMembers() {
        return membersBonus;
    }

    public void setMembers(int members) {
        Log.debug(Debug.SET_MEMBER_BONUS, "KingdomPlayer", "setMembers", getName(), members);

        this.membersBonus = members;

        PlayersDatabaseBridge.saveMemberBonus(this);
    }

    public void merge(@NotNull KingdomPlayer otherPlayer) {
        this.name = otherPlayer.getName();
        this.playerKingdom = otherPlayer.getKingdom();
        this.kingdomRole = otherPlayer.getKingdomRole();
        this.bypassModeEnabled |= otherPlayer.hasBypassModeEnabled();
        this.claimsBonus |= otherPlayer.getClaims();
        this.membersBonus |= otherPlayer.getMembers();
        this.lastOnlineStatus = otherPlayer.getLastOnlineStatus();

        PlayersDatabaseBridge.updatePlayer(this);
        PlayersDatabaseBridge.deletePlayer(otherPlayer);
    }

    public void openInventory(@NotNull KingdomMenu kingdomMenu) {
        Player player = asPlayer();
        if (player != null) {
            BukkitExecutor.sync(() -> {
                player.openInventory(kingdomMenu.getInventory());
            });
        }
    }

    public void closeInventory() {
        Player player = asPlayer();
        if (player != null) {
            player.closeInventory();
        }
    }

    public void sendTitle(@Nullable String title, @Nullable String subTitle) {
        sendTitle(title, subTitle, 10, 20, 10);
    }

    public void sendTitle(@Nullable String title, @Nullable String subTitle, int fadeIn, int duration, int fadeOut) {
        Player player = asPlayer();
        if (player != null) {
            player.sendTitle(title == null ? "" : Formatters.COLOR_FORMATTER.format(title),
                    subTitle == null ? "" : Formatters.COLOR_FORMATTER.format(subTitle),
                    fadeIn, duration, fadeOut);
        }
    }

    public DatabaseBridge getDatabaseBridge() {
        return databaseBridge;
    }

    public String getTextureValue() {
        return textureValue;
    }

    public static KingdomPlayerBuilder newBuilder() {
        return new KingdomPlayerBuilder();
    }
}


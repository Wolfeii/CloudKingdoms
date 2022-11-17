package se.deepcloud.cloudkingdoms.kingdom;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.builder.KingdomBuilder;
import se.deepcloud.cloudkingdoms.kingdom.claim.ClaimPosition;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivilege;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivileges;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRole;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRoles;
import se.deepcloud.cloudkingdoms.message.Message;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.scheduler.BukkitExecutor;
import se.deepcloud.cloudkingdoms.scheduler.Synchronized;
import se.deepcloud.cloudkingdoms.storage.bridge.DatabaseBridge;
import se.deepcloud.cloudkingdoms.storage.bridge.DatabaseBridgeMode;
import se.deepcloud.cloudkingdoms.storage.bridge.KingdomDatabaseBridge;
import se.deepcloud.cloudkingdoms.storage.sql.SQLDatabaseBridge;
import se.deepcloud.cloudkingdoms.utilities.formatting.Formatters;
import se.deepcloud.cloudkingdoms.utilities.logging.Debug;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;
import se.deepcloud.cloudkingdoms.utilities.sorting.SequentialListBuilder;
import se.deepcloud.cloudkingdoms.utilities.sorting.SortingComparators;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Kingdom {

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    private final DatabaseBridge databaseBridge;

    private final UUID uuid;
    private final long creationTime;

    private final Synchronized<SortedSet<KingdomPlayer>> members = Synchronized.of(new TreeSet<>(SortingComparators.PLAYER_NAMES_COMPARATOR));
    private final Synchronized<SortedSet<KingdomPlayer>> playersInside = Synchronized.of(new TreeSet<>(SortingComparators.PLAYER_NAMES_COMPARATOR));
    private final Map<KingdomRole, Map<KingdomPrivilege, KingdomPrivilege.Status>> rolePermissions = new ConcurrentHashMap<>();
    private final Set<KingdomPlayer> bannedPlayers = Sets.newConcurrentHashSet();
    private final Set<KingdomPlayer> coopPlayers = Sets.newConcurrentHashSet();
    private final Set<KingdomPlayer> invitedPlayers = Sets.newConcurrentHashSet();
    private final Set<ClaimPosition> claims = Sets.newConcurrentHashSet();

    private KingdomPlayer owner;
    private String creationTimeDate;

    private volatile int level;
    private volatile String kingdomName;
    private volatile String kingdomRawName;
    private Location homeLocation;

    public Kingdom(@NotNull KingdomBuilder kingdomBuilder) {
        this.uuid = kingdomBuilder.uuid;
        this.owner = kingdomBuilder.owner;

        if (this.owner != null) {
            this.owner.setKingdomRole(KingdomRoles.LEADER);
            this.owner.setKingdom(this);
        }

        this.creationTime = kingdomBuilder.creationTime;
        this.kingdomName = kingdomBuilder.kingdomName;
        this.kingdomRawName = Formatters.STRIP_COLOR_FORMATTER.format(kingdomName);
        this.homeLocation = kingdomBuilder.homeLocation;
        this.members.write(members -> {
            members.addAll(kingdomBuilder.members);
            members.forEach(member -> member.setKingdom(this));
        });
        this.claims.addAll(kingdomBuilder.claims);
        this.bannedPlayers.addAll(kingdomBuilder.members);
        this.rolePermissions.putAll(kingdomBuilder.rolePermissions);

        this.databaseBridge = new SQLDatabaseBridge();

        this.databaseBridge.setDatabaseBridgeMode(DatabaseBridgeMode.SAVE_DATA);
    }

    public @NotNull KingdomPlayer getOwner() {
        return owner;
    }

    public @NotNull UUID getUniqueId() {
        return uuid;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String getCreationTimeDate() {
        return creationTimeDate;
    }

    public void updateDatesFormatter() {
        this.creationTimeDate = Formatters.DATE_FORMATTER.format(new Date(creationTime * 1000));
    }

    public @NotNull List<KingdomPlayer> getKingdomMembers(boolean includeOwner) {
        List<KingdomPlayer> members = this.members.readAndGet(_members -> new SequentialListBuilder<KingdomPlayer>()
                .mutable()
                .build(_members));

        if (includeOwner)
            members.add(owner);

        return Collections.unmodifiableList(members);
    }

    public int getKingdomMemberSize() {
        return getKingdomMembers(true).size();
    }

    public @NotNull List<KingdomPlayer> getBannedPlayers() {
        return new SequentialListBuilder<KingdomPlayer>().build(this.bannedPlayers);
    }

    public List<KingdomPlayer> getAllPlayersInside() {
        return playersInside.readAndGet(playersInside -> new SequentialListBuilder<KingdomPlayer>()
                .filter(KingdomPlayer::isOnline)
                .build(playersInside));
    }

    public void inviteMember(@NotNull KingdomPlayer kingdomPlayer) {
        Log.debug(Debug.INVITE_MEMBER, "Kingdom", "inviteMember", owner.getName(), kingdomPlayer.getName());

        invitedPlayers.add(kingdomPlayer);
        kingdomPlayer.addInvite(this);

        // Ta bort invite efter fem minuter.
        BukkitExecutor.sync(() -> revokeInvite(kingdomPlayer), 6000L);
    }

    public void revokeInvite(@NotNull KingdomPlayer kingdomPlayer) {
        Log.debug(Debug.REVOKE_INVITE, "Kingdom", "revokeInvite", owner.getName(), kingdomPlayer.getName());

        invitedPlayers.remove(kingdomPlayer);
        kingdomPlayer.removeInvite(this);
    }

    public boolean isInvited(@NotNull KingdomPlayer kingdomPlayer) {
        return invitedPlayers.contains(kingdomPlayer);
    }

    public int getLevel() {
        return level;
    }

    public List<KingdomPlayer> getInvitedPlayers() {
        return new SequentialListBuilder<KingdomPlayer>().build(this.invitedPlayers);
    }

    public void replacePlayers(@NotNull KingdomPlayer originalPlayer, @NotNull KingdomPlayer newPlayer) {
        if (owner == originalPlayer) {
            owner = newPlayer;
            //TODO: IslandsDatabaseBridge.saveIslandLeader(this);
        } else if (isMember(originalPlayer)) {
            members.write(members -> {
                members.remove(originalPlayer);
                members.add(newPlayer);
            });

            KingdomDatabaseBridge.removeMember(this, originalPlayer);
            KingdomDatabaseBridge.addMember(this, newPlayer, System.currentTimeMillis());
        }

        replaceBannedPlayer(originalPlayer, newPlayer);
    }

    private void replaceBannedPlayer(KingdomPlayer originalPlayer, KingdomPlayer newPlayer) {
        if (bannedPlayers.remove(originalPlayer)) {
            bannedPlayers.add(newPlayer);
        }
    }


    public boolean isInside(@NotNull Location location) {
        return claims.contains(location.getChunk());
    }

    public void addMember(@NotNull KingdomPlayer kingdomPlayer, KingdomRole playerRole) {
        Log.debug(Debug.ADD_MEMBER, "Kingdom", "addMember", owner.getName(), kingdomPlayer.getName(), playerRole);

        boolean addedNewMember = members.writeAndGet(members -> members.add(kingdomPlayer));

        // This player is already an member of the kingdom
        if (!addedNewMember)
            return;

        // Removing player from being a coop.
        if (isCoop(kingdomPlayer)) {
            removeCoop(kingdomPlayer);
        }

        kingdomPlayer.setKingdom(this);

        // TODO: Update Member GUIs

        // TODO: Update Database
    }

    public boolean isMember(@NotNull KingdomPlayer kingdomPlayer) {
        return owner.equals(kingdomPlayer.getKingdomOwner());
    }

    public boolean isBanned(@NotNull KingdomPlayer kingdomPlayer) {
        return bannedPlayers.contains(kingdomPlayer);
    }

    public void addCoop(@NotNull KingdomPlayer kingdomPlayer) {

    }

    public void removeCoop(@NotNull KingdomPlayer kingdomPlayer) {

    }

    public boolean isCoop(@NotNull KingdomPlayer kingdomPlayer) {
        return coopPlayers.contains(kingdomPlayer);
    }

    public List<KingdomPlayer> getCoopPlayers() {
        return new SequentialListBuilder<KingdomPlayer>().build(this.coopPlayers);
    }

    public Set<ClaimPosition> getClaims() {
        return claims;
    }

    public int getClaimsSize() {
        return claims.size();
    }

    public boolean hasMaxClaims() {
        return getClaimsSize() >= getClaimBonus() + 5;
    }

    public void claimChunk(@NotNull KingdomPlayer kingdomPlayer, @NotNull Chunk chunk) {
        if (plugin.getKingdomManager().isChunkClaimed(chunk))
            return;

        ClaimPosition claimPosition = plugin.getKingdomManager().setChunkClaimed(chunk, this, kingdomPlayer.getUniqueId());
        claims.add(claimPosition);

        KingdomDatabaseBridge.addClaim(this, claimPosition);
    }

    public @Nullable Location getHomeLocation() {
        return homeLocation;
    }

    public void setKingdomHome(@Nullable Location homeLocation) {
        Log.debug(Debug.SET_KINGDOM_HOME, "Kingdom", "setKingdomHome", owner.getName(), homeLocation);

        this.homeLocation = homeLocation;

        // TODO: Update Database
    }

    public String getName() {
        return kingdomRawName;
    }

    public void setName(@NotNull String kingdomName) {
        Log.debug(Debug.SET_NAME, "Kingdom", "setName", owner.getName(), kingdomName);

        this.kingdomName = kingdomName;
        this.kingdomRawName = ChatColor.stripColor(kingdomName);

        KingdomDatabaseBridge.saveName(this);
    }

    public int getMemberBonus() {
        AtomicInteger memberBonus = new AtomicInteger();
        forEachKingdomMember(Collections.emptyList(), false, member -> {
            memberBonus.addAndGet(member.getMembers());
        });

        return memberBonus.get();
    }

    public int getClaimBonus() {
        AtomicInteger claimBonus = new AtomicInteger();
        forEachKingdomMember(Collections.emptyList(), false, member -> {
            claimBonus.addAndGet(member.getClaims());
        });

        return claimBonus.get();
    }

    public boolean hasPermission(CommandSender sender, KingdomPrivilege kingdomPrivilege) {
        return sender instanceof ConsoleCommandSender || hasPermission(plugin.getPlayerManager().getKingdomPlayer(sender), kingdomPrivilege);
    }

    public boolean hasPermission(KingdomPlayer kingdomPlayer, KingdomPrivilege kingdomPrivilege) {
        return kingdomPlayer.hasBypassModeEnabled() || kingdomPlayer.hasPermissionWithoutOP("kingdoms.admin.bypass.*") ||
                kingdomPlayer.hasPermissionWithoutOP("kingdoms.admin.bypass." + kingdomPrivilege.getName()) ||
                hasPermission(kingdomPlayer.getKingdomRole(), kingdomPrivilege);
    }

    public boolean hasPermission(KingdomRole playerRole, KingdomPrivilege kingdomPrivilege) {
        KingdomPrivilege.Status status = rolePermissions.get(playerRole).get(kingdomPrivilege);
        return status.equals(KingdomPrivilege.Status.ALLOW) || (status.equals(KingdomPrivilege.Status.UNSET) &&
                playerRole.getDefaultPermissions().contains(kingdomPrivilege));
    }

    public void setPermission(KingdomRole playerRole, KingdomPrivilege kingdomPrivilege, KingdomPrivilege.Status status) {
        Log.debug(Debug.SET_PERMISSION, "SIsland", "setPermission", owner.getName(), playerRole, kingdomPrivilege);

        rolePermissions.get(playerRole).put(kingdomPrivilege, status);

        // TODO: Update Island fly if it is necessary

        // TODO: Save to database
    }

    public KingdomRole getRequiredPlayerRole(KingdomPrivilege kingdomPrivilege) {
        return KingdomRole.values().stream()
                .filter(_playerRole -> hasPermission(_playerRole, kingdomPrivilege))
                .min(Comparator.comparingInt(KingdomRole::getWeight)).orElse(KingdomRoles.LEADER);
    }

    public void sendMessage(@NotNull String message, @NotNull UUID... ignoredMembers) {
        List<UUID> ignoredList = ignoredMembers.length == 0 ? Collections.emptyList() : Arrays.asList(ignoredMembers);

        Log.debug(Debug.SEND_MESSAGE, "Kingdom", "sendMessage", owner.getName(), message, ignoredList);

        forEachKingdomMember(ignoredList, false, kingdomPlayer -> Message.CUSTOM.send(kingdomPlayer, message, false));
    }

    public void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int duration,
                          int fadeOut, @NotNull UUID... ignoredMembers) {
        List<UUID> ignoredList = ignoredMembers.length == 0 ? Collections.emptyList() : Arrays.asList(ignoredMembers);

        Log.debug(Debug.SEND_TITLE, "Kingdom", "sendTitle", owner.getName(),
                title, subtitle, fadeIn, duration, fadeOut, ignoredList);

        forEachKingdomMember(ignoredList, true, kingdomPlayer ->
                kingdomPlayer.asPlayer().sendTitle(title, subtitle, fadeIn, duration, fadeOut)
        );
    }

    public void executeCommand(@NotNull String command, boolean onlyOnlineMembers, @NotNull UUID... ignoredMembers) {
        List<UUID> ignoredList = ignoredMembers.length == 0 ? Collections.emptyList() : Arrays.asList(ignoredMembers);

        Log.debug(Debug.EXECUTE_KINGDOM_COMMANDS, "Kingdom", "executeCommand", owner.getName(), command, onlyOnlineMembers, ignoredList);

        forEachKingdomMember(ignoredList, true, kingdomPlayer ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player-name}", kingdomPlayer.getName()))
        );
    }


    private void forEachKingdomMember(List<UUID> ignoredMembers, boolean onlyOnline, Consumer<KingdomPlayer> kingdomPlayerConsumer) {
        for (KingdomPlayer kingdomMember : getKingdomMembers(true)) {
            if (!ignoredMembers.contains(kingdomMember.getUniqueId()) && (!onlyOnline || kingdomMember.isOnline())) {
                kingdomPlayerConsumer.accept(kingdomMember);
            }
        }
    }

    public DatabaseBridge getDatabaseBridge() {
        return databaseBridge;
    }

    public static KingdomBuilder newBuilder() {
        return new KingdomBuilder();
    }
}

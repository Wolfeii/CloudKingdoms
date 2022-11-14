package se.deepcloud.cloudkingdoms.kingdom.builder;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.kingdom.name.KingdomName;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivilege;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRole;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

import java.math.BigDecimal;
import java.util.*;

public class KingdomBuilder {

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    @Nullable
    public KingdomPlayer owner;
    public UUID uuid = null;
    public String kingdomName = "";
    public Location homeLocation = null;
    public long creationTime = System.currentTimeMillis() / 1000;
    public final List<KingdomPlayer> members = new LinkedList<>();
    public final List<KingdomPlayer> bannedPlayers = new LinkedList<>();
    public final Map<KingdomRole, Map<KingdomPrivilege, KingdomPrivilege.Status>> rolePermissions = new LinkedHashMap<>();
    public BigDecimal balance = BigDecimal.ZERO;

    public KingdomBuilder() {

    }

    public KingdomBuilder setOwner(@Nullable KingdomPlayer owner) {
        this.owner = owner;
        return this;
    }

    @Nullable
    public KingdomPlayer getOwner() {
        return owner;
    }

    public KingdomBuilder setUniqueId(@NotNull UUID uuid) {
        Preconditions.checkState(plugin.getKingdomManager().getKingdomByUUID(uuid) == null, "Detta UUID är inte unikt.");
        this.uuid = uuid;
        return this;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public KingdomBuilder setKingdomName(@NotNull String kingdomName) {
        Preconditions.checkState(plugin.getKingdomManager().getKingdom(kingdomName) == null, "Detta namn är inte unikt.");
        this.kingdomName = kingdomName;
        return this;
    }

    public String getKingdomName() {
        return kingdomName;
    }

    public KingdomBuilder setHomeLocation(@NotNull Location homeLocation) {
        this.homeLocation = homeLocation;
        return this;
    }

    @Nullable
    public Location getHomeLocation() {
        return homeLocation;
    }

    public KingdomBuilder setCreationTime(long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public KingdomBuilder addKingdomMember(@NotNull KingdomPlayer member) {
        this.members.add(member);
        return this;
    }

    public Kingdom build() {
        if (this.uuid == null) {
            throw new IllegalStateException("Kan inte skapa Kingdom med ogiltligt UUID.");
        } else if (this.kingdomName == null) {
            throw new IllegalStateException("Kan inte skapa Kingdom med ogiltligt namn");
        }

        return new Kingdom(this);
    }
}

package se.deepcloud.cloudkingdoms.kingdom.claim;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;

import java.util.Objects;
import java.util.UUID;

public class ClaimPosition {

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    private final World world;
    private final int x;
    private final int z;

    private long claimedOn;
    private UUID claimedBy;
    private UUID owner;

    private ClaimPosition(@NotNull Chunk chunk) {
        this(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    public ClaimPosition(@NotNull World world, int x, int z) {
        this(null, null, world, x, z, System.currentTimeMillis() / 1000);
    }

    public ClaimPosition(@Nullable UUID owner, @Nullable UUID claimedBy, @NotNull World world, int x, int z, long claimedOn) {
        this.owner = owner;
        this.world = world;
        this.x = x;
        this.z = z;
        this.claimedOn = claimedOn;
    }

    public @NotNull World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public @Nullable Kingdom getOwner() {
        return owner == null ? null : plugin.getKingdomManager().getKingdomByUUID(owner);
    }

    public long getClaimedOn() {
        return claimedOn;
    }

    public void setOwner(@Nullable Kingdom owner) {
        this.owner = owner == null ? null : owner.getUniqueId();
    }

    public void setClaimedBy(UUID claimedBy) {
        this.claimedBy = claimedBy;
    }

    public void updateClaimedTime() {
        setClaimedOn(System.currentTimeMillis() / 1000);
    }

    public void setClaimedOn(long claimedOn) {
        this.claimedOn = claimedOn;
    }

    public UUID getClaimedBy() {
        return claimedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaimPosition that = (ClaimPosition) o;
        return x == that.x && z == that.z && world.equals(that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }

    public Chunk getChunk() {
        return world.getChunkAt(x, z);
    }

    public boolean isClaimed() {
        return owner != null;
    }

    public static ClaimPosition of(@NotNull Chunk chunk) {
        return new ClaimPosition(chunk);
    }

    public static ClaimPositionBuilder newBuilder() {
        return new ClaimPositionBuilder();
    }

    public static class ClaimPositionBuilder {
        private UUID owner = null;
        private UUID claimedBy = null;
        private World world = null;
        private long claimedOn;
        private int x;
        private int z;

        public ClaimPositionBuilder setWorld(World world) {
            this.world = world;
            return this;
        }

        public ClaimPositionBuilder setClaimedOn(long claimedOn) {
            this.claimedOn = claimedOn;
            return this;
        }

        public ClaimPositionBuilder setClaimedBy(UUID claimedBy) {
            this.claimedBy = claimedBy;
            return this;
        }

        public ClaimPositionBuilder setOwner(UUID owner) {
            this.owner = owner;
            return this;
        }

        public ClaimPositionBuilder setX(int x) {
            this.x = x;
            return this;
        }

        public ClaimPositionBuilder setZ(int z) {
            this.z = z;
            return this;
        }

        public ClaimPosition build() {
            if (world == null) {
                throw new IllegalArgumentException("Invalid World");
            }

            return new ClaimPosition(owner, claimedBy, world, x, z, claimedOn);
        }
    }
}

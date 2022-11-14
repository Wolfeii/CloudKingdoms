package se.deepcloud.cloudkingdoms.kingdom.chunk;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DataChunk {

    private final UUID kingdom;
    private final String worldName;
    private final int xCoordinate;
    private final int zCoordinate;

    public DataChunk(@NotNull UUID kingdom, @NotNull String worldName, int xCoordinate, int zCoordinate) {
        this.kingdom = kingdom;
        this.worldName = worldName;
        this.xCoordinate = xCoordinate;
        this.zCoordinate = zCoordinate;
    }

    public int getX() {
        return xCoordinate;
    }

    public int getZ() {
        return zCoordinate;
    }

    public String getWorldName() {
        return worldName;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public Chunk getChunk() {
        World world = getWorld();
        return world == null ? null : world.getChunkAt(xCoordinate, zCoordinate);
    }

    public boolean isEqual(@NotNull Chunk chunk) {
        return chunk.equals(getChunk());
    }
}

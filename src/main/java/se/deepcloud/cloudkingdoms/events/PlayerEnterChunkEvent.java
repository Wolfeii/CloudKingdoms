package se.deepcloud.cloudkingdoms.events;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.kingdom.claim.ClaimPosition;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

import java.util.Objects;
import java.util.UUID;

public class PlayerEnterChunkEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private boolean cancelled;

    public final KingdomPlayer player;
    public final Chunk previousChunk;
    public final Chunk nextChunk;
    public final ClaimPosition previousPosition;
    public final ClaimPosition nextPosition;

    // Handy
    public final boolean chunksHaveSameOwner;
    public final boolean isPlayerPreviousOwner;
    public final boolean isPlayerNextOwner;

    public PlayerEnterChunkEvent(
            @NotNull KingdomPlayer player,
            @NotNull Chunk previousChunk,
            @NotNull Chunk nextChunk,
            @NotNull ClaimPosition previousPosition,
            @NotNull ClaimPosition nextPosition) {
        // I'm sorry, but I have to be sure :|
        if (previousChunk.getX() == nextChunk.getX() && previousChunk.getZ() == nextChunk.getZ()) {
            throw new RuntimeException("previousChunk and nextChunk must be different!!!!");
        }

        // The needies
        this.player = Objects.requireNonNull(player);
        this.previousChunk = Objects.requireNonNull(previousChunk);
        this.nextChunk = Objects.requireNonNull(nextChunk);
        this.previousPosition = previousPosition;
        this.nextPosition = nextPosition;

        // Calculate the handies
        this.chunksHaveSameOwner = Objects.equals(previousPosition.getOwner(), nextPosition.getOwner());
        this.isPlayerPreviousOwner = Objects.equals(player.getKingdom(), previousPosition.getOwner());
        this.isPlayerNextOwner = Objects.equals(player.getKingdom(), nextPosition.getOwner());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public KingdomPlayer getPlayer() {
        return player;
    }

    public Chunk getNextChunk() {
        return nextChunk;
    }

    public Chunk getPreviousChunk() {
        return previousChunk;
    }

    public boolean doesChunksHaveSameOwner() {
        return chunksHaveSameOwner;
    }

    public boolean isPlayerNextOwner() {
        return isPlayerNextOwner;
    }

    public boolean isPlayerPreviousOwner() {
        return isPlayerPreviousOwner;
    }

    public ClaimPosition getNextPosition() {
        return nextPosition;
    }

    public ClaimPosition getPreviousPosition() {
        return previousPosition;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}

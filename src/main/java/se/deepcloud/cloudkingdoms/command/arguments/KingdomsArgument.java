package se.deepcloud.cloudkingdoms.command.arguments;

import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

public class KingdomsArgument extends Argument<Kingdom, KingdomPlayer> {

    public static final KingdomsArgument EMPTY = new KingdomsArgument(null, null);

    public KingdomsArgument(@Nullable Kingdom kingdom, KingdomPlayer kingdomPlayer) {
        super(kingdom, kingdomPlayer);
    }

    @Nullable
    public Kingdom getKingdom() {
        return super.k;
    }

    public KingdomPlayer getKingdomPlayer() {
        return super.v;
    }

}
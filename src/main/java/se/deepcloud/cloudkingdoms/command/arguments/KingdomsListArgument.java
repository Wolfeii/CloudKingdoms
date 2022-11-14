package se.deepcloud.cloudkingdoms.command.arguments;

import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

import java.util.List;

public class KingdomsListArgument extends Argument<List<Kingdom>, KingdomPlayer> {

    public KingdomsListArgument(@Nullable List<Kingdom> kingdom, KingdomPlayer kingdomPlayer) {
        super(kingdom, kingdomPlayer);
    }

    @Nullable
    public List<Kingdom> getKingdom() {
        return super.k;
    }

    @Nullable
    public KingdomPlayer getKingdomPlayer() {
        return super.v;
    }

}
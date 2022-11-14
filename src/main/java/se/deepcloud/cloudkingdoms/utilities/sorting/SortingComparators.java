package se.deepcloud.cloudkingdoms.utilities.sorting;

import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

import java.util.Comparator;

public class SortingComparators {

    public final static Comparator<KingdomPlayer> PLAYER_NAMES_COMPARATOR = Comparator.comparing(KingdomPlayer::getName);
}

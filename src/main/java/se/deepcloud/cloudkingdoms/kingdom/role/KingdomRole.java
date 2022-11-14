package se.deepcloud.cloudkingdoms.kingdom.role;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivilege;
import se.deepcloud.cloudkingdoms.utilities.sorting.Enumerable;

import java.util.*;

public class KingdomRole implements Enumerable {

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    private static final Map<String, KingdomRole> kingdomRoles = new HashMap<>();
    private static int ordinalCounter = 0;

    private final String name;
    private final String displayName;
    private final int weight;
    private final int ordinal;
    private final List<KingdomPrivilege> defaultPermissions;

    public KingdomRole(String name, @Nullable String displayName, int weight, boolean all) {
        this.name = name;
        this.displayName = displayName == null ? name : displayName;
        this.weight = weight;
        this.defaultPermissions = all ? new ArrayList<>(KingdomPrivilege.values()) : new ArrayList<>();
        this.ordinal = ordinalCounter++;
    }

    public KingdomRole(String name, @Nullable String displayName, int weight, KingdomPrivilege... privileges) {
        this.name = name;
        this.displayName = displayName == null ? name : displayName;
        this.weight = weight;
        this.defaultPermissions = Arrays.asList(privileges);
        this.ordinal = ordinalCounter++;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isHigherThan(KingdomRole role) {
        Preconditions.checkNotNull(role, "kingdomRole parameter cannot be null.");
        return getWeight() > role.getWeight();
    }

    public boolean isLessThan(KingdomRole role) {
        Preconditions.checkNotNull(role, "kingdomRole parameter cannot be null.");
        return getWeight() < role.getWeight();
    }

    public boolean isFirstRole() {
        return getWeight() == 0;
    }

    public boolean isLastRole() {
        return getWeight() == KingdomRoles.LEADER.getWeight();
    }

    public boolean isRoleLadder() {
        return getWeight() >= 0 && (getPreviousRole() != null || getNextRole() != null);
    }

    public KingdomRole getNextRole() {
        return getWeight() < 2 ? null : KingdomRoles.getByWeight(getWeight() + 1);
    }

    public KingdomRole getPreviousRole() {
        return getWeight() <= 2 ? null : KingdomRoles.getByWeight(getWeight() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KingdomRole that = (KingdomRole) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public String toString() {
        return name;
    }

    public static Collection<KingdomRole> values() {
        return kingdomRoles.values();
    }

    public static KingdomRole getByName(String name) {
        Preconditions.checkNotNull(name, "name parameter cannot be null.");

        KingdomRole kingdomRole = kingdomRoles.get(name.toUpperCase(Locale.ENGLISH));

        Preconditions.checkNotNull(kingdomRole, "Couldn't find an KingdomPrivilege with the name " + name + ".");

        return kingdomRole;
    }

    public static void register(@NotNull KingdomRole kingdomRole) {
        Preconditions.checkState(!kingdomRoles.containsKey(kingdomRole.name), "KingdomPrivilege with the name " + kingdomRole.name + " already exists.");

        kingdomRoles.put(kingdomRole.name, kingdomRole);
    }

    public List<KingdomPrivilege> getDefaultPermissions() {
        return defaultPermissions;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }
}

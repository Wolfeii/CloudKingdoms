package se.deepcloud.cloudkingdoms.kingdom.privilege;

import com.google.common.base.Preconditions;
import se.deepcloud.cloudkingdoms.utilities.sorting.Enumerable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KingdomPrivilege implements Enumerable {

    private static final Map<String, KingdomPrivilege> kingdomPrivileges = new HashMap<>();
    private static int ordinalCounter = 0;

    private final String name;
    private final int ordinal;

    private KingdomPrivilege(String name) {
        this.name = name.toUpperCase(Locale.ENGLISH);
        this.ordinal = ordinalCounter++;
    }

    @Override
    public int ordinal() {
        return this.ordinal;
    }

    /**
     * Get all the kingdom privileges.
     */
    public static Collection<KingdomPrivilege> values() {
        return kingdomPrivileges.values();
    }

    /**
     * Get a kingdom privilege by it's name.
     *
     * @param name The name to check.
     */
    public static KingdomPrivilege getByName(String name) {
        Preconditions.checkNotNull(name, "name parameter cannot be null.");

        KingdomPrivilege kingdomPrivilege = kingdomPrivileges.get(name.toUpperCase(Locale.ENGLISH));

        Preconditions.checkNotNull(kingdomPrivilege, "Couldn't find an KingdomPrivilege with the name " + name + ".");

        return kingdomPrivilege;
    }

    /**
     * Register a new kingdom privilege.
     *
     * @param name The name for the kingdom privilege.
     */
    public static void register(String name) {
        Preconditions.checkNotNull(name, "name parameter cannot be null.");

        name = name.toUpperCase(Locale.ENGLISH);

        Preconditions.checkState(!kingdomPrivileges.containsKey(name), "KingdomPrivilege with the name " + name + " already exists.");

        kingdomPrivileges.put(name, new KingdomPrivilege(name));
    }

    /**
     * Get the name of the kingdom privilege.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "KingdomPrivilege{name=" + name + "}";
    }

    public enum Status {
        ALLOW,
        DENIED,
        UNSET
    }
}

package se.deepcloud.cloudkingdoms.kingdom.privilege;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.utilities.ServerVersion;

import java.util.Objects;

public class KingdomPrivileges {

    public static final KingdomPrivilege BANK_DEPOSIT = register("BANK_DEPOSIT");
    public static final KingdomPrivilege BANK_WITHDRAW = register("BANK_WITHDRAW");
    public static final KingdomPrivilege BANNING = register("BANNING");
    public static final KingdomPrivilege BUILD = register("BUILD");
    public static final KingdomPrivilege CLAIM = register("CLAIM");
    public static final KingdomPrivilege COOP_ADD = register("COOP_ADD");
    public static final KingdomPrivilege COOP_REMOVE = register("COOP_REMOVE");
    public static final KingdomPrivilege DELETE_WARP = register("DELETE_WARP");
    public static final KingdomPrivilege FLIGHT = register("FLIGHT");
    public static final KingdomPrivilege INTERACTING = register("INTERACTING");
    public static final KingdomPrivilege INVITING = register("INVITING");
    public static final KingdomPrivilege KICKING = register("KICKING");
    public static final KingdomPrivilege MOB_DAMAGE = register("MOB_DAMAGE");
    public static final KingdomPrivilege SET_WARP = register("SET_WARP");
    public static final KingdomPrivilege UNCLAIM = register("UNCLAIM");

    private KingdomPrivileges() {

    }

    public static void registerPrivileges() {
        // Do nothing, only trigger all the register calls
    }

    @NotNull
    private static KingdomPrivilege register(String name) {
        return Objects.requireNonNull(register(name, true));
    }

    @Nullable
    private static KingdomPrivilege register(String name, boolean registerCheck) {
        if (!registerCheck)
            return null;

        KingdomPrivilege.register(name);
        return KingdomPrivilege.getByName(name);
    }

}

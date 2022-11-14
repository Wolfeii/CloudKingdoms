package se.deepcloud.cloudkingdoms.kingdom.role;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivilege;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivileges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class KingdomRoles {

    public static KingdomRole LEADER = register(new KingdomRole("LEADER", "Ägare", 8, true));
    public static KingdomRole CO_LEADER = register(new KingdomRole("CO_LEADER", "Delägare", 7, true));
    public static KingdomRole ADMIN = register(new KingdomRole("ADMIN", "Adminstratör", 6,
            KingdomPrivileges.KICKING, KingdomPrivileges.BANK_DEPOSIT, KingdomPrivileges.BANK_WITHDRAW, KingdomPrivileges.INVITING,
            KingdomPrivileges.KICKING, KingdomPrivileges.BANNING, KingdomPrivileges.MOB_DAMAGE,
            KingdomPrivileges.INTERACTING, KingdomPrivileges.CLAIM, KingdomPrivileges.UNCLAIM,
            KingdomPrivileges.SET_WARP, KingdomPrivileges.DELETE_WARP, KingdomPrivileges.FLIGHT));

    public static KingdomRole MOD = register(new KingdomRole("MOD", "Moderator", 5,
            KingdomPrivileges.KICKING, KingdomPrivileges.BANK_DEPOSIT, KingdomPrivileges.BANK_WITHDRAW, KingdomPrivileges.INVITING,
            KingdomPrivileges.KICKING, KingdomPrivileges.BANNING, KingdomPrivileges.MOB_DAMAGE,
            KingdomPrivileges.INTERACTING, KingdomPrivileges.CLAIM, KingdomPrivileges.UNCLAIM,
            KingdomPrivileges.SET_WARP, KingdomPrivileges.DELETE_WARP, KingdomPrivileges.FLIGHT));

    public static KingdomRole MEMBER = register(new KingdomRole("MEMBER", "Medlem", 4,
            KingdomPrivileges.BANK_DEPOSIT, KingdomPrivileges.MOB_DAMAGE, KingdomPrivileges.INTERACTING, KingdomPrivileges.BUILD, KingdomPrivileges.FLIGHT));

    public static KingdomRole COOP = register(new KingdomRole("COOP", "Coop", 3,
            KingdomPrivileges.KICKING, KingdomPrivileges.MOB_DAMAGE, KingdomPrivileges.CLAIM, KingdomPrivileges.UNCLAIM, KingdomPrivileges.FLIGHT));

    public static KingdomRole TRUSTEE = register(new KingdomRole("TRUSTEE", "Förvaltare", 2,
            KingdomPrivileges.BUILD, KingdomPrivileges.INTERACTING, KingdomPrivileges.FLIGHT));
    
    public static KingdomRole GUEST = register(new KingdomRole("GUEST", "Gäst", 1, KingdomPrivileges.FLIGHT));

    private KingdomRoles() {

    }

    public static void registerRoles() {
        // Do nothing, only trigger all the register calls
    }

    public static KingdomRole getByWeight(int weight) {
        return new ArrayList<>(KingdomRole.values()).get(weight);
    }

    @NotNull
    private static KingdomRole register(@NotNull KingdomRole kingdomRole) {
        return Objects.requireNonNull(register(kingdomRole, true));
    }

    @Nullable
    private static KingdomRole register(KingdomRole kingdomRole, boolean registerCheck) {
        if (!registerCheck)
            return null;

        KingdomRole.register(kingdomRole);
        return KingdomRole.getByName(kingdomRole.getName());
    }


}

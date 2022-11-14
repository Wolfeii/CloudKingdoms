package se.deepcloud.cloudkingdoms.utilities;

import org.bukkit.inventory.ItemStack;

public class Utilities {

    public static boolean isValidAndSpawnEgg(ItemStack itemStack) {
        return !itemStack.getType().isBlock() && itemStack.getType().name().contains(ServerVersion.isLegacy() ? "MONSTER_EGG" : "SPAWN_EGG");
    }
}

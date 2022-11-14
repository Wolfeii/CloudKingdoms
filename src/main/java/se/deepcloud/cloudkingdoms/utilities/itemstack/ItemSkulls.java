package se.deepcloud.cloudkingdoms.utilities.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import se.deepcloud.cloudkingdoms.utilities.ServerVersion;

import java.util.UUID;

public class ItemSkulls {

    private static final String NULL_PLAYER_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19";

    public static ItemStack getPlayerHead(ItemStack itemStack, String texture) {
        return itemStack;
    }

    public static String getNullPlayerTexture() {
        return NULL_PLAYER_TEXTURE;
    }
}

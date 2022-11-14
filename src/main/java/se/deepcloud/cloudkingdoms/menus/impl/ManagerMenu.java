package se.deepcloud.cloudkingdoms.menus.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.menus.KingdomMenu;
import se.deepcloud.cloudkingdoms.menus.items.CommonMenuItems;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.utilities.itemstack.ItemBuilder;
import se.deepcloud.cloudkingdoms.utilities.itemstack.TemplateItem;

import java.util.Arrays;
import java.util.List;

public final class ManagerMenu extends KingdomMenu {

    private final Inventory inventory = Bukkit.createInventory(this, 54, "Hantera Kingdom");

    public ManagerMenu(@NotNull KingdomPlayer kingdomPlayer, @Nullable Kingdom kingdom) {
        super(kingdomPlayer, kingdom);

        if (kingdom == null) {
            return;
        }

        for (int backgroundIndex = 0; backgroundIndex < inventory.getSize(); backgroundIndex++) {
            inventory.setItem(backgroundIndex, CommonMenuItems.SEPERATOR.build());
        }

        String homeAvailableLine = "&7Hem satt? " + (kingdom.getHomeLocation() != null ? "&a&lJA" : "&c&lNEJ");
        inventory.setItem(10, CommonMenuItems.MANAGER_HOME.getBuilder()
                .appendLore(List.of(homeAvailableLine))
                .build());
        inventory.setItem(11, CommonMenuItems.MANAGER_MEMBERS.build());
        inventory.setItem(12, CommonMenuItems.MANAGER_BANS.build());
        inventory.setItem(13, CommonMenuItems.MANAGER_BANK.build());
        inventory.setItem(14, CommonMenuItems.MANAGER_UPGRADES.build());
        inventory.setItem(15, CommonMenuItems.MANAGER_PERMISSIONS.build());
        inventory.setItem(16, CommonMenuItems.MANAGER_LOGS.build());
        inventory.setItem(30, CommonMenuItems.MANAGER_VAULT.build());
        inventory.setItem(32, CommonMenuItems.MANAGER_LEVEL.build());

        inventory.setItem(48, new TemplateItem(new ItemBuilder(Material.PAPER)
                .withName("&eKingdom Sammanfattning")
                .withLore("&8Statistik", "", "&dKingdom Namn: &f" + kingdom.getName(), "",
                        "&fMedlemmar: &a" + kingdom.getKingdomMembers(true).size() + "&7/&c" + (5 + kingdom.getMemberBonus()), "&f➞ 5 &7standard slots + &f" + kingdom.getMemberBonus() + " &7slots",
                        "&7från boosts.", "", "&fClaims: &a" + kingdom.getClaims().size() + "&7/&c" + (5 + kingdom.getClaimBonus()),
                        "&f➞ " + kingdom.getKingdomMembers(true).size() * 5 + " &7standard slots + &f" + kingdom.getClaimBonus() + " &7slots", "&7från boosts.", "",
                        "&cHopper Gräns: &f" + /* UpgradeType.HOPPERS.getValue(kingdom.getUpgrades().getLevel(UpgradeType.HOPPERS)) */ "0" + " &e(" + /* kingdom.getHopperPlaced() */ "0" + " placerade)",
                        "&cSpawner Gräns: &f" + /* UpgradeType.SPAWNERS.getValue(kingdom.getUpgrades().getLevel(UpgradeType.SPAWNERS)) */ "0" + " &e(" + /* kingdom.getSpawnerPlaced() */ "0" + " placerade)", "",
                        "&aBank Saldo: &2$0", "", "&eÖka dina max claims och", "&emedlemmar genom uppgradering", "&eav din rank @ &f&ndeepcloud.tebex.io")).build());

    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {

    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

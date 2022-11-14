package se.deepcloud.cloudkingdoms.menus.impl;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.menus.KingdomMenu;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

public final class LevelMenu extends KingdomMenu {

    private final Inventory inventory = Bukkit.createInventory(this, 36, "Kingdom Level");

    protected LevelMenu(@NotNull KingdomPlayer kingdomPlayer, @Nullable Kingdom kingdom) {
        super(kingdomPlayer, kingdom);
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

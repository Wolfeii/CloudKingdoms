package se.deepcloud.cloudkingdoms.menus.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.kingdom.name.KingdomName;
import se.deepcloud.cloudkingdoms.menus.KingdomMenu;
import se.deepcloud.cloudkingdoms.menus.items.MenuItems;
import se.deepcloud.cloudkingdoms.message.Message;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.player.chat.PlayerChat;
import se.deepcloud.cloudkingdoms.utilities.logging.Debug;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;

public final class CreateMenu extends KingdomMenu {

    private final Inventory inventory = Bukkit.createInventory(this, 27, "Skapa Kingdom");

    public CreateMenu(@NotNull KingdomPlayer player, @Nullable Kingdom kingdom) {
        super(player, kingdom);

        for (int backgroundIndex = 0; backgroundIndex < inventory.getSize(); backgroundIndex++) {
            this.inventory.setItem(backgroundIndex, MenuItems.SEPERATOR.build());
        }

        this.inventory.setItem(12, MenuItems.CREATE_CREATE.build());
        this.inventory.setItem(14, MenuItems.CREATE_INFO.build());
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getRawSlot() == 12) {
            kingdomPlayer.sendTitle("&eSkriv ditt Kingdom namn",
                    "&7Skriv 'avbryt' för att avbryta",
                    10, 10000, 20);
            kingdomPlayer.closeInventory();

            Log.debug(Debug.CREATE_KINGDOM, "CreateMenu", "onClick",
                    kingdomPlayer.getName());

            PlayerChat.listen((Player) event.getWhoClicked(), (message) -> {
                if (message.equalsIgnoreCase("avbryt")) {
                    Message.CREATE_KINGDOM_CANCELLED.send(kingdomPlayer);
                    return true;
                }

                if (!KingdomName.isValidName(event.getWhoClicked(), null, message)) {
                    return true;
                }

                plugin.getKingdomManager().createKingdom(Kingdom.newBuilder()
                        .setKingdomName(message)
                        .setOwner(kingdomPlayer));
                kingdomPlayer.sendTitle("&eKINGDOM SKAPAT", "&f" + message, 10, 70, 20);
                PlayerChat.remove((Player) event.getWhoClicked());
                return true;
            });
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

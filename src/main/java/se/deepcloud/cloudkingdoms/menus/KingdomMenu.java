package se.deepcloud.cloudkingdoms.menus;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

public abstract class KingdomMenu implements InventoryHolder {

    protected final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    protected final KingdomPlayer kingdomPlayer;
    protected final Kingdom kingdom;

    protected KingdomMenu(@NotNull KingdomPlayer kingdomPlayer, @Nullable Kingdom kingdom) {
        this.kingdomPlayer = kingdomPlayer;
        this.kingdom = kingdom;
    }

    public void onOpen(@NotNull InventoryOpenEvent event) {
        this.kingdomPlayer.runIfOnline(player -> player.playSound(player.getLocation(), Sound.UI_TOAST_IN, 1.0F, 2.0F));
    }

    public abstract void onClick(@NotNull InventoryClickEvent event);

    public void onClose(@NotNull InventoryCloseEvent event) {
        this.kingdomPlayer.runIfOnline(player -> player.playSound(player.getLocation(), Sound.UI_TOAST_OUT, 1.0F, 2.0F));
    }

    @NotNull
    public CloudKingdoms getPlugin() {
        return plugin;
    }

    @Nullable
    public Kingdom getKingdom() {
        return kingdom;
    }

    @NotNull
    public KingdomPlayer getPlayer() {
        return kingdomPlayer;
    }
}

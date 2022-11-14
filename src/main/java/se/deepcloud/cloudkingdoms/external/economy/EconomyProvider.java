package se.deepcloud.cloudkingdoms.external.economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

import java.math.BigDecimal;
import java.util.Optional;

import static org.bukkit.Bukkit.getServer;

public class EconomyProvider {

    private static final BigDecimal MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);

    private Economy economy;

    public void initializeVault() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            return;
        }

        Optional.ofNullable(getServer().getServicesManager().getRegistration(Economy.class))
                .ifPresent(econProvider -> this.economy = econProvider.getProvider());
        if (economy == null) {
            // Economy was not setup.
        }
    }

    public BigDecimal getBalance(@NotNull KingdomPlayer kingdomPlayer) {
        OfflinePlayer offlinePlayer = this.getOfflinePlayer(kingdomPlayer);
        if (offlinePlayer == null)
            return BigDecimal.ZERO;

        return BigDecimal.valueOf(economy.getBalance(offlinePlayer));
    }

    public EconomyResponse depositMoney(@NotNull KingdomPlayer kingdomPlayer, double amount) {
        OfflinePlayer offlinePlayer = this.getOfflinePlayer(kingdomPlayer);
        if (offlinePlayer == null)
            return new EconomyResponse(amount, -1, EconomyResponse.ResponseType.FAILURE, "Invalid Player.");

        return economy.depositPlayer(offlinePlayer, amount);
    }

    public EconomyResponse withdrawMoney(@NotNull KingdomPlayer kingdomPlayer, double amount) {
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not Implemented");
    }

    @Nullable
    public OfflinePlayer getOfflinePlayer(@NotNull KingdomPlayer kingdomPlayer) {
        return Bukkit.getServer().getOfflinePlayer(kingdomPlayer.getUniqueId());
    }

}

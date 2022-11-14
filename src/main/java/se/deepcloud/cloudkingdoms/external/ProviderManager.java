package se.deepcloud.cloudkingdoms.external;

import org.bukkit.Bukkit;
import se.deepcloud.cloudkingdoms.external.economy.EconomyProvider;
import se.deepcloud.cloudkingdoms.scheduler.BukkitExecutor;

import java.math.BigDecimal;

public final class ProviderManager {

    private static final BigDecimal MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);

    private EconomyProvider economyProvider = new EconomyProvider();

    public void loadData() {
        BukkitExecutor.sync(() -> {
            registerEconomyProvider();
        });
    }

    private void registerEconomyProvider() {
        if (canRegisterHook("Vault")) {
            
        }
    }


    private boolean canRegisterHook(String pluginName) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }

}

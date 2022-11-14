package se.deepcloud.cloudkingdoms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.command.CommandsManager;
import se.deepcloud.cloudkingdoms.command.admin.AdminCommandMap;
import se.deepcloud.cloudkingdoms.command.player.PlayerCommandMap;
import se.deepcloud.cloudkingdoms.config.Setting;
import se.deepcloud.cloudkingdoms.kingdom.KingdomManager;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivilege;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivileges;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRole;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRoles;
import se.deepcloud.cloudkingdoms.listeners.BukkitListeners;
import se.deepcloud.cloudkingdoms.message.Message;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.player.PlayerManager;
import se.deepcloud.cloudkingdoms.scheduler.BukkitExecutor;
import se.deepcloud.cloudkingdoms.storage.DataManager;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;

public final class CloudKingdoms extends JavaPlugin {

    private static CloudKingdoms cloudKingdomsInstance;

    private final DataManager dataManager = new DataManager(this);
    private final PlayerManager playerManager = new PlayerManager(this);
    private final KingdomManager kingdomManager = new KingdomManager(this);
    private final CommandsManager commandsManager = new CommandsManager(this,
            new PlayerCommandMap(this),
            new AdminCommandMap(this));

    private final BukkitListeners bukkitListeners = new BukkitListeners(this);

    @Override
    public void onLoad() {
        // Plugin load logic
        cloudKingdomsInstance = this;

        Log.toggleDebugMode();

        bukkitListeners.registerListenerFailureFilter();

        KingdomPrivileges.registerPrivileges();
        KingdomRoles.registerRoles();
    }

    @Override
    public void onEnable() {
        try {
            BukkitExecutor.init(this);

            try {
                reloadPlugin();
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }

            try {
                bukkitListeners.register();
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }

            // Update all currently active players (in case of reload)
            BukkitExecutor.sync(() -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    KingdomPlayer kingdomPlayer = playerManager.getKingdomPlayer(player);
                    kingdomPlayer.updateLastOnlineStatus();
                }
            }, 1L);
        } catch (RuntimeException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadPlugin() {
        commandsManager.loadData();

        Message.reload();

        dataManager.load();
    }

    public static CloudKingdoms getPlugin() {
        return cloudKingdomsInstance;
    }

    public @NotNull PlayerManager getPlayerManager() {
        return playerManager;
    }

    public @NotNull KingdomManager getKingdomManager() {
        return kingdomManager;
    }

    public CommandsManager getCommandsManager() {
        return commandsManager;
    }

    public BukkitListeners getBukkitListeners() {
        return bukkitListeners;
    }
}

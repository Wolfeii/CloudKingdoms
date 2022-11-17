package se.deepcloud.cloudkingdoms.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.utilities.sorting.Pair;

import java.util.List;
import java.util.Map;

public interface IKingdomCommand {

    /**
     * Get the aliases of the sub command.
     */
    List<String> getAliases();

    /**
     * Get the required permission to use the sub command.
     * If no permission is required, can be empty.
     */
    String getPermission();

    /**
     * Get the usage of the sub command.
     */
    String getUsage();

    /**
     * Get the description of the sub command.
     */
    String getDescription();

    /**
     * Get the minimum arguments required for the command.
     * For example, the command /is example PLAYER_NAME has 2 arguments.
     */
    int getMinArgs();

    /**
     * Get the maximum arguments required for the command.
     * For example, the command /is example PLAYER_NAME has 2 arguments.
     */
    int getMaxArgs();

    /**
     * Can the command be executed from console?
     * If true, sender cannot be casted directly into a player. Otherwise, it can be.
     */
    boolean canBeExecutedByConsole();


    /**
     * Should the command be displayed in /is help (or /is admin for admin commands)?
     */
    default boolean displayCommand() {
        return true;
    }

    /**
     * The method to be executed when the command is running.
     *
     * @param plugin The instance of the plugin.
     * @param sender The sender who ran the command.
     * @param args   The arguments of the command.
     */
    void execute(@NotNull CloudKingdoms plugin, CommandSender sender, String[] args);

    /**
     * Get the tab-complete arguments of the command.
     *
     * @param plugin The instance of the plugin.
     * @param sender The sender who ran the command.
     * @param args   The arguments of the command.
     */
    List<String> tabComplete(@NotNull CloudKingdoms plugin, CommandSender sender, String[] args);
}

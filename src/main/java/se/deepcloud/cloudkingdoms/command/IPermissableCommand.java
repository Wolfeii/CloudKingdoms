package se.deepcloud.cloudkingdoms.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.command.arguments.CommandArgument;
import se.deepcloud.cloudkingdoms.command.arguments.KingdomsArgument;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivilege;
import se.deepcloud.cloudkingdoms.message.Message;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

import java.util.Collections;
import java.util.List;

public interface IPermissableCommand extends IKingdomCommand {

    @Override
    default void execute(@NotNull CloudKingdoms plugin, CommandSender sender, String[] args) {
        Kingdom kingdom = null;
        KingdomPlayer kingdomPlayer = null;

        if (!canBeExecutedByConsole() || sender instanceof Player) {
            KingdomsArgument arguments = CommandArgument.getSenderKingdom(plugin, sender);

            kingdom = arguments.getKingdom();

            if (kingdom == null)
                return;

            kingdomPlayer = arguments.getKingdomPlayer();

            if (!kingdomPlayer.hasPermission(getPrivilege())) {
                getPermissionLackMessage().send(kingdomPlayer, kingdom.getRequiredPlayerRole(getPrivilege()));
                return;
            }
        }

        execute(plugin, kingdomPlayer, kingdom, args);
    }

    @Override
    default List<String> tabComplete(@NotNull CloudKingdoms plugin, CommandSender sender, String[] args) {
        Kingdom kingdom = null;
        KingdomPlayer kingdomPlayer = null;

        if (!canBeExecutedByConsole() || sender instanceof Player) {
            kingdomPlayer = plugin.getPlayerManager().getKingdomPlayer(sender);
            kingdom = kingdomPlayer.getKingdom();
        }

        return kingdomPlayer == null || (kingdom != null && kingdomPlayer.hasPermission(getPrivilege())) ?
                tabComplete(plugin, kingdomPlayer, kingdom, args) : Collections.emptyList();
    }

    KingdomPrivilege getPrivilege();

    Message getPermissionLackMessage();

    void execute(@NotNull CloudKingdoms plugin, KingdomPlayer kingdomPlayer, @Nullable Kingdom kingdom, String[] args);

    default List<String> tabComplete(@NotNull CloudKingdoms plugin, KingdomPlayer kingdomPlayer, @Nullable Kingdom kingdom, String[] args) {
        return Collections.emptyList();
    }

}
package se.deepcloud.cloudkingdoms.command.player;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.command.IPermissableCommand;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivilege;
import se.deepcloud.cloudkingdoms.kingdom.privilege.KingdomPrivileges;
import se.deepcloud.cloudkingdoms.message.Message;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.utilities.sorting.Pair;

import java.util.Arrays;
import java.util.List;

public final class CommandClaim implements IPermissableCommand {
    @Override
    public List<String> getAliases() {
        return List.of("claim");
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/kingdom claim";
    }

    @Override
    public String getDescription() {
        return Message.COMMAND_DESCRIPTION_CLAIM.getMessage();
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return false;
    }

    @Override
    public KingdomPrivilege getPrivilege() {
        return KingdomPrivileges.CLAIM;
    }

    @Override
    public Message getPermissionLackMessage() {
        return Message.NO_CLAIM_PERMISSION;
    }

    @Override
    public void execute(@NotNull CloudKingdoms plugin, KingdomPlayer kingdomPlayer, @Nullable Kingdom kingdom, String[] args) {
        if (kingdomPlayer.getLocation() != null && kingdom != null) {
            Chunk targetChunk = kingdomPlayer.getLocation().getChunk();
            if (plugin.getKingdomManager().isChunkClaimed(targetChunk)) {
                Message.CHUNK_ALREADY_CLAIMED.send(kingdomPlayer);
                return;
            }

            kingdom.claimChunk(kingdomPlayer, kingdomPlayer.getLocation().getChunk());
            Message.CHUNK_SUCCESSFULLY_CLAIMED.send(kingdomPlayer);
        }
    }
}

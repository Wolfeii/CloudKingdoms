package se.deepcloud.cloudkingdoms.storage.serialization;

import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.builder.KingdomBuilder;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRole;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRoles;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.storage.DatabaseResult;
import se.deepcloud.cloudkingdoms.storage.bridge.DatabaseBridge;
import se.deepcloud.cloudkingdoms.storage.cache.DatabaseCache;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;

import java.util.Optional;
import java.util.UUID;

public class KingdomsDeserializer {

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    public static void deserializeMembers(@NotNull DatabaseBridge databaseBridge, @NotNull DatabaseCache<KingdomBuilder> databaseCache) {
        databaseBridge.loadAllObjects("kingdom_members", membersRow -> {
            DatabaseResult members = new DatabaseResult(membersRow);

            Optional<UUID> uuid = members.getUUID("kingdom");
            if (uuid.isEmpty()) {
                Log.warn("Kan inte ladda medlemmar för ogiltlig Kingdom, hoppar över...");
                return;
            }

            Optional<UUID> playerUUID = members.getUUID("player");
            if (playerUUID.isEmpty()) {
                Log.warnFromFile("Kan inte ladda Kingdom medlemmar med ogiltliga UUIDs för ", uuid.get(), ", hoppar över...");
                return;
            }

            KingdomBuilder kingdomBuilder = databaseCache.computeIfAbsentInfo(uuid.get(), KingdomBuilder::new);

            KingdomRole kingdomRole = members.getString("role").map(KingdomRole::getByName)
                    .orElse(KingdomRoles.MEMBER);

            KingdomPlayer kingdomPlayer = plugin.getPlayerManager().getKingdomPlayer(playerUUID.get());
            kingdomPlayer.setKingdomRole(kingdomRole);

            kingdomBuilder.addKingdomMember(kingdomPlayer);
        });
    }
}

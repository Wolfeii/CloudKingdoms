package se.deepcloud.cloudkingdoms.storage.serialization;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.builder.KingdomBuilder;
import se.deepcloud.cloudkingdoms.kingdom.claim.ClaimPosition;
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

    public static void deserializeClaims(@NotNull DatabaseBridge databaseBridge, @NotNull DatabaseCache<KingdomBuilder> databaseCache) {
        databaseBridge.loadAllObjects("kingdom_claims", claimsRow -> {
            DatabaseResult databaseResult = new DatabaseResult(claimsRow);

            Optional<UUID> uuid = databaseResult.getUUID("uuid");
            if (uuid.isEmpty()) {
                Log.warn("Kan inte ladda claims för Kingdom med ogiltligt UUID.");
                return;
            }

            Optional<String> worldName = databaseResult.getString("world_name");
            Optional<Integer> xCoordinate = databaseResult.getInt("chunk_x_pos");
            Optional<Integer> zCoordinate = databaseResult.getInt("chunk_z_pos");

            if (worldName.isEmpty() || xCoordinate.isEmpty() || zCoordinate.isEmpty()) {
                Log.warn("Kan inte ladda Claim med ogiltiga koordinater.");
                return;
            }

            World world = Bukkit.getWorld(worldName.get());
            if (world == null) {
                return;
            }

            KingdomBuilder kingdomBuilder = databaseCache.computeIfAbsentInfo(uuid.get(), KingdomBuilder::new);

            kingdomBuilder.addClaim(ClaimPosition.newBuilder()
                    .setOwner(uuid.get())
                    .setClaimedOn(databaseResult.getLong("claimed_on").orElse(0L))
                    .setClaimedBy(databaseResult.getUUID("claimed_by").orElse(null))
                    .setX(xCoordinate.get())
                    .setZ(zCoordinate.get())
                    .setWorld(world).build());
        });
    }
}

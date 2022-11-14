package se.deepcloud.cloudkingdoms.player.builder;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRole;
import se.deepcloud.cloudkingdoms.kingdom.role.KingdomRoles;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

import java.util.UUID;

public class KingdomPlayerBuilder {

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    public UUID uuid = null;
    public String name = "null";
    public KingdomRole playerRole = KingdomRoles.GUEST;
    public String textureValue = "";
    public byte[] persistentData = new byte[0];

    public KingdomPlayerBuilder() {

    }

    public KingdomPlayerBuilder setUniqueId(@NotNull UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public KingdomPlayerBuilder setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public KingdomPlayerBuilder setPersistentData(byte[] persistentData) {
        this.persistentData = persistentData;
        return this;
    }

    public KingdomPlayerBuilder setPlayerRole(@NotNull KingdomRole kingdomRole) {
        this.playerRole = kingdomRole;
        return this;
    }

    public KingdomPlayerBuilder setTextureValue(@NotNull String textureValue) {
        this.textureValue = textureValue;
        return this;
    }

    public KingdomPlayer build() {
        if (this.uuid == null)
            throw new IllegalStateException("Cannot create a player with invalid uuid.");

        return new KingdomPlayer(this);
    }
}

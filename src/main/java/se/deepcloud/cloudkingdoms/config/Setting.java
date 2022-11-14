package se.deepcloud.cloudkingdoms.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;

import java.io.File;

public enum Setting {

    DATABASE_TYPE("database.type"),
    DATABASE_BACKUP("database.backup"),
    DATABASE_ADDRESS("database.address"),
    DATABASE_PORT("database.port"),
    DATABASE_DB_NAME("database.db-name"),
    DATABASE_USERNAME("database.username"),
    DATABASE_PASSWORD("database.password"),
    DATABASE_USE_SSL("database.useSSL"),
    DATABASE_ALLOW_PUBLIC_KEY_RETRIEVAL("database.allowPublicKeyRetrieval"),
    DATABASE_WAIT_TIMEOUT("database.waitTimeout"),
    DATABASE_MAX_LITE_TIME("database.maxLifetime");

    private static final FileConfiguration config = CloudKingdoms.getPlugin().getConfig();

    private final String path;

    Setting(@NotNull String path) {
        this.path = path;
    }

    public String getAsString() {
        return config.getString(path);
    }

    public Integer getAsInteger() {
        return config.getInt(path);
    }

    public boolean getAsBoolean() {
        return config.getBoolean(path);
    }

    public double getAsDouble() {
        return config.getDouble(path);
    }
}

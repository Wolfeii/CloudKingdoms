package se.deepcloud.cloudkingdoms.storage.sql.session.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.config.Setting;
import se.deepcloud.cloudkingdoms.storage.sql.session.QueryResult;
import se.deepcloud.cloudkingdoms.storage.sql.session.RemoteSQLSession;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;

import java.sql.ResultSet;
import java.util.Set;

public class MariaDBSession extends RemoteSQLSession {

    public MariaDBSession(@NotNull CloudKingdoms plugin, boolean logging) {
        super(plugin, logging);
    }

    @Override
    public boolean createConnection() {
        if (logging) {
            Log.info("Trying to connect to remote database (MariaDB)...");
        }

        try {
            HikariConfig config = new HikariConfig();
            config.setConnectionTestQuery("SELECT 1");
            config.setPoolName("SuperiorSkyblock Pool");

            config.setDriverClassName("com.mysql.jdbc.Driver");

            String address =  Setting.DATABASE_ADDRESS.getAsString();
            String dbName =  Setting.DATABASE_DB_NAME.getAsString();
            String userName =  Setting.DATABASE_USERNAME.getAsString();
            String password = Setting.DATABASE_PASSWORD.getAsString();
            int port = Setting.DATABASE_PORT.getAsInteger();

            boolean useSSL = Setting.DATABASE_USE_SSL.getAsBoolean();
            boolean publicKeyRetrieval = Setting.DATABASE_ALLOW_PUBLIC_KEY_RETRIEVAL.getAsBoolean();

            config.setJdbcUrl("jdbc:mysql://" + address + ":" + port + "/" + dbName + "?useSSL=" + useSSL);
            config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s?useSSL=%b&allowPublicKeyRetrieval=%b",
                    address, port, dbName, useSSL, publicKeyRetrieval));
            config.setUsername(userName);
            config.setPassword(password);
            config.setMinimumIdle(5);
            config.setMaximumPoolSize(50);
            config.setConnectionTimeout(10000);
            config.setIdleTimeout(Setting.DATABASE_WAIT_TIMEOUT.getAsInteger());
            config.setMaxLifetime(Setting.DATABASE_MAX_LITE_TIME.getAsInteger());
            config.addDataSourceProperty("characterEncoding", "utf8");
            config.addDataSourceProperty("useUnicode", "true");

            dataSource = new HikariDataSource(config);

            if (logging) {
                Log.info("Successfully established connection with remote database!");
            }

            ready.complete(null);

            return true;
        } catch (Throwable error) {
            Log.error(error, "An unexpected error occurred while connecting to the MariaDB database:");
        }

        return false;
    }

    @Override
    public void setJournalMode(String journalMode, QueryResult<ResultSet> queryResult) {
        queryResult.fail(new UnsupportedOperationException("Cannot change journal mode in MariaDB."));
    }

}
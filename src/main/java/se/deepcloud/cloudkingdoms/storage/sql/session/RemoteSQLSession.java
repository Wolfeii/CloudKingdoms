package se.deepcloud.cloudkingdoms.storage.sql.session;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.utilities.logging.Debug;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;
import se.deepcloud.cloudkingdoms.utilities.sorting.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public abstract class RemoteSQLSession implements SQLSession {

    protected static final String TABLE_PREFIX = "kingdoms_";

    protected final CompletableFuture<Void> ready = new CompletableFuture<>();
    protected final CloudKingdoms plugin;

    @Nullable
    protected HikariDataSource dataSource;
    protected boolean logging;

    protected RemoteSQLSession(@NotNull CloudKingdoms plugin, boolean logging) {
        this.plugin = plugin;
        setLogging(logging);
    }

    @Override
    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    @Override
    public abstract boolean createConnection();

    @Override
    public void closeConnection() {
        Preconditions.checkNotNull(this.dataSource, "Session was not initialized.");
        dataSource.close();
    }

    @Override
    public void waitForConnection() {
        try {
            ready.get();
        } catch (Exception error) {
            Log.error(error, "An unexpected error occurred while waiting for connection:");
        }
    }

    @Override
    public void createTable(String tableName, Pair<String, String>[] columns, QueryResult<Void> queryResult) {
        StringBuilder columnsSection = new StringBuilder();
        for (Pair<String, String> column : columns) {
            columnsSection.append(",")
                    .append(column.getKey())
                    .append(" ")
                    .append(column.getValue());
        }

        executeUpdate(String.format("CREATE TABLE IF NOT EXISTS %s%s (%s);",
                TABLE_PREFIX, tableName, columnsSection.substring(1)), queryResult);
    }

    @Override
    public void renameTable(String tableName, String newName, QueryResult<Void> queryResult) {
        executeUpdate(String.format("RENAME TABLE %s%s TO %s%s;", TABLE_PREFIX, tableName, TABLE_PREFIX, newName), queryResult);
    }

    @Override
    public void createIndex(String indexName, String tableName, String[] columns, QueryResult<Void> queryResult) {
        StringBuilder columnsSection = new StringBuilder();
        for (String column : columns) {
            columnsSection.append(",").append(column);
        }

        executeUpdate(String.format("CREATE UNIQUE INDEX %s ON %s%s (%s);",
                indexName, TABLE_PREFIX, tableName, columnsSection.substring(1)), queryResult);
    }

    @Override
    public void modifyColumnType(String tableName, String columnName, String newType, QueryResult<Void> queryResult) {
        executeUpdate(String.format("ALTER TABLE %s%s MODIFY COLUMN %s %s;",
                TABLE_PREFIX, tableName, columnName, newType), queryResult);
    }

    @Override
    public void removePrimaryKey(String tableName, String columnName, QueryResult<Void> queryResult) {
        executeUpdate(String.format("ALTER TABLE %s%s DROP PRIMARY KEY;", TABLE_PREFIX, tableName), queryResult);
    }

    @Override
    public void select(String tableName, String filters, QueryResult<ResultSet> queryResult) {
        executeQuery(String.format("SELECT * FROM %s%s%s;", TABLE_PREFIX, tableName, filters), queryResult);
    }

    @Override
    public void setJournalMode(String jounralMode, QueryResult<ResultSet> queryResult) {
        executeQuery(String.format("PRAGMA journal_mode=%s;", jounralMode), queryResult);
    }

    @Override
    public void customQuery(String statement, QueryResult<PreparedStatement> queryResult) {
        Preconditions.checkNotNull(this.dataSource, "Session was not initialized.");

        String query = statement.replace("{prefix}", TABLE_PREFIX);

        Log.debug(Debug.DATABASE_QUERY, "RemoteSQLSession", "customQuery", query);

        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            queryResult.complete(preparedStatement);
        } catch (SQLException error) {
            queryResult.fail(error);
        }
    }

    private void executeUpdate(String statement, QueryResult<Void> queryResult) {
        Preconditions.checkNotNull(this.dataSource, "Session was not initialized.");

        String query = statement
                .replace("BIG_DECIMAL", "TEXT")
                .replace("DECIMAL", "DECIMAL(10, 2)")
                .replace("UUID", "VARCHAR(36)")
                .replace("LONG_UNIQUE_TEXT", "VARCHAR(255)")
                .replace("UNIQUE_TEXT", "VARCHAR(30)");

        Log.debug(Debug.DATABASE_QUERY, "RemoteSQLSession", "executeUpdate", query);

        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            queryResult.complete(null);
        } catch (SQLException error) {
            queryResult.fail(error);
        }
    }

    private void executeQuery(String query, QueryResult<ResultSet> queryResult) {
        Preconditions.checkNotNull(this.dataSource, "Session was not initialized.");

        Log.debug(Debug.DATABASE_QUERY, "RemoteSQLSession", "executequery", query);

        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            queryResult.complete(resultSet);
        } catch (SQLException error) {
            queryResult.fail(error);
        }
    }

}
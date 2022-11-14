package se.deepcloud.cloudkingdoms.storage.sql;

import se.deepcloud.cloudkingdoms.scheduler.BukkitExecutor;
import se.deepcloud.cloudkingdoms.storage.sql.session.QueryResult;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatementHolder {

    private final List<Map<Integer, Object>> batches = new LinkedList<>();

    private final Map<Integer, Object> values = new HashMap<>();
    private String query;
    private int currentIndex = 1;

    public StatementHolder(String statement) {
        setQuery(statement);
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void addBatch() {
        batches.add(new HashMap<>(values));
        values.clear();
        currentIndex = 1;
    }

    public void setObject(Object value) {
        values.put(currentIndex++, value);
    }

    public void executeBatch(boolean async) {
        if (batches.isEmpty())
            return;

        StringHolder errorQuery = new StringHolder(query);

        executeQuery(async, new QueryResult<PreparedStatement>().onSuccess(preparedStatement -> {
            Connection connection = preparedStatement.getConnection();
            connection.setAutoCommit(false);

            for (Map<Integer, Object> values : batches) {
                for (Map.Entry<Integer, Object> entry : values.entrySet()) {
                    preparedStatement.setObject(entry.getKey(), entry.getValue());
                    errorQuery.value = errorQuery.value.replaceFirst("\\?", entry.getValue() + "");
                }
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            try {
                connection.commit();
            } catch (Throwable ignored) {
            }

            connection.setAutoCommit(true);
        }).onFail(error ->
            Log.error(error, "An unexpected error occurred while executing query `", errorQuery, "`:")
        ));
    }

    public void execute(boolean async) {
        StringHolder errorQuery = new StringHolder(query);

        executeQuery(async, new QueryResult<PreparedStatement>().onSuccess(preparedStatement -> {
            for (Map.Entry<Integer, Object> entry : values.entrySet()) {
                preparedStatement.setObject(entry.getKey(), entry.getValue());
                errorQuery.value = errorQuery.value.replaceFirst("\\?", entry.getValue() + "");
            }
            preparedStatement.executeUpdate();
        }).onFail(error ->
            Log.error(error, "An unexpected error occurred while executing query `", errorQuery, "`:")
        ));
    }

    private void executeQuery(boolean async, QueryResult<PreparedStatement> queryResult) {
        if ((query == null || query.isEmpty()) || !SQLHelper.isReady())
            return;

        if (async && !BukkitExecutor.isDataThread()) {
            BukkitExecutor.data(() -> executeQuery(false, queryResult));
            return;
        }

        SQLHelper.waitForConnection();

        try {
            SQLHelper.customQuery(query, queryResult);
        } finally {
            values.clear();
        }
    }

    private static class StringHolder {

        private String value;

        StringHolder(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

}
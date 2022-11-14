package se.deepcloud.cloudkingdoms.storage.loader.sql;

import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.storage.loader.MachineStateDatabaseLoader;
import se.deepcloud.cloudkingdoms.storage.sql.SQLHelper;
import se.deepcloud.cloudkingdoms.storage.sql.session.QueryResult;
import se.deepcloud.cloudkingdoms.utilities.sorting.Pair;

public class SQLDatabaseLoader extends MachineStateDatabaseLoader {

    private final CloudKingdoms plugin;

    public SQLDatabaseLoader(@NotNull CloudKingdoms plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void handleInitialize() throws RuntimeException {
        if (!SQLHelper.createConnection(plugin)) {
            throw new RuntimeException("Couldn't connect to the database.\nMake sure all information is correct.");
        }

        createPlayersTable();
        createKingdomsTable();
    }

    @Override
    protected void handlePostInitialize() {
        SQLHelper.createIndex("kingdoms_bans_index", "kingdom_bans",
                "kingdom", "player");

        SQLHelper.createIndex("kingdoms_members_index", "kingdom_members",
                "kingdom", "player");

        SQLHelper.createIndex("role_permissions_index", "kingdoms_role_permissions",
                "kingdom", "permission");
    }

    @Override
    protected void handlePreLoadData() {
        SQLHelper.setJournalMode("MEMORY", QueryResult.EMPTY_QUERY_RESULT);
    }

    @Override
    protected void handlePostLoadData() {
        SQLHelper.setJournalMode("DELETE", QueryResult.EMPTY_QUERY_RESULT);
    }

    @Override
    protected void handleShutdown() {
        SQLHelper.close();
    }

    private void createPlayersTable() {
        SQLHelper.createTable("players",
                new Pair<>("uuid", "UUID PRIMARY KEY"),
                new Pair<>("last_used_name", "TEXT"),
                new Pair<>("last_used_skin", "TEXT"),
                new Pair<>("member_bonus", "INTEGER"),
                new Pair<>("claim_bonus", "INTEGER"),
                new Pair<>("last_online_status", "BIGINT")
        );

        SQLHelper.createTable("players_custom_data",
                new Pair<>("player", "UUID PRIMARY KEY"),
                new Pair<>("data", "BLOB")
        );
    }

    private void createKingdomsTable() {
        SQLHelper.createTable("kingdoms",
                new Pair<>("uuid", "UUID PRIMARY KEY"),
                new Pair<>("owner", "UUID"),
                new Pair<>("creation_time", "BIGINT"),
                new Pair<>("level", "INTEGER"),
                new Pair<>("name", "TEXT")
        );

        SQLHelper.createTable("kingdom_claims",
                new Pair<>("uuid", "UUID"),
                new Pair<>("world_name", "TEXT"),
                new Pair<>("chunk_x_pos", "INTEGER"),
                new Pair<>("chunk_y_pos", "INTEGER"));

        SQLHelper.createTable("kingdoms_banks",
                new Pair<>("kingdom", "UUID PRIMARY KEY"),
                new Pair<>("balance", "BIG_DECIMAL")
        );

        SQLHelper.createTable("kingdoms_vault",
                new Pair<>("kingdom", "UUID PRIMARY KEY"),
                new Pair<>("`index`", "INTEGER"),
                new Pair<>("content", "LONGTEXT")
        );

        SQLHelper.createTable("kingdom_bans",
                new Pair<>("kingdom", "UUID"),
                new Pair<>("player", "UUID"),
                new Pair<>("banned_by", "UUID"),
                new Pair<>("banned_time", "BIGINT")
        );

        SQLHelper.createTable("kingdoms_block_limits",
                new Pair<>("kingdom", "UUID"),
                new Pair<>("block", "UNIQUE_TEXT"),
                new Pair<>("`limit`", "INTEGER")
        );

        SQLHelper.createTable("kingdoms_custom_data",
                new Pair<>("kingdom", "UUID PRIMARY KEY"),
                new Pair<>("data", "BLOB")
        );

        SQLHelper.createTable("kingdoms_effects",
                new Pair<>("kingdom", "UUID"),
                new Pair<>("effect_type", "UNIQUE_TEXT"),
                new Pair<>("level", "INTEGER")
        );

        SQLHelper.createTable("kingdoms_entity_limits",
                new Pair<>("kingdom", "UUID"),
                new Pair<>("entity", "UNIQUE_TEXT"),
                new Pair<>("`limit`", "INTEGER")
        );

        SQLHelper.createTable("kingdoms_flags",
                new Pair<>("kingdom", "UUID"),
                new Pair<>("name", "UNIQUE_TEXT"),
                new Pair<>("status", "INTEGER")
        );

        SQLHelper.createTable("kingdom_members",
                new Pair<>("kingdom", "UUID"),
                new Pair<>("player", "UUID"),
                new Pair<>("role", "INTEGER"),
                new Pair<>("join_time", "BIGINT")
        );

        SQLHelper.createTable("kingdom_permissions",
                new Pair<>("kingdom", "UUID"),
                new Pair<>("role", "INTEGER"),
                new Pair<>("permission", "UNIQUE_TEXT"),
                new Pair<>("status", "TEXT")
        );

        SQLHelper.createTable("kingdoms_upgrades",
                new Pair<>("kingdom", "UUID PRIMARY KEY"),
                new Pair<>("size", "INTEGER"),
                new Pair<>("max_hoppers", "INTEGER"),
                new Pair<>("max_spawners", "INTEGER"),
                new Pair<>("mob_xp_multiplier", "DECIMAL"),
                new Pair<>("crop_growth_multiplier", "DECIMAL"),
                new Pair<>("spawner_rates_multiplier", "DECIMAL"),
                new Pair<>("mob_drops_multiplier", "DECIMAL")
        );

        // Up to 1.9.0.574, decimals would not be saved correctly in MySQL
        // This occurred because the field type was DECIMAL(10,0) instead of DECIMAL(10,2)
        // Updating the column types to "DECIMAL" again should fix the issue.
        // https://github.com/BG-Software-LLC/SuperiorSkyblock2/issues/1021
        SQLHelper.modifyColumnType("kingdoms_upgrades", "crop_growth_multiplier", "DECIMAL");
        SQLHelper.modifyColumnType("kingdoms_upgrades", "spawner_rates_multiplier", "DECIMAL");
        SQLHelper.modifyColumnType("kingdoms_upgrades", "mob_drops_multiplier", "DECIMAL");
        SQLHelper.modifyColumnType("kingdoms_upgrades", "mob_xp_multiplier", "DECIMAL");


        SQLHelper.createTable("kingdoms_warps",
                new Pair<>("kingdom", "UUID"),
                new Pair<>("name", "LONG_UNIQUE_TEXT"),
                new Pair<>("visitors", "INTEGER"),
                new Pair<>("location", "TEXT"),
                new Pair<>("creation_time", "BIGINT"),
                new Pair<>("icon", "TEXT")
        );

        SQLHelper.modifyColumnType("kingdoms_warps", "name", "LONG_UNIQUE_TEXT");
    }
}


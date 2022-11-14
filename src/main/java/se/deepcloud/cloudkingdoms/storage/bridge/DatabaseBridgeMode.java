package se.deepcloud.cloudkingdoms.storage.bridge;

public enum DatabaseBridgeMode {

    /**
     * When this mode is selected, the database-bridge should run queries to the database.
     */
    SAVE_DATA,

    /**
     * When this mode is selected, the database-bridge should not execute updates to the database.
     * Reading from the database should still work.
     */
    IDLE

}
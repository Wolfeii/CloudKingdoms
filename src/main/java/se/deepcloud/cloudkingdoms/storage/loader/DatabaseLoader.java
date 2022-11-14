package se.deepcloud.cloudkingdoms.storage.loader;

public interface DatabaseLoader {

    void setState(State state) throws RuntimeException;

    enum State {

        INITIALIZE,
        POST_INITIALIZE,
        PRE_LOAD_DATA,
        POST_LOAD_DATA,
        SHUTDOWN

    }

}
package se.deepcloud.cloudkingdoms.storage.loader;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public abstract class MachineStateDatabaseLoader implements DatabaseLoader {

    private final Map<State, StateAction> STATE_MACHINE_HANDLES = Collections.unmodifiableMap(new EnumMap<State, StateAction>(State.class) {{
        put(State.INITIALIZE, MachineStateDatabaseLoader.this::handleInitialize);
        put(State.POST_INITIALIZE, MachineStateDatabaseLoader.this::handlePostInitialize);
        put(State.PRE_LOAD_DATA, MachineStateDatabaseLoader.this::handlePreLoadData);
        put(State.POST_LOAD_DATA, MachineStateDatabaseLoader.this::handlePostLoadData);
        put(State.SHUTDOWN, MachineStateDatabaseLoader.this::handleShutdown);
    }});

    @Override
    public void setState(State state) throws RuntimeException {
        StateAction action = STATE_MACHINE_HANDLES.get(state);
        if (action != null) {
            try {
                action.run();
            } catch (Throwable error) {
                if (error instanceof RuntimeException)
                    throw error;

                throw new RuntimeException(error.getMessage());
            }
        }
    }

    protected abstract void handleInitialize() throws RuntimeException;

    protected abstract void handlePostInitialize() throws RuntimeException;

    protected abstract void handlePreLoadData() throws RuntimeException;

    protected abstract void handlePostLoadData() throws RuntimeException;

    protected abstract void handleShutdown() throws RuntimeException;

    private interface StateAction {

        void run() throws RuntimeException;

    }

}

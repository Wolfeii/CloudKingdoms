package se.deepcloud.cloudkingdoms.listeners;

import com.google.common.collect.ImmutableMap;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.utilities.sorting.Singleton;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitListeners {

    private static final Pattern LISTENER_REGISTER_FAILURE =
            Pattern.compile("Plugin CloudKingdoms v(.*) has failed to register events for (.*) because (.*) does not exist\\.");

    private final Map<Class<?>, Singleton<Listener>> LISTENERS = new ImmutableMap.Builder<Class<?>, Singleton<Listener>>()
            .put(PlayersListener.class, new Singleton<>() {
                @Override
                protected Listener create() {
                    return new PlayersListener(plugin);
                }
            }).put(InventoryListener.class, new Singleton<>() {
                @Override
                protected Listener create() {
                    return new InventoryListener(plugin);
                }
            })
            .build();


    private final CloudKingdoms plugin;

    private String listenerRegisterFailure = "";

    public BukkitListeners(@NotNull CloudKingdoms plugin) {
        this.plugin = plugin;
    }

    public void register() {
        LISTENERS.values().forEach(listener -> safeEventsRegister(listener.get()));
    }

    public void registerListenerFailureFilter() {
        plugin.getLogger().setFilter(record -> {
            Matcher matcher = LISTENER_REGISTER_FAILURE.matcher(record.getMessage());
            if (matcher.find())
                listenerRegisterFailure = matcher.group(3);

            return true;
        });
    }

    public <E extends Listener> Singleton<E> getListener(Class<E> listenerClass) {
        Singleton<Listener> listener = LISTENERS.get(listenerClass);

        if (listener == null)
            throw new IllegalArgumentException("Listener class " + listenerClass + " is not a valid listener.");

        return (Singleton<E>) listener;
    }

    private void safeEventsRegister(Listener listener) {
        listenerRegisterFailure = "";
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        if (!listenerRegisterFailure.isEmpty())
            throw new RuntimeException(listenerRegisterFailure);
    }

}

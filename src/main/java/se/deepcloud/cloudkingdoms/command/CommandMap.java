package se.deepcloud.cloudkingdoms.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.utilities.sorting.SequentialListBuilder;

import java.util.*;

public abstract class CommandMap {

    private final Map<String, IKingdomCommand> subCommands = new LinkedHashMap<>();
    private final Map<String, IKingdomCommand> aliasesToCommand = new HashMap<>();

    private final CloudKingdoms plugin;

    protected CommandMap(@NotNull CloudKingdoms plugin) {
        this.plugin = plugin;
    }

    public abstract void loadDefaultCommands();

    public void registerCommand(@NotNull IKingdomCommand kingdomCommand, boolean sort) {
        List<String> aliases = new LinkedList<>(kingdomCommand.getAliases());
        String label = aliases.get(0).toLowerCase(Locale.ENGLISH);

        if (subCommands.containsKey(label)) {
            subCommands.remove(label);
            aliasesToCommand.values().removeIf(sC -> sC.getAliases().get(0).equals(aliases.get(0)));
        }
        subCommands.put(label, kingdomCommand);

        for (String alias : aliases) {
            aliasesToCommand.put(alias.toLowerCase(Locale.ENGLISH), kingdomCommand);
        }

        if (sort) {
            List<IKingdomCommand> superiorCommands = new LinkedList<>(subCommands.values());
            superiorCommands.sort(Comparator.comparing(o -> o.getAliases().get(0)));
            subCommands.clear();
            superiorCommands.forEach(s -> subCommands.put(s.getAliases().get(0), s));
        }
    }

    public void unregisterCommand(@NotNull IKingdomCommand kingdomCommand) {
        List<String> aliases = new LinkedList<>(kingdomCommand.getAliases());
        String label = aliases.get(0).toLowerCase(Locale.ENGLISH);

        subCommands.remove(label);
        aliasesToCommand.values().removeIf(sC -> sC.getAliases().get(0).equals(aliases.get(0)));
    }

    @Nullable
    public IKingdomCommand getCommand(String label) {
        label = label.toLowerCase(Locale.ENGLISH);
        return subCommands.getOrDefault(label, aliasesToCommand.get(label));
    }

    public List<IKingdomCommand> getSubCommands() {
        SequentialListBuilder<IKingdomCommand> listBuilder = new SequentialListBuilder<>();
        return listBuilder.build(this.subCommands.values());
    }
}

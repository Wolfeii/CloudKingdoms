package se.deepcloud.cloudkingdoms.command.player;

import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.command.CommandMap;

public class PlayerCommandMap extends CommandMap {

    public PlayerCommandMap(@NotNull CloudKingdoms plugin) {
        super(plugin);
    }

    @Override
    public void loadDefaultCommands() {
        registerCommand(new CommandClaim(), true);
    }

}

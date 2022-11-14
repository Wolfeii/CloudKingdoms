package se.deepcloud.cloudkingdoms.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.menus.impl.CreateMenu;
import se.deepcloud.cloudkingdoms.menus.impl.ManagerMenu;
import se.deepcloud.cloudkingdoms.message.Message;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.utilities.formatting.Formatters;
import se.deepcloud.cloudkingdoms.utilities.logging.Debug;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;
import se.deepcloud.cloudkingdoms.utilities.sorting.Pair;

import java.io.File;
import java.time.Duration;
import java.util.*;

public class CommandsManager {

    private static final String KINGDOMS_COMMAND = "kingdom";

    private final Map<UUID, Map<String, Long>> commandsCooldown = new HashMap<>();

    private final CloudKingdoms plugin;
    private final CommandMap playerCommandsMap;
    private final CommandMap adminCommandsMap;

    private Set<Runnable> pendingCommands = new HashSet<>();

    private PluginCommand pluginCommand;
    private String label = null;

    public CommandsManager(@NotNull CloudKingdoms plugin, @NotNull CommandMap playerCommandsMap, @NotNull CommandMap adminCommandsMap) {
        this.plugin = plugin;
        this.playerCommandsMap = playerCommandsMap;
        this.adminCommandsMap = adminCommandsMap;
    }

    public void loadData() {
        label = KINGDOMS_COMMAND.split(",")[0];

        pluginCommand = new PluginCommand(label);

        String[] commandSections = KINGDOMS_COMMAND.split(",");

        if (commandSections.length > 1) {
            pluginCommand.setAliases(Arrays.asList(Arrays.copyOfRange(commandSections, 1, commandSections.length)));
        }

        ((CraftServer) plugin.getServer()).getCommandMap().register("cloudkingdoms", pluginCommand);

        playerCommandsMap.loadDefaultCommands();
        adminCommandsMap.loadDefaultCommands();

        if (this.pendingCommands != null) {
            Set<Runnable> pendingCommands = new HashSet<>(this.pendingCommands);
            this.pendingCommands = null;
            pendingCommands.forEach(Runnable::run);
        }
    }

    public void registerCommand(IKingdomCommand kingdomCommand) {
        registerCommand(kingdomCommand, true);
    }

    public void unregisterCommand(IKingdomCommand superiorCommand) {
        playerCommandsMap.unregisterCommand(superiorCommand);
    }

    public void registerAdminCommand(IKingdomCommand superiorCommand) {
        if (pendingCommands != null) {
            pendingCommands.add(() -> registerAdminCommand(superiorCommand));
            return;
        }

        adminCommandsMap.registerCommand(superiorCommand, true);
    }

    public void unregisterAdminCommand(@NotNull IKingdomCommand superiorCommand) {
        adminCommandsMap.unregisterCommand(superiorCommand);
    }

    public List<IKingdomCommand> getSubCommands() {
        return playerCommandsMap.getSubCommands();
    }


    @Nullable
    public IKingdomCommand getCommand(String commandLabel) {
        return playerCommandsMap.getCommand(commandLabel);
    }

    public List<IKingdomCommand> getAdminSubCommands() {
        return adminCommandsMap.getSubCommands();
    }

    @Nullable
    public IKingdomCommand getAdminCommand(String commandLabel) {
        return adminCommandsMap.getCommand(commandLabel);
    }

    public void dispatchSubCommand(CommandSender sender, String subCommand) {
        dispatchSubCommand(sender, subCommand, "");
    }

    public void dispatchSubCommand(CommandSender sender, String subCommand, String args) {
        String[] argsSplit = args.split(" ");
        String[] commandArguments;

        if (argsSplit.length == 1 && argsSplit[0].isEmpty()) {
            commandArguments = new String[1];
            commandArguments[0] = subCommand;
        } else {
            commandArguments = new String[argsSplit.length + 1];
            commandArguments[0] = subCommand;
            System.arraycopy(argsSplit, 0, commandArguments, 1, argsSplit.length);
        }

        pluginCommand.execute(sender, "", commandArguments);
    }

    public String getLabel() {
        return label;
    }

    public void registerCommand(IKingdomCommand kingdomCommand, boolean sort) {
        if (pendingCommands != null) {
            pendingCommands.add(() -> registerCommand(kingdomCommand, sort));
            return;
        }

        playerCommandsMap.registerCommand(kingdomCommand, sort);
    }

    private class PluginCommand extends BukkitCommand {

        PluginCommand(String islandCommandLabel) {
            super(islandCommandLabel);
        }

        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            if (args.length > 0) {
                Log.debug(Debug.EXECUTE_COMMAND, "CommandsManager#PluginCommand", "execute",
                        sender.getName(), args[0]);

                IKingdomCommand command = playerCommandsMap.getCommand(args[0]);
                if (command != null) {
                    if (!(sender instanceof Player) && !command.canBeExecutedByConsole()) {
                        Message.CUSTOM.send(sender, "&cCan be executed only by players!", true);
                        return false;
                    }

                    if (!command.getPermission().isEmpty() && !sender.hasPermission(command.getPermission())) {
                        Log.debugResult(Debug.EXECUTE_COMMAND, "CommandsManager#PluginCommand", "execute",
                                "Return Missing Permission", command.getPermission());
                        Message.NO_COMMAND_PERMISSION.send(sender);
                        return false;
                    }

                    if (args.length < command.getMinArgs() || args.length > command.getMaxArgs()) {
                        Log.debugResult(Debug.EXECUTE_COMMAND, "CommandsManager#PluginCommand", "execute",
                                "Return Incorrect Usage", command.getUsage());
                        Message.COMMAND_USAGE.send(sender, getLabel() + " " + command.getUsage());
                        return false;
                    }

                    if (sender instanceof Player) {
                        UUID uuid = ((Player) sender).getUniqueId();
                        KingdomPlayer superiorPlayer = plugin.getPlayerManager().getKingdomPlayer(uuid);
                        if (!superiorPlayer.hasPermission("superior.admin.bypass.cooldowns")) {
                            Pair<Integer, String> commandCooldown = getCooldown(command);
                            if (commandCooldown != null) {
                                String commandLabel = command.getAliases().get(0);

                                Map<String, Long> playerCooldowns = commandsCooldown.get(uuid);
                                long timeNow = System.currentTimeMillis();

                                if (playerCooldowns != null) {
                                    Long timeToExecute = playerCooldowns.get(commandLabel);
                                    if (timeToExecute != null) {
                                        if (timeNow < timeToExecute) {
                                            String formattedTime = Formatters.TIME_FORMATTER.format(Duration.ofMillis(timeToExecute - timeNow));
                                            Log.debugResult(Debug.EXECUTE_COMMAND, "CommandsManager#PluginCommand", "execute",
                                                    "Return Cooldown", formattedTime);
                                            Message.COMMAND_COOLDOWN_FORMAT.send(sender, formattedTime);
                                            return false;
                                        }
                                    }
                                }

                                commandsCooldown.computeIfAbsent(uuid, u -> new HashMap<>()).put(commandLabel,
                                        timeNow + commandCooldown.getKey());
                            }
                        }
                    }

                    command.execute(plugin, sender, args);
                    return false;
                }
            }

            if (sender instanceof Player) {
                KingdomPlayer kingdomPlayer = plugin.getPlayerManager().getKingdomPlayer(sender);

                if (kingdomPlayer != null) {
                    Kingdom kingdom = kingdomPlayer.getKingdom();

                    if (kingdom == null) {
                        kingdomPlayer.openInventory(new CreateMenu(kingdomPlayer, null));
                    } else {
                        kingdomPlayer.openInventory(new ManagerMenu(kingdomPlayer, kingdom));
                    }

                    return false;
                }
            }

            Message.NO_COMMAND_PERMISSION.send(sender);

            return false;
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String label, String[] args) {
            if (args.length > 0) {
                IKingdomCommand command = playerCommandsMap.getCommand(args[0]);
                if (command != null) {
                    return command.getPermission() != null && !sender.hasPermission(command.getPermission()) ?
                            Collections.emptyList() : command.tabComplete(plugin, sender, args);
                }
            }

            List<String> list = new LinkedList<>();

            for (IKingdomCommand subCommand : getSubCommands()) {
                if (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission())) {
                    for (String aliases : subCommand.getAliases()) {
                        if (aliases.contains(args[0].toLowerCase(Locale.ENGLISH))) {
                            list.add(aliases);
                        }
                    }
                }
            }

            return list;
        }

    }

    @Nullable
    private Pair<Integer, String> getCooldown(@NotNull IKingdomCommand command) {
        return command.getCooldowns();
    }

}
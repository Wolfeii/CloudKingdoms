package se.deepcloud.cloudkingdoms.command.arguments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.message.Message;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.utilities.formatting.Formatters;

import java.util.*;

public class CommandArgument {

    private CommandArgument() {

    }

    public static KingdomsArgument getKingdom(@NotNull CloudKingdoms plugin, CommandSender sender, String argument) {
        KingdomPlayer targetPlayer = plugin.getPlayerManager().getKingdomPlayer(argument);
        Kingdom kingdom = targetPlayer == null ? plugin.getKingdomManager().getKingdom(argument) : targetPlayer.getKingdom();

        if (kingdom == null) {
            if (argument.equalsIgnoreCase(sender.getName()))
                Message.INVALID_KINGDOM.send(sender);
            else if (targetPlayer == null)
                Message.INVALID_KINGDOM_OTHER_NAME.send(sender, Formatters.STRIP_COLOR_FORMATTER.format(argument));
            else
                Message.INVALID_KINGDOM_OTHER.send(sender, targetPlayer.getName());
        }

        return new KingdomsArgument(kingdom, targetPlayer);
    }

    public static KingdomsListArgument getMultipleKingdoms(@NotNull CloudKingdoms plugin, CommandSender sender, String argument) {
        List<Kingdom> kingdoms = new LinkedList<>();
        KingdomPlayer targetPlayer;

        if (argument.equals("*")) {
            targetPlayer = null;
            kingdoms = plugin.getKingdomManager().getKingdoms();
        } else {
            KingdomsArgument arguments = getKingdom(plugin, sender, argument);
            targetPlayer = arguments.getKingdomPlayer();
            if (arguments.getKingdom() != null)
                kingdoms.add(arguments.getKingdom());
        }

        return new KingdomsListArgument(Collections.unmodifiableList(kingdoms), targetPlayer);
    }

    public static KingdomsArgument getSenderKingdom(@NotNull CloudKingdoms plugin, CommandSender sender) {
        KingdomPlayer kingdomPlayer = plugin.getPlayerManager().getKingdomPlayer(sender);
        Kingdom kingdom = kingdomPlayer.getKingdom();

        if (kingdom == null)
            Message.INVALID_KINGDOM.send(kingdomPlayer);

        return new KingdomsArgument(kingdom, kingdomPlayer);
    }

    public static KingdomPlayer getPlayer(@NotNull CloudKingdoms plugin, KingdomPlayer kingdomPlayer, String argument) {
        return getPlayer(plugin, kingdomPlayer.asPlayer(), argument);
    }

    public static KingdomPlayer getPlayer(@NotNull CloudKingdoms plugin, CommandSender sender, String argument) {
        KingdomPlayer targetPlayer = plugin.getPlayerManager().getKingdomPlayer(argument);

        if (targetPlayer == null)
            Message.INVALID_PLAYER.send(sender, argument);

        return targetPlayer;
    }

    public static List<KingdomPlayer> getMultiplePlayers(@NotNull CloudKingdoms plugin, CommandSender sender, String argument) {
        List<KingdomPlayer> players = new LinkedList<>();

        if (argument.equals("*")) {
            players = plugin.getPlayerManager().getAllPlayers();
        } else {
            KingdomPlayer targetPlayer = getPlayer(plugin, sender, argument);
            if (targetPlayer != null)
                players.add(targetPlayer);
        }

        return Collections.unmodifiableList(players);
    }

    public static KingdomsArgument getKingdomWhereStanding(@NotNull CloudKingdoms plugin, CommandSender sender) {
        if (!(sender instanceof Player)) {
            Message.CUSTOM.send(sender, "&cYou must specify a player's name.", true);
            return KingdomsArgument.EMPTY;
        }

        KingdomPlayer kingdomPlayer = plugin.getPlayerManager().getKingdomPlayer(sender);
        Kingdom locationKingdom = plugin.getKingdomManager().getKingdomAt(kingdomPlayer.getLocation());
        Kingdom kingdom = locationKingdom == null ? kingdomPlayer.getKingdom() : locationKingdom;

        if (kingdom == null)
            Message.INVALID_KINGDOM.send(sender);

        return new KingdomsArgument(kingdom, kingdomPlayer);
    }


    public static String buildLongString(String[] args, int start, boolean colorize) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = start; i < args.length; i++)
            stringBuilder.append(" ").append(args[i]);

        return colorize ? Formatters.COLOR_FORMATTER.format(stringBuilder.substring(1)) : stringBuilder.substring(1);
    }



    public static NumberArgument<Integer> getAmount(CommandSender sender, String argument) {
        return getInt(sender, argument, Message.INVALID_AMOUNT);
    }

    public static NumberArgument<Double> getMultiplier(CommandSender sender, String argument) {
        double multiplier = 0;
        boolean status = true;

        try {
            multiplier = Double.parseDouble(argument);
            // Makes sure the multiplier is rounded.
            multiplier = Math.round(multiplier * 100) / 100D;
        } catch (IllegalArgumentException ex) {
            Message.INVALID_MULTIPLIER.send(sender, argument);
            status = false;
        }

        return new NumberArgument<>(multiplier, status);
    }

    public static NumberArgument<Integer> getSize(CommandSender sender, String argument) {
        return getInt(sender, argument, Message.INVALID_SIZE);
    }


    public static Location getLocation(CommandSender sender, World world, String x, String y, String z) {
        Location location = null;

        try {
            location = new Location(world, Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
        } catch (Throwable ex) {
            Message.INVALID_BLOCK.send(sender, world.getName() + ", " + x + ", " + y + ", " + z);
        }

        return location;
    }

    public static NumberArgument<Integer> getPage(CommandSender sender, String argument) {
        return getInt(sender, argument, Message.INVALID_PAGE);
    }

    public static NumberArgument<Integer> getInterval(CommandSender sender, String argument) {
        NumberArgument<Integer> interval = getInt(sender, argument, Message.INVALID_INTERVAL);

        if (interval.isSucceed() && interval.getNumber() < 0) {
            Message.INVALID_INTERVAL.send(sender, argument);
            return new NumberArgument<>(interval.getNumber(), false);
        }

        return interval;
    }

    public static Map<String, String> parseArguments(String[] args) {
        Map<String, String> parsedArgs = new HashMap<>();
        String currentKey = null;
        StringBuilder stringBuilder = new StringBuilder();

        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (currentKey != null && stringBuilder.length() > 0) {
                    parsedArgs.put(currentKey, stringBuilder.substring(1));
                }

                currentKey = arg.substring(1).toLowerCase(Locale.ENGLISH);
                stringBuilder = new StringBuilder();
            } else if (currentKey != null) {
                stringBuilder.append(" ").append(arg);
            }
        }

        if (currentKey != null && stringBuilder.length() > 0) {
            parsedArgs.put(currentKey, stringBuilder.substring(1));
        }

        return parsedArgs;
    }

    private static NumberArgument<Integer> getInt(CommandSender sender, String argument, Message message) {
        int i = 0;
        boolean status = true;

        try {
            i = Integer.parseInt(argument);
        } catch (IllegalArgumentException ex) {
            message.send(sender, argument);

            status = false;
        }

        return new NumberArgument<>(i, status);
    }

}
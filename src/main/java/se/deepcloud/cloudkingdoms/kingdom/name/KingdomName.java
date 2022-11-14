package se.deepcloud.cloudkingdoms.kingdom.name;

import org.bukkit.command.CommandSender;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.kingdom.Kingdom;
import se.deepcloud.cloudkingdoms.message.Message;
import se.deepcloud.cloudkingdoms.utilities.formatting.Formatters;

import java.util.Locale;

public class KingdomName {

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    private KingdomName() {

    }

    public static boolean isValidName(CommandSender sender, Kingdom currentKingdom, String kingdomName) {
        if (kingdomName.length() > 16) {
            Message.NAME_TOO_LONG.send(sender);
            return false;
        }

        if (kingdomName.length() < 3) {
            Message.NAME_TOO_SHORT.send(sender);
            return false;
        }

        if (plugin.getPlayerManager().getKingdomPlayer(kingdomName) != null) {
            Message.NAME_SAME_AS_PLAYER.send(sender);
            return false;
        }

        if (currentKingdom != null && currentKingdom.getName().equalsIgnoreCase(kingdomName)) {
            Message.SAME_NAME_CHANGE.send(sender);
            return false;
        }

        if (plugin.getKingdomManager().getKingdom(kingdomName) != null) {
            Message.KINGDOM_ALREADY_EXIST.send(sender);
            return false;
        }

        return true;
    }
}

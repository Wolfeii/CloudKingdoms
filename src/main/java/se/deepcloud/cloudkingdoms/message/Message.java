package se.deepcloud.cloudkingdoms.message;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.command.IKingdomCommand;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.utilities.formatting.Formatters;
import se.deepcloud.cloudkingdoms.utilities.logging.Log;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public enum Message {

    ADMIN_ADD_PLAYER,
    ADMIN_ADD_PLAYER_NAME,
    ADMIN_DEPOSIT_MONEY,
    ADMIN_DEPOSIT_MONEY_NAME,
    ADMIN_HELP_FOOTER,
    ADMIN_HELP_HEADER,
    ADMIN_HELP_LINE,
    ADMIN_HELP_NEXT_PAGE,
    ALREADY_IN_KINGDOM,
    ALREADY_IN_KINGDOM_OTHER,
    BANK_DEPOSIT_COMPLETED,
    BANK_DEPOSIT_CUSTOM,
    BANK_WITHDRAW_COMPLETED,
    BANK_WITHDRAW_CUSTOM,
    BANNED_FROM_KINGDOM,
    BAN_ANNOUNCEMENT,
    BAN_PLAYERS_WITH_LOWER_ROLE,
    CANNOT_SET_ROLE,
    CHANGED_KINGDOM_EFFECT_LEVEL,
    CHANGED_KINGDOM_EFFECT_LEVEL_ALL,
    CHANGED_KINGDOM_EFFECT_LEVEL_NAME,
    CHANGED_HOME_LOCATION,
    CHANGE_PERMISSION_FOR_HIGHER_ROLE,
    CHUNK_SUCCESSFULLY_CLAIMED,
    CHUNK_ALREADY_CLAIMED,
    COMMAND_ARGUMENT_ALL_KINGDOMS("*"),
    COMMAND_ARGUMENT_ALL_PLAYERS("*"),
    COMMAND_ARGUMENT_AMOUNT("amount"),
    COMMAND_ARGUMENT_COMMAND("command..."),
    COMMAND_ARGUMENT_EFFECT("effect"),
    COMMAND_ARGUMENT_ENTITY("entity"),
    COMMAND_ARGUMENT_KINGDOM_NAME("kingdom-namn"),
    COMMAND_ARGUMENT_KINGDOM_ROLE("kingdom-roll"),
    COMMAND_ARGUMENT_LEADER("ägare"),
    COMMAND_ARGUMENT_LEVEL("level"),
    COMMAND_ARGUMENT_MATERIAL("material"),
    COMMAND_ARGUMENT_MENU("menu-name"),
    COMMAND_ARGUMENT_MESSAGE("message..."),
    COMMAND_ARGUMENT_MODULE_NAME("module-name"),
    COMMAND_ARGUMENT_MULTIPLIER("multiplier"),
    COMMAND_ARGUMENT_NEW_LEADER("ny-ägare"),
    COMMAND_ARGUMENT_PAGE("page"),
    COMMAND_ARGUMENT_PATH("path"),
    COMMAND_ARGUMENT_PERMISSION("permission"),
    COMMAND_ARGUMENT_PLAYER_NAME("spelar-namn"),
    COMMAND_ARGUMENT_PRIVATE("private"),
    COMMAND_ARGUMENT_TIME("time"),
    COMMAND_ARGUMENT_TITLE_DURATION("duration"),
    COMMAND_ARGUMENT_TITLE_FADE_IN("fade-in"),
    COMMAND_ARGUMENT_TITLE_FADE_OUT("fade-out"),
    COMMAND_ARGUMENT_VALUE("value"),
    COMMAND_COOLDOWN_FORMAT,
    COMMAND_DESCRIPTION_ACCEPT,
    COMMAND_DESCRIPTION_ADMIN,
    COMMAND_DESCRIPTION_ADMIN_ADD,
    COMMAND_DESCRIPTION_ADMIN_BYPASS,
    COMMAND_DESCRIPTION_ADMIN_VAULT,
    COMMAND_DESCRIPTION_ADMIN_CLOSE,
    COMMAND_DESCRIPTION_ADMIN_CMD_ALL,
    COMMAND_DESCRIPTION_ADMIN_COUNT,
    COMMAND_DESCRIPTION_ADMIN_DATA,
    COMMAND_DESCRIPTION_ADMIN_DEBUG,
    COMMAND_DESCRIPTION_ADMIN_DEMOTE,
    COMMAND_DESCRIPTION_ADMIN_DEPOSIT,
    COMMAND_DESCRIPTION_ADMIN_DISBAND,
    COMMAND_DESCRIPTION_ADMIN_IGNORE,
    COMMAND_DESCRIPTION_ADMIN_JOIN,
    COMMAND_DESCRIPTION_ADMIN_KICK,
    COMMAND_DESCRIPTION_ADMIN_MISSION,
    COMMAND_DESCRIPTION_ADMIN_MODULES,
    COMMAND_DESCRIPTION_ADMIN_MSG,
    COMMAND_DESCRIPTION_ADMIN_MSG_ALL,
    COMMAND_DESCRIPTION_ADMIN_NAME,
    COMMAND_DESCRIPTION_ADMIN_OPEN,
    COMMAND_DESCRIPTION_ADMIN_OPEN_MENU,
    COMMAND_DESCRIPTION_ADMIN_PROMOTE,
    COMMAND_DESCRIPTION_ADMIN_PURGE,
    COMMAND_DESCRIPTION_ADMIN_RANKUP,
    COMMAND_DESCRIPTION_ADMIN_RELOAD,
    COMMAND_DESCRIPTION_ADMIN_SETTINGS,
    COMMAND_DESCRIPTION_ADMIN_SET_PERMISSION,
    COMMAND_DESCRIPTION_ADMIN_SHOW,
    COMMAND_DESCRIPTION_ADMIN_SPAWN,
    COMMAND_DESCRIPTION_ADMIN_SPY,
    COMMAND_DESCRIPTION_ADMIN_STATS,
    COMMAND_DESCRIPTION_ADMIN_TELEPORT,
    COMMAND_DESCRIPTION_ADMIN_TITLE,
    COMMAND_DESCRIPTION_ADMIN_TITLE_ALL,
    COMMAND_DESCRIPTION_ADMIN_UNIGNORE,
    COMMAND_DESCRIPTION_ADMIN_UNLOCK_WORLD,
    COMMAND_DESCRIPTION_ADMIN_WITHDRAW,
    COMMAND_DESCRIPTION_BALANCE,
    COMMAND_DESCRIPTION_BAN,
    COMMAND_DESCRIPTION_BANK,
    COMMAND_DESCRIPTION_VAULT,
    COMMAND_DESCRIPTION_CLOSE,
    COMMAND_DESCRIPTION_COOP,
    COMMAND_DESCRIPTION_COOPS,
    COMMAND_DESCRIPTION_COUNTS,
    COMMAND_DESCRIPTION_CLAIM,
    COMMAND_DESCRIPTION_CREATE,
    COMMAND_DESCRIPTION_DEMOTE,
    COMMAND_DESCRIPTION_DEPOSIT,
    COMMAND_DESCRIPTION_FLY,
    COMMAND_DESCRIPTION_HELP,
    COMMAND_DESCRIPTION_INVITE,
    COMMAND_DESCRIPTION_KICK,
    COMMAND_DESCRIPTION_LANG,
    COMMAND_DESCRIPTION_LEAVE,
    COMMAND_DESCRIPTION_MEMBERS,
    COMMAND_DESCRIPTION_MISSION,
    COMMAND_DESCRIPTION_MISSIONS,
    COMMAND_DESCRIPTION_NAME,
    COMMAND_DESCRIPTION_OPEN,
    COMMAND_DESCRIPTION_PANEL,
    COMMAND_DESCRIPTION_PARDON,
    COMMAND_DESCRIPTION_PERMISSIONS,
    COMMAND_DESCRIPTION_PROMOTE,
    COMMAND_DESCRIPTION_RANKUP,
    COMMAND_DESCRIPTION_RATE,
    COMMAND_DESCRIPTION_SETTINGS,
    COMMAND_DESCRIPTION_SET_ROLE,
    COMMAND_DESCRIPTION_SET_HOME,
    COMMAND_DESCRIPTION_SHOW,
    COMMAND_DESCRIPTION_TEAM,
    COMMAND_DESCRIPTION_TEAM_CHAT,
    COMMAND_DESCRIPTION_TELEPORT,
    COMMAND_DESCRIPTION_TOGGLE,
    COMMAND_DESCRIPTION_TOP,
    COMMAND_DESCRIPTION_TRANSFER,
    COMMAND_DESCRIPTION_UNCOOP,
    COMMAND_DESCRIPTION_UPGRADE,
    COMMAND_DESCRIPTION_VALUE,
    COMMAND_DESCRIPTION_VALUES,
    COMMAND_DESCRIPTION_VISIT,
    COMMAND_DESCRIPTION_WITHDRAW,
    COMMAND_USAGE,
    COOP_ANNOUNCEMENT,
    COOP_BANNED_PLAYER,
    CREATE_KINGDOM,
    CREATE_KINGDOM_CANCELLED,
    CREATE_KINGDOM_FAILURE,
    CREATE_WORLD_FAILURE,
    DEBUG_MODE_DISABLED,
    DEBUG_MODE_ENABLED,
    DEBUG_MODE_FILTER_ADD,
    DEBUG_MODE_FILTER_CLEAR,
    DEBUG_MODE_FILTER_REMOVE,
    DEMOTED_MEMBER,
    DEMOTE_LEADER,
    DEMOTE_PLAYERS_WITH_LOWER_ROLE,
    DEPOSIT_ANNOUNCEMENT,
    DEPOSIT_ERROR,
    DESTROY_OUTSIDE_KINGDOM,
    DISBANDED_KINGDOM,
    DISBANDED_KINGDOM_OTHER,
    DISBANDED_KINGDOM_OTHER_NAME,
    DISBAND_ANNOUNCEMENT,
    DISBAND_GIVE,
    DISBAND_GIVE_ALL,
    DISBAND_GIVE_OTHER,
    DISBAND_KINGDOM_BALANCE_REFUND,
    DISBAND_SET,
    DISBAND_SET_ALL,
    DISBAND_SET_OTHER,
    ENTER_PVP_KINGDOM,
    EXPELLED_PLAYER,
    FORMAT_BILLION,
    FORMAT_DAYS_NAME,
    FORMAT_DAY_NAME,
    FORMAT_HOURS_NAME,
    FORMAT_HOUR_NAME,
    FORMAT_MILLION,
    FORMAT_MINUTES_NAME,
    FORMAT_MINUTE_NAME,
    FORMAT_QUAD,
    FORMAT_SECONDS_NAME,
    FORMAT_SECOND_NAME,
    FORMAT_THOUSANDS,
    FORMAT_TRILLION,
    GLOBAL_COMMAND_EXECUTED,
    GLOBAL_COMMAND_EXECUTED_NAME,
    GLOBAL_MESSAGE_SENT,
    GLOBAL_MESSAGE_SENT_NAME,
    GLOBAL_TITLE_SENT,
    GLOBAL_TITLE_SENT_NAME,
    GOT_BANNED,
    GOT_DEMOTED,
    GOT_EXPELLED,
    GOT_INVITE {
        @Override
        public void send(@NotNull CommandSender sender, Object... args) {
            if (!(sender instanceof Player)) {
                super.send(sender, args);
            } else {
                String message = getMessage();

                if (message == null)
                    return;

                BaseComponent[] baseComponents = TextComponent.fromLegacyText(message);
                if (!GOT_INVITE_TOOLTIP.isEmpty()) {
                    for (BaseComponent baseComponent : baseComponents)
                        baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new TextComponent[]{new TextComponent(GOT_INVITE_TOOLTIP.getMessage())})));
                }

                for (BaseComponent baseComponent : baseComponents)
                    baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kingdom accept " + args[0]));

                send(sender, args);
            }
        }
    },
    GOT_INVITE_TOOLTIP,
    GOT_KICKED,
    GOT_PROMOTED,
    GOT_REVOKED,
    GOT_UNBANNED,
    HIT_KINGDOM_MEMBER,
    HIT_PLAYER_IN_KINGDOM,
    IGNORED_KINGDOM,
    IGNORED_KINGDOM_NAME,
    INVALID_AMOUNT,
    INVALID_BLOCK,
    INVALID_ENTITY,
    INVALID_INTERVAL,
    INVALID_KINGDOM,
    INVALID_KINGDOM_HOME,
    INVALID_KINGDOM_OTHER,
    INVALID_KINGDOM_OTHER_NAME,
    INVALID_KINGDOM_PERMISSION,
    INVALID_MISSION,
    INVALID_MODULE,
    INVALID_MULTIPLIER,
    INVALID_PAGE,
    INVALID_PERCENTAGE,
    INVALID_PLAYER,
    INVALID_ROLE,
    INVALID_SETTINGS,
    INVALID_SIZE,
    INVALID_TOGGLE_MODE,
    INVALID_UPGRADE,
    INVALID_HOME_LOCATION,
    INVALID_HOME_LOCATION_BYPASS,
    INVALID_WORLD,
    INVITE_ANNOUNCEMENT,
    INVITE_BANNED_PLAYER,
    INVITE_TO_FULL_KINGDOM,
    KINGDOM_ALREADY_EXIST,
    KINGDOM_BANK_EMPTY,
    KINGDOM_BANK_SHOW,
    KINGDOM_BANK_SHOW_OTHER,
    KINGDOM_BANK_SHOW_OTHER_NAME,
    KINGDOM_CLOSED,
    KINGDOM_CREATE_PROCCESS_REQUEST,
    KINGDOM_CREATE_PROCESS_FAIL,
    KINGDOM_FLY_DISABLED,
    KINGDOM_FLY_ENABLED,
    KINGDOM_GOT_DELETED_WHILE_INSIDE,
    KINGDOM_GOT_PVP_ENABLED_WHILE_INSIDE,
    KINGDOM_HELP_FOOTER,
    KINGDOM_HELP_HEADER,
    KINGDOM_HELP_LINE,
    KINGDOM_HELP_NEXT_PAGE,
    KINGDOM_OPENED,
    KINGDOM_PREVIEW_CANCEL,
    KINGDOM_PREVIEW_CANCEL_DISTANCE,
    KINGDOM_PREVIEW_CANCEL_TEXT,
    KINGDOM_PREVIEW_CONFIRM_TEXT,
    KINGDOM_PREVIEW_START,
    KINGDOM_PROTECTED,
    KINGDOM_PROTECTED_OPPED,
    KINGDOM_TEAM_STATUS_FOOTER,
    KINGDOM_TEAM_STATUS_HEADER,
    KINGDOM_TEAM_STATUS_OFFLINE,
    KINGDOM_TEAM_STATUS_ONLINE,
    KINGDOM_TEAM_STATUS_ROLES,
    KINGDOM_TOP_STATUS_OFFLINE,
    KINGDOM_TOP_STATUS_ONLINE,
    KINGDOM_WARP_PRIVATE,
    KINGDOM_WARP_PUBLIC,
    KINGDOM_WAS_CLOSED,
    KINGDOM_WORTH_ERROR,
    KINGDOM_WORTH_RESULT,
    KINGDOM_WORTH_TIME_OUT,
    JOINED_KINGDOM,
    JOINED_KINGDOM_AS_COOP,
    JOINED_KINGDOM_AS_COOP_NAME,
    JOINED_KINGDOM_NAME,
    JOIN_ADMIN_ANNOUNCEMENT,
    JOIN_ANNOUNCEMENT,
    JOIN_FULL_KINGDOM,
    JOIN_WHILE_IN_KINGDOM,
    KICK_ANNOUNCEMENT,
    KICK_KINGDOM_LEADER,
    KICK_PLAYERS_WITH_LOWER_ROLE,
    LACK_CHANGE_PERMISSION,
    LAST_ROLE_DEMOTE,
    LAST_ROLE_PROMOTE,
    LEAVE_ANNOUNCEMENT,
    LEAVE_KINGDOM_AS_LEADER,
    LEFT_KINGDOM,
    LEFT_KINGDOM_COOP,
    LEFT_KINGDOM_COOP_NAME,
    MATERIAL_NOT_SOLID,
    MAXIMUM_LEVEL,
    MESSAGE_SENT,
    MISSION_CANNOT_COMPLETE,
    MISSION_NOT_COMPLETE_REQUIRED_MISSIONS,
    MISSION_NO_AUTO_REWARD,
    MISSION_STATUS_COMPLETE,
    MISSION_STATUS_COMPLETE_ALL,
    MISSION_STATUS_RESET,
    MISSION_STATUS_RESET_ALL,
    MODULES_LIST,
    MODULES_LIST_MODULE_NAME,
    MODULES_LIST_SEPARATOR,
    MODULE_ALREADY_INITIALIZED,
    MODULE_INFO,
    MODULE_LOADED_FAILURE,
    MODULE_LOADED_SUCCESS,
    MODULE_UNLOADED_SUCCESS,
    NAME_ANNOUNCEMENT,
    NAME_BLACKLISTED,
    NAME_CHAT_FORMAT,
    NAME_SAME_AS_PLAYER,
    NAME_TOO_LONG,
    NAME_TOO_SHORT,
    NOT_ENOUGH_MONEY_TO_DEPOSIT,
    NOT_ENOUGH_MONEY_TO_UPGRADE,
    NOT_ENOUGH_MONEY_TO_WARP,
    NO_BAN_PERMISSION,
    NO_CLOSE_BYPASS,
    NO_CLAIM_PERMISSION,
    NO_CLOSE_PERMISSION,
    NO_COMMAND_PERMISSION,
    NO_COOP_PERMISSION,
    NO_DELETE_WARP_PERMISSION,
    NO_DEMOTE_PERMISSION,
    NO_DEPOSIT_PERMISSION,
    NO_DISBAND_PERMISSION,
    NO_EXPEL_PERMISSION,
    NO_INVITE_PERMISSION,
    NO_KINGDOMS_TO_PURGE,
    NO_KINGDOM_CHEST_PERMISSION,
    NO_KINGDOM_INVITE,
    NO_KICK_PERMISSION,
    NO_MORE_DISBANDS,
    NO_MORE_WARPS,
    NO_NAME_PERMISSION,
    NO_OPEN_PERMISSION,
    NO_PERMISSION_CHECK_PERMISSION,
    NO_PROMOTE_PERMISSION,
    NO_RANKUP_PERMISSION,
    NO_RATINGS_PERMISSION,
    NO_SET_HOME_PERMISSION,
    NO_SET_SETTINGS_PERMISSION,
    NO_TRANSFER_PERMISSION,
    NO_UNCOOP_PERMISSION,
    NO_UPGRADE_PERMISSION,
    NO_WITHDRAW_PERMISSION,
    OPEN_MENU_WHILE_SLEEPING,
    PANEL_TOGGLE_OFF,
    PANEL_TOGGLE_ON,
    PERMISSIONS_RESET_PLAYER,
    PERMISSIONS_RESET_ROLES,
    PERMISSION_CHANGED,
    PERMISSION_CHANGED_ALL,
    PERMISSION_CHANGED_NAME,
    PERSISTENT_DATA_EMPTY,
    PERSISTENT_DATA_MODIFY,
    PERSISTENT_DATA_SHOW,
    PERSISTENT_DATA_SHOW_PATH,
    PERSISTENT_DATA_SHOW_VALUE,
    PERSISTENT_DATA_SHOW_SPACER,
    PERSISTENT_DATA_REMOVED,
    PLACEHOLDER_NO,
    PLACEHOLDER_YES,
    PLAYER_ALREADY_BANNED,
    PLAYER_ALREADY_COOP,
    PLAYER_ALREADY_IN_KINGDOM,
    PLAYER_ALREADY_IN_ROLE,
    PLAYER_EXPEL_BYPASS,
    PLAYER_JOIN_ANNOUNCEMENT,
    PLAYER_NOT_BANNED,
    PLAYER_NOT_COOP,
    PLAYER_NOT_INSIDE_KINGDOM,
    PLAYER_NOT_ONLINE,
    PLAYER_QUIT_ANNOUNCEMENT,
    PROMOTED_MEMBER,
    PROMOTE_PLAYERS_WITH_LOWER_ROLE,
    PURGED_KINGDOMS,
    PURGE_CLEAR,
    RELOAD_COMPLETED,
    RELOAD_PROCCESS_REQUEST,
    REVOKE_INVITE_ANNOUNCEMENT,
    SAME_NAME_CHANGE,
    SELF_ROLE_CHANGE,
    SETTINGS_UPDATED,
    SETTINGS_UPDATED_ALL,
    SETTINGS_UPDATED_NAME,
    SET_UPGRADE_LEVEL,
    SET_UPGRADE_LEVEL_NAME,
    SET_WARP,
    SET_WARP_OUTSIDE,
    SIZE_BIGGER_MAX,
    SPAWN_SET_SUCCESS,
    SPAWN_TELEPORT_SUCCESS,
    SPY_TEAM_CHAT_FORMAT,
    TEAM_CHAT_FORMAT,
    TELEPORTED_FAILED,
    TELEPORTED_SUCCESS,
    TELEPORTED_TO_WARP,
    TELEPORTED_TO_WARP_ANNOUNCEMENT,
    TELEPORT_LOCATION_OUTSIDE_KINGDOM,
    TELEPORT_OUTSIDE_KINGDOM,
    TELEPORT_WARMUP,
    TELEPORT_WARMUP_CANCEL,
    TITLE_SENT,
    TOGGLED_BYPASS_OFF,
    TOGGLED_BYPASS_ON,
    TOGGLED_FLY_OFF,
    TOGGLED_FLY_ON,
    TOGGLED_SCHEMATIC_OFF,
    TOGGLED_SCHEMATIC_ON,
    TOGGLED_SPY_OFF,
    TOGGLED_SPY_ON,
    TOGGLED_STACKED_BLOCKS_OFF,
    TOGGLED_STACKED_BLOCKS_ON,
    TOGGLED_TEAM_CHAT_OFF,
    TOGGLED_TEAM_CHAT_ON,
    TOGGLED_WORLD_BORDER_OFF,
    TOGGLED_WORLD_BORDER_ON,
    TRANSFER_ADMIN,
    TRANSFER_ADMIN_ALREADY_LEADER,
    TRANSFER_ADMIN_DIFFERENT_KINGDOM,
    TRANSFER_ADMIN_NOT_LEADER,
    TRANSFER_ALREADY_LEADER,
    TRANSFER_BROADCAST,
    UNBAN_ANNOUNCEMENT,
    UNCOOP_ANNOUNCEMENT,
    UNCOOP_AUTO_ANNOUNCEMENT,
    UNCOOP_LEFT_ANNOUNCEMENT,
    UNIGNORED_KINGDOM,
    UNIGNORED_KINGDOM_NAME,
    UNLOCK_WORLD_ANNOUNCEMENT,
    UNSAFE_WARP,
    UPDATED_PERMISSION,
    UPDATED_SETTINGS,
    UPGRADE_COOLDOWN_FORMAT,
    VISITOR_BLOCK_COMMAND,
    WITHDRAWN_MONEY,
    WITHDRAWN_MONEY_NAME,
    WITHDRAW_ALL_MONEY,
    WITHDRAW_ANNOUNCEMENT,
    WITHDRAW_ERROR,
    WORLD_NOT_ENABLED,
    WORLD_NOT_UNLOCKED,

    PROTECTION(true) {

        @Nullable
        private Collection<UUID> noInteractMessages;

        @Override
        public void send(CommandSender sender, Object... args) {
            if (!(sender instanceof Player))
                return;

            UUID playerUUID = ((Player) sender).getUniqueId();

            if (noInteractMessages != null && noInteractMessages.add(playerUUID)) {
                Message.KINGDOM_PROTECTED.send(sender, args);

                IKingdomCommand bypassCommand = plugin.getCommandsManager().getAdminCommand("bypass");

                if (bypassCommand != null && sender.hasPermission(bypassCommand.getPermission()))
                    Message.KINGDOM_PROTECTED_OPPED.send(sender, args);
            }
        }
    },

    CUSTOM(true) {
        @Override
        public void send(@NotNull CommandSender sender, Object... args) {
            String message = args.length == 0 ? null : args[0] == null ? null : args[0].toString();
            boolean translateColors = args.length >= 2 && args[1] instanceof Boolean && (boolean) args[1];
            if (message != null && !message.isEmpty())
                sender.sendMessage(translateColors ? Formatters.COLOR_FORMATTER.format(message) : message);
        }
    };

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    private final String defaultMessage;
    private final boolean isCustom;

    private String message;

    Message() {
        this(null);
    }

    Message(boolean isCustom) {
        this(null, isCustom);
    }

    Message(String defaultMessage) {
        this(defaultMessage, false);
    }

    Message(String defaultMessage, boolean isCustom) {
        this.defaultMessage = defaultMessage;
        this.isCustom = isCustom;
    }

    public static void reload() {
        Log.info("Laddar om meddelanden...");
        long startTime = System.currentTimeMillis();

        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(messagesFile);
        int messagesAmount = 0;

        for (Message message : values()) {
            if (!message.isCustom()) {
                String coloredMessage = Formatters.COLOR_FORMATTER.format(configuration.getString(message.name()));
                if (coloredMessage != null) {
                    message.setMessage(coloredMessage);
                    messagesAmount++;
                }
            }
        }

        Log.info(" - Hittade " + messagesAmount + " i din messages.yml fil.");
        Log.info("Laddade meddelanden (Tog " + (System.currentTimeMillis() - startTime) + "ms)");
    }

    public final void send(@NotNull KingdomPlayer kingdomPlayer, Object... arguments) {
        kingdomPlayer.runIfOnline(player -> send(player, arguments));
    }

    public void send(@NotNull CommandSender sender, Object... objects) {
        replaceArgs(message, objects).ifPresent(sender::sendMessage);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isEmpty() {
        return message.isEmpty();
    }

    public boolean isCustom() {
        return isCustom;
    }

    public static Optional<String> replaceArgs(String msg, Object... objects) {
        if (msg == null || msg.isEmpty())
            return Optional.empty();

        for (int i = 0; i < objects.length; i++) {
            String objectString = objects[i] instanceof BigDecimal ?
                    Formatters.NUMBER_FORMATTER.format((BigDecimal) objects[i]) : objects[i].toString();
            msg = msg.replace("{" + i + "}", objectString);
        }

        return msg.isEmpty() ? Optional.empty() : Optional.of(msg);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}

package se.deepcloud.cloudkingdoms.menus.items;

import org.bukkit.Material;
import se.deepcloud.cloudkingdoms.utilities.itemstack.ItemBuilder;
import se.deepcloud.cloudkingdoms.utilities.itemstack.TemplateItem;

public class MenuItems {

    public static final TemplateItem SEPERATOR = new TemplateItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
            .withName("&7DeepCloud"));
    
    public static final TemplateItem BACK = new TemplateItem(new ItemBuilder(Material.ENDER_PEARL)
            .withName("&dGå Tillbaka")
            .withLore("&7Gå tillbaka till föregående meny"));
    
    public static final TemplateItem CLOSE = new TemplateItem(new ItemBuilder(Material.ARROW)
            .withName("&cStäng")
            .withLore("&7Tryck för att stänga menyn"));
    
    public static final TemplateItem PREVIOUS = new TemplateItem(new ItemBuilder(Material.RED_WOOL)
            .withName("&cFöregående Sida"));
    
    public static final TemplateItem NEXT = new TemplateItem(new ItemBuilder(Material.LIME_WOOL)
            .withName("&aNästa Sida"));
    
    public static final TemplateItem CREATE_CREATE = new TemplateItem(new ItemBuilder(Material.YELLOW_BED)
            .withName("&aSkapa ett Kingdom")
            .withLore("&7Tryck för att skapa ett Kingdom"));
    
    public static final TemplateItem CREATE_INFO = new TemplateItem(new ItemBuilder(Material.ENCHANTED_BOOK)
            .withName("&eVad är Kingdoms?")
            .withLore("&8Hjälpfull Information", "", "&7Skapa ett Kingdom och börja", "&7claima chunks för det.",
                    "&7Du kan också köpa uppgraderingar", "&7och sätta in pengar i din", "&7Kingdoms bank.",
                    "&bDu bör också bjuda in andra", "&bspelare till ditt kingdom!"));

    public static final TemplateItem MANAGER_HOME = new TemplateItem(new ItemBuilder(Material.OAK_DOOR)
            .withName("&eTeleportera Hem")
            .withLore("&7Teleportera till hemmet", "&7i ditt Kingdom.", "",
                    "&bDu kan sätta ett", "&bgenom /kingdom sethome", ""));

    public static final TemplateItem MANAGER_MEMBERS = new TemplateItem(new ItemBuilder(Material.PLAYER_HEAD)
            .withName("&eVisa Medlemmar")
            .withLore("&7Visa alla spelare som är", "&7medlemmar i ditt Kingdom."));

    public static final TemplateItem MANAGER_BANS = new TemplateItem(new ItemBuilder(Material.BARRIER)
            .withName("&eHantera Bannlysningar")
            .withLore("&7Visa alla spelare som är", "&7bannlysta från att komma in i", "&7ditt Kingdom."));

    public static final TemplateItem MANAGER_BANK = new TemplateItem(new ItemBuilder(Material.GOLD_INGOT)
            .withName("&eBank")
            .withLore("&7Sätt in eller ta ut pengar", "&7till/från din Kingdoms bank."));

    public static final TemplateItem MANAGER_UPGRADES = new TemplateItem(new ItemBuilder(Material.DISPENSER)
            .withName("&eUppgraderingar")
            .withLore("&7Olika aspekter av ditt Kingdom."));

    public static final TemplateItem MANAGER_PERMISSIONS = new TemplateItem(new ItemBuilder(Material.ANVIL)
            .withName("&eUppgraderingar")
            .withLore("&7Ändra vad medlemmar av ditt", "&7Kingdom kan & kan inte göra"));

    public static final TemplateItem MANAGER_LOGS = new TemplateItem(new ItemBuilder(Material.BOOK)
            .withName("&eUppgraderingar")
            .withLore("&7Kolla logsen av olika", "&7handlingar gjort av dina", "&7Kingdom medlemmar."));

    public static final TemplateItem MANAGER_VAULT = new TemplateItem(new ItemBuilder(Material.CHEST)
            .withName("&6Kingdom Kassaskåp")
            .withLore("&7Öppna ditt Kingdoms kassaskåp."));

    public static final TemplateItem MANAGER_LEVEL = new TemplateItem(new ItemBuilder(Material.EXPERIENCE_BOTTLE)
            .withName("&aKingdom Level")
            .withLore("&7Köp levlar för ditt", "&7Kingdom och lås upp spawners &", "&7rank högre på topplistan."));

    public static final TemplateItem BANK_DEPOSIT = new TemplateItem(new ItemBuilder(Material.FURNACE_MINECART)
            .withName("&aInsättning")
            .withLore("&7Överför pengar från ditt", "&7personaliga konto till Kingdom", ""));

    public static final TemplateItem BANK_TRANSACTIONS = new TemplateItem(new ItemBuilder(Material.FURNACE_MINECART)
            .withName("&aInsättning")
            .withLore("&7Överför pengar från ditt", "&7personaliga konto till Kingdom", ""));

    /*
        static {
            separator = Material.GRAY_STAINED_GLASS_PANE, med namn: "&7DeepCloud").build();
            back = Material.ENDER_PEARL, med namn: "&dGå Tillbaka", och lore:"&7Gå tillbaka till föregående meny"}).build();
            close = Material.ARROW, med namn: "&cStäng", och lore:"&7Klicka för att stänga menyn"}).build();
            previous = Material.RED_WOOL, med namn: "&cFöregående Sida").build();
            next = Material.GREEN_WOOL, med namn: "&aNästa Sida").build();
            create_create = Material.YELLOW_BED, med namn: "&aSkapa ett Kingdom", och lore:"&7Klicka för att skapa ett Kingdom"}).build();
            create_info = Material.ENCHANTED_BOOK, med namn: "&eVad är Kingdoms?", och lore:"&8Hjälpfull Information", "", "&7Skapa ett Kingdom och börja", "&7claima chunks för det.", "&7Du kan också köpa uppgraderingar", "&7och sätta in pengar i din", "&7Kingdoms bank.", "&bDu bör också bjuda in andra", "&bspelare till ditt kingdom!"}).build();
            manager_teleport = Material.OAK_DOOR, med namn: "&eTeleportera Hem", och lore:"&7Teleportera till hemmet", "&7i ditt Kingdom.", "", "&bDu kan sätta ett", "&bgenom /kingdom sethome", ""}).build();
            manager_members = Material.PLAYER_HEAD, med namn: "&eMembers Lista", och lore:"&7Visa alla spelare som är", "&7medlemmar i ditt Kingdom."}).build();
            manager_bans = Material.BARRIER, med namn: "&eSpelar Bannlysningar", och lore:"&7Visa alla spelare som är", "&7bannlysta från att komma in i", "&7ditt Kingdom."}).build();
            manager_bank = Material.GOLD_INGOT, med namn: "&eBank", och lore:"&7Sätt in eller ta ut pengar", "&7till/från din Kingdoms bank."}).build();
            manager_upgrades = Material.DISPENSER, med namn: "&eUppgraderingar", och lore:"&7Olika aspekter av ditt Kingdom."}).build();
            manager_permissions = Material.ANVIL, med namn: "&eRoll Permissions", och lore:"&7Ändra vad medlemmar av ditt", "&7Kingdom kan & kan inte göra"}).build();
            manager_logs = Material.BOOK, med namn: "&eLogs", och lore:"&7Kolla logsen av olika", "&7handlingar gjort av dina", "&7Kingdom medlemmar."}).build();
            manager_vault = Material.CHEST, med namn: "&6Kingdom Kassaskåp", och lore:"&7Öppna ditt Kingdoms kassaskåp."}).build();
            manager_level = Material.EXPERIENCE_BOTTLE, med namn: "&aKingdom Level", och lore:"&7Köp levlar för ditt", "&7Kingdom och lås upp spawners &", "&7rank högre på topplistan."}).build();
            bank_deposit = Material.FURNACE_MINECART, med namn: "&aInsättning", och lore:"&7Överför pengar från ditt", "&7personaliga konto till Kingdom", ""}).build();
            bank_transactions = Material.BOOK, med namn: "&eSenaste Transaktioner", och lore:"&7Dessa är de senaste transaktionerna", "&7för din Kingdom Bank"}).build();
            bank_withdraw = Material.MINECART, med namn: "&cUttagning", och lore:"&7Överför pengar ditt", "&7Kingdoms konto till dig själv", ""}).build();
            bank_deposit_all = Material.EMERALD_BLOCK, med namn: "&aLägg in hela saldot", och lore:"&7Överför alla pengar från", "&7ditt personliga konto till ditt", "&7Kingdoms konto.", ""}).build();
            bank_deposit_half = Material.EMERALD_ORE, med namn: "&aLägg in halva saldot", och lore:"&7Överför hälften av pengar från", "&7ditt personliga konto till ditt", "&7Kingdoms konto.", ""}).build();
            bank_deposit_specific = Material.EMERALD, med namn: "&aSätt in specifierat antal", och lore:"&7Överför ett specifierat antal från", "&7ditt personliga konto till ditt", "&7Kingdoms konto.", "", "&eSkriv /k bank insättning <antal>"}).build();
            bank_withdraw_all = Material.REDSTONE_BLOCK, med namn: "&cTa Ut Hela Saldot", och lore:"&7Överför alla pengar från", "&7ditt Kingdoms konto till ditt", "&7konto.", ""}).build();
            bank_withdraw_half = Material.REDSTONE_ORE, med namn: "&cTa Ut Halva Saldot", och lore:"&7Överför hälften av pengarna från", "&7ditt Kingdoms konto till ditt", "&7konto.", ""}).build();
            bank_withdraw_specific = Material.REDSTONE, med namn: "&cTa Ut Specifierat Antal", och lore:"&7Överför specifierat pengar antal", "&7från ditt Kingdoms konto till", "&7ditt konto.", "", "&eSkriv /k bank uttagning <antal>"}).build();
            upgrades_spawners = Material.SPAWNER, med namn: "&e↑ Max Spawners Uppgradering", och lore:"&7Öka det tillåtna antalet", "&7spawners i ditt Kingdom.", ""}).build();
            upgrades_loot = Material.IRON_INGOT, med namn: "&e↑ Mob Loot Uppgradering", och lore:"&7Öka antalet av loot", "&7som ges från mobs på ditt Kingdom.", ""}).build();
            upgrades_spawning = Material.ZOMBIE_HEAD, med namn: "&e↑ Mob Spawning Uppgradering", och lore:"&7Öka hastigheten av", "&7alla spawners i ditt Kingdom.", ""}).build();
            upgrades_hoppers = Material.HOPPER, med namn: "&e↑ Max Hoppers Uppgradering", och lore:"&7Öka det tillåtna antalet", "&7hoppers i ditt Kingdom.", ""}).build();
            upgrades_mobxp = Material.EXPERIENCE_BOTTLE, med namn: "&e↑ Mob XP Uppgradering", och lore:"&7Öka det antalet av XP", "&7som ges från mobs i ditt Kingdom.", ""}).build();
            upgrades_grow = Material.CACTUS, med namn: "&e↑ Gröd Växt Uppgradering", och lore:"&7Öka växten av", "&7grödor i ditt Kingdom.", ""}).build();
            permissions_coleader = Material.EMERALD, med namn: "&eColeader Permissions", och lore:"&7Konfigurera permissions", "&7för alla spelare med denna roll."}).build();
            permissions_admin = Material.DIAMOND, med namn: "&bAdmin Permissions", och lore:"&7Konfigurera permissions", "&7för alla spelare med denna roll."}).build();
            permissions_mod = Material.GOLD_INGOT, med namn: "&6Mod Permissions", och lore:"&7Konfigurera permissions", "&7för alla spelare med denna roll."}).build();
            permissions_member = Material.IRON_INGOT, med namn: "&fMember Permissions", och lore:"&7Konfigurera permissions", "&7för alla spelare med denna roll."}).build();
            permissions_coop = Material.REDSTONE, med namn: "&cCoop Permissions", och lore:"&7Konfigurera permissions", "&7för alla spelare med denna roll."}).build();
            permissions_guest = Material.COAL, med namn: "&eGäst Permissions", och lore:"&7Konfigurera permissions", "&7för alla spelare med denna roll."}).build();
            permissions_role_bank_deposit = Material.FURNACE_MINECART, med namn: "&eBank Insättning", och lore:"&7Förmågan att kunna sätta in", "&7pengar till Kingdom banken."}).build();
            permissions_role_bank_withdraw = Material.MINECART, med namn: "&eBank Uttagning", och lore:"&7Förmågan att kunna ta ut", "&7pengar från Kingdom banken."}).build();
            permissions_role_invite = Material.BOOK, med namn: "&eBjuda In", och lore:"&7Förmågan att bjuda in", "&7spelare till Kingdomet."}).build();
            permissions_role_coop_add = Material.PLAYER_HEAD, med namn: "&eCoop Lägga Till", och lore:"&7Förmågan att lägga till en spelare", "&7till Kingdomets COOP lista."}).build();
            permissions_role_coop_remove = Material.SKELETON_SKULL, med namn: "&eCoop Ta Bort", och lore:"&7Förmågan att ta bort en spelare", "&7från Kingdomets COOP lista."}).build();
            permissions_role_kick = Material.SHEARS, med namn: "&eSparka Ut", och lore:"&7Förmågan att ta bort en spelare", "&7från Kingdomets medlemmslista."}).build();
            permissions_role_ban = Material.BARRIER, med namn: "&eBannlysning", och lore:"&7Förmågan att bannlysa eller unbanna", "&7en spelare från ditt Kingdom."}).build();
            permissions_role_mob_damage = Material.LEATHER, med namn: "&eMob Skada", och lore:"&7Förmågan att skada/slå mobs", "&7i någon av Kingdoms chunks."}).build();
            permissions_role_interact = Material.LEVER, med namn: "&eInteragera", och lore:"&7Förmågan att använda dörrar, spakar", "&7osv i någon av Kingdomets chunks."}).build();
            permissions_role_change_name = Material.NAME_TAG, med namn: "&eÄndra Namn", och lore:"&7Förmågan att byta ditt", "&7Kingdoms namn."}).build();
            permissions_role_claim = Material.GRASS_BLOCK, med namn: "&eClaim", och lore:"&7Förmågan att claima chunks", "&7för ditt Kingdom."}).build();
            permissions_role_unclaim = Material.DIRT, med namn: "&eUnclaim", och lore:"&7Förmågan att unclaima", "&7någon av ditt Kingdoms chunks."}).build();
            level_one = Material.ANVIL, med namn: "&aKöp 1 Level", och lore:"&8Uppgradera Kingdom Level"}).build();
            level_ten = Material.ANVIL, med namn: "&aKöp 10 Levlar", och lore:"&8Uppgradera Kingdom Level"}).build();
            logs_deposit = Material.FURNACE_MINECART, med namn: "&eSenaste Bank Insättningar").build();
            logs_withdraw = Material.MINECART, med namn: "&eSenaste Bank Uttagningar").build();
            logs_spawners_break = Material.SPAWNER, med namn: "&eSenaste Spawners Borttagna").build();
            logs_spawners_placed = Material.SPAWNER, med namn: "&eSenaste Spawners Placerade").build();
            logs_vault_addition = Material.ENDER_CHEST, med namn: "&eSenaste Kassaskåp Tillägg").build();
            logs_vault_removal = Material.CHEST, med namn: "&eSenaste Kassaskåp Borttagningar").build();
            logs_members = Material.PLAYER_HEAD, med namn: "&eSenaste Medlemms Tillägg").build();
            logs_bans = Material.BARRIER, med namn: "&eSenaste Medlemms Borttagningar").build();
            logs_kicks = Material.OAK_DOOR, med namn: "&eSenaste Medlemms Utsparkningar").build();
            logs_member_invites = Material.ENCHANTED_BOOK, med namn: "&eSenaste Member Inbjudningar").build();
            logs_permissions = Material.ANVIL, med namn: "&eSenaste Permission Ändringar").build();
        }
    }


     */
}

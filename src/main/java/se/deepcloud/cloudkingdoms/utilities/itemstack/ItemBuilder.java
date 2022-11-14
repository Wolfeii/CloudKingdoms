package se.deepcloud.cloudkingdoms.utilities.itemstack;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;
import se.deepcloud.cloudkingdoms.CloudKingdoms;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;
import se.deepcloud.cloudkingdoms.utilities.ServerVersion;
import se.deepcloud.cloudkingdoms.utilities.Utilities;
import se.deepcloud.cloudkingdoms.utilities.formatting.Formatters;
import se.deepcloud.cloudkingdoms.utilities.sorting.SequentialListBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class ItemBuilder {

    private static final CloudKingdoms plugin = CloudKingdoms.getPlugin();

    private ItemStack itemStack;
    @Nullable
    private ItemMeta itemMeta;
    private String textureValue = "";

    public ItemBuilder(ItemStack itemStack) {
        this(itemStack.getType(), itemStack.getAmount());
        this.itemMeta = itemStack.getItemMeta().clone();
    }

    public ItemBuilder(Material type) {
        this(type, 1);
    }

    public ItemBuilder(Material type, int amount) {
        itemStack = new ItemStack(type, amount);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder withType(Material type) {
        this.itemStack.setType(type);
        return this;
    }

    @Deprecated
    public ItemBuilder withDurablity(short durability) {
        if (durability >= 0)
            this.itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder withAmount(int amount) {
        if (amount >= 1 && amount <= itemStack.getMaxStackSize())
            itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder asSkullOf(KingdomPlayer kingdomPlayer) {
        if (itemStack.getType() == Material.PLAYER_HEAD)
            textureValue = kingdomPlayer == null ? ItemSkulls.getNullPlayerTexture() : kingdomPlayer.getTextureValue();
        return this;
    }

    public ItemBuilder asSkullOf(String textureValue) {
        if (itemStack.getType() == Material.PLAYER_HEAD)
            this.textureValue = textureValue;
        return this;
    }

    public ItemBuilder withName(String name) {
        if (itemMeta != null && name != null)
            itemMeta.setDisplayName(Formatters.COLOR_FORMATTER.format(name));
        return this;
    }

    public ItemBuilder replaceName(String regex, String replace) {
        if (itemMeta != null && itemMeta.hasDisplayName())
            withName(itemMeta.getDisplayName().replace(regex, replace));
        return this;
    }

    public ItemBuilder withLore(List<String> lore) {
        if (itemMeta != null && lore != null)
            itemMeta.setLore(new SequentialListBuilder<String>()
                    .build(lore, Formatters.COLOR_FORMATTER::format));
        return this;
    }

    public ItemBuilder appendLore(List<String> lore) {
        if (itemMeta == null || itemMeta.getLore() == null) {
            return withLore(lore);
        } else {
            List<String> currentLore = itemMeta.getLore();
            currentLore.addAll(lore);
            return withLore(currentLore);
        }
    }

    public ItemBuilder withLore(String... lore) {
        return withLore(Arrays.asList(lore));
    }

    public ItemBuilder withLore(String firstLine, List<String> listLine) {
        if (itemMeta == null)
            return this;

        List<String> loreList = new LinkedList<>();

        firstLine = Formatters.COLOR_FORMATTER.format(firstLine);
        loreList.add(firstLine);

        for (String line : listLine)
            loreList.add(ChatColor.getLastColors(firstLine) + Formatters.COLOR_FORMATTER.format(line));

        if (loreList.size() > 10) {
            for (int i = 10; i < loreList.size(); i++) {
                loreList.remove(loreList.get(i));
            }
            loreList.add(ChatColor.getLastColors(firstLine) + "...");
        }

        itemMeta.setLore(loreList);
        return this;
    }

    public ItemBuilder replaceLore(String regex, String replace) {
        if (itemMeta == null || !itemMeta.hasLore())
            return this;

        List<String> loreList = new ArrayList<>(itemMeta.getLore().size());

        for (String line : itemMeta.getLore()) {
            loreList.add(line.replace(regex, replace));
        }

        withLore(loreList);
        return this;
    }

    public ItemBuilder replaceLoreWithLines(String regex, String... lines) {
        if (itemMeta == null || !itemMeta.hasLore())
            return this;

        List<String> currentLore = itemMeta.getLore();

        List<String> loreList = new ArrayList<>(currentLore.size());
        List<String> linesToAdd = Arrays.asList(lines);
        boolean isEmpty = linesToAdd.isEmpty() || linesToAdd.stream().allMatch(String::isEmpty);

        for (String line : currentLore) {
            if (line.contains(regex)) {
                if (!isEmpty)
                    loreList.addAll(linesToAdd);
            } else {
                loreList.add(line);
            }
        }

        withLore(loreList);
        return this;
    }

    public ItemBuilder replaceAll(String regex, String replace) {
        replaceName(regex, replace);
        replaceLore(regex, replace);
        return this;
    }

    public ItemBuilder withEnchant(Enchantment enchant, int level) {
        if (itemMeta != null)
            itemMeta.addEnchant(enchant, level, true);
        return this;
    }

    public ItemBuilder withFlags(ItemFlag... itemFlags) {
        if (itemMeta != null)
            itemMeta.addItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        if (itemMeta != null)
            itemMeta.setUnbreakable(true);
        return this;
    }

    public ItemBuilder withPotionEffect(PotionEffect potionEffect) {
        if (itemMeta instanceof PotionMeta potionMeta) {
            if (!potionMeta.hasCustomEffects()) {
                potionMeta.setColor(potionEffect.getType().getColor());
            }
            potionMeta.addCustomEffect(potionEffect, true);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder withEntityType(EntityType entityType) {
        if (itemMeta == null)
            return this;

        if (Utilities.isValidAndSpawnEgg(itemStack)) {
            if (ServerVersion.isLegacy()) {
                try {
                    ((SpawnEggMeta) itemMeta).setSpawnedType(entityType);
                } catch (NoClassDefFoundError error) {
                    itemStack.setDurability(entityType.getTypeId());
                }
            } else {
                itemStack.setType(Material.valueOf(entityType.name() + "_SPAWN_EGG"));
            }
        }

        return this;
    }

    public ItemBuilder withCustomModel(int customModel) {
        if (itemMeta != null)
            itemMeta.setCustomModelData(customModel);
        return this;
    }

    public ItemBuilder withLeatherColor(int leatherColor) {
        if (itemMeta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
            leatherArmorMeta.setColor(Color.fromRGB(leatherColor));
        }
        return this;
    }

    @Nullable
    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public ItemStack build(KingdomPlayer kingdomPlayer) {
        OfflinePlayer offlinePlayer = kingdomPlayer.asOfflinePlayer();

        //TODO: PlaceholdersService placeholdersService = plugin.getServices().getPlaceholdersService();

        if (itemMeta != null) {
            if (itemMeta.hasDisplayName()) {
                //withName(placeholdersService.parsePlaceholders(offlinePlayer, itemMeta.getDisplayName()));
            }

            if (itemMeta.hasLore()) {
                withLore(new SequentialListBuilder<String>()
                        .build(itemMeta.getLore()/*, line -> placeholdersService.parsePlaceholders(offlinePlayer, line)*/));
            }
        }

        if (textureValue.equals("%kingdom_player_texture%"))
            textureValue = kingdomPlayer.getTextureValue();

        return build();
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return textureValue.isEmpty() ? itemStack : ItemSkulls.getPlayerHead(itemStack, textureValue);
    }

    public ItemBuilder copy() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.AIR);
        itemBuilder.itemStack = itemStack.clone();
        if (itemMeta != null)
            itemBuilder.itemMeta = itemMeta.clone();
        itemBuilder.textureValue = textureValue;
        return itemBuilder;
    }
}
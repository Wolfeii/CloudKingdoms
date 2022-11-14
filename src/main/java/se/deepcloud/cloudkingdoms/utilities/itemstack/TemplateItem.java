package se.deepcloud.cloudkingdoms.utilities.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.deepcloud.cloudkingdoms.player.KingdomPlayer;

public class TemplateItem {

    public static final TemplateItem AIR = new TemplateItem(new ItemBuilder(Material.AIR));

    private final ItemBuilder itemBuilder;

    public TemplateItem(ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    public ItemBuilder getBuilder() {
        return itemBuilder.copy();
    }

    public ItemBuilder getEditableBuilder() {
        return itemBuilder;
    }

    public ItemStack build() {
        return getBuilder().build();
    }

    public ItemStack build(KingdomPlayer kingdomPlayer) {
        return getBuilder().build(kingdomPlayer);
    }

    public TemplateItem copy() {
        return new TemplateItem(this.itemBuilder.copy());
    }

}
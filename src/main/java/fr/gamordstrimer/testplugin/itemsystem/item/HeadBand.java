package fr.gamordstrimer.testplugin.itemsystem.item;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.itemsystem.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HeadBand extends Item {
    @Override
    public @NotNull Material setMaterial() {
        return Material.LEATHER_HELMET;
    }

    @Override
    public int setQuantity() {
        return 1;
    }

    @Override
    public ItemMeta setItemMeta() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
        if (itemMeta != null && leatherArmorMeta != null) {
            itemMeta.displayName(Component.text("Bandeau").color(TextColor.fromHexString("#8800ED")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Cette Item permet de rendre").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("aveugle un joueur.").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
            leatherArmorMeta.setColor(Color.fromRGB(0, 0, 0));
            leatherArmorMeta.addItemFlags(ItemFlag.HIDE_DYE);
        }
        return leatherArmorMeta;
    }

    @Override
    public void setRecipe() {
        ShapedRecipe hb = new ShapedRecipe(new NamespacedKey(Main.getInstance(), "headband"), new HeadBand().getItemStack());
        hb.shape(
                "LLL",
                "LBL",
                "   "
        );
        hb.setIngredient('L', Material.LEATHER);
        hb.setIngredient('B', new BlindnessPotion().getItemStack());

        this.recipe = hb;

        Bukkit.getServer().addRecipe(hb);
    }

    @Override
    public void handleItem(Event event) {

    }

    @Override
    public boolean isItem(ItemStack item) {
        return true;
    }
}

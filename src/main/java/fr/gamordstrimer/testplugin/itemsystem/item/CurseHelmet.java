package fr.gamordstrimer.testplugin.itemsystem.item;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.itemsystem.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CurseHelmet extends Item {
    @Override
    public @NotNull Material setMaterial() {
        return Material.NETHERITE_HELMET;
    }

    @Override
    public int setQuantity() {
        return 1;
    }

    @Override
    public ItemMeta setItemMeta() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text("ᛍᚢᚱᛌᛂ ᚼᛂᛚᛘᛂᛐ").color(TextColor.fromHexString("#D00000")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("≈ Casque Maudit").color(TextColor.fromHexString("#FF5D00")).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
        }
        return itemMeta;
    }

    @Override
    public void setRecipe() {
        ShapedRecipe ch = new ShapedRecipe(new NamespacedKey(Main.getInstance(), "cursehelmet"), new CurseHelmet().getItemStack());
        ch.shape(
                "DND",
                "DHD",
                "   "
        );
        ch.setIngredient('D', Material.DIAMOND);
        ch.setIngredient('N', Material.NETHERITE_INGOT);
        ch.setIngredient('H', Material.BLAZE_POWDER);

        this.recipe = ch;

        Bukkit.getServer().addRecipe(ch);
    }
}
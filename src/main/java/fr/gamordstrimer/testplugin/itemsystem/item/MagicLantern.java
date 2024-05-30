package fr.gamordstrimer.testplugin.itemsystem.item;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.itemsystem.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MagicLantern extends Item {
    @Override
    public @NotNull Material setMaterial() {
        return Material.SOUL_LANTERN;
    }

    @Override
    public int setQuantity() {
        return 1;
    }

    @Override
    public ItemMeta setItemMeta() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta != null) {
            itemMeta.displayName(Component.text("Lanterne Magique").color(TextColor.fromHexString("#A89CF0")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Cette Lanterne vous procure une vision").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("dans le noir en appuyant sur Clique droit").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
        }
        return itemMeta;
    }

    @Override
    public void setRecipe() {

        ShapedRecipe ml = new ShapedRecipe(new NamespacedKey(Main.getInstance(), "magiclantern"), new MagicLantern().getItemStack());
        ml.shape(
                " S ",
                "GDG",
                " S "
        );
        ml.setIngredient('S', Material.SOUL_LANTERN);
        ml.setIngredient('D', Material.DIAMOND);
        ml.setIngredient('G', Material.GLASS);

        this.recipe = ml;

        Bukkit.getServer().addRecipe(ml);
    }
}

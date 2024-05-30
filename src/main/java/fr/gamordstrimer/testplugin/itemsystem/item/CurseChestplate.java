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

public class CurseChestplate extends Item {
    @Override
    public @NotNull Material setMaterial() {
        return Material.NETHERITE_CHESTPLATE;
    }

    @Override
    public int setQuantity() {
        return 1;
    }

    @Override
    public ItemMeta setItemMeta() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text("ᚿᛂᛐᚼᛂᚱᛁᛐᛂ ᛍᚼᛂᛌᛐᛔᛚᛆᛐᛂ").color(TextColor.fromHexString("#D00000")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("≈ Plastron Maudit").color(TextColor.fromHexString("#FF5D00")).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
        }
        return itemMeta;
    }

    @Override
    public void setRecipe() {
        ShapedRecipe cc = new ShapedRecipe(new NamespacedKey(Main.getInstance(), "cursechestplate"), new CurseChestplate().getItemStack());
        cc.shape(
                "D D",
                "DHD",
                "DND"
        );
        cc.setIngredient('D', Material.DIAMOND);
        cc.setIngredient('H', Material.BLAZE_POWDER);
        cc.setIngredient('N', Material.NETHERITE_INGOT);

        this.recipe = cc;

        Bukkit.getServer().addRecipe(cc);
    }
}

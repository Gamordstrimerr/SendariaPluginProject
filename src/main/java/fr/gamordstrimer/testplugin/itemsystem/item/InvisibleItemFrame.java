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
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InvisibleItemFrame extends Item {
    @Override
    public @NotNull Material setMaterial() {
        return Material.ITEM_FRAME;
    }

    @Override
    public int setQuantity() {
        return 1;
    }

    @Override
    public ItemMeta setItemMeta() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text("Cadre Invisible").color(TextColor.fromHexString("#00c6ff")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Ce Cadre est Invisible lorsque vois le placez").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
        }
        return itemMeta;
    }

    @Override
    public void setRecipe() {
        ShapelessRecipe iif = new ShapelessRecipe(new NamespacedKey(Main.getInstance(), "invisibleitemframe"), new InvisibleItemFrame().getItemStack());
        iif.addIngredient(1, Material.ITEM_FRAME);
        iif.addIngredient(1, Material.GLASS_PANE);

        this.recipe = iif;

        Bukkit.getServer().addRecipe(iif);
    }
}

package fr.gamordstrimer.testplugin.itemsystem.item;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.itemsystem.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CurseLeggings extends Item {
    @Override
    public @NotNull Material setMaterial() {
        return Material.NETHERITE_LEGGINGS;
    }

    @Override
    public int setQuantity() {
        return 1;
    }

    @Override
    public ItemMeta setItemMeta() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            var mm = MiniMessage.miniMessage();
            Component displayName = mm.deserialize("<gradient:#660000:#cc0000>Pantalon</gradient><gradient:#cc0000:#660000> Maudit</gradient>");
            itemMeta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
        }
        return itemMeta;
    }

    @Override
    public void setRecipe() {
        ShapedRecipe cl = new ShapedRecipe(new NamespacedKey(Main.getInstance(), "curseleggings"), new CurseLeggings().getItemStack());
        cl.shape(
                "DND",
                "DHD",
                "D D"
        );
        cl.setIngredient('D', Material.DIAMOND);
        cl.setIngredient('N', Material.NETHERITE_INGOT);
        cl.setIngredient('H', Material.BLAZE_POWDER);

        this.recipe = cl;

        Bukkit.getServer().addRecipe(cl);
    }

    @Override
    public void handleItem(Event event) {

    }

    @Override
    public boolean isItem(ItemStack item) {
        return true;
    }
}

package fr.gamordstrimer.testplugin.itemsystem.item;

import fr.gamordstrimer.testplugin.itemsystem.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlindnessPotion extends Item {
    @Override
    public @NotNull Material setMaterial() {
        return Material.POTION;
    }

    @Override
    public int setQuantity() {
        return 1;
    }

    @Override
    public ItemMeta setItemMeta() {
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setColor(Color.BLACK);
            potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3600, 0), true);
            potionMeta.displayName(Component.text("Potion de Cécité").color(TextColor.fromHexString("#FFFFFF")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            potionMeta.lore(lore);
        }
        return potionMeta;
    }

    @Override
    public void setRecipe() {
        //NO RECIPE FOR THIS ITEM
    }

    @Override
    public void handleItem(Event event) {

    }

    @Override
    public boolean isItem(ItemStack item) {
        return true;
    }
}

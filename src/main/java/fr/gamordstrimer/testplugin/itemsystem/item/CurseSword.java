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
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CurseSword extends Item {

    @Override
    public @NotNull Material setMaterial() {
        return Material.NETHERITE_SWORD;
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
            Component displayName = mm.deserialize("<gradient:#660000:#cc0000>Épée</gradient><gradient:#cc0000:#660000> Maudite</gradient>");
            itemMeta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 15.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        }
        return itemMeta;
    }

    @Override
    public void setRecipe() {
        ShapedRecipe cs = new ShapedRecipe(new NamespacedKey(Main.getInstance(), "cursesword"), new CurseSword().getItemStack());
        cs.shape(
                " N ",
                " H ",
                " S "
        );
        cs.setIngredient('N', Material.NETHERITE_INGOT);
        cs.setIngredient('H', Material.BLAZE_POWDER);
        cs.setIngredient('S', Material.STICK);

        this.recipe = cs;

        // Adding the CurseSword recipe to the server and the CurseSword Item inside the Hashmap
        Bukkit.getServer().addRecipe(cs);
    }

    @Override
    public void handleItem(Event event) {

    }

    @Override
    public boolean isItem(ItemStack item) {
        return true;
    }
}
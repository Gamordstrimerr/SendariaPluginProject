package fr.gamordstrimer.testplugin.itemsystem.item;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.itemsystem.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TeleportationStone extends Item {

    public static ItemStack activateItem() {
        ItemStack activateStone = new TeleportationStone().getItemStack();
        ItemMeta activateStoneMeta = activateStone.getItemMeta();
        if (activateStoneMeta != null) {
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique droit pour se téléporter.").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            activateStoneMeta.lore(lore);
            activateStoneMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            activateStoneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            activateStone.setItemMeta(activateStoneMeta);
            return activateStone;
        }
        return null;
    }

    @Override
    public @NotNull Material setMaterial() {
        return Material.AMETHYST_SHARD;
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
            Component displayName = mm.deserialize("<gradient:#6a008a:#DEADED>Pierre d</gradient><gradient:#DEADED:#6a008a>e Téléportation</gradient>");
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
        ShapedRecipe ts = new ShapedRecipe(new NamespacedKey(Main.getInstance(), "teleportation_stone"), new TeleportationStone().getItemStack());
        ts.shape(
                "AQA",
                "QEQ",
                "AQA"
        );
        ts.setIngredient('A', Material.AMETHYST_SHARD);
        ts.setIngredient('Q', Material.QUARTZ);
        ts.setIngredient('E', Material.ENDER_EYE);

        this.recipe = ts;

        Bukkit.getServer().addRecipe(ts);
    }

    @Override
    public void handleItem(Event event) {
        if (event instanceof PlayerInteractEvent e) {
            Player player = e.getPlayer();
            ItemStack item = e.getItem();
            if (item != null && item.isSimilar(new TeleportationStone().getItemStack())) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    int slot = player.getInventory().first(item);
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        player.getInventory().setItem(slot, activateItem());
                    }, 1L);

                    player.sendMessage("You click with the teleportation stone.");
                }
            } else if (item != null && item.isSimilar(activateItem())) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    int slot = player.getInventory().first(item);
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        player.getInventory().setItem(slot, new TeleportationStone().getItemStack());
                    }, 1L);

                    player.sendMessage("You click with the §eactivate §fteleportation stone.");
                }
            }
        }
    }
}

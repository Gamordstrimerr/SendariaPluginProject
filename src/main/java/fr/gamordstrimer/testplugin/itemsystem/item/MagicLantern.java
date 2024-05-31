package fr.gamordstrimer.testplugin.itemsystem.item;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.Utils.CooldownManager;
import fr.gamordstrimer.testplugin.itemsystem.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        if (itemMeta != null) {
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

    @Override
    public void handleItem(Event event) {
        if (event instanceof PlayerInteractEvent e) {
            Player player = e.getPlayer();
            Action action = e.getAction();
            ItemStack item = e.getItem();
            CooldownManager cooldownManager = new CooldownManager(Main.getInstance().getDataFolder());
            String prefixServer = Main.getInstance().getConfig().getString("messages.prefixserver").replace('&', '§');

            if (item != null && item.isSimilar(new MagicLantern().getItemStack())) {
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    UUID playerUUID = player.getUniqueId();

                    if (cooldownManager.hasCooldown(playerUUID)) {
                        long remainingTimeMillis = cooldownManager.getCooldown(playerUUID) - System.currentTimeMillis();
                        long remainingTimeSeconds = remainingTimeMillis / 1000;
                        long minutes = remainingTimeSeconds / 60;
                        long seconds = remainingTimeSeconds % 60;

                        String remainingTimeMessage = String.format("%d minutes et %d secondes", minutes, seconds);
                        player.sendMessage(Component.text("Tu dois patienter ").color(NamedTextColor.RED)
                                .append(Component.text(remainingTimeMessage).color(NamedTextColor.DARK_RED)
                                        .append(Component.text(" pour utiliser la lanterne magique").color(NamedTextColor.RED))));

                        e.setCancelled(true);
                        return;
                    }

                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 60 * 15, 1));
                    player.sendMessage(Component.text(prefixServer + "Tu as reçu un effet de Vision Nocture pendant ")
                            .append(Component.text("15 minutes.").color(NamedTextColor.GOLD)));
                    e.setCancelled(true);

                    long cooldownTime = System.currentTimeMillis() + (30 * 60 * 1000);
                    cooldownManager.setCooldown(playerUUID, cooldownTime);

                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> cooldownManager.removeCooldown(playerUUID), 30 * 60 * 20L); // 30 minutes in ticks
                }
            }
        }
    }

    @Override
    public boolean isItem(ItemStack item) {
        return true;
    }
}

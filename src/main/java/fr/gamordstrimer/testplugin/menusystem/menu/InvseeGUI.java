package fr.gamordstrimer.testplugin.menusystem.menu;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.menusystem.Menu;
import fr.gamordstrimer.testplugin.menusystem.PlayerMenuUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.bukkit.scheduler.BukkitTask;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvseeGUI extends Menu {

    private static BukkitTask task;
    private static InvseeGUI instance;

    public InvseeGUI(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        instance = this;
    }

    @Override
    public @NotNull TextComponent getMenuName() {
        Player target = SelPlayerGUI.getTarget();
        return Component.text("Inventaire de ").append(Component.text(target.getName()).color(NamedTextColor.GOLD));
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }

    @Override
    public void setMenuItems() {
        Player target = SelPlayerGUI.getTarget();

        inventory.clear();

        //Create Custom ItemStack
        UUID playerUUID = UUID.fromString(String.valueOf(target.getUniqueId()));
        ItemStack skull  = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        double health = target.getHealth();
        String formattedHealth = String.format("%.1f", Math.max(health, 0.0));
        skullMeta.setPlayerProfile(target.getPlayerProfile());
        skullMeta.displayName(Component.text("Profile de ").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(target.getName()).color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("------------------------").color(NamedTextColor.GRAY).decoration(TextDecoration.STRIKETHROUGH, true).decoration(TextDecoration.BOLD, true));
        lore.add(Component.text("Vie : ").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(formattedHealth).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text(" ❤").color(NamedTextColor.DARK_RED).decoration(TextDecoration.ITALIC, false))));
        lore.add(Component.text("Nourriture : ").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(target.getFoodLevel()).color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text(" \uD83C\uDF57")).color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)));
        lore.add(Component.text("Emplacement : ").color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(target.getLocation().getBlockX() + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ()).color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false)));
        lore.add(Component.text("------------------------").color(NamedTextColor.GRAY).decoration(TextDecoration.STRIKETHROUGH, true).decoration(TextDecoration.BOLD, true));
        skullMeta.lore(lore);
        skull.setItemMeta(skullMeta);

        ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta backgroundMeta = background.getItemMeta();
        backgroundMeta.displayName(Component.text(" "));
        background.setItemMeta(backgroundMeta);

        // Set the inventory items
        ItemStack[] inventoryContents = target.getInventory().getContents();
        for (int i = 0; i < inventoryContents.length; i++) {
            inventory.setItem(i, inventoryContents[i]);
        }
        inventory.setItem(41, background);
        inventory.setItem(42, background);
        inventory.setItem(43, background);
        inventory.setItem(44, skull);

    }

    // Method to start the BukkitScheduler
    public static void startScheduler() {
        if(instance != null) {
            task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), instance::setMenuItems, 0, 20L);
        }
    }

    // Method to cancel the BukkitScheduler
    public static void cancelScheduler() {
        if (task != null) {
            task.cancel(); // Cancel the task if it's not null
        }
    }
}

package fr.gamordstrimer.testplugin.menusystem.menu;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.menusystem.Menu;
import fr.gamordstrimer.testplugin.menusystem.PlayerMenuUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MainGUI extends Menu {

    private static String itemName;
    private static ItemStack itemStack;

    public MainGUI(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public @NotNull TextComponent getMenuName() {
        return Component.text("Custom Item de Sendaria").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true);
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        for (Map.Entry<String, ItemStack> entry : CustomItems.getCustomItems().entrySet()) {
            if (clickedItem.isSimilar(entry.getValue())) {
                itemName = entry.getKey();
                itemStack = entry.getValue();

                new ItemGUI(Main.getPlayerMenuUtility(player)).open();
            }
        }

    }

    @Override
    public void setMenuItems() {

        //GlassPane
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = glassPane.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(" "));
            glassPane.setItemMeta(meta);
        }

        for (int i = 0; i < 9; i++) { // Top border
            inventory.setItem(i, glassPane);
        }
        for (int i = 9; i < 36; i += 9) { // Left border
            inventory.setItem(i, glassPane);
        }
        for (int i = 17; i < 45; i += 9) { // Right border
            inventory.setItem(i, glassPane);
        }
        for (int i = 36; i < 45; i++) { // Bottom border
            if (i != 39 && i != 40 && i != 41) {
                inventory.setItem(i, glassPane);
            }
        }

        //Button Right
        ItemStack buttonRight = new ItemStack(Material.SPRUCE_BUTTON);
        ItemMeta buttonRightMeta = buttonRight.getItemMeta();
        if (buttonRightMeta != null) {
            buttonRightMeta.displayName(Component.text("Page Suivante").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            buttonRight.setItemMeta(buttonRightMeta);
        }

        inventory.setItem(41, buttonRight);

        //Button Left
        ItemStack buttonLeft = new ItemStack(Material.SPRUCE_BUTTON);
        ItemMeta buttonLeftMeta = buttonLeft.getItemMeta();
        if (buttonLeftMeta != null) {
            buttonLeftMeta.displayName(Component.text("Page Précédente").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            buttonLeft.setItemMeta(buttonLeftMeta);
        }

        inventory.setItem(39, buttonLeft);

        // Iterate over the entries of the map
        for (Map.Entry<String, ItemStack> entry : CustomItems.getCustomItems().entrySet()) {
            ItemStack item = entry.getValue();
            String itemName = entry.getKey();
            inventory.addItem(item);
        }

    }

    public static void setItemName(String itemName) {
        MainGUI.itemName = itemName;
    }

    public static String getItemName() {
        return itemName;
    }

    public static void setItemStack(ItemStack itemStack) {
        MainGUI.itemStack = itemStack;
    }

    public static ItemStack getItemStack() {
        return itemStack;
    }
}

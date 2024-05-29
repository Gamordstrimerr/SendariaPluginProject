package fr.gamordstrimer.testplugin.menusystem.menu;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.menusystem.PaginatedMenu;
import fr.gamordstrimer.testplugin.menusystem.PlayerMenuUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PaginatedMainGUI extends PaginatedMenu {

    private static String itemName;
    private static ItemStack itemStack;

    public PaginatedMainGUI(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public @NotNull TextComponent getMenuName() {
        return Component.text("Custom Item de Sendaria").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true);
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        Map<String, ItemStack> customItems = CustomItems.getCustomItems();
        ArrayList<ItemStack> items = new ArrayList<>(customItems.values());

        ItemStack clickedItem = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        if (CustomItems.getCustomItems().containsValue(clickedItem)) {
            for (Map.Entry<String, ItemStack> entry : CustomItems.getCustomItems().entrySet()) {
                if (entry.getValue().equals(clickedItem)) {
                    itemName = entry.getKey();
                    itemStack = entry.getValue();

                    new ItemGUI(Main.getPlayerMenuUtility(player)).open();
                    break;
                }
            }
        } else if (clickedItem.isSimilar(super.close)) {
            player.closeInventory();
        } else if (clickedItem.isSimilar(super.left)) {
            if (page == 0) {
                player.sendMessage(Component.text("Tu es déjà à la première page").color(NamedTextColor.RED));
            } else {
                page = page - 1;
                super.open();
            }
        } else if (clickedItem.isSimilar(super.right)) {
            if (!((index + 1) >= items.size())) {
                page = page + 1;
                super.open();
            } else {
                player.sendMessage(Component.text("Tu es à la dernière page").color(NamedTextColor.RED));
            }
        }
    }

    @Override
    public void setMenuItems() {

        addMenuBorder();

        Map<String, ItemStack> customItems = CustomItems.getCustomItems();
        ArrayList<ItemStack> items = new ArrayList<>(customItems.values());

        if (items != null && !items.isEmpty()) {

            for(int i = 0; i < super.maxItemsPerPage; i++) {
                index = super.maxItemsPerPage * page + i;
                if (index >= items.size()) break;
                if (items.get(index) != null) {
                    ///////////////////////////

                    ItemStack customItem = items.get(index);
                    inventory.addItem(customItem);

                    ///////////////////////////
                }
            }
        }
    }

    public static void setItemName(String itemName) {
        PaginatedMainGUI.itemName = itemName;
    }

    public static String getItemName() {
        return itemName;
    }

    public static void setItemStack(ItemStack itemStack) {
        PaginatedMainGUI.itemStack = itemStack;
    }

    public static ItemStack getItemStack() {
        return itemStack;
    }
}

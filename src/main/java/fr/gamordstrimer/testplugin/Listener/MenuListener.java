package fr.gamordstrimer.testplugin.Listener;

import fr.gamordstrimer.testplugin.menusystem.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        // Add null check for clicked inventory
        if (event.getClickedInventory() == null) {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();
        InventoryHolder holder = clickedInventory.getHolder();

        if (holder instanceof Menu) {
            // Cancel the event for all interactions within the menu
            event.setCancelled(true);

            // Additional check to handle shift-clicks
            if (event.isShiftClick()) return;

            if (event.getCurrentItem() == null) return;

            Menu menu = (Menu) holder;
            menu.handleMenu(event);

        } else if (event.isShiftClick()) {
            // Prevent shift-clicking items into the menu from the player's inventory
            Inventory topInventory = player.getOpenInventory().getTopInventory();

            if (topInventory.getHolder() instanceof Menu) event.setCancelled(true);
        }
    }
}

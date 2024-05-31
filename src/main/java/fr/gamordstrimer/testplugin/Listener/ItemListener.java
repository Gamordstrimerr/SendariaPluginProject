package fr.gamordstrimer.testplugin.Listener;

import fr.gamordstrimer.testplugin.itemsystem.Item;
import fr.gamordstrimer.testplugin.itemsystem.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        ItemStack item = event.getItemStack();
        for (Item customItem : ItemManager.getCustomItems().values()) {
            if (isSimilar(item, customItem.getItemStack())) {
                customItem.handleItem(event);
                break; // No need to check further once handled
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        for (Item customItem : ItemManager.getCustomItems().values()) {
            if (isSimilar(item, customItem.getItemStack())) {
                customItem.handleItem(event);
                break;
            }
        }
    }

    private boolean isSimilar(ItemStack item1, ItemStack item2) {
        return item1 != null && item2 != null && item1.isSimilar(item2);
    }
}

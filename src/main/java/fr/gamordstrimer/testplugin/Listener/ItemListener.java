package fr.gamordstrimer.testplugin.Listener;

import fr.gamordstrimer.testplugin.itemsystem.Item;
import fr.gamordstrimer.testplugin.itemsystem.ItemManager;
import fr.gamordstrimer.testplugin.itemsystem.item.TeleportationStone;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        ItemStack item = event.getItemStack();
        if (event.getHand() == EquipmentSlot.HAND) {
            if (item == null) {
                Bukkit.getLogger().info("HangingPlaceEvent triggered with a null item.");
                return;
            }

            boolean itemMatched = false;
            for (Item customItem : ItemManager.getCustomItems().values()) {
                if (item.isSimilar(customItem.getItemStack())) {
                    customItem.handleItem(event);
                    itemMatched = true;
                    break;
                }
            }

            if (!itemMatched) {
                Bukkit.getLogger().info("Item not similar to any custom items.");
            }
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (event.getHand() == EquipmentSlot.HAND) {
            if (item == null) {
                Bukkit.getLogger().info("Player interacted with an empty hand or a non-item.");
                return;
            }

            boolean itemMatched = false;
            for (Item customItem : ItemManager.getCustomItems().values()) {
                if (item.isSimilar(customItem.getItemStack())) {
                    customItem.handleItem(event);
                    itemMatched = true;
                    break;
                }
            }
            if (item.isSimilar(TeleportationStone.activateItem())) {
                new TeleportationStone().handleItem(event);
            }

            if (!itemMatched) {
                Bukkit.getLogger().info("Item not similar to any custom items.");
            }
        }
    }


}

package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerManager {
    private static Main plugin;
    private final Player player;
    private ItemStack[] items = new ItemStack[41];

    public PlayerManager(Player player) {
        this.player = player;
    }

    public static void setPlugin(Main main) {
        plugin = main;
    }

    public void init() {
        plugin.getPlayers().put(player.getUniqueId(), this);
    }

    public void destroy() {
        plugin.getPlayers().remove(player.getUniqueId());
    }

    public static PlayerManager getFromPlayer(Player player) {
        return plugin.getPlayers().get(player.getUniqueId());
    }

    public ItemStack[] getItems() {
        return items;
    }

    public void saveInventory() {
        for( int slot = 0; slot < 36; slot++) {
            ItemStack item = player.getInventory().getItem(slot);
            if(item != null) {
                items[slot] = item;
            }
        }
        items[36] = player.getInventory().getHelmet();
        items[37] = player.getInventory().getChestplate();
        items[38] = player.getInventory().getLeggings();
        items[39] = player.getInventory().getBoots();
        items[40] = player.getInventory().getItemInOffHand();

        player.getInventory().clear();
        player.getEquipment().clear();
    }

    public void giveInventory() {
        player.getInventory().clear();
        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = items[slot];
            if(item != null) {
                player.getInventory().setItem(slot, item);
            }
        }
        player.getInventory().setHelmet(items[36]);
        player.getInventory().setChestplate(items[37]);
        player.getInventory().setLeggings(items[38]);
        player.getInventory().setBoots(items[39]);
        player.getInventory().setItemInOffHand(items[40]);
    }
}

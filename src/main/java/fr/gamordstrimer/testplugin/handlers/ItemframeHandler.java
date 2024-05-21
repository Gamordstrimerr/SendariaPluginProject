package fr.gamordstrimer.testplugin.handlers;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemframeHandler implements Listener {
    private final Plugin plugin;

    public ItemframeHandler (Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemFramePlace(HangingPlaceEvent event) {
        ItemStack item = event.getItemStack();

        if (item.isSimilar(CustomItems.getItem("invisible_item_frame"))) {
            ItemFrame itemFrame = (ItemFrame) event.getEntity();
            itemFrame.setVisible(false);

            Player player = event.getPlayer();
            player.sendMessage("§bTu as placé un Cadre Invisible.");
        }
    }
}

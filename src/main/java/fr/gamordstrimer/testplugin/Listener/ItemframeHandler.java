package fr.gamordstrimer.testplugin.Listener;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.itemsystem.ItemManager;
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
    }

    @EventHandler
    public void onItemFramePlace(HangingPlaceEvent event) {
        ItemStack item = event.getItemStack();

        if (item.isSimilar(ItemManager.getCustomItem("invisible_item_frame"))) {
            ItemFrame itemFrame = (ItemFrame) event.getEntity();
            itemFrame.setVisible(false);

            Player player = event.getPlayer();
            player.sendMessage("§bTu as placé un Cadre Invisible.");
        }
    }
}

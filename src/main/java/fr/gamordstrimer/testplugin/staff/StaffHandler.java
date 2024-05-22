package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class StaffHandler implements Listener {
    private Main plugin;
    public StaffHandler(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        event.setCancelled(plugin.isFreeze((Player) event.getEntity()));
    }

    @EventHandler
    public void onItemDrop(EntityDropItemEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        event.setCancelled(plugin.isFreeze((Player) event.getEntity()));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(plugin.isFreeze(event.getPlayer()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(plugin.isFreeze(event.getPlayer()));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        event.setCancelled(plugin.isFreeze((Player) event.getEntity()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        event.setCancelled(plugin.isFreeze(event.getPlayer()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(plugin.isFreeze((Player) event.getWhoClicked()));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(plugin.isFreeze(player)) {
            event.setTo(event.getFrom());
        }
    }
}

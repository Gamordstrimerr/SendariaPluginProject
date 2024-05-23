package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class StaffHandler implements Listener {
    private Main plugin;
    public StaffHandler(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        event.setCancelled(plugin.isInStaff((Player) event.getEntity()) || plugin.isFreeze((Player) event.getEntity()));
    }

    @EventHandler
    public void onItemDrop(EntityDropItemEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        event.setCancelled(plugin.isInStaff((Player) event.getEntity()) || plugin.isFreeze((Player) event.getEntity()));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(plugin.isInStaff(event.getPlayer()) || plugin.isFreeze(event.getPlayer()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(plugin.isInStaff(event.getPlayer()) || plugin.isFreeze(event.getPlayer()));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        event.setCancelled(plugin.isInStaff((Player) event.getEntity()) || plugin.isFreeze((Player) event.getEntity()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(plugin.isFreeze(player));
        if (event.getHand() != EquipmentSlot.HAND) return;
        if(!plugin.isInStaff(player)) return;

        ItemStack itemInHand = player.getInventory().getItem(EquipmentSlot.HAND);
        if(itemInHand == null) return;

        Action action = event.getAction();
        Map<String, ItemStack> staffItems = StaffMode.getInstance().getStaffItems();
        for (Map.Entry<String, ItemStack> entry : staffItems.entrySet()) {
            if(itemInHand.equals(entry.getValue())) {
                boolean left = action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK);
                boolean right = action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
                switch (entry.getKey()) {
                    case "compass":
                        if(right) {
                            player.sendMessage("You used right click with the compass!");
                        } else if (left) {
                            player.sendMessage("You used left click with the compass!");
                        }
                        break;
                    case "disable_vanish":
                        if(right) {
                            player.sendMessage("You used disable vanish!");
                        }
                        break;
                    case "enable_vanish":
                        if(right) {
                            player.sendMessage("You used enable vanish!");
                        }
                        break;
                    case "paper":
                        if(right) {
                            player.sendMessage("You used the paper!");
                        }
                        break;
                    case "headstaff":
                        if(right) {
                            player.sendMessage("You used the headstaff!");
                        }
                        break;
                    case "ice":
                        if(right) {
                            player.sendMessage("You used the ice!");
                        }
                        break;
                    case "chest":
                        if(right) {
                            player.sendMessage("You used the chest!");
                        }
                        break;
                    default:
                        // Handle unknown item
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null)  return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(plugin.isInStaff(player) || plugin.isFreeze(player));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(plugin.isFreeze(player)) {
            event.setTo(event.getFrom());
        }
    }


}

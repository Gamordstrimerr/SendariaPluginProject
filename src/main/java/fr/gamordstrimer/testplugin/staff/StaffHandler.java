package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.menusystem.menu.InvseeGUI;
import fr.gamordstrimer.testplugin.menusystem.menu.SelPlayerGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class StaffHandler implements Listener {
    private final Main plugin;

    public StaffHandler(Main plugin) {
        this.plugin = plugin;
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
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for(int i = 0; i < plugin.getVanish().size(); i++) {
            UUID vanishedPlayerUUID = plugin.getVanish().get(i);
            Player vanishedPlayer = Bukkit.getPlayer(vanishedPlayerUUID);
            if (vanishedPlayer != null) {
                player.hidePlayer(plugin, vanishedPlayer);
            }
        }
        // Update the GUI when a player joins
        SelPlayerGUI.updateMenu();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player quittingPlayer = event.getPlayer();
        List<Player> toRemove = new ArrayList<>();

        // Iterate over all player-target mappings to find if quitting player is a target
        SelPlayerGUI.getTargets().forEach((opener, target) -> {
            if (target.equals(quittingPlayer)) {

                UUID targetUUID = target.getUniqueId();
                String targetName = target.getName();
                Location targetLoc = target.getLocation();
                int locX = targetLoc.getBlockX();
                int locY = targetLoc.getBlockY();
                int locZ = targetLoc.getBlockZ();

                //Using the MiniMessage API to make Gradient Color Message
                var mm = MiniMessage.miniMessage();
                Component border = mm.deserialize("<gradient:#ff5555:#6b0000><st>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</st></gradient>");
                Component coordonate_message = mm.deserialize("<gold>Coordonnées où le joueur c'est déconnecté :</gold> <click:run_command:'/tp " + locX + " " + locY + " " + locZ + "'><yellow><hover:show_text:'<green>Clique ici pour te Téléporter</green>'>x: " + locX + " y: " + locY + " z: " + locZ + "</hover></yellow></click>");

                List<Component> messageLines = new ArrayList<>();
                messageLines.add(border);
                messageLines.add(Component.text("➤ Le joueur : ").color(NamedTextColor.RED)
                        .append(Component.text(targetName).color(NamedTextColor.DARK_RED)
                        .append(Component.text(" viens de ce déconnecter.").color(NamedTextColor.RED))));
                messageLines.add(Component.text(" "));
                messageLines.add(Component.text("UUID du joueur :").color(NamedTextColor.GOLD)
                        .append(Component.text(" " + targetUUID).color(NamedTextColor.YELLOW)));
                messageLines.add(Component.text("Pseudo du player : " ).color(NamedTextColor.GOLD)
                        .append(Component.text(targetName).color(NamedTextColor.YELLOW)));
                messageLines.add(coordonate_message);
                messageLines.add(border);

                Component finalMessage = Component.empty();
                for (Component line : messageLines) {
                    finalMessage = finalMessage.append(line).append(Component.newline());
                }

                // Notify the opener that their target has disconnected
                opener.sendMessage(finalMessage);

                toRemove.add(opener); // Collect keys to remove
            }
        });

        // Remove the collected keys outside the iteration
        toRemove.forEach(SelPlayerGUI::removeTarget);

        // Update the GUI when a player quits with a slight delay
        Bukkit.getScheduler().runTaskLater(plugin, SelPlayerGUI::updateMenu, 1L);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        String prefixServer = Objects.requireNonNull(plugin.getConfig().getString("messages.prefixserver"), "Prefix server message not found");
        prefixServer = prefixServer.replace("&", "§");

        Player player = event.getPlayer();
        if (!plugin.isInStaff(player)) return;

        Action action = event.getAction();
        boolean isLeftClick = action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK);
        boolean isRightClick = action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);

        if (event.getHand() == EquipmentSlot.HAND) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            for(Map.Entry<String, ItemStack> entry : StaffMode.getInstance().getStaffItems().entrySet()) {
                if(itemInHand.equals(entry.getValue())) {
                    switch (entry.getKey()) {
                        case "compass":
                            if(isRightClick) {
                                new SelPlayerGUI(Main.getPlayerMenuUtility(player)).open();
                            } else if (isLeftClick) {
                                Player target = SelPlayerGUI.getTarget(player);
                                if(target != null) {
                                    player.teleport(target);
                                    player.sendMessage(Component.text(prefixServer)
                                            .append(Component.text("Tu as été téléporter à ")
                                                    .append(Component.text(target.getName()).color(NamedTextColor.GOLD)
                                                            .append(Component.text(".").color(NamedTextColor.WHITE)))));
                                } else {
                                    player.sendMessage(Component.text(prefixServer)
                                            .append(Component.text("Tu dois séléctionner à un joueur avant d'utiliser cette Fonction.").color(NamedTextColor.RED)));
                                }
                            }
                            break;
                        case "disable_vanish":
                            if(isRightClick) {
                                Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(plugin, player));
                                plugin.getVanish().remove(player.getUniqueId());
                                player.sendMessage(Component.text(prefixServer)
                                        .append(Component.text("Vanish ")
                                        .append(Component.text("Désactiver.").color(NamedTextColor.RED))));
                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                    player.getInventory().setItem(1, StaffMode.getInstance().getStaffItems().get("enable_vanish"));
                                }, 1L);
                            }
                            break;
                        case "enable_vanish":
                            if(isRightClick) {
                                Bukkit.getOnlinePlayers().forEach(players -> players.hidePlayer(plugin, player));
                                plugin.getVanish().add(player.getUniqueId());
                                player.sendMessage(Component.text(prefixServer)
                                        .append(Component.text("Vanish ")
                                        .append(Component.text("Activé.").color(NamedTextColor.GREEN))));
                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                    player.getInventory().setItem(1, StaffMode.getInstance().getStaffItems().get("disable_vanish"));
                                }, 1L);
                            }
                            break;
                        case "paper":
                            if(isRightClick) {
                                player.sendMessage("You used the paper!");
                            }
                            break;
                        case "headstaff":
                            if(isRightClick) {
                                player.sendMessage("You used the headstaff!");
                            }
                            break;
                        case "ice":
                            if(isRightClick) {
                                Player target = SelPlayerGUI.getTarget(player);
                                if (target != null) {
                                    plugin.getFreezedplayer().put(target.getUniqueId(), target.getLocation());
                                    player.sendMessage(Component.text(prefixServer).append(Component.text(target.getName()).color(NamedTextColor.DARK_RED))
                                            .append(Component.text(" à été Gelé.").color(NamedTextColor.RED)));
                                    target.sendMessage(Component.text(prefixServer).append(Component.text("Vous avez été Gelé.").color(NamedTextColor.RED)));
                                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                        player.getInventory().setItem(5, StaffMode.getInstance().getStaffItems().get("unice"));
                                    }, 1L);
                                } else {
                                    player.sendMessage(Component.text(prefixServer)
                                            .append(Component.text("Tu dois séléctionner à un joueur avant d'utiliser cette Fonction.").color(NamedTextColor.RED)));
                                }
                            }
                            break;
                        case "unice":
                            if(isRightClick) {
                                Player target = SelPlayerGUI.getTarget(player);
                                if(target != null) {
                                    plugin.getFreezedplayer().remove(target.getUniqueId());
                                    player.sendMessage(Component.text(prefixServer).append(Component.text(target.getName()).color(NamedTextColor.DARK_RED))
                                            .append(Component.text(" à été Degelé.").color(NamedTextColor.RED)));
                                    target.sendMessage(Component.text(prefixServer).append(Component.text("Vous avez été Degelé.").color(NamedTextColor.RED)));
                                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                        player.getInventory().setItem(5, StaffMode.getInstance().getStaffItems().get("ice"));
                                    }, 1L);
                                } else {
                                    player.sendMessage(Component.text(prefixServer)
                                            .append(Component.text("Tu dois séléctionner à un joueur avant d'utiliser cette Fonction.").color(NamedTextColor.RED)));
                                }
                            }
                            break;
                        case "chest":
                            if(isRightClick) {
                                Player target = SelPlayerGUI.getTarget(player);
                                if (target != null) {
                                    new InvseeGUI(Main.getPlayerMenuUtility(player)).open();
                                    InvseeGUI.startScheduler();
                                } else {
                                    player.sendMessage(Component.text(prefixServer)
                                            .append(Component.text("Tu dois séléctionner à un joueur avant d'utiliser cette Fonction.").color(NamedTextColor.RED)));
                                }
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

        event.setCancelled(plugin.isFreeze(player));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null)  return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(plugin.isInStaff(player) || plugin.isFreeze(player));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof SelPlayerGUI) {
            SelPlayerGUI selPlayerGUI = (SelPlayerGUI) event.getInventory().getHolder();
            SelPlayerGUI.removeInstance(selPlayerGUI);
        } else if (event.getInventory().getHolder() instanceof InvseeGUI) {
            InvseeGUI.cancelScheduler();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(plugin.isFreeze(player)) {
            event.setTo(event.getFrom());
        }
    }
}

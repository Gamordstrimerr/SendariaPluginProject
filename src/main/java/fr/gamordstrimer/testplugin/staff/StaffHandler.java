package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.menusystem.menu.InvseeGUI;
import fr.gamordstrimer.testplugin.menusystem.menu.SelPlayerGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class StaffHandler implements Listener {
    private final Main plugin;
    private static Inventory invSee;
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
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for(int i = 0; i < plugin.getVanish().size(); i++) {
            UUID vanishedPlayerUUID = plugin.getVanish().get(i);
            Player vanishedPlayer = Bukkit.getPlayer(vanishedPlayerUUID);
            if (vanishedPlayer != null) {
                player.hidePlayer(plugin, vanishedPlayer);
            }
        }
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
                                if (target != null) {
                                    new InvseeGUI(Main.getPlayerMenuUtility(player)).open();
                                    startInvSeeGUILoop(player);
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
        Player player = (Player) event.getPlayer();
        if (event.getInventory().equals(invSee)) {
            // Retrieve and cancel the BukkitRunnable associated with the inventory
            if (player.hasMetadata("InvSeeTask")) {
                BukkitRunnable task = (BukkitRunnable) player.getMetadata("InvSeeTask").get(0).value();
                assert task != null;
                task.cancel();
                player.removeMetadata("InvSeeTask", plugin);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(plugin.isFreeze(player)) {
            event.setTo(event.getFrom());
        }
    }

    public void updateInvSeeGUI() {
        invSee.clear();

        //Create Custom ItemStack
        UUID playerUUID = UUID.fromString(String.valueOf(target.getUniqueId()));
        ItemStack skull  = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setPlayerProfile(target.getPlayerProfile());
        skullMeta.displayName(Component.text("Profile de ").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(target.getName()).color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("------------------------").color(NamedTextColor.GRAY).decoration(TextDecoration.STRIKETHROUGH, true).decoration(TextDecoration.BOLD, true));
        lore.add(Component.text("Vie : ").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(target.getHealth()).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(" ❤").color(NamedTextColor.DARK_RED).decoration(TextDecoration.ITALIC, false))));
        lore.add(Component.text("Nourriture : ").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(target.getFoodLevel()).color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(" \uD83C\uDF57")).color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)));
        lore.add(Component.text("Emplacement : ").color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(target.getLocation().getBlockX() + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ()).color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false)));
        lore.add(Component.text("------------------------").color(NamedTextColor.GRAY).decoration(TextDecoration.STRIKETHROUGH, true).decoration(TextDecoration.BOLD, true));
        skullMeta.lore(lore);
        skull.setItemMeta(skullMeta);

        ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta backgroundMeta = background.getItemMeta();
        backgroundMeta.displayName(Component.text(" "));
        background.setItemMeta(backgroundMeta);

        // Set the inventory items
        ItemStack[] inventoryContents = target.getInventory().getContents();
        for (int i = 0; i < inventoryContents.length; i++) {
            invSee.setItem(i, inventoryContents[i]);
        }
        invSee.setItem(41, background);
        invSee.setItem(42, background);
        invSee.setItem(43, background);
        invSee.setItem(44, skull);
    }

    public void startInvSeeGUILoop(Player player) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                updateInvSeeGUI();
            }
        };
        task.runTaskTimer(plugin, 0, 20);

        // Store the BukkitRunnable in the player's metadata so it can be canceled when the inventory is closed
        player.setMetadata("InvSeeTask", new FixedMetadataValue(plugin, task));
    }
}

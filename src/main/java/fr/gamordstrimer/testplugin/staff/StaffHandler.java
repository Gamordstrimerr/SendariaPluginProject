package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class StaffHandler implements Listener {
    private final Main plugin;
    private static Inventory selPlayer;
    private Player target;
    private Player opener;
    public StaffHandler(Main plugin) {
        this.plugin = plugin;
        selPlayer = Bukkit.createInventory(null, 9*5, Component.text("Séléctionnez un joueur").color(NamedTextColor.DARK_PURPLE));
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
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, StaffHandler.this::updateSelPlayerGUI, 20);
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
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, StaffHandler.this::updateSelPlayerGUI, 20);
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
                                DefineOpener(player);
                                selPlayerGUI(player);
                            } else if (isLeftClick) {
                                if(target != null) {
                                    player.teleport(target);
                                    player.sendMessage(Component.text(prefixServer)
                                            .append(Component.text(" Tu as été téléporter à ")
                                                    .append(Component.text(target.getName()).color(NamedTextColor.GOLD)
                                                            .append(Component.text(".").color(NamedTextColor.WHITE)))));
                                } else {
                                    player.sendMessage(Component.text(prefixServer)
                                            .append(Component.text("tu dois te téléporter à un joueur avant d'utiliser cette Fonction.").color(NamedTextColor.RED)));
                                }
                            }
                            break;
                        case "disable_vanish":
                            if(isRightClick) {
                                Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(plugin, player));
                                plugin.getVanish().remove(player.getUniqueId());
                                player.sendMessage(Component.text(prefixServer)
                                        .append(Component.text(" Vanish ")
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
                                        .append(Component.text(" Vanish ")
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
                                    if(plugin.getFreezedplayer().containsKey(target.getUniqueId())) {
                                        plugin.getFreezedplayer().remove(target.getUniqueId());
                                        player.sendMessage(Component.text(prefixServer).append(Component.text(" " + target.getName()).color(NamedTextColor.DARK_RED))
                                                .append(Component.text(" à été Degelé.").color(NamedTextColor.RED)));
                                        target.sendMessage(Component.text(prefixServer).append(Component.text(" Vous avez été Degelé.").color(NamedTextColor.RED)));
                                    } else {
                                        plugin.getFreezedplayer().put(target.getUniqueId(), target.getLocation());
                                        player.sendMessage(Component.text(prefixServer).append(Component.text(" " + target.getName()).color(NamedTextColor.DARK_RED))
                                                .append(Component.text(" à été Gelé.").color(NamedTextColor.RED)));
                                        target.sendMessage(Component.text(prefixServer).append(Component.text(" Vous avez été Gelé.").color(NamedTextColor.RED)));
                                    }

                                } else {
                                    player.sendMessage(Component.text(prefixServer)
                                            .append(Component.text("tu dois te téléporter à un joueur avant d'utiliser cette Fonction.").color(NamedTextColor.RED)));
                                }
                            }
                            break;
                        case "chest":
                            if(isRightClick) {
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

        event.setCancelled(plugin.isFreeze(player));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null)  return;
        Player player = (Player) event.getWhoClicked();
        String prefixserver = Objects.requireNonNull(plugin.getConfig().getString("messages.prefixserver")).replace("&", "§");
        Inventory inventory = event.getInventory();
        ItemStack item = event.getCurrentItem();
        if (inventory.equals(selPlayer)) {
            event.setCancelled(true);
            int lastSlot = inventory.getSize() - 1;
            if(event.getSlot() == lastSlot) {
                List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                if (onlinePlayers.size() > 1) {
                    onlinePlayers.remove(player);
                    Player targetPlayer = onlinePlayers.get(new Random().nextInt(onlinePlayers.size()));
                    DefineTarget(targetPlayer);
                    player.teleport(target);
                    player.sendMessage(Component.text(prefixserver)
                            .append(Component.text(" Tu as été téléporter à ")
                            .append(Component.text(target.getName()).color(NamedTextColor.GOLD)
                            .append(Component.text(".").color(NamedTextColor.WHITE)))));
                } else {
                    player.sendMessage(Component.text(prefixserver).append(Component.text(" Il n'y a pas assez de joueur en ligne pour utiliser cette fonction !").color(NamedTextColor.RED)));
                }
            } else {
                if (item != null && item.getType() == Material.PLAYER_HEAD) {
                    String playerName = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(item.getItemMeta().displayName()));
                    Player selplayer = Bukkit.getPlayerExact(playerName);
                    if (selplayer != null && player != selplayer) {
                        DefineTarget(selplayer);
                        player.teleport(target);
                        player.sendMessage(Component.text(prefixserver)
                                .append(Component.text(" Tu as été téléporter à ")
                                .append(Component.text(target.getName()).color(NamedTextColor.GOLD)
                                .append(Component.text(".").color(NamedTextColor.WHITE)))));
                    } else {
                        player.sendMessage(Component.text(prefixserver).append(Component.text(" Joueur Invalide.").color(NamedTextColor.RED)));
                    }
                    player.closeInventory();
                }
            }
        }

        event.setCancelled(plugin.isInStaff(player) || plugin.isFreeze(player));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(plugin.isFreeze(player)) {
            event.setTo(event.getFrom());
        }
    }

    public void selPlayerGUI(Player player) {
        selPlayer.clear();
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        Set<UUID> addedPlayers = new HashSet<>(); // Track added players

        for (Player onlinePlayer : onlinePlayers) {
            if (addedPlayers.contains(onlinePlayer.getUniqueId())) {
                continue;
            }

            ItemStack headItem = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
            assert skullMeta != null;
            skullMeta.setPlayerProfile(onlinePlayer.getPlayerProfile());
            skullMeta.displayName(Component.text(onlinePlayer.getName()).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            if(opener != null && onlinePlayer.getUniqueId().equals(opener.getUniqueId())) {
                lore.add(Component.text(" "));
                lore.add(Component.text("⚠ Tu ne peux pas te").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text("   téléporter à toi même.").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text(" "));
            } else {
                lore.add(Component.text(" "));
                lore.add(Component.text("➢ Clique pour te Téléporter").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text(" "));
            }
            skullMeta.lore(lore);
            headItem.setItemMeta(skullMeta);
            selPlayer.addItem(headItem);

            addedPlayers.add(onlinePlayer.getUniqueId());
        }

        int invsize = selPlayer.getSize();
        int lastslot = invsize - 1;
        selPlayer.setItem(lastslot, StaffMode.getInstance().getStaffItems().get("dicerandom"));

        player.openInventory(selPlayer);
    }

    public void updateSelPlayerGUI() {
        selPlayer.clear();
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        Set<UUID> addedPlayers = new HashSet<>(); // Track added players

        for (Player onlinePlayer : onlinePlayers) {
            if (addedPlayers.contains(onlinePlayer.getUniqueId())) {
                continue;
            }

            ItemStack headItem = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
            assert skullMeta != null;
            skullMeta.setPlayerProfile(onlinePlayer.getPlayerProfile());
            skullMeta.displayName(Component.text(onlinePlayer.getName()).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            if(opener != null && onlinePlayer.getUniqueId().equals(opener.getUniqueId())) {
                lore.add(Component.text(" "));
                lore.add(Component.text("⚠ Tu ne peux pas te").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text("   téléporter à toi même.").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text(" "));
            } else {
                lore.add(Component.text(" "));
                lore.add(Component.text("➢ Clique pour te Téléporter").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text(" "));
            }
            skullMeta.lore(lore);
            headItem.setItemMeta(skullMeta);
            selPlayer.addItem(headItem);

            addedPlayers.add(onlinePlayer.getUniqueId());
        }

        int invsize = selPlayer.getSize();
        int lastslot = invsize - 1;
        selPlayer.setItem(lastslot, StaffMode.getInstance().getStaffItems().get("dicerandom"));
    }

    public void DefineTarget(Player player) {
        target = player;
    }

    public void DefineOpener(Player player) {
        opener = player;
    }
}

package fr.gamordstrimer.testplugin.handlers;

import fr.gamordstrimer.testplugin.CooldownManager;
import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.heads.SkullTextureChanger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PlayerHandler implements Listener {
    private final Main plugin;
    private final ItemStack magicLantern;
    private final CooldownManager cooldownManager;
    private static Inventory mainGUI;
    private static Map<String, ItemStack> setupItemGUI = new HashMap<>();

    public PlayerHandler(Main plugin, CooldownManager cooldownManager) {
        this.plugin = plugin;
        this.magicLantern = CustomItems.getItem("magic_lantern");
        this.cooldownManager = cooldownManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String joinMessage = Objects.requireNonNull(plugin.getConfig().getString("messages.joinmessage")).replace('&', '§');
        event.joinMessage(Component.text(joinMessage + " " + player.getName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String leaveMessage = Objects.requireNonNull(plugin.getConfig().getString("messages.leavemessage")).replace('&', '§');
        event.quitMessage(Component.text(leaveMessage + " " + player.getName()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        String prefixserver = plugin.getConfig().getString("messages.prefixserver").replace("&", "§");

        if (item != null && item.isSimilar(magicLantern)) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                UUID playerUUID = player.getUniqueId();

                if (cooldownManager.hasCooldown(playerUUID)) {
                    long remainingTimeMillis = cooldownManager.getCooldown(playerUUID) - System.currentTimeMillis();
                    long remainingTimeSeconds = remainingTimeMillis / 1000;
                    long minutes = remainingTimeSeconds / 60;
                    long seconds = remainingTimeSeconds % 60;

                    String remainingTimeMessage = String.format("%d minutes et %d secondes", minutes, seconds);
                    player.sendMessage(Component.text("Tu dois patienter ").color(NamedTextColor.RED)
                            .append(Component.text(remainingTimeMessage).color(NamedTextColor.DARK_RED)
                            .append(Component.text(" pour utiliser la lanterne magique").color(NamedTextColor.RED))));

                    event.setCancelled(true);
                    return;
                }

                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 60 * 15, 1));
                player.sendMessage(Component.text(prefixserver + "Tu as reçu un effet de Vision Nocture pendant ")
                        .append(Component.text("15 minutes.").color(NamedTextColor.GOLD)));
                event.setCancelled(true);

                long cooldownTime = System.currentTimeMillis() + (30 * 60 * 1000);
                cooldownManager.setCooldown(playerUUID, cooldownTime);

                Bukkit.getScheduler().runTaskLater(plugin, () -> cooldownManager.removeCooldown(playerUUID), 30 * 60 * 20L); // 30 minutes in ticks
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedInventory != null && clickedInventory.equals(mainGUI)) {
            event.setCancelled(true);
        }
    }

    public static void mainGUI(Player player) {
        mainGUI = Bukkit.createInventory(null, 9*5, Component.text("Custom Item de Sendaria").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true));
        // Iterate over the entries of the map
        for (Map.Entry<String, ItemStack> entry : CustomItems.getCustomItems().entrySet()) {
            ItemStack item = entry.getValue();
            mainGUI.addItem(item);
        }
        player.openInventory(mainGUI);
    }

    private static void setupItemGUI() {

        //headback in menu (go back)
        ItemStack headback = new ItemStack(Material.PLAYER_HEAD);
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjZkOGVmZjRjNjczZTA2MzY5MDdlYTVjMGI1ZmY0ZjY0ZGMzNWM2YWFkOWI3OTdmMWRmNjYzMzUxYjRjMDgxNCJ9fX0=";
        SkullTextureChanger.setSkullTexture(headback, base64Texture);
        SkullMeta headbackmeta = (SkullMeta) headback.getItemMeta();
        headbackmeta.displayName(Component.text("Retour").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
        headback.setItemMeta(headbackmeta);
        setupItemGUI.put("headback", headback);

        // crafting table
        ItemStack craftingtable = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta craftingtablemeta = craftingtable.getItemMeta();
        craftingtablemeta.displayName(Component.text("Table de Craft").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("━━━━━━━━━━━━━━━━━━━━").color(NamedTextColor.GRAY));
        lore.add(Component.text("Cette Item peut être").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("fabriqué dans une").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("Table de Craft").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("━━━━━━━━━━━━━━━━━━━━").color(NamedTextColor.GRAY));
        craftingtablemeta.lore(lore);
        craftingtable.setItemMeta(craftingtablemeta);
        setupItemGUI.put("craftingtable", craftingtable);
    }
}

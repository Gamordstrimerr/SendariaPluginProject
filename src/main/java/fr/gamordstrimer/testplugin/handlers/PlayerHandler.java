package fr.gamordstrimer.testplugin.handlers;

import fr.gamordstrimer.testplugin.CooldownManager;
import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.UUID;

public class PlayerHandler implements Listener {
    private final Main plugin;
    private final ItemStack magicLantern;
    private final CooldownManager cooldownManager;

    public PlayerHandler(Main plugin, CooldownManager cooldownManager) {
        this.plugin = plugin;
        this.magicLantern = CustomItems.getItem("magic_lantern");
        this.cooldownManager = cooldownManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
}

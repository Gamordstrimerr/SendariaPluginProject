package fr.gamordstrimer.testplugin.Listener;

import fr.gamordstrimer.testplugin.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class PlayerHandler implements Listener {
    private final Main plugin;

    public PlayerHandler(Main plugin) {
        this.plugin = plugin;
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

}

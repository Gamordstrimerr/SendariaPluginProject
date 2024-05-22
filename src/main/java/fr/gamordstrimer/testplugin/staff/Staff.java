package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.CooldownManager;
import fr.gamordstrimer.testplugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Staff implements CommandExecutor {

    private final CooldownManager cooldownManager;
    private final Main plugin;

    public Staff(Main plugin, CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String perms = Objects.requireNonNull(plugin.getConfig().getString("messages.haspermissions")).replace("&", "§");
        String prefixserver = Objects.requireNonNull(plugin.getConfig().getString("messages.prefixserver")).replace("&", "§");
        if (command.getName().equalsIgnoreCase("resetcooldown")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            if(player.hasPermission("")) {
                if (args.length == 0) {
                    cooldownManager.removeCooldown(player.getUniqueId());
                    player.sendMessage(prefixserver + " Votre cooldown à été réinitialisé.");
                } else if (args.length == 1) {
                    Player target = player.getServer().getPlayer(args[0]);
                    if (target != null) {
                        cooldownManager.removeCooldown(target.getUniqueId());
                        player.sendMessage(prefixserver + " le cooldown de " + target.getName() + " à été réinitialisé.");
                    } else {
                        player.sendMessage(prefixserver + " Player not found.");
                    }
                } else {
                    player.sendMessage(prefixserver + " Utilisation: /resetcooldown [player]");
                }
                return true;
            } else {
                player.sendMessage(prefixserver + " " +perms);
            }
        } else if (command.getName().equalsIgnoreCase("freeze")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            if(player.hasPermission("")) {

            } else {
                player.sendMessage(prefixserver + " " +perms);
            }
        }
        // Add more commands here if needed
        return true;
    }
}

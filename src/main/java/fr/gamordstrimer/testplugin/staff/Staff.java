package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.CooldownManager;
import fr.gamordstrimer.testplugin.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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
                        player.sendMessage(prefixserver + " Joueur non trouvé.");
                    }
                } else {
                    player.sendMessage(Component.text(prefixserver)
                            .append(Component.text("Utilisation : ")
                            .append(Component.text("/resetcooldown [player]").color(NamedTextColor.GOLD))));
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
                if (args.length == 1) {
                    Player target = player.getServer().getPlayer(args[0]);
                    if (target != null) {
                        if(plugin.getFreezedplayer().containsKey(target.getUniqueId())) {
                            plugin.getFreezedplayer().remove(target.getUniqueId());
                            player.sendMessage(Component.text(prefixserver).append(Component.text(" " + target.getName()).color(NamedTextColor.DARK_RED))
                                    .append(Component.text(" à été Degelé.").color(NamedTextColor.RED)));
                            target.sendMessage(Component.text(prefixserver).append(Component.text(" Vous avez été Degelé.").color(NamedTextColor.RED)));
                        } else {
                            plugin.getFreezedplayer().put(target.getUniqueId(), target.getLocation());
                            player.sendMessage(Component.text(prefixserver).append(Component.text(" " + target.getName()).color(NamedTextColor.DARK_RED))
                                    .append(Component.text(" à été Gelé.").color(NamedTextColor.RED)));
                            target.sendMessage(Component.text(prefixserver).append(Component.text(" Vous avez été Gelé.").color(NamedTextColor.RED)));
                        }

                    } else {
                        player.sendMessage(prefixserver + " Joueur non trouvé.");
                    }
                } else {
                    player.sendMessage(Component.text(prefixserver)
                            .append(Component.text("Utilisation : ")
                            .append(Component.text("/freeze [player]").color(NamedTextColor.GOLD))));
                }
            } else {
                player.sendMessage(prefixserver + " " +perms);
            }
        } else if (command.getName().equalsIgnoreCase("vanish")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            if(player.hasPermission("")) {
                if (args.length == 0) {
                    if (plugin.isVanish(player)) {
                        Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(plugin, player));
                        plugin.getVanish().remove(player.getUniqueId());
                        player.sendMessage(Component.text(prefixserver)
                                .append(Component.text(" Vanish ")
                                .append(Component.text("Désactiver.").color(NamedTextColor.RED))));
                    } else {
                        Bukkit.getOnlinePlayers().forEach(players -> players.hidePlayer(plugin, player));
                        plugin.getVanish().add(player.getUniqueId());
                        player.sendMessage(Component.text(prefixserver)
                                .append(Component.text(" Vanish ")
                                .append(Component.text("Activé.").color(NamedTextColor.GREEN))));
                    }
                } else {
                    player.sendMessage(Component.text(prefixserver)
                            .append(Component.text(" Utilisation : ")
                            .append(Component.text("/vanish").color(NamedTextColor.GOLD))));
                }
            } else {
                player.sendMessage(prefixserver + " " +perms);
            }
        }
        // Add more commands here if needed
        return true;
    }
}

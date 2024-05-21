package fr.gamordstrimer.testplugin.commands;

import fr.gamordstrimer.testplugin.Main;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeAliases implements CommandExecutor {
    private final Main plugin;

    public GamemodeAliases(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        String perms = plugin.getConfig().getString("messages.haspermissions").replace("&", "§");
        String prefixserver = plugin.getConfig().getString("messages.prefixserver").replace("&", "§");

        if (player.hasPermission("sendaria.staff.gm") || player.hasPermission("*")) {
            if (args.length > 0) {
                String arg = args[0].toLowerCase(); // Convert to lowercase for case-insensitive comparison
                if (arg.equals("su") || arg.equals("0")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(prefixserver + " Votre Gamemode a été mis à jour.");
                } else if (arg.equals("c") || arg.equals("1")) {
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(prefixserver + " Votre Gamemode a été mis à jour.");
                } else if (arg.equals("a") || arg.equals("2")) {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(prefixserver + " Votre Gamemode a été mis à jour.");
                } else if (arg.equals("sp") || arg.equals("3")) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(prefixserver + " Votre Gamemode a été mis à jour.");
                } else {
                    player.sendMessage(prefixserver + " Utilisation : /gamemode <Type> <Player>");
                }
            } else {
                player.sendMessage(prefixserver + " Utilisation : /gamemode <Type> <Player>");
            }
        } else {
            player.sendMessage(prefixserver + " " + perms);
        }
        return true;
    }
}

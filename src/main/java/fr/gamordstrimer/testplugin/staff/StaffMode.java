package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffMode implements CommandExecutor {
    private Main plugin;
    public StaffMode(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String s, String[] args) {
        String perms = plugin.getConfig().getString("messages.haspermissions").replace("&", "§");
        String prefixserver = plugin.getConfig().getString("messages.prefixserver").replace("&", "§");
        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command.");
            return false;
        }
        Player player = (Player) sender;
        if(player.hasPermission("") || player.hasPermission("*")) {
            if(plugin.getStaff().contains(player.getUniqueId())) {
                PlayerManager pm = PlayerManager.getFromPlayer(player);
                plugin.getStaff().remove(player.getUniqueId());
                player.getInventory().clear();
                player.sendMessage(Component.text(prefixserver)
                        .append(Component.text(" vous n'êtes plus en mode modération.").color(TextColor.fromHexString("#9e0000"))));
                pm.giveInventory();
                pm.destroy();
                return false;
            }
            PlayerManager pm = new PlayerManager(player);
            pm.init();
            plugin.getStaff().add(player.getUniqueId());
            player.sendMessage(Component.text(prefixserver)
                    .append(Component.text(" vous êtes en mode modération").color(TextColor.fromHexString("#9e0000"))));
            pm.saveInventory();

        } else {
            player.sendMessage(Component.text(prefixserver + " " + perms));
        }
        return true;
    }
}

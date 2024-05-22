package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StaffMode implements CommandExecutor {
    private Main plugin;
    public StaffMode(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String s, String[] args) {
        return false;
    }
}

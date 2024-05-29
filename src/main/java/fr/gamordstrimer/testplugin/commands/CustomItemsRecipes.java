package fr.gamordstrimer.testplugin.commands;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.menusystem.menu.PaginatedMainGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomItemsRecipes implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true; // Return true to indicate the command was handled
        }

        if (player.hasPermission("sendaria.cir") || player.hasPermission("*")) {
            new PaginatedMainGUI(Main.getPlayerMenuUtility(player)).open();
        }
        return true;
    }
}

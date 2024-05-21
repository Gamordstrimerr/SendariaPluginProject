package fr.gamordstrimer.testplugin.commands;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static net.md_5.bungee.api.ChatColor.translateAlternateColorCodes;

public class CustomItemsGive implements CommandExecutor, TabCompleter {
    private final Main plugin;

    public CustomItemsGive(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true; // Return true to indicate the command was handled
        }

        Player player = (Player) sender;
        String perms = plugin.getConfig().getString("messages.haspermissions").replace("&", "§");
        String prefixserver = plugin.getConfig().getString("messages.prefixserver").replace("&", "§");

        if (player.hasPermission("sendaria.staff.gci") || player.hasPermission("*")) {
            if (args.length == 0) {
                player.sendMessage(translateAlternateColorCodes('&', prefixserver + " Utilisation : /givecustomitem <itemName> [amount]"));
                return true; // Return true to avoid triggering the default usage message
            }

            if (args.length == 1) {
                List<String> itemNames = new ArrayList<>(CustomItems.getCustomItems().keySet());
                itemNames.sort(String.CASE_INSENSITIVE_ORDER);
                StringBuilder itemList = new StringBuilder();
                itemList.append(translateAlternateColorCodes('&', prefixserver + " Liste des Items disponibles :\n"));
                for (String itemName : itemNames) {
                    itemList.append("§7- §f" + itemName + "\n");
                }
                player.sendMessage(itemList.toString());
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(translateAlternateColorCodes('&', prefixserver + " Utilisation : /givecustomitem <itemName> [amount]"));
                return true; // Return true to avoid triggering the default usage message
            }

            String itemName = args[0];
            ItemStack customItem = CustomItems.getItem(itemName);

            if (customItem == null) {
                player.sendMessage(translateAlternateColorCodes('&', prefixserver + " Item '" + itemName + "' non trouvé."));
                return true; // Return true to indicate the command was handled
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(translateAlternateColorCodes('&', prefixserver + " Le Nombre Spécifié est Invalide."));
                return true; // Return true to indicate the command was handled
            }

            customItem.setAmount(amount);
            player.getInventory().addItem(customItem);
            player.sendMessage(translateAlternateColorCodes('&', prefixserver + " Tu as reçu " + amount + " " + itemName));
            return true;
        } else {
            player.sendMessage(translateAlternateColorCodes('&', prefixserver + " " + perms));
            return true; // Return true to indicate the command was handled
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> itemNames = new ArrayList<>(CustomItems.getCustomItems().keySet());
            itemNames.sort(String.CASE_INSENSITIVE_ORDER);
            return itemNames;
        }
        return null;
    }
}

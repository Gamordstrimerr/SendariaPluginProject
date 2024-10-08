package fr.gamordstrimer.testplugin.commands;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.itemsystem.Item;
import fr.gamordstrimer.testplugin.itemsystem.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomItemsGive implements CommandExecutor, TabCompleter {
    private final Main plugin;

    public CustomItemsGive(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true; // Return true to indicate the command was handled
        }

        String perms = plugin.getConfig().getString("messages.haspermissions").replace("&", "§");
        String prefixserver = plugin.getConfig().getString("messages.prefixserver").replace("&", "§");

        if (player.hasPermission("sendaria.staff.gci") || player.hasPermission("*")) {
            if (args.length == 0) {
                player.sendMessage(prefixserver + "Utilisation : /givecustomitem <itemName> [amount]");
                return true; // Return true to avoid triggering the default usage message
            }

            if (args.length == 1) {
                List<String> itemNames = new ArrayList<>(ItemManager.getCustomItems().keySet());
                itemNames.sort(String.CASE_INSENSITIVE_ORDER);
                StringBuilder itemList = new StringBuilder();
                itemList.append(prefixserver + "Liste des Items disponibles :\n");
                for (String itemName : itemNames) {
                    itemList.append("§7- §f" + itemName + "\n");
                }
                player.sendMessage(itemList.toString());
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(prefixserver + "Utilisation : /givecustomitem <itemName> [amount]");
                return true; // Return true to avoid triggering the default usage message
            }

            String itemName = args[0];
            Item customItem = ItemManager.getCustomItems().get(itemName);

            if (customItem == null) {
                player.sendMessage(prefixserver + "Item '" + itemName + "' non trouvé.");
                return true; // Return true to indicate the command was handled
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(prefixserver + "Le Nombre Spécifié est Invalide.");
                return true; // Return true to indicate the command was handled
            }

            ItemStack itemStack = customItem.getItemStack();
            itemStack.setAmount(amount);
            player.getInventory().addItem(itemStack);
            player.sendMessage(prefixserver + "Tu as reçu " + amount + " " + itemName);
            return true;
        } else {
            player.sendMessage(prefixserver + perms);
            return true; // Return true to indicate the command was handled
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> itemNames = new ArrayList<>(ItemManager.getCustomItems().keySet());
            itemNames.sort(String.CASE_INSENSITIVE_ORDER);
            return itemNames;
        }
        return null;
    }
}

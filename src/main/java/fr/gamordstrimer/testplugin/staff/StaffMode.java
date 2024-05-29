package fr.gamordstrimer.testplugin.staff;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.Utils.SkullTextureChanger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StaffMode implements CommandExecutor {
    private Main plugin;
    private Map<String, ItemStack> staffItems = new LinkedHashMap<>();
    private static StaffMode instance;
    public StaffMode(Main plugin) {
        this.plugin = plugin;
        instance = this;
        ItemStackManager();
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
                        .append(Component.text("Vous n'êtes plus en mode modération.").color(NamedTextColor.DARK_RED)));
                pm.giveInventory();
                pm.destroy();
                player.sendActionBar(Component.empty());
                return true;
            }
            PlayerManager pm = new PlayerManager(player);
            pm.init();
            plugin.getStaff().add(player.getUniqueId());
            player.sendMessage(Component.text(prefixserver)
                    .append(Component.text("Vous êtes en mode modération").color(NamedTextColor.GREEN)));
            pm.saveInventory();

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!plugin.getStaff().contains(player.getUniqueId())) {
                        // Stop the task if the player is no longer in staff mode
                        cancel();
                        return;
                    }
                    player.sendActionBar(Component.text("⚠ Vous êtes en mode modération").color(NamedTextColor.RED));
                }
            }.runTaskTimer(plugin, 0, 20);

            player.getInventory().setItem(0, staffItems.get("compass"));
            if(plugin.isVanish(player)) {
                player.getInventory().setItem(1, staffItems.get("disable_vanish"));
            } else {
                player.getInventory().setItem(1, staffItems.get("enable_vanish"));
            }
            player.getInventory().setItem(3, staffItems.get("paper"));
            player.getInventory().setItem(4, staffItems.get("headstaff"));
            player.getInventory().setItem(5, staffItems.get("ice"));
            player.getInventory().setItem(7, staffItems.get("stick"));
            player.getInventory().setItem(8, staffItems.get("chest"));

            return true;
        } else {
            player.sendMessage(Component.text(prefixserver + perms));
            return false;
        }
    }

    private void ItemStackManager() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        if (compassMeta != null) {
            compassMeta.displayName(Component.text("Séléctionnez un Joueur").color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique Droit pour Séléctionner un Joueur").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique Gauche pour se Téléporter").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            compassMeta.lore(lore);
            compass.setItemMeta(compassMeta);
        }
        staffItems.put("compass", compass);

        ItemStack greenDye = new ItemStack(Material.LIME_DYE);
        ItemMeta greenDyeMeta = greenDye.getItemMeta();
        if (greenDyeMeta != null) {
            greenDyeMeta.displayName(Component.text("Désactiver le Vanish").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique Droit pour Désactiver le Vanish").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            greenDyeMeta.lore(lore);
            greenDye.setItemMeta(greenDyeMeta);
        }
        staffItems.put("disable_vanish", greenDye);

        ItemStack grayDye = new ItemStack(Material.GRAY_DYE);
        ItemMeta grayDyeMeta = grayDye.getItemMeta();
        if (grayDyeMeta != null) {
            grayDyeMeta.displayName(Component.text("Activer le Vanish").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique Droit pour Activer le Vanish").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            grayDyeMeta.lore(lore);
            grayDye.setItemMeta(grayDyeMeta);
        }
        staffItems.put("enable_vanish", grayDye);

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        if (paperMeta != null) {
            paperMeta.displayName(Component.text("Voir les Signalements").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique Droit pour Consulter les Signalements").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            paperMeta.lore(lore);
            paper.setItemMeta(paperMeta);
        }
        staffItems.put("paper", paper);

        ItemStack headStaff = new ItemStack(Material.PLAYER_HEAD);
        String base64Texture1 = "eyJ0aW1lc3RhbXAiOjE1ODcwNTA5MDgwNTMsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ2ZjU0NDBiNTlkMjhlYjVjOTlkMGZkMWZkYTQ2N2MzZTRiYjUwMmVhYzNiM2VjYjIyOTY5ZGQyMzU5MTRmYyJ9fX0=";
        SkullTextureChanger.setSkullTexture(headStaff, base64Texture1);
        SkullMeta headStaffMeta = (SkullMeta) headStaff.getItemMeta();
        if (headStaffMeta != null) {
            headStaffMeta.displayName(Component.text("Voir les Modérateurs Connectés").color(NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique Droit pour Ouvrir la Liste").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            headStaffMeta.lore(lore);
            headStaff.setItemMeta(headStaffMeta);
        }
        staffItems.put("headstaff", headStaff);

        ItemStack ice = new ItemStack(Material.ICE);
        ItemMeta iceMeta = ice.getItemMeta();
        if (iceMeta != null) {
            iceMeta.displayName(Component.text("Gèle le Joueur").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique Droit pour Gelé le Joueur").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            iceMeta.lore(lore);
            ice.setItemMeta(iceMeta);
        }
        staffItems.put("ice", ice);

        ItemStack unice = new ItemStack(Material.ICE);
        ItemMeta uniceMeta = unice.getItemMeta();
        if (uniceMeta != null) {
            uniceMeta.displayName(Component.text("Dégèle le Joueur").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique Droit pour Dégelé le Joueur").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            uniceMeta.lore(lore);
            uniceMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            uniceMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            unice.setItemMeta(uniceMeta);
        }
        staffItems.put("unice", unice);

        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stick.getItemMeta();
        if (stickMeta != null) {
            stickMeta.displayName(Component.text("Test le Recul du Joueur").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique Gauche pour tester le recul d'un joueur").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            stickMeta.lore(lore);
            stickMeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
            stickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            stick.setItemMeta(stickMeta);
        }
        staffItems.put("stick", stick);

        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta chestMeta = chest.getItemMeta();
        if (chestMeta != null) {
            chestMeta.displayName(Component.text("Voir l'inventaire").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            lore.add(Component.text(" "));
            lore.add(Component.text("➤ Clique Droit pour voir l'Inventaire du Joueur").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("------------------------").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true));
            chestMeta.lore(lore);
            chest.setItemMeta(chestMeta);
        }
        staffItems.put("chest", chest);
    }

    public static StaffMode getInstance() {
        return instance;
    }

    public Map<String, ItemStack> getStaffItems() {
        return staffItems;
    }
}

package fr.gamordstrimer.testplugin.menusystem.menu;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.menusystem.Menu;
import fr.gamordstrimer.testplugin.menusystem.PlayerMenuUtility;
import fr.gamordstrimer.testplugin.staff.StaffMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SelPlayerGUI extends Menu {

    private static final List<SelPlayerGUI> instances = new ArrayList<>();
    private final Player opener;
    private static final Map<Player, Player> playerTargets = new HashMap<>();

    public SelPlayerGUI(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        this.opener = playerMenuUtility.getOwner();
        instances.add(this);
    }

    @Override
    public @NotNull TextComponent getMenuName() {
        return Component.text("Séléctionnez un joueur").color(NamedTextColor.DARK_PURPLE);
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        String prefixserver = Objects.requireNonNull(Main.getInstance().getConfig().getString("messages.prefixserver")).replace("&", "§");
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        int lastSlot = inventory.getSize() - 1;
        if(e.getSlot() == lastSlot) {
            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            if (onlinePlayers.size() > 1) {
                onlinePlayers.remove(player);
                Player targetPlayer = onlinePlayers.get(new Random().nextInt(onlinePlayers.size()));
                setTarget(opener, targetPlayer);
                Player target = getTarget(opener);
                player.teleport(target);
                player.sendMessage(Component.text(prefixserver)
                        .append(Component.text("Tu as été téléporter à ")
                        .append(Component.text(target.getName()).color(NamedTextColor.GOLD)
                        .append(Component.text(".").color(NamedTextColor.WHITE)))));
            } else {
                player.sendMessage(Component.text(prefixserver).append(Component.text("Il n'y a pas assez de joueur en ligne pour utiliser cette fonction !").color(NamedTextColor.RED)));
            }
        } else {
            if (item != null && item.getType() == Material.PLAYER_HEAD) {
                String playerName = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(item.getItemMeta().displayName()));
                Player selplayer = Bukkit.getPlayerExact(playerName);
                if (selplayer != null && player != selplayer) {
                    setTarget(opener, selplayer);
                    Player target = getTarget(opener);
                    player.teleport(target);
                    player.sendMessage(Component.text(prefixserver)
                            .append(Component.text("Tu as été téléporter à ")
                            .append(Component.text(target.getName()).color(NamedTextColor.GOLD)
                            .append(Component.text(".").color(NamedTextColor.WHITE)))));
                } else {
                    player.sendMessage(Component.text(prefixserver).append(Component.text("Joueur Invalide.").color(NamedTextColor.RED)));
                }
                player.closeInventory();
            }
        }
    }

    @Override
    public void setMenuItems() {

        inventory.clear();

        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        Set<UUID> addedPlayers = new HashSet<>(); // Track added players

        for (Player onlinePlayer : onlinePlayers) {
            if (addedPlayers.contains(onlinePlayer.getUniqueId())) {
                continue;
            }

            ItemStack headItem = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
            assert skullMeta != null;
            skullMeta.setPlayerProfile(onlinePlayer.getPlayerProfile());
            skullMeta.displayName(Component.text(onlinePlayer.getName()).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            if(opener != null && onlinePlayer.getUniqueId().equals(opener.getUniqueId())) {
                lore.add(Component.text(" "));
                lore.add(Component.text("⚠ Tu ne peux pas te").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text("   téléporter à toi même.").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text(" "));
            } else {
                lore.add(Component.text(" "));
                lore.add(Component.text("➢ Clique pour te Téléporter").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text(" "));
            }
            skullMeta.lore(lore);
            headItem.setItemMeta(skullMeta);
            inventory.addItem(headItem);

            addedPlayers.add(onlinePlayer.getUniqueId());
        }

        int invsize = inventory.getSize();
        int lastslot = invsize - 1;
        inventory.setItem(lastslot, StaffMode.getInstance().getStaffItems().get("dicerandom"));

    }

    public static void updateMenu() {
        for (SelPlayerGUI instance : instances) {
            instance.setMenuItems();
        }
    }


    public static Player getTarget(Player player) {
        return playerTargets.get(player);
    }

    public static Map<Player, Player> getTargets() {
        return playerTargets;
    }

    public static void setTarget(Player opener, Player target) {
        playerTargets.put(opener, target);
    }

    public static void removeInstance(SelPlayerGUI instance) {
        instances.remove(instance);
    }

    public static void removeTarget(Player opener) {
        playerTargets.remove(opener);
    }

}

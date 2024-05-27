package fr.gamordstrimer.testplugin.Listener;

import fr.gamordstrimer.testplugin.CooldownManager;
import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.heads.SkullTextureChanger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PlayerHandler implements Listener {
    private final Main plugin;
    private final ItemStack magicLantern;
    private final CooldownManager cooldownManager;
    private static Inventory mainGUI;
    private static Map<String, ItemStack> setupItemGUI = new HashMap<>();
    private static Map<String, Inventory> itemGUIs = new HashMap<>();


    public PlayerHandler(Main plugin, CooldownManager cooldownManager) {
        this.plugin = plugin;
        this.magicLantern = CustomItems.getItem("magic_lantern");
        this.cooldownManager = cooldownManager;
        setupItemGUI();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String joinMessage = Objects.requireNonNull(plugin.getConfig().getString("messages.joinmessage")).replace('&', '§');
        event.joinMessage(Component.text(joinMessage + " " + player.getName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String leaveMessage = Objects.requireNonNull(plugin.getConfig().getString("messages.leavemessage")).replace('&', '§');
        event.quitMessage(Component.text(leaveMessage + " " + player.getName()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        String prefixserver = plugin.getConfig().getString("messages.prefixserver").replace("&", "§");

        if (item != null && item.isSimilar(magicLantern)) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                UUID playerUUID = player.getUniqueId();

                if (cooldownManager.hasCooldown(playerUUID)) {
                    long remainingTimeMillis = cooldownManager.getCooldown(playerUUID) - System.currentTimeMillis();
                    long remainingTimeSeconds = remainingTimeMillis / 1000;
                    long minutes = remainingTimeSeconds / 60;
                    long seconds = remainingTimeSeconds % 60;

                    String remainingTimeMessage = String.format("%d minutes et %d secondes", minutes, seconds);
                    player.sendMessage(Component.text("Tu dois patienter ").color(NamedTextColor.RED)
                            .append(Component.text(remainingTimeMessage).color(NamedTextColor.DARK_RED)
                            .append(Component.text(" pour utiliser la lanterne magique").color(NamedTextColor.RED))));

                    event.setCancelled(true);
                    return;
                }

                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 60 * 15, 1));
                player.sendMessage(Component.text(prefixserver + "Tu as reçu un effet de Vision Nocture pendant ")
                        .append(Component.text("15 minutes.").color(NamedTextColor.GOLD)));
                event.setCancelled(true);

                long cooldownTime = System.currentTimeMillis() + (30 * 60 * 1000);
                cooldownManager.setCooldown(playerUUID, cooldownTime);

                Bukkit.getScheduler().runTaskLater(plugin, () -> cooldownManager.removeCooldown(playerUUID), 30 * 60 * 20L); // 30 minutes in ticks
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        Inventory openInventory = player.getOpenInventory().getTopInventory();

        if (openInventory.equals(mainGUI)) {
            event.setCancelled(true); // Prevent the default behavior

            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }

            // Find the item name from the clicked item
            for (Map.Entry<String, ItemStack> entry : CustomItems.getCustomItems().entrySet()) {
                if (clickedItem.isSimilar(entry.getValue())) {
                    String itemName = entry.getKey();

                    // Open the corresponding item GUI
                    Inventory itemInventory = itemGUIs.get(itemName);
                    if (itemInventory != null) {
                        player.openInventory(itemInventory);
                    }
                    return;
                }
            }
        }
        for (Inventory inventory : itemGUIs.values()) {
            if (openInventory.equals(inventory)) {
                event.setCancelled(true); // Prevent the default behavior
                if (clickedItem != null && clickedItem.isSimilar(setupItemGUI.get("headback"))) {
                    // Go back to the main GUI
                    player.openInventory(mainGUI);
                }
                return;
            }
        }
    }

    public static void itemGUI(String itemName, ItemStack itemStack) {
        Component displayNameComponent = CustomItems.getItem(itemName).getItemMeta().displayName();
        String displayName = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(displayNameComponent));

        Inventory itemGUI = Bukkit.createInventory(null, 9 * 5, Component.text(displayName).color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true));

        Recipe recipe = CustomItems.getRecipe(itemName);

        if (recipe != null) {
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();
                String[] shape = shapedRecipe.getShape();

                int index = 10;
                for (String s : shape) {
                    for (int col = 0; col < s.length(); col++) {
                        char ingredientChar = s.charAt(col);
                        if (ingredientChar != ' ') {
                            ItemStack ingredient = ingredientMap.get(ingredientChar);
                            if (ingredient != null) {
                                itemGUI.setItem(index++, ingredient);
                            }
                        } else {
                            ItemStack air = new ItemStack(Material.AIR);
                            itemGUI.setItem(index++, air);
                        }
                    }
                    index += 6; // Move to the next row in the GUI
                }
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                int index = 10; // Starting position in a 3x3 grid
                for (ItemStack ingredient : shapelessRecipe.getIngredientList()) {
                    itemGUI.setItem(index++, ingredient);
                }
            }

            itemGUI.setItem(23, setupItemGUI.get("craftingtable"));
        } else {
            // Fill crafting slots with barriers
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta barrierMeta = barrier.getItemMeta();
            if (barrierMeta != null) {
                barrierMeta.displayName(Component.text("Pas de Recette Disponible").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
                barrier.setItemMeta(barrierMeta);
            }

            int[] craftingSlots = {10, 11, 12, 19, 20, 21, 28, 29, 30};
            for (int slot : craftingSlots) {
                itemGUI.setItem(slot, barrier);
            }
        }

        itemGUI.setItem(25, itemStack);
        itemGUI.setItem(44, setupItemGUI.get("headback"));

        // Fill empty slots with glass panes, except specific slots 10/11/12, 19/20/21, 28/29/30

        // Set of slots to exclude
        Set<Integer> excludedSlots = new HashSet<>(Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30));
        for (int i = 0; i < 45; i++) {
            if (itemGUI.getItem(i) == null && !excludedSlots.contains(i)) {
                itemGUI.setItem(i, setupItemGUI.get("glasspane"));
            }
        }

        // Store the item GUI in the map
        itemGUIs.put(itemName, itemGUI);
    }


    private void setupItemGUI() {

        //headback in menu (go back)
        ItemStack headback = new ItemStack(Material.PLAYER_HEAD);
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjZkOGVmZjRjNjczZTA2MzY5MDdlYTVjMGI1ZmY0ZjY0ZGMzNWM2YWFkOWI3OTdmMWRmNjYzMzUxYjRjMDgxNCJ9fX0=";
        SkullTextureChanger.setSkullTexture(headback, base64Texture);
        SkullMeta headbackmeta = (SkullMeta) headback.getItemMeta();
        if (headbackmeta != null) {
            headbackmeta.displayName(Component.text("Retour").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            headback.setItemMeta(headbackmeta);
        }
        setupItemGUI.put("headback", headback);

        // crafting table
        ItemStack craftingtable = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta craftingtablemeta = craftingtable.getItemMeta();
        if (craftingtablemeta != null) {
            craftingtablemeta.displayName(Component.text("Table de Craft").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("━━━━━━━━━━━━━━━━━━━━").color(NamedTextColor.GRAY));
            lore.add(Component.text("Cette Item peut être").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("fabriqué dans une").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("Table de Craft").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("━━━━━━━━━━━━━━━━━━━━").color(NamedTextColor.GRAY));
            craftingtablemeta.lore(lore);
            craftingtable.setItemMeta(craftingtablemeta);
        }
        setupItemGUI.put("craftingtable", craftingtable);

        //GlassPane
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = glassPane.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(" "));
            glassPane.setItemMeta(meta);
        }
        setupItemGUI.put("glasspane", glassPane);

        //Button Right
        ItemStack buttonRight = new ItemStack(Material.SPRUCE_BUTTON);
        ItemMeta buttonRightMeta = buttonRight.getItemMeta();
        if (buttonRightMeta != null) {
            buttonRightMeta.displayName(Component.text("Page Suivante").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            buttonRight.setItemMeta(buttonRightMeta);
        }
        setupItemGUI.put("buttonright", buttonRight);

        //Button Left
        ItemStack buttonLeft = new ItemStack(Material.SPRUCE_BUTTON);
        ItemMeta buttonLeftMeta = buttonLeft.getItemMeta();
        if (buttonLeftMeta != null) {
            buttonLeftMeta.displayName(Component.text("Page Précédente").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            buttonLeft.setItemMeta(buttonLeftMeta);
        }
        setupItemGUI.put("buttonleft", buttonLeft);
    }
}

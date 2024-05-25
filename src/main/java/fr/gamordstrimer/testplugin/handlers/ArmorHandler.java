package fr.gamordstrimer.testplugin.handlers;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;


public class ArmorHandler implements Listener {
    private final List<ItemStack> curseArmorList;

    public ArmorHandler(Main plugin) {
        curseArmorList = Arrays.asList(
                CustomItems.getItem("curse_helmet"),
                CustomItems.getItem("curse_chestplate"),
                CustomItems.getItem("curse_leggings"),
                CustomItems.getItem("curse_boots")
        );
    }

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        ItemStack oldItem = event.getOldItem();
        ItemStack newItem = event.getNewItem();

        if (isWearingFullArmorSet(player, curseArmorList)) {
            // Apply the effect if the player is wearing the full armor set
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        }

        if(newItem.isSimilar(CustomItems.getItem("headband"))) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false));
        }

        // Check if the old item was part of the curse armor set and the new item is not
        if (oldItem != null && isCurseArmorItem(oldItem) && (newItem == null || !isCurseArmorItem(newItem))) {
            player.removePotionEffect(PotionEffectType.SPEED);
        } else if (oldItem != null && oldItem.isSimilar(CustomItems.getItem("headband"))) {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }

    private boolean isCurseArmorItem(ItemStack item) {
        return item != null && curseArmorList.contains(item);
    }

    private boolean isWearingFullArmorSet(Player player, List<ItemStack> armorList) {
        ItemStack head = player.getInventory().getHelmet();
        ItemStack chest = player.getInventory().getChestplate();
        ItemStack legs = player.getInventory().getLeggings();
        ItemStack feet = player.getInventory().getBoots();

        return itemIsSimilar(head, armorList.get(0)) &&
                itemIsSimilar(chest, armorList.get(1)) &&
                itemIsSimilar(legs, armorList.get(2)) &&
                itemIsSimilar(feet, armorList.get(3));
    }

    private boolean itemIsSimilar(ItemStack item, ItemStack customItem) {
        return item != null && item.isSimilar(customItem);
    }
}

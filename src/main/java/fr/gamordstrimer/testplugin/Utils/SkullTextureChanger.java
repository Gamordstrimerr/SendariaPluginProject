package fr.gamordstrimer.testplugin.Utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class SkullTextureChanger {
    public static ItemStack setSkullTexture(ItemStack skullItem, String base64Texture) {
        if (skullItem.getType() != Material.PLAYER_HEAD) {
            throw new IllegalArgumentException("SkullTextureChanger only works with PLAYER_HEAD items.");
        }

        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        // Provide a non-null profile name
        GameProfile profile = new GameProfile(UUID.randomUUID(), "CustomSkull");
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        } else {
            propertyMap.put("textures", new Property("textures", base64Texture));
        }

        try {
            Method setProfileMethod = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            setProfileMethod.setAccessible(true);
            setProfileMethod.invoke(skullMeta, profile);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        skullItem.setItemMeta(skullMeta);
        return skullItem;
    }
}


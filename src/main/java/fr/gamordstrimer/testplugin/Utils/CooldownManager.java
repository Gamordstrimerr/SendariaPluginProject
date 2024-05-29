package fr.gamordstrimer.testplugin.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final File configFile;
    private final FileConfiguration config;

    public CooldownManager(File dataFolder) {
        configFile = new File(dataFolder, "cooldowns.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        loadCooldowns();
    }

    public void setCooldown(UUID playerUUID, long cooldownTime) {
        cooldowns.put(playerUUID, cooldownTime);
        saveCooldowns();
    }

    public boolean hasCooldown(UUID playerUUID) {
        return cooldowns.containsKey(playerUUID) && cooldowns.get(playerUUID) > System.currentTimeMillis();
    }

    public long getCooldown(UUID playerUUID) {
        return cooldowns.getOrDefault(playerUUID, 0L);
    }

    public void removeCooldown(UUID playerUUID) {
        cooldowns.remove(playerUUID);
        saveCooldowns();
    }

    public void saveCooldowns() {
        for (Map.Entry<UUID, Long> entry : cooldowns.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCooldowns() {
        for (String key : config.getKeys(false)) {
            cooldowns.put(UUID.fromString(key), config.getLong(key));
        }
    }
}

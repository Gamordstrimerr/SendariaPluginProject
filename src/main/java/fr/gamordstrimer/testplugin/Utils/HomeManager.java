package fr.gamordstrimer.testplugin.Utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeManager {
    private final Map<UUID, Location> home = new HashMap<>();
    private final File configFile;
    private final FileConfiguration config;

    public HomeManager(File dataFolder) {
        configFile = new File(dataFolder, "homemanager.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        loadHomes();
    }

    public boolean hasHome(UUID playerUUID) {
        return home.containsKey(playerUUID);
    }

    public void setHome(UUID playerUUID, Location playerLoc) {
        home.put(playerUUID, playerLoc);
        saveHomes();
    }

    public Location getHome(UUID playerUUID) {
        return home.get(playerUUID);
    }

    public void removeHome(UUID playerUUID) {
        home.remove(playerUUID);
        saveHomes();
    }

    public void saveHomes() {
        for (Map.Entry<UUID, Location> entry : home.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHomes() {
        for (String key : config.getKeys(false)) {
            home.put(UUID.fromString(key), config.getLocation(key));
        }
    }
}

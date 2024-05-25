package fr.gamordstrimer.testplugin;

import fr.gamordstrimer.testplugin.commands.CustomItemsRecipes;
import fr.gamordstrimer.testplugin.commands.CustomItemsGive;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.handlers.ArmorHandler;
import fr.gamordstrimer.testplugin.handlers.ItemframeHandler;
import fr.gamordstrimer.testplugin.handlers.PlayerHandler;
import fr.gamordstrimer.testplugin.staff.PlayerManager;
import fr.gamordstrimer.testplugin.staff.Staff;
import fr.gamordstrimer.testplugin.staff.StaffHandler;
import fr.gamordstrimer.testplugin.staff.StaffMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Main extends JavaPlugin {
    private CooldownManager cooldownManager;
    private List<UUID> staff;
    private List<UUID> vanish;
    private Map<UUID, Location> freezedplayer;
    private Map<UUID, PlayerManager> players = new HashMap<>();

    // ================================================
    //              onEnable() & onDisable()
    // ================================================

    @Override
    public void onEnable() {
        getLogger().info("TestPlugin has been enabled!"); // Message sent in the console at the start of the plugin

        //Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Initialize the CooldownManager first
        cooldownManager = new CooldownManager(getDataFolder());

        // Initialize Variable
        staff = new ArrayList<>();
        vanish = new ArrayList<>();
        freezedplayer = new HashMap<>();

        // Registering elements
        CustomItems.init(this);
        PlayerManager.setPlugin(this);

        //register Commands
        Objects.requireNonNull(getCommand("customitemsgive")).setExecutor(new CustomItemsGive(this));
        Objects.requireNonNull(getCommand("customitemsgive")).setTabCompleter(new CustomItemsGive(this));
        Objects.requireNonNull(getCommand("customitemsrecipes")).setExecutor(new CustomItemsRecipes());
        // Staff commands
        Objects.requireNonNull(getCommand("resetcooldown")).setExecutor(new Staff(this, cooldownManager));
        Objects.requireNonNull(getCommand("freeze")).setExecutor(new Staff(this, cooldownManager));
        Objects.requireNonNull(getCommand("staff")).setExecutor(new StaffMode(this));
        Objects.requireNonNull(getCommand("vanish")).setExecutor(new Staff(this, cooldownManager));

        //register Event
        Bukkit.getPluginManager().registerEvents(new ArmorHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new ItemframeHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new StaffHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerHandler(this, cooldownManager), this);
    }

    @Override
    public void onDisable() {
        cooldownManager.saveCooldowns(); // Save cooldowns before shutting down the server
    }

    // ================================================

    public List<UUID> getStaff() {
        return staff;
    }

    public boolean isInStaff(Player player) {
        return getStaff().contains(player.getUniqueId());
    }

    public Map<UUID, Location> getFreezedplayer() {
        return freezedplayer;
    }

    public boolean isFreeze(Player player) {
        return getFreezedplayer().containsKey(player.getUniqueId());
    }

    public Map<UUID, PlayerManager> getPlayers() {
        return players;
    }

    public List<UUID> getVanish() {
        return vanish;
    }

    public boolean isVanish(Player player) {
        return getVanish().contains(player.getUniqueId());
    }
}

package fr.gamordstrimer.testplugin;

import fr.gamordstrimer.testplugin.Listener.*;
import fr.gamordstrimer.testplugin.Utils.CooldownManager;
import fr.gamordstrimer.testplugin.commands.CustomItemsRecipes;
import fr.gamordstrimer.testplugin.commands.CustomItemsGive;
import fr.gamordstrimer.testplugin.itemsystem.ItemManager;
import fr.gamordstrimer.testplugin.menusystem.PlayerMenuUtility;
import fr.gamordstrimer.testplugin.staff.PlayerManager;
import fr.gamordstrimer.testplugin.staff.Staff;
import fr.gamordstrimer.testplugin.staff.StaffHandler;
import fr.gamordstrimer.testplugin.staff.StaffMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Main extends JavaPlugin {
    private CooldownManager cooldownManager;
    private static Main instance;
    private List<UUID> staff;
    private List<UUID> vanish;
    private Map<UUID, Location> freezedplayer;
    private Map<UUID, PlayerManager> players = new HashMap<>();
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

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
        instance = this;
        staff = new ArrayList<>();
        vanish = new ArrayList<>();
        freezedplayer = new HashMap<>();

        // Registering elements
        //CustomItems.init(this);
        new ItemManager().initializeCustomItems();
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
        getServer().getPluginManager().registerEvents(new ArmorHandler(this), this);
        getServer().getPluginManager().registerEvents(new ItemframeHandler(this), this);
        getServer().getPluginManager().registerEvents(new StaffHandler(this), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(this, cooldownManager), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        cooldownManager.saveCooldowns(); // Save cooldowns before shutting down the server
        instance = null;
    }

    // ================================================

    public static PlayerMenuUtility getPlayerMenuUtility(Player player) {
        PlayerMenuUtility playerMenuUtility;

        if (playerMenuUtilityMap.containsKey(player)) {
            return playerMenuUtilityMap.get(player);
        } else {
            playerMenuUtility = new PlayerMenuUtility(player);
            playerMenuUtilityMap.put(player, playerMenuUtility);

            return playerMenuUtility;
        }
    }

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

    public static Main getInstance() {
        return instance;
    }
}

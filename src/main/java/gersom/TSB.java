package gersom;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import gersom.bosses.MainGenerator;
import gersom.commands.MainCommand;
import gersom.commands.MainTabCompleter;
import gersom.config.BossPersistenceManager;
import gersom.config.MainConfigManager;
import gersom.listeners.MainListeners;
import gersom.utils.Console;
import gersom.utils.General;
import gersom.utils.Vars;

public class TSB extends JavaPlugin {

    private MainConfigManager configs;
    private MainGenerator mainMobs;
    private BossPersistenceManager bossPersistenceManager;

    @Override
    public void onEnable() {
        // Inicializar Vars primero
        Vars.initialize(getDescription());
        
        // Crear la instancia de MainConfigManager
        this.configs = new MainConfigManager(this);
        
        // Inicializar configs
        this.configs.initialize();

        // Inicializar BossPersistenceManager
        this.bossPersistenceManager = new BossPersistenceManager(this);
        
        // Inicializar MainGenerator
        this.mainMobs = new MainGenerator(this);

        registerCommands();
        registerEvents();

        Console.sendMessage(General.generateHeadFrame());
        Console.printBlankLine();
        Console.sendMessage(
            "&a" + configs.getPrefix() + "&a&l> " + configs.getLangPluginEnabled()
        );
        Console.printBlankLine();
        Console.printFooter(getConfigs().getLanguage(), configs.getPrefix());
        Console.printBlankLine();
        Console.sendMessage(General.generateSeparator());

        autoSpawnBosses();

        // Agregar esto al final del método onEnable()
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            getMainMobs().checkAndRecoverBosses();
        }, 20 * 60, 20 * 10); // Ejecutar cada 60 segundos (20 ticks * 60)
    }

    @Override
    public void onDisable() {
        if (configs != null) {
            Console.sendMessage(General.generateHeadFrame());
            Console.printBlankLine();
            Console.sendMessage(
                "&c" + configs.getPrefix() + "&c&l> " + configs.getLangPluginDisabled()
            );
            Console.printBlankLine();
            Console.printFooter(getConfigs().getLanguage(), configs.getPrefix());
            Console.printBlankLine();
            Console.sendMessage(General.generateSeparator());
        }
    }

    public void registerCommands() {
        MainCommand mainCommand = new MainCommand(this);
        MainTabCompleter tabCompleter = new MainTabCompleter();
        PluginCommand command = Objects.requireNonNull(this.getCommand("tsb"));
        command.setExecutor(mainCommand);
        command.setTabCompleter(tabCompleter);
    }

    public void autoSpawnBosses() {
        if (getConfigs().getSpawnEnabled()) {
            World world = getServer().getWorld(getConfigs().getSpawnWorld());
            this.mainMobs.startAutoSpawnBoss(
                world, 
                new Location(
                    world, 
                    getConfigs().getSpawnLocationX(), 
                    getConfigs().getSpawnLocationY(), 
                    getConfigs().getSpawnLocationZ()
                ), 
                getConfigs().getSpawnMinRadius(),
                getConfigs().getSpawnMaxRadius(),
                20 * 60 * getConfigs().getSpawnInterval()
            );
        }
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new MainListeners(this), this);
    }

    public MainConfigManager getConfigs() {
        return configs;
    }

    public MainGenerator getMainMobs() {
        return mainMobs;
    }

    public BossPersistenceManager getBossPersistenceManager() {
        return bossPersistenceManager;
    }
}
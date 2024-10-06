package mu.gersom;

import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import mu.gersom.commands.MainCommand;
import mu.gersom.commands.MainTabCompleter;
import mu.gersom.config.BossPersistenceManager;
import mu.gersom.config.MainConfigManager;
import mu.gersom.generators.MainGenerator;
import mu.gersom.listeners.MainListeners;
import mu.gersom.utils.Console;
import mu.gersom.utils.General;
import mu.gersom.utils.Vars;

public class MuMc extends JavaPlugin {

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
            "&a" + Vars.prefix + "&a&l> " + configs.getMsgPluginEnabled()
        );
        Console.printBlankLine();
        Console.printFooter(getConfigs().getLanguage());
        Console.printBlankLine();
        Console.sendMessage(General.generateSeparator());

        autoSpawnBosses();
    }

    @Override
    public void onDisable() {
        if (configs != null) {
            Console.sendMessage(General.generateHeadFrame());
            Console.printBlankLine();
            Console.sendMessage(
                "&c" + Vars.prefix + "&c&l> " + configs.getMsgPluginDisabled()
            );
            Console.printBlankLine();
            Console.printFooter(getConfigs().getLanguage());
            Console.printBlankLine();
            Console.sendMessage(General.generateSeparator());
        }
    }

    public void registerCommands() {
        MainCommand mainCommand = new MainCommand(this);
        MainTabCompleter tabCompleter = new MainTabCompleter();
        PluginCommand command = Objects.requireNonNull(this.getCommand("mumc"));
        command.setExecutor(mainCommand);
        command.setTabCompleter(tabCompleter);
    }

    public void autoSpawnBosses() {
        if (getConfigs().getSpawnEnabled()) {
            this.mainMobs.startAutoSpawnBoss(
                getServer().getWorld(getConfigs().getSpawnWorld()), 
                new Location(
                    getServer().getWorld(getConfigs().getSpawnWorld()), 
                    getConfigs().getSpawnLocationX(), 
                    getConfigs().getSpawnLocationY(), 
                    getConfigs().getSpawnLocationZ()
                ), 
                getConfigs().getSpawnRadius(), 
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
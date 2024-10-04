package mu.gersom;

import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import mu.gersom.commands.MainCommand;
import mu.gersom.commands.MainTabCompleter;
import mu.gersom.config.MainConfigManager;
import mu.gersom.generators.MainGenerator;
import mu.gersom.listeners.MainListeners;
import mu.gersom.utils.Console;
import mu.gersom.utils.General;
import mu.gersom.utils.Vars;

public class MuMc extends JavaPlugin {

    private MainConfigManager configs;
    private MainGenerator mainMobs;

    @Override
    public void onEnable() {
        // Inicializar Vars primero
        Vars.initialize(getDescription());
        
        // Crear la instancia de MainConfigManager
        this.configs = new MainConfigManager(this);
        
        // Inicializar configs
        this.configs.initialize();
        
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
        Console.printFooter();
        Console.printBlankLine();
        Console.sendMessage(General.generateSeparator());

        this.mainMobs.startAutoSpawnBoss(
            getServer().getWorld("world"), 
            new Location(getServer().getWorld("world"), -80, 80, 277), 
            10, 
            20 * 60 * 1
        );
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
            Console.printFooter();
            Console.printBlankLine();
            Console.sendMessage(General.generateSeparator());
        }
    }

    public void registerCommands() {
        // Objects.requireNonNull(this.getCommand("mumc")).setExecutor(new MainCommand(this));

        MainCommand mainCommand = new MainCommand(this);
        MainTabCompleter tabCompleter = new MainTabCompleter();
        PluginCommand command = Objects.requireNonNull(this.getCommand("mumc"));
        command.setExecutor(mainCommand);
        command.setTabCompleter(tabCompleter);
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
}
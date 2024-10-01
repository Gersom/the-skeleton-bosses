package mu.gersom;

import java.util.Objects;

import org.bukkit.plugin.java.JavaPlugin;

import mu.gersom.commands.MainCommand;
import mu.gersom.config.MainConfigManager;
import mu.gersom.utils.Console;
import mu.gersom.utils.General;
import mu.gersom.utils.Vars;

public class MuMc extends JavaPlugin {

    private MainConfigManager configs;

    @Override
    public void onEnable() {
        // Inicializar Vars primero
        Vars.initialize(getDescription());
        
        // Crear la instancia de MainConfigManager
        this.configs = new MainConfigManager(this);
        
        // Inicializar configs
        this.configs.initialize();
        
        registerCommands();

        Console.sendMessage(General.generateHeadFrame());
        Console.printBlankLine();
        Console.sendMessage(
            "&a" + Vars.prefix + "&a&l> " + configs.getMsgPluginEnabled()
        );
        Console.printBlankLine();
        Console.printFooter();
        Console.printBlankLine();
        Console.sendMessage(General.generateSeparator());
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
        Objects.requireNonNull(this.getCommand("mumc")).setExecutor(new MainCommand(this));
    }

    public MainConfigManager getConfigs() {
        return configs;
    }
}
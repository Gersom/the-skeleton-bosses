package mu.gersom;

import java.util.Objects;

import org.bukkit.plugin.java.JavaPlugin;

import mu.gersom.commands.MainCommand;
import mu.gersom.utils.Console;
import mu.gersom.utils.General;

public class MuMc extends JavaPlugin {

    public MuMc() {
        // Inicializar Utils en el constructor
        General.initialize(getDescription());
    }

    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("mumc")).setExecutor(new MainCommand());
    }

    @Override
    public void onEnable() {
        registerCommands();

        Console.sendMessage(General.generateHeadFrame());
        Console.printBlankLine();
        Console.sendMessage("&a" + General.prefix + "&a&l> plugin has been enabled!");
        Console.printBlankLine();
        Console.printFooter();
        Console.printBlankLine();
        Console.sendMessage(General.generateSeparator());
    }

    @Override
    public void onDisable() {
        Console.sendMessage(General.generateHeadFrame());
        Console.printBlankLine();
        Console.sendMessage("&c" + General.prefix + "&c&l> plugin has been disabled!");
        Console.printBlankLine();
        Console.printFooter();
        Console.printBlankLine();
        Console.sendMessage(General.generateSeparator());
    }
}
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

    public MuMc() {
        Vars.initialize(getDescription());
    }

    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("mumc")).setExecutor(new MainCommand(this));
    }

    @Override
    public void onEnable() {
        configs = new MainConfigManager(this);
        registerCommands();

        Console.sendMessage(General.generateHeadFrame());
        Console.printBlankLine();
        Console.sendMessage(
            "&a" + Vars.prefix + " " + configs.getMsgPluginEnabled()
        );
        Console.printBlankLine();
        Console.printFooter();
        Console.printBlankLine();
        Console.sendMessage(General.generateSeparator());
    }

    @Override
    public void onDisable() {
        Console.sendMessage(General.generateHeadFrame());
        Console.printBlankLine();
        Console.sendMessage(
            "&c" + Vars.prefix + " " + configs.getMsgPluginDisabled()
        );
        Console.printBlankLine();
        Console.printFooter();
        Console.printBlankLine();
        Console.sendMessage(General.generateSeparator());
    }

    public MainConfigManager getConfigs() {
        return configs;
    }
}
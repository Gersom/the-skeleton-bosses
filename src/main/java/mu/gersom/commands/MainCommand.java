
package mu.gersom.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mu.gersom.MuMc;
import mu.gersom.utils.Console;
import mu.gersom.utils.General;
import mu.gersom.utils.Vars;

public class MainCommand implements CommandExecutor {
    private MuMc plugin = null;

    public MainCommand(MuMc plugin) {
        this.plugin = plugin;
    }

    public void sendTextHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(General.setColor("* " + plugin.getConfigs().getHelpText()));
        sender.sendMessage(General.setColor("  &6/mumc help"));
    }

    public void notFoundCommand(CommandSender sender) {
        sender.sendMessage(General.setColor(
            "&c" + Vars.prefix + plugin.getConfigs().getCommandNotFound()
        ));

        sendTextHelp(sender);
    }

    public void about(CommandSender sender, Player player) {
        sender.sendMessage(General.generateTextFrame(Vars.name));

        sender.sendMessage("");
        // .replace("{player}", player.getName())
        sender.sendMessage(General.setColor(
            "* " + plugin.getConfigs().getWelcomeMessage() + " &b&l" + player.getName()
        ));
        for (String line : plugin.getConfigs().getDescriptionMessages()) {
            sender.sendMessage(General.setColor("&7  " + line));
        }

        sendTextHelp(sender);

        sender.sendMessage("");
        sender.sendMessage(General.generateSeparator());
    }

    public void subCommands(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(General.generateTextFrame(Vars.name));

            sender.sendMessage("");
            sender.sendMessage(General.setColor("_ " + plugin.getConfigs().getListCommands() + ":"));
            sender.sendMessage(General.setColor("  &6/mumc reload"));
            sender.sendMessage(General.setColor("  &6/mumc author"));
            sender.sendMessage(General.setColor("  &6/mumc version"));

            sender.sendMessage("");
            sender.sendMessage(General.generateSeparator());
        }

        else if (args[0].equalsIgnoreCase("reload")) {
            plugin.getConfigs().reloadConfig();
            sender.sendMessage(General.setColor("&a" + Vars.prefix + plugin.getConfigs().getReloadText()));
        }

        else if (args[0].equalsIgnoreCase("author")) {
            sender.sendMessage(General.setColor(
                "&b" + Vars.prefix + "Author: " + Vars.author
            ));
        }

        else if (args[0].equalsIgnoreCase("version")) {
            sender.sendMessage(General.setColor(
                "&b" + Vars.prefix + "Versión: " + Vars.version
            ));
        }

        else {
            notFoundCommand(sender);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        // Si se ejecuta el comando desde la consola
        if (!(sender instanceof Player)) {
            Console.sendMessage(General.setColor(
                "&c" + Vars.prefix + plugin.getConfigs().getPlayerOnlyCommand()
            ));
            return true;
        }

        Player player = (Player) sender;
        
        // no existe argumentos
        if (args.length == 0) {
            about(sender, player);
            return true;
        }

        // si al menos hay un argumento
        if (args.length > 0) {
            subCommands(sender, args);
            return true;
        }

        return true;
    }    
}

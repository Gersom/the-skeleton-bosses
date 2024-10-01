
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
        for (String line : plugin.getConfigs().getHelpText()) {
            sender.sendMessage(General.setColor(line));
        }
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
        sender.sendMessage(General.setColor(
            plugin.getConfigs().getWelcomeMessage().replace("{player}", player.getName())
        ));
        for (String line : plugin.getConfigs().getDescriptionMessages()) {
            sender.sendMessage(General.setColor(line));
        }

        sendTextHelp(sender);

        sender.sendMessage("");
        sender.sendMessage(General.generateSeparator());
    }

    public void subCommands(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(General.generateTextFrame(Vars.name));

            sender.sendMessage("");
            sender.sendMessage(General.setColor("_ Listado de comandos:"));
            sender.sendMessage(General.setColor("  &e/mumc reload"));
            sender.sendMessage(General.setColor("  &e/mumc author"));
            sender.sendMessage(General.setColor("  &e/mumc version"));

            sender.sendMessage("");
            sender.sendMessage(General.generateSeparator());
        }

        else if (args[0].equalsIgnoreCase("reload")) {
            plugin.getConfigs().reloadConfig();
            sender.sendMessage(General.setColor("&a" + Vars.prefix + "¡Configuración recargada!"));
        }

        else if (args[0].equalsIgnoreCase("author")) {
            sender.sendMessage(General.setColor("&e" + Vars.prefix + "Autor del plugin:"));
            sender.sendMessage(General.setColor(Vars.prefix + Vars.author));
        }

        else if (args[0].equalsIgnoreCase("version")) {
            sender.sendMessage(General.setColor("&e" + Vars.prefix + "Versión del plugin:"));
            sender.sendMessage(General.setColor(Vars.prefix + "Version " + Vars.version));
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

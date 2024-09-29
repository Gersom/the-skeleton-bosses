
package mu.gersom.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mu.gersom.utils.Console;
import mu.gersom.utils.General;
import mu.gersom.utils.Vars;

public class MainCommand implements CommandExecutor {
    public void sendTextHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(General.setColor(
            "&a_ Para ver el listado de comandos,"
        ));
        sender.sendMessage(General.setColor(
            "&a  escribe &e/mumc help"
        ));
    }

    public void notFoundCommand(CommandSender sender) {
        sender.sendMessage(General.setColor(
            "&c" + Vars.prefix + "&c¡Comando no encontrado!"
        ));

        sendTextHelp(sender);
    }

    public void about(CommandSender sender, Player player) {
        sender.sendMessage(General.generateTextFrame(Vars.name));

        sender.sendMessage("");
        sender.sendMessage(General.setColor("_ ¡Bienvenido &b" + player.getName() + "&f!"));
        sender.sendMessage(General.setColor(
            "&7  Este plugin te permite recrear"));
        sender.sendMessage(General.setColor(
            "&7  las dinamicas del clasico juego &6&lMu"
        ));

        sendTextHelp(sender);

        sender.sendMessage("");
        sender.sendMessage(General.generateSeparator());
    }

    public void subCommands(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(General.generateTextFrame(Vars.name));

            sender.sendMessage("");
            sender.sendMessage(General.setColor("_ Listado de comandos:"));
            sender.sendMessage(General.setColor("  &e/mumc author"));
            sender.sendMessage(General.setColor("  &e/mumc version"));

            sender.sendMessage("");
            sender.sendMessage(General.generateSeparator());
        }

        else if (args[0].equalsIgnoreCase("author")) {
            sender.sendMessage(General.setColor("&e" + Vars.prefix + "Autor del plugin:"));
            sender.sendMessage(General.setColor(Vars.prefix + Vars.author));
        }

        else if (args[0].equalsIgnoreCase("version")) {
            sender.sendMessage(General.setColor("&e" + Vars.prefix + "Versión del plugin:"));
            sender.sendMessage(General.setColor(Vars.prefix + " Version " + Vars.version));
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
                "&c" + Vars.prefix + "¡Solo puedes usar este comando desde el juego!"
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

package gersom.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gersom.TSB;
import gersom.utils.General;

public class MainCommand implements CommandExecutor {
    private final TSB plugin;
    private final SubCommands subCommands;

    public MainCommand(TSB plugin) {
        this.plugin = plugin;
        this.subCommands = new SubCommands(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        // Si se ejecuta el comando desde la consola
        if (!(sender instanceof Player)) {
            // No existe argumentos
            if (args.length == 0) {
                subCommands.showAboutText(sender, "Console");
                return true;
            }
    
            // Si al menos hay un argumento
            if (args.length > 0) {
                subCommands.handleSubCommands(sender, args);
                return true;
            }

            return true;
        }

        Player player = (Player) sender;

        // No existe argumentos
        if (args.length == 0) {
            // Mostrar información básica incluso sin permisos
            subCommands.showAboutText(sender, player.getName());
            return true;
        }

        // Si hay argumentos, verificar el comando específico
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            
            // Comandos que requieren permiso básico
            if (subCommand.equals("location") || subCommand.equals("author")) {
                if (player.hasPermission("the-skeleton-bosses.use")) {
                    subCommands.handleSubCommands(player, args);
                } else {
                    player.sendMessage(General.setColor(
                        "&c" + plugin.getConfigs().getPrefix() + plugin.getConfigs().getLangCommandNotPermission()
                    ));
                }
                return true;
            }
            
            // Comandos que requieren permiso de admin
            if (subCommand.equals("reload") || subCommand.equals("spawn") || 
                subCommand.equals("help") || subCommand.equals("clear") || 
                subCommand.equals("version")) {
                if (player.hasPermission("the-skeleton-bosses.admin")) {
                    subCommands.handleSubCommands(player, args);
                } else {
                    player.sendMessage(General.setColor(
                        "&c" + plugin.getConfigs().getPrefix() + plugin.getConfigs().getLangCommandNotPermission()
                    ));
                }
                return true;
            }

            // Si el comando no existe
            subCommands.showNotFoundCommandText(sender);
        }

        return true;
    }    
}
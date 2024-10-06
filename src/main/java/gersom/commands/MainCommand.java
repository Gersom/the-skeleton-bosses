package gersom.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gersom.TSB;
import gersom.utils.General;
import gersom.utils.Vars;

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
            // Console.sendMessage(General.setColor(
            //     "&c" + Vars.prefix + plugin.getConfigs().getPlayerOnlyCommand()
            // ));

            // no existe argumentos
            if (args.length == 0) {
                subCommands.showAboutText(sender, "Console");
                return true;
            }
    
            // si al menos hay un argumento
            if (args.length > 0) {
                subCommands.handleSubCommands(sender, args);
                return true;
            }

            return true;
        }

        Player player = (Player) sender;

        if (player.hasPermission("the-skeleton-bosses.use")) {
            // no existe argumentos
            if (args.length == 0) {
                subCommands.showAboutText(sender, player.getName());
                return true;
            }
    
            // si al menos hay un argumento
            if (args.length > 0) {
                subCommands.handleSubCommands(player, args);
                return true;
            }
        }

        else {
            player.sendMessage(General.setColor(
                "&c" + Vars.prefix + plugin.getConfigs().getNotPermission()
            ));
        }

        return true;
    }    
}
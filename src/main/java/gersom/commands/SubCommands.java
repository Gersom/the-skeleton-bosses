package gersom.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gersom.TSB;
import gersom.utils.Console;
import gersom.utils.General;
import gersom.utils.Vars;

public class SubCommands {
    private final TSB plugin;

    public SubCommands(TSB plugin) {
        this.plugin = plugin;
    }

    public void handleSubCommands(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("help") 
            || args[0].equalsIgnoreCase("list")
            || "?".equals(args[0])
        ) {
            showListCommands(sender);
        } else if (args[0].equalsIgnoreCase("reload")) {
            reloadConfig(sender);
        } else if (args[0].equalsIgnoreCase("author")) {
            showAuthor(sender);
        } else if (args[0].equalsIgnoreCase("version")) {
            showVersion(sender);
        } else if (args[0].equalsIgnoreCase("spawn")) {
            spawnMobCustom(sender, args);
        } else {
            showNotFoundCommandText(sender);
        }
    }

    public void showNotFoundCommandText(CommandSender sender) {
        sender.sendMessage(General.setColor(
            "&c" + plugin.getConfigs().getPrefix() + plugin.getConfigs().getCommandNotFound()
        ));
        showHelpText(sender);
    }

    private void showListCommands(CommandSender sender) {
        sender.sendMessage(General.generateTextFrame(Vars.name));
        sender.sendMessage("");
        sender.sendMessage(General.setColor("_ " + plugin.getConfigs().getListCommands() + ":"));
        sender.sendMessage(General.setColor("  &6/tsb spawn [emperor, king]"));
        sender.sendMessage(General.setColor("  &6/tsb reload"));
        sender.sendMessage(General.setColor("  &6/tsb author"));
        sender.sendMessage(General.setColor("  &6/tsb version"));
        sender.sendMessage("");
        sender.sendMessage(General.generateSeparator());
    }

    private void reloadConfig(CommandSender sender) {
        plugin.getConfigs().reloadConfig();

        if (plugin.getMainMobs().getTaskAutoSpawn() != null) {
            plugin.getMainMobs().getTaskAutoSpawn().cancel();
        }

        if (plugin.getConfigs().getSpawnEnabled()) {
            plugin.autoSpawnBosses();
        }

        sender.sendMessage(General.setColor("&a" + plugin.getConfigs().getPrefix() + plugin.getConfigs().getReloadText()));
    }

    private void showAuthor(CommandSender sender) {
        sender.sendMessage(General.setColor(
            "&b" + plugin.getConfigs().getPrefix() + "Author: " + Vars.author
        ));
    }

    private void showVersion(CommandSender sender) {
        sender.sendMessage(General.setColor(
            "&b" + plugin.getConfigs().getPrefix() + "Versión: " + Vars.version
        ));
    }

    public void showHelpText(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(General.setColor("* " + plugin.getConfigs().getHelpText()));
        sender.sendMessage(General.setColor("  &6/tsb help"));
    }

    public void showAboutText(CommandSender sender, String playerName) {
        sender.sendMessage(General.generateTextFrame(Vars.name));
        sender.sendMessage("");
        sender.sendMessage(General.setColor(
            "* " + plugin.getConfigs().getWelcomeMessage() + " &b&l" + playerName
        ));
        for (String line : plugin.getConfigs().getDescriptionMessages()) {
            sender.sendMessage(General.setColor("&7  " + line));
        }
        showHelpText(sender);
        sender.sendMessage("");
        sender.sendMessage(General.generateSeparator());
    }

    private void spawnMobCustom(CommandSender sender, String[] arg) {
        if (!(sender instanceof Player)) {
            Console.sendMessage("&c" + plugin.getConfigs().getPrefix() + plugin.getConfigs().getPlayerOnlyCommand());
            return;
        }

        Player player = (Player) sender;

        if (arg.length > 1) {
            if (arg[1].equalsIgnoreCase("emperor")) {
                plugin.getMainMobs().generateEmperor(player.getWorld(), player.getLocation());
            }
            
            else if (arg[1].equalsIgnoreCase("king")) {
                plugin.getMainMobs().generateKing(player.getWorld(), player.getLocation());
            }

            else {
                showNotFoundCommandText(sender);
            }
        }
        
        else {
            showNotFoundCommandText(sender);
        }

        
    }
}

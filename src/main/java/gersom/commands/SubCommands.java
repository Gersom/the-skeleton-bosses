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
            "&c" + plugin.getConfigs().getPrefix() + plugin.getConfigs().getLangCommandNotFound()
        ));
        showHelpText(sender);
    }

    private void showListCommands(CommandSender sender) {
        sender.sendMessage(General.generateTextFrame(Vars.name));
        sender.sendMessage("");
        sender.sendMessage(General.setColor("_ " + plugin.getConfigs().getLangCommandsList() + ":"));
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

        sender.sendMessage(General.setColor("&a" + plugin.getConfigs().getPrefix() + plugin.getConfigs().getLangCommandReload()));
    }

    private void showAuthor(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(General.setColor(
            "&b" + plugin.getConfigs().getPrefix() + "&r&bAuthor: " + Vars.author
        ));
        sender.sendMessage("");
    }

    private void showVersion(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(General.setColor(
            "&b" + plugin.getConfigs().getPrefix() + "&r&bVersión: " + Vars.version
        ));
        sender.sendMessage("");
    }

    public void showHelpText(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(General.setColor("* " + plugin.getConfigs().getLangCommandHelpText()));
        sender.sendMessage(General.setColor("  &6/tsb help"));
    }

    public void showAboutText(CommandSender sender, String playerName) {
        sender.sendMessage(General.generateTextFrame(Vars.name));
        sender.sendMessage("");
        sender.sendMessage(General.setColor(
            "* " + plugin.getConfigs().getLangWelcome() + " &b&l" + playerName
        ));
        for (String line : plugin.getConfigs().getLangDescription()) {
            sender.sendMessage(General.setColor("&7  " + line));
        }
        showHelpText(sender);
        sender.sendMessage("");
        sender.sendMessage(General.generateSeparator());
    }

    private void spawnMobCustom(CommandSender sender, String[] arg) {
        if (!(sender instanceof Player)) {
            Console.sendMessage("&c" + plugin.getConfigs().getPrefix() + plugin.getConfigs().getLangCommandPlayerOnly());
            return;
        }

        Player player = (Player) sender;

        if (arg.length > 1) {
            if (arg[1].equalsIgnoreCase("SkeletonEmperor")) {
                plugin.getMainMobs().generateEmperor(player.getWorld(), player.getLocation());
            }
            else if (arg[1].equalsIgnoreCase("skeletonemperor")) {
                plugin.getMainMobs().generateEmperor(player.getWorld(), player.getLocation());
            }
            else if (arg[1].equalsIgnoreCase("skeleton-emperor")) {
                plugin.getMainMobs().generateEmperor(player.getWorld(), player.getLocation());
            }
            
            else if (arg[1].equalsIgnoreCase("SkeletonKing")) {
                plugin.getMainMobs().generateKing(player.getWorld(), player.getLocation());
            }
            else if (arg[1].equalsIgnoreCase("skeletonking")) {
                plugin.getMainMobs().generateKing(player.getWorld(), player.getLocation());
            }
            else if (arg[1].equalsIgnoreCase("skeleton-king")) {
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

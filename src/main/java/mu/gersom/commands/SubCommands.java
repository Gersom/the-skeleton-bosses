package mu.gersom.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mu.gersom.MuMc;
import mu.gersom.utils.CustomSkeletonSpawner;
import mu.gersom.utils.General;
import mu.gersom.utils.Vars;

public class SubCommands {
    private final MuMc plugin;

    public SubCommands(MuMc plugin) {
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
            spawnMobCustom(sender);
        } else {
            showNotFoundCommandText(sender);
        }
    }

    public void showNotFoundCommandText(CommandSender sender) {
        sender.sendMessage(General.setColor(
            "&c" + Vars.prefix + plugin.getConfigs().getCommandNotFound()
        ));
        showHelpText(sender);
    }

    private void showListCommands(CommandSender sender) {
        sender.sendMessage(General.generateTextFrame(Vars.name));
        sender.sendMessage("");
        sender.sendMessage(General.setColor("_ " + plugin.getConfigs().getListCommands() + ":"));
        sender.sendMessage(General.setColor("  &6/mumc reload"));
        sender.sendMessage(General.setColor("  &6/mumc author"));
        sender.sendMessage(General.setColor("  &6/mumc version"));
        sender.sendMessage("");
        sender.sendMessage(General.generateSeparator());
    }

    private void reloadConfig(CommandSender sender) {
        plugin.getConfigs().reloadConfig();
        sender.sendMessage(General.setColor("&a" + Vars.prefix + plugin.getConfigs().getReloadText()));
    }

    private void showAuthor(CommandSender sender) {
        sender.sendMessage(General.setColor(
            "&b" + Vars.prefix + "Author: " + Vars.author
        ));
    }

    private void showVersion(CommandSender sender) {
        sender.sendMessage(General.setColor(
            "&b" + Vars.prefix + "Versión: " + Vars.version
        ));
    }

    public void showHelpText(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(General.setColor("* " + plugin.getConfigs().getHelpText()));
        sender.sendMessage(General.setColor("  &6/mumc help"));
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

    private void spawnMobCustom(CommandSender sender) {
        // if (!(sender instanceof Player)) {
        //     sender.sendMessage(General.setColor("&c" + Vars.prefix + plugin.getConfigs().getPlayerOnlyCommand()));
        //     return;
        // }
        
        Player player = (Player) sender;
        new CustomSkeletonSpawner(plugin).spawnCustomSkeleton(player);
    }
}

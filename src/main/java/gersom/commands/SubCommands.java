package gersom.commands;

import org.bukkit.Location;
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
        } else if (args[0].equalsIgnoreCase("spawn")) {
            spawnBossCustom(sender, args);
        } else if (args[0].equalsIgnoreCase("location")) {
            showLocationBoss(sender);
        } else if (args[0].equalsIgnoreCase("clear")) {
            clearRecords(sender);
        } else if (args[0].equalsIgnoreCase("reload")) {
            reloadConfig(sender);
        } else if (args[0].equalsIgnoreCase("author")) {
            showAuthor(sender);
        } else if (args[0].equalsIgnoreCase("version")) {
            showVersion(sender);
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
        sender.sendMessage(General.setColor("  &6/tsb location"));
        sender.sendMessage(General.setColor("  &6/tsb clear"));
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

        sender.sendMessage(General.setColor(
            "&a" + plugin.getConfigs().getPrefix() + "&a" + plugin.getConfigs().getLangCommandReload()
        ));
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

    private void spawnBossCustom(CommandSender sender, String[] arg) {
        if (!(sender instanceof Player)) {
            Console.sendMessage("&c" + plugin.getConfigs().getPrefix() + plugin.getConfigs().getLangCommandPlayerOnly());
            return;
        }

        Location locationKing = plugin.getBossPersistenceManager().getBossLocation("skeletonKing");
        Location locationEmperor = plugin.getBossPersistenceManager().getBossLocation("skeletonEmperor");

        Player player = (Player) sender;

        if(locationEmperor != null) {
            sendMessageAlreadyExist(
                player,
                plugin.getConfigs().getLangBossEmperorName(),
                plugin.getConfigs().getBossEmperorColor(),
                String.format(
                    "(%.0f, %.0f, %.0f)",
                    locationEmperor.getX(),
                    locationEmperor.getY(),
                    locationEmperor.getZ()
                )
            );
            return;
        }

        if(locationKing != null) {
            sendMessageAlreadyExist(
                player,
                plugin.getConfigs().getLangBossKingName(),
                plugin.getConfigs().getBossKingColor(),
                String.format(
                    "(%.0f, %.0f, %.0f)",
                    locationKing.getX(),
                    locationKing.getY(),
                    locationKing.getZ()
                )
            );
        }

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

    public void clearRecords(CommandSender sender) {
        plugin.getBossPersistenceManager().removeBossData("skeletonEmperor");
        plugin.getBossPersistenceManager().removeBossData("skeletonKing");

        sender.sendMessage(General.setColor(
            "&a" + plugin.getConfigs().getPrefix() + plugin.getConfigs().getLangCommandClearRecords()
        ));
    }

    public void showLocationBoss(CommandSender sender) {
        Location locationKing = plugin.getBossPersistenceManager().getBossLocation("skeletonKing");
        Location locationEmperor = plugin.getBossPersistenceManager().getBossLocation("skeletonEmperor");
        String bossCoords = "";
        String bossColor = "";
        String bossName = "";
        
        if (locationEmperor != null) {
            if (plugin.getMainMobs().getSkeletonEmperor() != null && plugin.getMainMobs().getSkeletonEmperor().getEntity() != null) {
                bossCoords = String.format(
                    "(%.0f, %.0f, %.0f)",
                    plugin.getMainMobs().getSkeletonEmperor().getEntity().getLocation().getX(),
                    plugin.getMainMobs().getSkeletonEmperor().getEntity().getLocation().getY(),
                    plugin.getMainMobs().getSkeletonEmperor().getEntity().getLocation().getZ()
                );
            } else {
                bossCoords = String.format("(%.0f, %.0f, %.0f)", locationEmperor.getX(), locationEmperor.getY(), locationEmperor.getZ());
            }
            bossColor = plugin.getConfigs().getBossEmperorColor();
            bossName = plugin.getConfigs().getLangBossEmperorName();
        }
        
        else if (locationKing != null) {
            if (plugin.getMainMobs().getSkeletonKing() != null && plugin.getMainMobs().getSkeletonKing().getEntity() != null) {
                bossCoords = String.format(
                    "(%.0f, %.0f, %.0f)",
                    plugin.getMainMobs().getSkeletonKing().getEntity().getLocation().getX(),
                    plugin.getMainMobs().getSkeletonKing().getEntity().getLocation().getY(),
                    plugin.getMainMobs().getSkeletonKing().getEntity().getLocation().getZ()
                );
            } else {
                bossCoords = String.format("(%.0f, %.0f, %.0f)", locationKing.getX(), locationKing.getY(), locationKing.getZ());
            }
            bossColor = plugin.getConfigs().getBossKingColor();
            bossName = plugin.getConfigs().getLangBossKingName();
        }

        sender.sendMessage("");
        sender.sendMessage(General.setColor(
            "&e" + plugin.getConfigs().getPrefix() + bossColor + "&l" + bossName
        ));
        sender.sendMessage(General.setColor(
            "&e" + plugin.getConfigs().getPrefix() + "&7coords: &e" + bossCoords
        ));
        sender.sendMessage("");
    }

    public void sendMessageAlreadyExist (CommandSender sender, String bossName, String bossColor, String bossCoords) {
        sender.sendMessage("");
        sender.sendMessage(General.setColor(
            "&c" + plugin.getConfigs().getPrefix() + "&c" + plugin.getConfigs().getLangCommandAlreadyExist()
        ));
        sender.sendMessage(General.setColor(
            "&c" + plugin.getConfigs().getPrefix() + bossColor + "&l" + bossName + ": &e" + bossCoords
        ));
        sender.sendMessage("");
    }
}

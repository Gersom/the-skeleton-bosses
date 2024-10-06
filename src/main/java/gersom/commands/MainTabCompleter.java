package gersom.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class MainTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (sender.hasPermission("the-skeleton-bosses.use")) {
            if (args.length == 1) {
                // Main commands
                completions.add("help");
                completions.add("spawn");
                completions.add("reload");
                completions.add("author");
                completions.add("version");
            } else if (args.length == 2) {
                // Subcommands
                if (args[0].equalsIgnoreCase("spawn")) {
                    completions.add("king");
                    completions.add("emperor");
                    // Add more mob types here as you implement them
                }
            }
        }
            
        // Filter completions based on what the user has already typed
        return completions.stream()
            .filter(completion -> completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
            .collect(Collectors.toList());
    }
}
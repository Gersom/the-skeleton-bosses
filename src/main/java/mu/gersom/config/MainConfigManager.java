package mu.gersom.config;

import java.util.List;

import mu.gersom.MuMc;

public class MainConfigManager {
    private final CustomConfig configFile;

    public MainConfigManager(MuMc plugin) {
        configFile = new CustomConfig("config.yml", null, plugin);
        configFile.registerConfig();
    }

    public void reloadConfig() {
        configFile.reloadConfig();
    }

    public String getLanguage() {
        return configFile.getString("language", "en");
    }

    public String getMsgPluginEnabled() {
        return configFile.getString("messages.plugin.enabled", "Plugin has been enabled!");
    }

    public String getMsgPluginDisabled() {
        return configFile.getString("messages.plugin.disabled", "Plugin has been disabled!");
    }

    public List<String> getHelpText() {
        return configFile.getStringList("messages.commands.help_text");
    }

    public String getCommandNotFound() {
        return configFile.getString("messages.commands.not_found", "Command not found!");
    }

    public String getPlayerOnlyCommand() {
        return configFile.getString("messages.commands.player_only", "This command can only be used in-game!");
    }

    public String getWelcomeMessage() {
        return configFile.getString("messages.about.welcome", "Welcome {player}!");
    }

    public List<String> getDescriptionMessages() {
        return configFile.getStringList("messages.about.description");
    }

    // Add more methods as needed for other configuration values 
}
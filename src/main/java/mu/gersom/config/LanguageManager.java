package mu.gersom.config;

import java.io.File;
import java.util.List;

import mu.gersom.MuMc;
import mu.gersom.utils.Console;

public class LanguageManager {
    private final MuMc plugin;
    private CustomConfig langConfig;

    public LanguageManager(MuMc plugin) {
        this.plugin = plugin;
        createLanguageFiles();
        loadLanguageConfig();
    }

    private void createLanguageFiles() {
        String[] languages = {"en", "es"};
        for (String lang : languages) {
            File langFile = new File(plugin.getDataFolder() + File.separator + "lang", lang + ".yml");
            if (!langFile.exists()) {
                plugin.saveResource("lang" + File.separator + lang + ".yml", false);
                Console.sendMessage("&aCreated language file: " + lang + ".yml");
            }
        }
    }

    private void loadLanguageConfig() {
        String language = plugin.getConfigs().getLanguage();
        Console.sendMessage("&aLoading language: " + language);
        langConfig = new CustomConfig(language + ".yml", "lang", plugin);
        langConfig.registerConfig();
    }

    public void reloadLanguageConfig() {
        loadLanguageConfig();
    }

    public String getMessage(String path, String defaultValue) {
        return langConfig.getString(path, defaultValue);
    }

    public List<String> getMessageList(String path) {
        return langConfig.getStringList(path);
    }

    // Add methods for each message you need to retrieve
    public String getMsgPluginEnabled() {
        return getMessage("messages.plugin.enabled", "Plugin has been enabled!");
    }

    public String getMsgPluginDisabled() {
        return getMessage("messages.plugin.disabled", "Plugin has been disabled!");
    }

    public List<String> getHelpText() {
        return getMessageList("messages.commands.help_text");
    }

    public String getCommandNotFound() {
        return getMessage("messages.commands.not_found", "Command not found!");
    }

    public String getPlayerOnlyCommand() {
        return getMessage("messages.commands.player_only", "This command can only be used in-game!");
    }

    public String getWelcomeMessage() {
        return getMessage("messages.about.welcome", "Welcome {player}!");
    }

    public List<String> getDescriptionMessages() {
        return getMessageList("messages.about.description");
    }
}
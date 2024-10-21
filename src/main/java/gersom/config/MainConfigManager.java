package gersom.config;

import java.util.List;

import gersom.TSB;

public class MainConfigManager {
    private final CustomConfig configFile;
    private LanguageManager languageManager;
    private final TSB plugin;

    public MainConfigManager(TSB plugin) {
        this.plugin = plugin;
        this.configFile = new CustomConfig("config.yml", null, plugin);
        this.configFile.registerConfig();
    }

    public void initialize() {
        this.languageManager = new LanguageManager(plugin);
    }

    public void reloadConfig() {
        configFile.reloadConfig();
        languageManager.reloadLanguageConfig();
    }

    public void setLanguage(String language) {
        configFile.getConfig().set("language", language);
        configFile.saveConfig();
        languageManager.reloadLanguageConfig();
    }

    public String getLanguage() {
        return configFile.getString("language", "en");
    }

    public String getPrefix() {
        return configFile.getString("prefix", "[TSB] ");
    }

    // Spawn data
    public Boolean getSpawnEnabled() {
        return configFile.getBoolean("spawn.enabled", false);
    }
    public String getSpawnWorld() {
        return configFile.getString("spawn.world", "world");
    }
    public int getSpawnInterval() {
        return configFile.getInt("spawn.interval", 20);
    }
    public int getSpawnMinRadius() {
        return configFile.getInt("spawn.min_radius", 0);
    }
    public int getSpawnMaxRadius() {
        return configFile.getInt("spawn.max_radius", 10);
    }
    public double getSpawnChance() {
        return configFile.getInt("spawn.chance", 100) / 100;
    }
    public int getSpawnLocationX() {
        return configFile.getInt("spawn.location.x", 0);
    }
    public int getSpawnLocationY() {
        // This field is irrelevant, 
        // it is only there as a reference to generate
        // the Location variable, since position «y» 
        // will be the highest non-empty coordinate (impassable).
        return 150;
    }
    public int getSpawnLocationZ() {
        return configFile.getInt("spawn.location.z", 0);
    }

    // BOSSES
    // Percentage of all bosses that will spawn
    public double  getBossKingPercentage() {
        double kpercentage = configFile.getInt("bosses.skeleton_king.percentage", 50);
        return kpercentage / 100;
    }
    public double  getBossEmperorPercentage() {
        return getBossKingPercentage() + (1 - getBossKingPercentage());
    }
    // Color Boss
    public String getBossKingColor() {
        return configFile.getString("bosses.skeleton_king.color", "&d");
    }
    public String getBossEmperorColor() {
        return configFile.getString("bosses.skeleton_emperor.color", "&6");
    }
    // Health of the bosses
    public double getBossKingHealth() {
        return configFile.getDouble("bosses.skeleton_king.health", 200.0);
    }
    public double getBossEmperorHealth() {
        return configFile.getDouble("bosses.skeleton_emperor.health", 140.0);
    }
    // Experience of the bosses
    public int getBossKingExp() {
        return configFile.getInt("bosses.skeleton_king.drop_experience", 350);
    }
    public int getBossEmperorExp() {
        return configFile.getInt("bosses.skeleton_emperor.drop_experience", 250);
    }
    // Projectile evasion of the bosses
    public int getBossKingProjectileEvasion() {
        return configFile.getInt("bosses.skeleton_king.projectile_evasion", 75);
    }
    public int getBossEmperorProjectileEvasion() {
        return configFile.getInt("bosses.skeleton_emperor.projectile_evasion", 50);
    }
    // Bossbar messages
    public String getBossKingBossbarTitle() {
        return configFile.getString("bosses.skeleton_king.bossbar.title", "{boss_color} &l{boss_name} ({health}/{max_health})❤");
    }
    public String getBossKingBossbarColor() {
        return configFile.getString("bosses.skeleton_king.bossbar.color", "PURPLE");
    }
    public String getBossEmperorBossbarTitle() {
        return configFile.getString("bosses.skeleton_emperor.bossbar.title", "{boss_color} &l{boss_name} ({health}/{max_health})❤");
    }
    public String getBossEmperorBossbarColor() {
        return configFile.getString("bosses.skeleton_emperor.bossbar.color", "YELLOW");
    }
    // Command for player killed the boss
    public Boolean getBossesCommandEnabled() {
        return configFile.getBoolean("commands_after_death", false);
    }
    public String getBossKingCommand() {
        return configFile.getString("bosses.skeleton_king.command", "");
    }
    public String getBossEmperorCommand() {
        return configFile.getString("bosses.skeleton_emperor.command", "");
    }
    // Command for nearby players
    public boolean getBossKingNearbyCommandEnabled() {
        return configFile.getBoolean("bosses.skeleton_king.command_for_nearby_players.enabled", false);
    }
    public int getBossKingNearbyCommandRadius() {
        return configFile.getInt("bosses.skeleton_king.command_for_nearby_players.radius", 50);
    }
    public String getBossKingNearbyCommand() {
        return configFile.getString("bosses.skeleton_king.command_for_nearby_players.command", "give {player} diamond 1");
    }
    public boolean getBossEmperorNearbyCommandEnabled() {
        return configFile.getBoolean("bosses.skeleton_emperor.command_for_nearby_players.enabled", false);
    }
    public int getBossEmperorNearbyCommandRadius() {
        return configFile.getInt("bosses.skeleton_emperor.command_for_nearby_players.radius", 40);
    }
    public String getBossEmperorNearbyCommand() {
        return configFile.getString("bosses.skeleton_emperor.command_for_nearby_players.command", "give {player} diamond 1");
    }

    // Delegate message methods to LanguageManager
    // Plugin messages
    public String getLangPluginEnabled() {
        return languageManager.getLangPluginEnabled();
    }
    public String getLangPluginDisabled() {
        return languageManager.getLangPluginDisabled();
    }

    // About messages
    public String getLangWelcome() {
        return languageManager.getLangWelcome();
    }
    public List<String> getLangDescription() {
        return languageManager.getLangDescription();
    }

    // Commands messages
    public String getLangCommandsList() {
        return languageManager.getLangCommandsList();
    }
    public String getLangCommandPlayerOnly() {
        return languageManager.getLangCommandPlayerOnly();
    }
    public String getLangCommandHelpText() {
        return languageManager.getLangCommandHelpText();
    }
    public String getLangCommandReload() {
        return languageManager.getLangCommandReload();
    }
    public String getLangCommandNotFound() {
        return languageManager.getLangCommandNotFound();
    }
    public String getLangCommandNotPermission() {
        return languageManager.getLangCommandNotPermission();
    }
    public String getLangCommandAlreadyExist() {
        return languageManager.getLangCommandAlreadyExist();
    }
    public String getLangCommandClearRecords() {
        return languageManager.getLangCommandClearRecords();
    }
    
    // BOSSES MESSAGES LANGUAGE
    // Name of the bosses
    public String getLangBossEmperorName() {
        return languageManager.getLangBossEmperorName();
    }
    public String getLangBossKingName() {
        return languageManager.getLangBossKingName();
    }
    
    // Messages of the bosses
    public String getLangBossesMsgSpawn() {
        return languageManager.getLangBossesMsgSpawn();
    }
    public String getLangBossesMsgDeath() {
        return languageManager.getLangBossesMsgDeath();
    }
    public String getLangBossesMsgKilled() {
        return languageManager.getLangBossesMsgKilled();
    }

    // Name of the objects left behind when he dies
    public String getLangBossesItemBow() {
        return languageManager.getLangBossesItemBow();
    }
    public String getLangBossesItemSword() {
        return languageManager.getLangBossesItemSword();
    }
    public String getLangBossesItemHelmet() {
        return languageManager.getLangBossesItemHelmet();
    }
    
}
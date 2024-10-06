<p align="center">
    <a href="https://github.com/Gersom/mu-mc">
        <img src="https://img.shields.io/github/stars/Gersom/mu-mc?style=for-the-badge&logo=github&color=brightgreen" alt="stars">
    </a>
    <a href="https://github.com/Gersom/mu-mc/issues">
        <img src="https://img.shields.io/github/issues/Gersom/mu-mc?style=for-the-badge&logo=github&color=red" alt="issues">
    </a>
    <a href="https://github.com/Gersom/mu-mc/blob/main/LICENSE">
        <img src="https://img.shields.io/github/license/Gersom/mu-mc?style=for-the-badge&logo=github&color=blue" alt="license">
    </a>
</p>

# THE SKELETON BOSSES

## Welcome to THE SKELETON BOSSES!

TSB introduces formidable skeleton bosses to your Minecraft world: the Skeleton Emperor and the Skeleton King. These powerful entities spawn automatically in designated areas, boasting enhanced attributes that set them apart from ordinary skeletons. Defeat these challenging foes to claim unique and valuable loot, adding an exciting new dimension to your gameplay experience.

## Features

- Generation of custom bosses:
  - Skeleton Emperor
  - Skeleton King
- Automatic boss spawn system
- Special item drops when defeating bosses
- Health bar for bosses
- Command system to interact with the plugin
- Multi-language support (English and Spanish)

## Project Structure

```
MUMC/
├── src/
│   └── main/
│       ├── java/
│       │   └── mu/
│       │       └── gersom/
│       │           ├── commands/
│       │           │   ├── MainCommand.java
│       │           │   ├── MainTabCompleter.java
│       │           │   └── SubCommands.java
│       │           ├── config/
│       │           │   ├── BossPersistenceManager.java
│       │           │   ├── CustomConfig.java
│       │           │   ├── LanguageManager.java
│       │           │   └── MainConfigManager.java
│       │           ├── generators/
│       │           │   ├── MainGenerator.java
│       │           │   ├── SkeletonEmperor.java
│       │           │   └── SkeletonKing.java
│       │           ├── listeners/
│       │           │   └── MainListeners.java
│       │           ├── utils/
│       │           │   ├── Console.java
│       │           │   ├── General.java
│       │           │   └── Vars.java
│       │           └── MuMc.java
│       └── resources/
│           ├── lang/
│           │   ├── en.yml
│           │   ├── es.yml
│           │   └── other.yml
│           ├── config.yml
│           └── plugin.yml
├── pom.xml
└── README.md
```

## Commands

### /tsb spawn [emperor|king]

Spawns a specific boss at the player's location.

### /tsb reload

Reloads the plugin configuration.

### /tsb author

Shows information about the plugin author.

### /tsb version

Displays the current version of the plugin.

### /tsb help

Shows the list of available commands.

## Configuration

The `config.yml` file allows you to customize various aspects of the plugin:

- Plugin language
- Automatic boss spawn configuration
- Experience granted by bosses
- Commands executed when defeating bosses

## Supported Languages

- English (en)
- Spanish (es)
- Others (template for adding new languages)

## Author

**Gersom**

- GitHub: [@Gersom](https://github.com/Gersom)
- LinkedIn: [Gersom Alaja](https://www.linkedin.com/in/gersomalaja/)

## License

This plugin is licensed under the MIT License.
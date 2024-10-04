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



# MuMc

## ¡Bienvenido a MuMc!

Este plugin te permite recrear las dinamicas del clasico juego **Mu**.

## Estructura del proyecto

```
MUMC/
├── .idea/
├── .vscode/
├── src/
│   └── main/
│       ├── java/
│       │   └── mu/
│       │       └── gersom/
│       │           ├── commands/
│       │           │   ├── MainCommand.java
│       │           │   ├── MainTabCompleter.java
│       │           |   └── SubCommands.java
│       │           ├── config/
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
├── target/ (gitignore)
├── .gitignore
├── pom.xml
└── readme.md
```

## Comandos

### /mumc reload

Recarga la configuración del plugin.

### /mumc author

MuMc es un plugin de **Spigot** para **Minecraft**, creado por **Gersom**.

### /mumc version

MuMc versión 1.0.0

### /mumc help

MuMc listado de comandos:

* /mumc reload
* /mumc author
* /mumc version

## Autor

**Gersom**

- GitHub: [@Gersom](https://github.com/Gersom)
- Linkedin: [Gersom Alaja](https://www.linkedin.com/in/gersomalaja/)

## Licencia

Este plugin está licenciado bajo la licencia MIT.

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

Este plugin te permite recrear las dinámicas del clásico juego **Mu Online** en Minecraft.

## Características

- Generación de bosses personalizados:
  - Skeleton Emperor
  - Skeleton King
- Sistema de spawn automático de bosses
- Drops de items especiales al derrotar bosses
- Barra de vida para los bosses
- Sistema de comandos para interactuar con el plugin
- Soporte multilenguaje (Inglés y Español)

## Estructura del proyecto

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

## Comandos

### /mumc spawn [emperor|king]

Genera un boss específico en la ubicación del jugador.

### /mumc reload

Recarga la configuración del plugin.

### /mumc author

Muestra información sobre el autor del plugin.

### /mumc version

Muestra la versión actual del plugin.

### /mumc help

Muestra la lista de comandos disponibles.

## Configuración

El archivo `config.yml` permite personalizar varios aspectos del plugin:

- Idioma del plugin
- Configuración de spawn automático de bosses
- Experiencia otorgada por los bosses
- Comandos ejecutados al derrotar bosses

## Idiomas soportados

- Inglés (en)
- Español (es)
- Otros (plantilla para agregar nuevos idiomas)

## Autor

**Gersom**

- GitHub: [@Gersom](https://github.com/Gersom)
- LinkedIn: [Gersom Alaja](https://www.linkedin.com/in/gersomalaja/)

## Licencia

Este plugin está licenciado bajo la licencia MIT.
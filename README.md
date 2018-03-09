# ✨NovaMobsX✨ 

Development: **[PikyCZ](https://github.com/PikyCZ)**

![](https://circleci.com/gh/PikyCZ/NovaMobsX.svg?style=shield&circle-token=ef:a9:c6:67:1a:e3:2b:fe:67:4b:d6:d1:d7:bf:35:18)
[![Github All Releases](https://img.shields.io/github/downloads/PikyCZ/NovaMobsX/total.svg)](https://github.com/PikyCZ/NovaMobsX/releases)
[![GitHub release](https://img.shields.io/github/release/PikyCZ/NovaMobsX.svg)](https://github.com/PikyCZ/NovaMobsX/releases/latest)
[![](https://img.shields.io/badge/stable-status-brightgreen.svg)](status/status.md)

NovaMobs is a plugin that implements the mob entities for MCPE including movement, aggression etc.

# Download
 ---------
__[NovaMobs Download at Circle CI](https://circleci.com/gh/PikyCZ/NovaMobsX/tree/master/)__ (**login required**)

# Commands
-----------
| Command | Usage | Description |
| ------- |  ----- | ----------- |
| `/mob` | `/mob` | Display list of commands|
| `/mob spawn <mob_name>` | `/mob spawn <mob_name>` | Spawn mob (mob_name write always with a capital letter.. For example: Pig,Zombie)
| `/mob removemobs` | `/mob removemobs` | Remove all living mobs|
| `/mob removeitems` | `/mob removeitems` | Remove all items from all levels (ground)|
| `/mob version` | `/mob version` | Show novamobs version|

# Permissions
-------------
```yml
 novamobs.mob:
    default: op
  ```
# Config
--------
```yml
#NovaMobsX Config file
#Disabled Worlds 
WorldsSpawnDisabled:
- Lobby
#Spawn Settings >> #300ticks = 15s +-
Spawning Settings:
spawnAnimals: true
spawnMonsters: true
spawnNethermobs: true
AutoSpawnTime: 300 
DespawnTime: 1500
#Spawn Settings >>
#TaskSettings
AutoSpawnTask: true
DespawnTask: true
#TaskSettings
#ConfigSettings
ConfigVersion: "1.0"
#Do not Remove or Edit!
```

# Contributed code since 2016
* [PikyCZ](//github.com/PikyCZ)
* [Creeperface01](//github.com/Creeperface01)

# UUID Command

![Modrinth Version](https://img.shields.io/modrinth/v/nKBHyafW?logo=modrinth&color=008800)
![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/nKBHyafW?logo=modrinth&color=008800)
![Modrinth Downloads](https://img.shields.io/modrinth/dt/nKBHyafW?logo=modrinth&color=008800)
[![Discord Badge](https://img.shields.io/badge/chat-discord-%235865f2)](https://discord.gg/CNNkyWRkqm)
[![Github Badge](https://img.shields.io/badge/github-uuidcommand-white?logo=github)](https://github.com/eclipseisoffline/uuidcommand)
![GitHub License](https://img.shields.io/github/license/eclipseisoffline/uuidcommand)

This mod adds a simple `/uuid` command that shows the UUID of any entity currently loaded,
and allows you to copy the UUID string or the NBT data representation of the UUID.

Feel free to report any bugs, or suggest new features, at the issue tracker.

## License

This mod is licensed under GNU LGPLv3.

## Version support

| Minecraft Version | Status       |
|-------------------|--------------|
| 1.21.11           | ✅ Current    |
| 1.21.9+10         | ✔️ Available |
| 1.21.6+7+8        | ✔️ Available |
| 1.21.5            | ✔️ Available |
| 1.21.2            | ✔️ Available |
| 1.21+1            | ✅ Current    |
| 1.20.5+6          | ✔️ Available |
| 1.20.4            | ✔️ Available |

## Usage

Mod builds can be found on the releases page, as well as on [Modrinth](https://modrinth.com/mod/uuid-command).

The Fabric API is required. This mod is both supported on the server and on the client:

- When installed on the server, any OP-ed players are able to use the `/uuid` command, even if they don't have the mod installed client side.
- When installed on the client, you can use the `/uuid` command in single- and multiplayer, even if you don't have OP permissions.
  - On multiplayer, the command is only able to fetch the UUIDs of entities in render distance and all players. 

Command syntax:

- `/uuid <entity>` - displays the UUID of the given entity.

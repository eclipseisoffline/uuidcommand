# UUID Command

![Modrinth Version](https://img.shields.io/modrinth/v/nKBHyafW?logo=modrinth&color=008800)
![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/nKBHyafW?logo=modrinth&color=008800)
![Modrinth Downloads](https://img.shields.io/modrinth/dt/nKBHyafW?logo=modrinth&color=008800)

This mod adds a simple `/uuid` command that shows the UUID of any entity currently loaded,
and allows you to copy the UUID string or the NBT data representation of the UUID.

Feel free to report any bugs, or suggest new features, at the issue tracker.

## License

This mod is licensed under GNU LGPLv3.

## Usage

Mod builds can be found [here](https://github.com/eclipseisoffline/uuidcommand/packages/2098177) and on [Modrinth](https://modrinth.com/mod/uuid-command).

This mod is currently available for Minecraft 1.21.2, 1.21+1 and 1.20.5+6/1.20.4 (no longer updated), Fabric loader 0.16.5 or above.
Version port requests can be made at the issue tracker. The Fabric API is required. 
This mod is both supported on the server and on the client:

- When installed on the server, any OP-ed players are able to use the `/uuid` command, even if they don't have the mod installed client side.
- When installed on the client, you can use the `/uuid` command in single- and multiplayer, even if you don't have OP permissions.
  - On multiplayer, the command is only able to fetch the UUIDs of entities in render distance and all players. 

Command syntax:

- `/uuid <entity>` - displays the UUID of the given entity.

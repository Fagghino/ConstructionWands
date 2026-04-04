# 🏗️ ConstructionWands

[![Version](https://img.shields.io/badge/version-1.7.0-blue.svg)]()
[![Minecraft](https://img.shields.io/badge/minecraft-1.20+-green.svg)](https://www.minecraft.net/)
[![License](https://img.shields.io/badge/license-MIT-yellow.svg)](LICENSE)
[![Spigot](https://img.shields.io/badge/Spigot-1.20+-orange.svg)](https://www.spigotmc.org/)

[![en](https://img.shields.io/badge/lang-en-red.svg)](README.md)
[![it](https://img.shields.io/badge/lang-it-green.svg)](MD/README.it.md)

> 📝 [Changelog](MD/CHANGELOG.en.md)

**ConstructionWands** is a Spigot/Paper plugin for custom construction wands with full support for SuperiorSkyblock2. Create wands to place blocks in configurable grid sizes with granular protection control.

## 📋 Description

ConstructionWands is a Minecraft plugin that lets you create custom wands to place blocks in grids. Supports advanced configuration for range, height, cooldown, and complete integration with SuperiorSkyblock2 for island protection management.

## ✨ Features

### 🪄 Customisable Wands
- **Configurable range**: Place blocks in grids from 1x1 up to NxN
- **Height (length)**: Control the thickness of the placed grid
- **Cooldown**: Prevent spam with customisable delays for each wand
- **Limited or infinite uses**: Configure consumable or permanent wands
- **Custom model data**: Support for custom texture packs
- **Non-stackable**: Each wand is unique thanks to unique UUIDs
- **Configurable left-click action**: Choose left-click function (NONE/UNDO/MODE)

### 🎯 Placement Modes
- **AUTO** (default): Direction based on clicked block face
- **VERTICAL**: Always vertical placement (Y axis), independent of clicked face
- **HORIZONTAL**: Always horizontal placement (XZ plane), independent of clicked face
- **Mode switching**: With left-click if `left-click-action: MODE` is configured
- **Persistence**: Mode saved for each player and each individual wand

### 🔄 Block Source
- **Offhand**: Takes blocks from offhand (classic behaviour)
- **Inventory**: Takes blocks from inventory matching clicked block type

### 🔒 Protections and Integrations
- **World Border**: Always respects world limits, even with admin bypass
- **SuperiorSkyblock2**: Complete integration with permission control
  - Owners and members can build on their islands
  - Coop players support
  - Custom permissions via `/is permission`
  - Admin bypass with `/is admin bypass` for staff
  - **Island block counting**: Placed blocks are correctly tracked for island level calculation
- **Other protection plugins**: Compatible with WorldGuard, GriefPrevention, etc. via BlockPlaceEvent

### ⚙️ Commands
- `/wand <wand_name>` - Get a wand for yourself
- `/wand <wand_name> <player_name>` - Give a wand to a player (also from console)
- `/wand <wand_name> <player_name> <quantity>` - Give multiple wands

### 🔑 Permissions
- `constructionwands.give` - Permission to execute /wand command (default: op)
- `constructionwands.use` - Permission to use wands (default: true)

## 📦 Requirements

- **Server**: Spigot/Paper 1.20.1+
- **Java**: 17+
- **Optional dependencies**:
  - SuperiorSkyblock2 (for island protection)

## 🚀 Installation

1. Download the latest plugin version
2. Copy the `.jar` file to the server's `plugins` folder
3. (Optional) Install SuperiorSkyblock2 for island support
4. Start/restart the server
5. Edit `config.yml` and `wands.yml` to customise wands

## ⚙️ Configuration

The plugin uses two separate configuration files:
- **`config.yml`** - General configuration (protected blocks, messages, undo timeout)
- **`wands.yml`** - Definitions of all available wands

### Example Wand (wands.yml)

```yaml
wands:
  iron_wand:
    name: "&fIron Wand"
    model-data: 1002
    lore:
      - "&7Range: 3x3x1"
      - "&7Right-click to place"
      - "&7Left-click to change mode"
      - "&eUSES: {uses}"
    range: 3           # Grid width/depth (1 = 1x1, 3 = 3x3, 5 = 5x5, etc.)
    length: 1          # Grid height (number of layers)
    delay: 500         # Cooldown in milliseconds (0 = no delay, 1000 = 1 second)
    source: offhand    # Where to get blocks from: "offhand" or "inventory"
    type: IRON_INGOT   # Item type of the wand
    uses: 100          # Available uses (-1 for infinite)
    infinite: false    # true = infinite uses
    left-click-action: MODE  # Left-click function: NONE, UNDO, or MODE
```

### Wand Parameters

- **name**: Display name (supports colour codes with `&`)
- **model-data**: ID for custom model data (texture pack)
- **lore**: Wand description (list of strings)
  - Use `{uses}` to show remaining uses
- **range**: Grid size (e.g. 3 = 3x3 grid)
- **length**: Height/thickness (e.g. 2 = two layers of blocks)
- **delay**: Cooldown in milliseconds to prevent spam
- **source**: 
  - `offhand` - Use blocks from offhand
  - `inventory` - Use blocks from inventory matching clicked block type
- **type**: Minecraft item type (BLAZE_ROD, STICK, etc.)
- **uses**: Number of uses (-1 for infinite)
- **infinite**: true/false for infinite uses
- **left-click-action**: Left-click function (default: NONE)
  - `NONE` - Left-click does nothing
  - `UNDO` - Undo last placement (if blocks unmodified)
  - `MODE` - Change placement mode (AUTO → VERTICAL → HORIZONTAL)

### Customisable Messages

```yaml
messages:
  no-permission: "&cYou don't have permission to use this command!"
  wand-given: "&aYou received a {wand}!"
  wand-not-found: "&cWand not found!"
  no-blocks: "&cYou don't have enough blocks in your offhand!"
  no-blocks-inventory: "&cYou don't have enough blocks in your inventory!"
  no-island-permission: "&cYou don't have permission to build on this island!"
  cooldown: "&cYou must wait before using the wand again!"
  uses-depleted: "&cThe wand has run out of uses!"
  blocked-block: "&cYou can't use the wand on this block type!"
  mode-auto: "&aPlacement mode: &eAUTO"
  mode-vertical: "&aPlacement mode: &eVERTICAL"
  mode-horizontal: "&aPlacement mode: &eHORIZONTAL"
```

### 🚫 Protected Blocks

You can configure a list of blocks on which wands **cannot work**, preventing placement even if the player has all necessary permissions.

```yaml
blocked-blocks:
  - SPAWNER
  - BEDROCK
  - BARRIER
  - COMMAND_BLOCK
  - CHAIN_COMMAND_BLOCK
  - REPEATING_COMMAND_BLOCK
  - STRUCTURE_BLOCK
  - JIGSAW
  - END_PORTAL_FRAME
```

**Features:**
- ❌ Blocks wand use when clicking on a block in the list
- ✅ Automatic validation: ignores invalid blocks in config
- 📝 Warning logs in console for unrecognised blocks
- 💬 Customisable message: `blocked-block`
- 🔒 Additional protection for critical server blocks

**Usage example:**
If you configure `SPAWNER` in the list and click with the wand on a spawner, you'll receive the message configured in `blocked-block` and the action will be cancelled.

## 🤝 SuperiorSkyblock2 Support

The plugin automatically integrates with SuperiorSkyblock2 if present on the server.

### Who can use wands on islands:
✅ Island owner  
✅ Team members  
✅ Coop players (with `/is coop <name>`)  
✅ Players with custom permissions (with `/is permission <name>` with BLOCK_PLACE privilege)  
✅ Staff with admin bypass active (`/is admin bypass`)  
❌ Visitors without permissions  

### Error Messages
- If you try to use the wand on an island where you don't have permissions, you'll receive the message configured in `no-island-permission`

## 👨‍💻 Project Structure
```
src/main/
├── java/com/franchino961/constructionwands/
│   ├── ConstructionWands.java          # Plugin main class
│   ├── commands/
│   │   └── WandCommand.java            # /wand command
│   ├── hooks/
│   │   └── Protections.java            # SS2 hook and protections
│   ├── listeners/
│   │   └── WandInteractListener.java   # Wand click event
│   ├── managers/
│   │   └── WandManager.java            # Wand management
│   └── models/
│       └── Wand.java                   # Wand data model
└── resources/
    ├── config.yml                       # General configuration
    ├── wands.yml                        # Wand definitions
    └── plugin.yml                       # Plugin metadata
```

## 📄 License

This project is released under the **MIT** license — see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Franchino961** — [GitHub](https://github.com/Franchino961-Plugins)

## 🤝 Contributing

Contributions are welcome!
- 🐛 Report bugs in [issues](../../issues)
- 💡 Propose new features
- 🔧 Submit Pull Requests

## 💬 Support

For bug reports, feature requests, or questions:
- Open an [issue](../../issues) on GitHub
- Contact the developer

## 🔗 Useful Links

- 📚 [Spigot API Documentation](https://hub.spigotmc.org/javadocs/spigot/)
- 🏙️ [SuperiorSkyblock2 Wiki](https://wiki.bg-software.com/superiorskyblock2/)

## 📝 Changelog

See [CHANGELOG.en.md](MD/CHANGELOG.en.md) for complete version history.

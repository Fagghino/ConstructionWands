# Changelog - ConstructionWands

All notable changes to the **ConstructionWands** plugin will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.7.0] - 2026-06-15

### Added
- **Placement Modes**: Configurable placement modes system — Three modes: `AUTO`, `VERTICAL`, `HORIZONTAL`.
- **BlockPlaceEvent**: Added `BlockPlaceEvent` dispatch for full SuperiorSkyblock2 compatibility.
- **Left Click Action**: Configurable `left-click-action` option (`NONE` / `UNDO` / `MODE`).
- **Mode Switch**: Mode switch on left click when `left-click-action: MODE`.
- **Mode Persistence**: Per-player and per-wand mode persistence.
- **Mode Messages**: Configurable messages for mode changes (`mode-auto`, `mode-vertical`, `mode-horizontal`).

### Changed
- **SuperiorSkyblock2**: Placed blocks now correctly counted for SS2 island level.
- **Left Click**: Left-click system unified with switch-case.
- **Placement Logic**: Placement direction calculation now based on selected mode.

### Fixed
- **SS2 Level**: Blocks placed with wand not counted in SS2 island level.
- **BlockPlaceEvent**: `BlockPlaceEvent` not triggered correctly.

### Removed
- **enable-undo**: `enable-undo` parameter removed (replaced by `left-click-action`).

---

## [1.6.0] - 2026-05-15

### Added
- **Separate Config Files**: Dedicated `wands.yml` file for wand definitions.
- **Auto-Loading**: Automatic `wands.yml` loading from resources.
- **API**: Added `loadWandsConfig()` method and `getWandsConfig()` getter.

### Changed
- **config.yml**: Now contains only general settings, separated from wand definitions.

---

## [1.5.0] - 2026-04-15

### Added
- **Per-Wand Undo**: Configurable undo system per wand via `enable-undo` option in `config.yml`.

---

## [1.4.0] - 2026-03-15

### Added
- **Protected Blocks**: Configurable protected blocks system via `blocked-blocks` option.
- **Validation**: Automatic block validation with error logging.
- **Messages**: Configurable `blocked-block` message on protected block click.

### Fixed
- **Security**: Prevented wand use on sensitive server blocks (spawners, bedrock, command blocks).

---

## [1.2.0] - 2026-02-15

### Added
- **SuperiorSkyblock2 Integration**: Full SuperiorSkyblock2 integration.
- **Coop Support**: Support for coop players and custom island permissions.
- **Admin Bypass**: Admin bypass support (`/is admin bypass`).
- **Build Permission**: Error message when lacking build permission on an island.
- **World Border**: World border check (always active, even with admin bypass).
- **Plugin Compatibility**: Compatibility with other protection plugins via `BlockPlaceEvent`.
- **Reflection System**: Reflection system for SS2 API version compatibility.
- **Protections Class**: Dedicated `Protections.java` class.

### Fixed
- **World Border**: Blocks placed outside world border with admin bypass.

---

## [1.1.0] - 2026-01-15

### Added
- **Block Source**: `source` option to choose where blocks are taken from (offhand/inventory).
- **Inventory Mode**: Place blocks of the same type as the clicked block.
- **Length Option**: `length` option to control grid height/thickness.
- **Cooldown**: Configurable cooldown system with `delay` option.
- **Anti-Spam**: Anti-spam protection — prevents double click within 100ms.
- **Unique UUIDs**: Unique UUIDs per wand (non-stackable).
- **Console Support**: Command supports player name and quantity from console.
- **Tab Completion**: Tab completion for online player names and suggested quantities.
- **Missing Blocks Message**: Custom message for missing inventory blocks.

### Fixed
- **Grid Placement**: Single block placed instead of full grid.
- **Cooldown Message**: Cooldown message on first click.
- **Double Click**: Double click processed as two separate actions.

---

## [1.0.0] - 2025-12-15

### Added
- **Initial Release**: First release of ConstructionWands.
- **Wand System**: Customisable wand system configurable via `config.yml`.
- **Range**: Configurable range for placement grids.
- **Use System**: Limited or unlimited use system.
- **Custom Model Data**: Custom model data support for texture packs.
- **Dynamic Lore**: Dynamic lore with `{uses}` placeholder.
- **Commands**: `/wand` command to obtain wands.
- **Permissions**: Permission system (give and use).
- **Block Validation**: Validity checks for placeable blocks.
- **Creative Support**: Creative mode support (infinite blocks).
- **Messages**: Configurable messages for errors and successes.
- **Tab Completion**: Tab completion for wand names.
- **Persistence**: Use persistence via `PersistentDataContainer`.

---

## Development Roadmap

### Phase 1 - Initial Release ✅
- Core wand system with configurable range and use limits.

### Phase 2 - Usability ✅
- Anti-spam, cooldown, inventory mode, tab completion.

### Phase 3 - Integrations ✅
- SuperiorSkyblock2 integration with permission and island support.

### Phase 4 - Configuration ✅
- Protected blocks, separate config files, per-wand undo.

### Phase 5 - Placement Modes ✅
- AUTO/VERTICAL/HORIZONTAL placement modes.

### Phase 6 - Advanced Features 📋
- Multi-block shape support.
- Undo history with multiple levels.
- Wand crafting recipes.

---

*Format: [Version] - Date*
*Categories: Added, Changed, Fixed, Removed*

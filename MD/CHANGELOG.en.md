# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.7.0] - Current — Configurable Placement Modes

### Added
- Configurable placement modes system (MAJOR)
- `BlockPlaceEvent` for full SuperiorSkyblock2 compatibility (MAJOR)
- Configurable `left-click-action` option (`NONE` / `UNDO` / `MODE`)
- Three placement modes: `AUTO`, `VERTICAL`, `HORIZONTAL`
- Mode switch with left click when `left-click-action: MODE`
- Per-player and per-wand mode persistence
- Configurable messages for mode changes (`mode-auto`, `mode-vertical`, `mode-horizontal`)

### Improved
- Placed blocks now trigger `BlockPlaceEvent` for SS2 and other plugins
- SuperiorSkyblock2 now correctly counts placed blocks for island level
- Left-click system unified with switch-case
- Placement direction calculation based on selected mode

### Fixed
- Blocks placed with wand not counted in SS2 island level
- `BlockPlaceEvent` not triggered correctly

### Removed
- `enable-undo` parameter (replaced by `left-click-action`)

## [1.6.0] — Separate Configuration Files

### Added
- Separate configuration files system (MAJOR)
- Dedicated `wands.yml` file for wand definitions
- Automatic `wands.yml` loading from resources
- `loadWandsConfig()` method and `getWandsConfig()` getter

### Improved
- Separation of general config and wand definitions
- `config.yml` now contains only general settings

## [1.5.0] — Configurable Undo per Wand

### Added
- Configurable undo system per wand (MAJOR)
- `enable-undo` option per wand in `config.yml`

## [1.4.0] — Protected Blocks System

### Added
- Configurable protected blocks system
- `blocked-blocks` option in `config.yml`
- Automatic block validation with error logging
- Configurable `blocked-block` message on protected block click

### Fixed
- Ability to use wands on sensitive server blocks (spawners, bedrock, command blocks)

## [1.2.0] — SuperiorSkyblock2 Integration

### Added
- Full SuperiorSkyblock2 integration
- Support for coop players and custom island permissions
- Admin bypass support (`/is admin bypass`)
- Error message when lacking build permission on an island
- World border check (always active, even with admin bypass)
- Compatibility with other protection plugins via `BlockPlaceEvent`
- Reflection system for SS2 API version compatibility
- Dedicated `Protections.java` class

### Fixed
- Blocks placed outside world border with admin bypass

## [1.1.0]

### Added
- `source` option to choose where blocks are taken from (offhand/inventory)
- Inventory mode: place blocks of the same type as the clicked block
- `length` option to control grid height/thickness
- Configurable cooldown system with `delay` option
- Anti-spam protection: prevents double click within 100ms
- Unique UUIDs per wand (non-stackable)
- Command supports player name and quantity (also from console)
- Tab completion for online player names and suggested quantities
- Custom message for missing inventory blocks

### Fixed
- Single block placed instead of full grid
- Cooldown message on first click
- Double click processed as two separate actions

## [1.0.0] — Initial Release

### Added
- Customisable wand system
- Configuration via `config.yml`
- Configurable range for placement grids
- Limited or unlimited use system
- Custom model data for texture packs
- Dynamic lore with `{uses}` placeholder
- `/wand` command to obtain wands
- Permission system (give and use)
- Validity checks for placeable blocks
- Creative mode support (infinite blocks)
- Configurable messages for errors and successes
- Tab completion for wand names
- Use persistence via `PersistentDataContainer`

## [Unreleased]

### Planned
- Multi-block shape support
- Undo history with multiple levels
- Wand crafting recipes

---

## Version History

### How to Read Version Numbers
- **Major.Minor.Patch** (e.g., 1.7.0)
  - **Major**: Breaking changes or major feature additions
  - **Minor**: New features, backward compatible
  - **Patch**: Bug fixes and small improvements

[1.7.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.7.0
[1.6.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.6.0
[1.5.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.5.0
[1.4.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.4.0
[1.2.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.2.0
[1.1.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.1.0
[1.0.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.0.0

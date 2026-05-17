# TerraFirmaCraft: Time Flies

**Read this in other languages: [简体中文](README_CN.md)**

Time In A Bottle integration for TerraFirmaCraft and Firmalife.  

TerraFirmaCraft and Firmalife use TFC calendar timestamps for many long-running processes. Because of that, Time In A Bottle's normal extra block ticks do not always speed them up. This mod adds timestamp-aware acceleration while preserving vanilla-style and machine ticker behavior.

## Features

- ✅ Makes Time In A Bottle work with TFC timestamp-based processes
- ✅ Supports TFC crops, saplings, fruit trees, berry bushes, composters, barrels, pit kilns, bloomeries, and similar blocks
- ✅ Supports Firmalife greenhouse planters, grapes, drying/smoking blocks, compost tumblers, jarbnets, beehives, and related timestamp processes
- ✅ Keeps normal block entity ticking for machines that already use vanilla-style progress
- ✅ Does not modify Time In A Bottle, TerraFirmaCraft, or Firmalife directly
- ✅ Server-side focused; no new blocks, items, or recipes

## Requirements

- Minecraft 1.20.1
- Forge 47.1.3+
- TerraFirmaCraft
- Time In A Bottle 4.0.0+
- Firmalife is optional

## Installation

1. Put this mod into the `mods` folder.
2. Make sure TerraFirmaCraft and Time In A Bottle are installed.
3. Install Firmalife if you want Firmalife integration.
4. Start the game.

## How It Works

Time In A Bottle creates a temporary acceleration entity on the clicked block. Normally, that entity repeatedly calls the target block entity ticker, or triggers random ticks for random-ticking blocks.

This is not enough for many TFC systems because they compare the current TFC calendar tick against a saved timestamp. This mod intercepts the Time In A Bottle acceleration entity when the target belongs to `tfc` or `firmalife`, then:

- advances the target's relevant timestamp state locally;
- still runs the target block entity ticker when present;
- still performs random tick attempts for random-ticking blocks;
- aligns `ICalendarTickable` machines so repeated extra ticks do not create invalid negative calendar deltas.

The intent is to speed up timestamp-driven TFC behavior without breaking machines that already use normal ticking.

## Configuration

A server config is generated at:

```text
config/tfc_tiab-server.toml
```

Available option:

- `averageRandomTickInterval`: Average extra ticks between random tick attempts during acceleration. The default is `1365`, matching Time In A Bottle's default random tick estimate.

## Compatibility Notes

This mod targets the Forge 1.20.1 versions of TerraFirmaCraft, Firmalife, and Time In A Bottle.

Covered process types include:

- `TickCounterBlockEntity`-based blocks such as TFC saplings and time-counting placed blocks
- `CropBlockEntity` growth timestamps
- berry bush update timestamps
- selected TFC machine/process timestamps such as barrel, bloomery, pit kiln, and composter
- selected Firmalife timestamps such as large planters, grape plants, drying/smoking start ticks, compost tumblers, jarbnets, and beehives

## Building

Use the included Gradle wrapper:

```bash
./gradlew build
```

The built jar will be in:

```text
build/libs/
```

## License

MIT License.

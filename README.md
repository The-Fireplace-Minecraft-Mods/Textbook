# Textbook — NeoForge port

Unofficial NeoForge port of [Textbook](https://github.com/The-Fireplace-Minecraft-Mods/Textbook) by The_Fireplace.

## What it does

Import text from `.txt` files or the clipboard into writable books, and export
written books to `.txt` files. Supports splitting long imports across multiple
100-page volumes, with the title automatically suffixed `-01`, `-02`, etc.

## Why this port exists

The original Textbook depends on **Fireplace Lib** and **Annotated DI**, both
Fabric-only libraries. There is no NeoForge build, so NeoForge users had no way
to use Textbook. This port reimplements the dependency surface using NeoForge's
native APIs and removes both libraries entirely.

## Compatibility

- Minecraft 1.21.1
- NeoForge 21.1.x (tested on 21.1.249)
- Java 21
- Client-side only

## Installation

Drop the JAR into your `mods/` folder. No additional dependencies required.

## Configuration

One client-side config option, `pageByteLimit`, available via the Mods menu → Config:
- `0` = no limit (vanilla 1024 chars / 128 lines per page applies)
- Any positive integer = max UTF-8 bytes per page (useful on Paper servers that
  enforce byte limits)

## Building from source

```bash
./gradlew :build
```

The JAR will appear in `build/libs/`.

## Credits

- Original mod: **The_Fireplace** — https://github.com/The-Fireplace-Minecraft-Mods/Textbook
- NeoForge port: this repository's contributors

## License

Mozilla Public License 2.0 (MPL-2.0). See [LICENSE](LICENSE) and [NOTICE.md](NOTICE.md).

This project is not affiliated with or endorsed by The_Fireplace.

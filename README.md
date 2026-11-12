# Random Gun Drops

A Forge 1.20.1 mod for **TACZ (Timeless and Classics Zero)**. Breaking a block drops a random TACZ gun instead of the
block's normal items. It reads TACZ's own gun list, so guns from the base game and **every installed TACZ addon gunpack**
(like Omega Guns) are included.

## Press M for the menu
- Turn the mod on or off
- Same gun per block type, or a new random gun every break
- Drop chance slider (0 to 100%)
- Also keep the normal drops
- Reroll which gun each block gives

Changing settings needs operator permission on a server. In single player you can always change them. You can rebind
the key under Options > Controls > Random Gun Drops.

## Install
Put `random_gun_drops-1.0.0.jar` in `mods/` with Forge 1.20.1 (47.3.0 or newer) and TACZ.

## Build
Push to GitHub and open Actions > Build mod, then download `random-gun-drops-mod`. Or run `gradle build` with JDK 17.

## Known limits
- Guns drop with just their gun ID. If a gun can't fire until you switch fire mode, press the fire-mode key (default B).
- Chests, mobs and other non-block loot are not changed.
- On a dedicated server the menu shows the client's copy of the settings, not the server's.
- Settings are also in `config/random_gun_drops-common.toml`.

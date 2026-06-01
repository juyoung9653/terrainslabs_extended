# Terrain Slabs Extended

A Fabric Minecraft mod extending the [terrain slabs](https://modrinth.com/mod/terrain-slabs) concept.
Adds a custom grass block (`grass_block2`) that naturally spreads to dirt blocks beneath nearby `grass_slab`, creating seamless terrain transitions.

## Features

- **Grass Block 2** — A custom grass block that pairs with `terrainslabs:grass_slab`.
  - Renders snowy when a snowy `grass_slab` sits directly above.
  - If the slab above is removed, reverts to vanilla grass block via random tick.
- **Auto-Conversion** — On chunk generation, any dirt block under a `grass_slab` is replaced with `grass_block2`.
- **Compatibility** — Works alongside the base Terrain Slabs mod; respects slab snowiness.

## Requirements

- Minecraft 1.21.11
- Fabric Loader >=0.19.2
- Fabric API
- [Terrain Slabs](https://modrinth.com/mod/terrain-slabs) (the base mod)

## License

CC0 1.0 Universal — see [LICENSE](LICENSE).

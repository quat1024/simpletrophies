# simpletrophies
Simple trophies mod for modpack makers

## Usage

Give your players a `simple_trophies:trophy` item with special NBT.

- `TrophyName`: a string. Determines the name of the trophy item. Will change the item name and also the name that is displayed when you hover over the trophy block.
- `TrophyItem`: an item stack. Determines the item that is rotating on the trophy. Will show on the tooltip.
- `TrophyColorRed`, `TrophyColorGreen`, `TrophyColorBlue`: an integer 0 - 255, determines the tint of the inner ring of the trophy.

If you are in Creative mode, you can also create these trophies in-game without resorting to NBT hacking, if you prefer.

- Give yourself a trophy
- Right click it with an item to set the item
- Rename it in an anvil to change the name (when you pull it out, it uses Minecraft's standard anvil naming format instead of my custom one, but it will get changed as soon as you bring it in to your inventory)
- Dye it like leather armor to change the color TODO actually implement that lol.

These functions are not available outside of Creative mode, so no need to worry about your players screwing up their hard earned trophies.
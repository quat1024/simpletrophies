# simpletrophies
Simple trophies mod for modpack makers

## Usage

Give your players a `simple_trophies:trophy` item with special NBT.

- `TrophyName`: a string. Determines the name of the trophy item. Will change the item name and also the name that is displayed when you hover over the trophy block.
- `TrophyItem`: an item stack. Determines the item that is rotating on the trophy. Will show on the tooltip.
- `TrophyColor`: an integer in 0xRRGGBB format, determines the tint of the inner ring of the trophy.

TODO: don't make trophycolor a packed int lol that's not user friendly

# Simple Trophies

Simple Trophies adds one block, and if you guess what it is correctly first try I'll give you a hundred bucks

<img src="https://raw.githubusercontent.com/quat1024/simpletrophies/master/.github/trophoo.png"/>

## Features

- Trophies!
- They display a rotating item (which cannot be retrieved by players)
- Can optionally have a custom color!
- And custom name!
- The name will show up when you hover over a placed trophy!

## Not features

- No methods are added for actually *obtaining* these trophies. That's on you.
- Definitely not a rip off of Age of Engineering's trophies. No sir.

<img src="https://raw.githubusercontent.com/quat1024/simpletrophies/master/.github/topee.png"/>

## Usage

### Illegal hacker NBT

Give your players a `simple_trophies:trophy` item with special NBT. All values are optional; if not specified, they will be created (which helps avoid problems caused by placing/breaking a trophy causing it to not stack with other trophies etc etc)

- `TrophyName`: a string. Determines the name of the trophy item. Will change the item's name, and also the name that is displayed when you hover over the trophy block. Translation keys are acceptable values - in fact, all names passed in are treated as localization keys. Defaults to an empty string, which causes the name to display as "Trophy" and no in-world tooltip to appear.
- `TrophyItem`: an item stack. Determines the item that is rotating on the trophy. Will show on the tooltip. Defaults to air.
- `TrophyColorRed`, `TrophyColorGreen`, `TrophyColorBlue`: an integer 0 - 255, determines the tint of the inner ring of the trophy. All default to 255 which makes white.
- `TrophyVariant`: a string. Determines the look of the trophy. There's a few to choose from:
  * "classic": The one you see above. Designed by yours truly. This is the default one.
  * "neon": A pulsating grey look. Designed by 0x00FF00.
  * "gold": A shiny, smooth gold look. Designed by 0x00FF00.

As an example, here's how I could use an NBT tag to give myself that diamond axe trophy. Newlines added for clarity.

    /give @p simple_trophies:trophy 1 0 {
      TrophyItem:{id:"minecraft:diamond_axe",Count:1b,Damage:0s},
      TrophyName:"Cut th tree yes haha",
      TrophyColorRed:179,
      TrophyColorGreen:49,
      TrophyColorBlue:44,
      TrophyVariant:"classic"
    }

### Creative mode magic fairy stuff

Only if you are in Creative mode, you can create these trophies in-game without resorting to NBT hacking, if you prefer.

- Right click with a (vanilla) dye to set the color.
  - Holding dye in your mainhand *and* offhand at the same time, will set the color to the RGB average of the two.
- Right click with any other item to set the item to whatever you're holding.
  - Empty hand will clear it.
  - Yeah this means you can't make a trophy of a vanilla dye without NBT hacking. Deal with it.
- You can pick-block the trophy (or just break it, it will drop, even in creative) to get an item form. No need to hold CTRL when you pick block.
- Rename a trophy in an anvil to change the name.
  - Since you can't *remove* a custom name with an anvil, the special anvil name `<CLEAR>` has been special cased for that purpose.
  - NB: when you pull the item out, it uses Minecraft's standard anvil naming format (italic) instead of the cool custom one (not italic), but it will get changed as soon as you bring it in to your inventory. The stupid anvil tags will get erased too.

Once you are happy with your trophy item, right-click in the air with it. This will dump its NBT tag into the log and also copy it to your clipboard.

*None of these functions are available outside of Creative mode* so no need to worry about your players screwing up their hard earned trophies lol.

## Wtf dude it looks like GAR-BOGE!!!!!! pls fix kthx!!!!!!!!!!!!!!!

It's just a plain ol block model, so feel free to remodel it however you'd like. Anything with a `tintindex` of 0 will be tinted the custom color. How neat is that.

- Tiny brain: Retexture it and load your texture with Resource Loader.
- Medium brain: Remodel it and load your new model with Resource Loader.
- Galaxy brain: Pull request your model.

## Boring legal shit noone cares about except free software zealots

> This Source Code Form is subject to the terms of the Mozilla Public
> License, v. 2.0. If a copy of the MPL was not distributed with this
> file, You can obtain one at http://mozilla.org/MPL/2.0/.

Tl;dr Do whatever you want, use it, share it, update it, repost it, make money off of it, etc etc etc. All I care about is that you keep it free software.
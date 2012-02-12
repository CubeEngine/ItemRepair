ItemRepair
==========

Description:
------------
ItemRepair adds repair blocks to minecraft, which are used to repair your items for a dynamic price.
The price depends on how damaged the item is, how many items you want to repair and on the enchantment
level of the item. Enchanted items get exponential more expensive based on a formlar.
The base price per repaired damage and the parameters of the enchantment formular are all
configurable. The repair blocks are configurable as well.
The plugin depends an on iConomy 6, newer version **may** work

Features:
---------
- different repair blocks:
    - single item repair: repairs the item in your hand (default: iron block)
    - complete repair: repairs **all** your items (default: gold block)
    - cheap repair: repairs the item in your hand cheaper, but the itme may break (chances configurable, default: diamond block)
- uses iConomy
- dynamic pricing

Permissions:
------------
- itemrepair.allblocks - *Allows the player to use all blocks*
    - itemrepair.block.singleRepair - *Allows the player to use single repair blocks*
    - itemrepair.block.completeRepair - *Allows the player to use complete repair blocks*
    - itemrepair.block.cheapRepair - *Allows the player to use cheap repair blocks*
- itemrepair.commands.\* - *Allows the player to use all commands*
    - itemrepair.commands.add - *Allows the player to add repair blocks*
    - itemrepair.commands.remove - *Allows the player to remove repair blocks*

***[Source on Github](https://github.com/quickwango/ItemRepair)***

Plugin developed by Quick_Wango - [Parallel Universe](http://parallel-universe.de)

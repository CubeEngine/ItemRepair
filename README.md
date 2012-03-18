ItemRepair
==========

Description:
------------
ItemRepair adds repair blocks to minecraft, which are used to repair your items for a dynamic price.
The price depends on how damaged the item is, how many items you want to repair and on the enchantment
level of the item. Enchanted items get exponential more expensive based on a formlar.
The base price per repaired damage and the parameters of the enchantment formular are all
configurable. The repair blocks are configurable as well.

Supports [CraftBukkitUpToDate](http://dev.bukkit.org/server-mods/craftbukkituptodate/)

Features:
---------
- different repair blocks:
    - single item repair: repairs the item in your hand (default: iron block)
    - complete repair: repairs **all** your items (default: gold block)
    - cheap repair: repairs the item in your hand cheaper, but the itme may break (chances configurable, default: diamond block)
- uses [Vault](http://dev.bukkit.org/server-mods/Vault/)
- dynamic pricing

Commands:
---------
- **/itemrepair add** -- registers a block as a repair block
- **/itemrepair remove** -- unregisters a repair block

Installation:
-------------
- put the ItemRepair.jar into you plugins folder
- make sure Vault is installed
- restart/reload your server
- The configuration file will be generated/updated as soon as the plugin gets enabled

Permissions:
------------
- **itemrepair.allblocks** -- Allows the player to use all blocks
    - **itemrepair.block.singleRepair** -- Allows the player to use single repair blocks
    - **itemrepair.block.completeRepair** -- Allows the player to use complete repair blocks
    - **itemrepair.block.cheapRepair** -- Allows the player to use cheap repair blocks
- **itemrepair.commands.\*** -- Allows the player to use all commands
    - **itemrepair.commands.add** -- Allows the player to add repair blocks
    - **itemrepair.commands.remove** -- Allows the player to remove repair blocks

***[Source on Github](https://github.com/quickwango/ItemRepair)***

Plugin developed by Quick_Wango - [Parallel Universe](http://parallel-universe.de)


***IMPORTANT***
===============

I'm searching for e Java developer who continues this plugin! Please contact me if you're interessted.

***README***
============

Plugin developed by Quick_Wango - [Parallel Universe](http://parallel-universe.de)

- You want new features?
- You want the plugin to be always up to date?
- You want good support?

I'm doing this for literally nothing in my freetime, so keep me interessted in my plugins and help pay my bills by simply donating a few bucks.

[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif "Donate")](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=2QU7NLJW3W58A)

Thanks in advance!

***[Talk to the developer](http://webchat.esper.net/?channels=code-infection&nick=)*** (#code-infection on EsperNet)

***[Source on Github](https://github.com/quickwango/ItemRepair)***
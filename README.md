ItemRepair
==========

Description:
------------
ItemRepair adds repair blocks to minecraft, which are used to repair your items for a dynamic price.
The price depends on how damaged the item is, what the item is made of, how many items you want to
repair and on the enchantment level of the item. Enchanted items get exponential more expensive based
on a formlar.
The base price of the materials and the parameters of the enchantment formular are all
configurable. The repair blocks are configurable as well.

Supports [CraftBukkitUpToDate](http://dev.bukkit.org/server-mods/craftbukkituptodate/)

How it works:
-------------
After someone added a repair block via command (see below) you right-click
a block. This opens an inventory where you can place your damaged items.
When all items are in there close the inventory and left-click the block.
After that, the repair request message appears in your chat which tells you
how much the repair will cost and how much you have. Left-clicking the block
again will confirm the request, any other interaction or disconnecting will
cancel it.

if the administrator specified a server bank or player, the money you paid
will be transfered to that.

Features:
---------
- different repair blocks:
    - normal repair: repairs the items for the normal price (default: iron block)
    - cheap repair: repairs the items for a cheaper price, but some items may break (chances configurable, default: diamond block)
- uses [Vault](http://dev.bukkit.org/server-mods/Vault/)
- dynamic pricing

Commands:
---------
- **/itemrepair add** -- registers a block as a repair block
- **/itemrepair remove** -- unregisters a repair block
- **/itemrepair reload** -- reloads the plugin

Installation:
-------------
- put the ItemRepair.jar into you plugins folder
- make sure Vault is installed
- restart/reload your server
- The configuration file will be generated/updated as soon as the plugin gets enabled
- it is recommended to remove your old config.yml when you're updating from 1.x

Permissions:
------------
- **itemrepair.allblocks** -- Allows the player to use all blocks
    - **itemrepair.block.normal** -- Allows the player to use normal repair blocks
    - **itemrepair.block.cheap** -- Allows the player to use cheap repair blocks
- **itemrepair.commands.\*** -- Allows the player to use all commands
    - **itemrepair.commands.add** -- Allows the player to add repair blocks
    - **itemrepair.commands.remove** -- Allows the player to remove repair blocks

***IMPORTANT***
===============

I'm searching for a Java developer who continues this plugin! Please contact me if you're interested.

***README***
============

Plugin developed by Quick_Wango - [Cube Island](http://cubeisland.de)

- You want new features?
- You want the plugin to be always up to date?
- You want good support?

I'm doing this for literally nothing in my freetime, so keep me interessted in my plugins and help pay my bills by simply donating a few bucks.

[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif "Donate")](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=2QU7NLJW3W58A)

Thanks in advance!

***[Talk to the developers](http://webchat.esper.net/?channels=cubeisland-dev&nick=)*** (#cubeisland-dev on EsperNet)

***[Source on Github](https://github.com/CubeIsland/ItemRepair)***
# SkyX Mystery Box Plugin
Devloped by ✨ | Sky X Network | ✨  
-
[![Discord](https://badgen.net/badge/icon/discord?icon=discord&label)](https://discord.gg/pTErYjTh5h)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-no-red.svg)](https://bitbucket.org/lbesson/ansi-colors)
[![Website](https://img.shields.io/website-up-down-green-red/http/shields.io.svg)](https://skyxnetwork.net)
# Overview  
**A custom Minecraft plugin that allows players to consume special Mystery Boxes to receive random rewards, including items and executed commands.**  
Designed for both Java and Bedrock servers, this plugin offers an engaging way to reward players with unique loot or triggers.  

## 🎮 Features  
  - ✅ **Right-click to open a Mystery Box.**  
  - 🔄 **Randomized loot system:** Rewards players with items or executes commands.  
  - ⚙️ **Customizable via** ``config.yml``: Add unlimited custom Mystery Boxes.  
  - 🎨 **Support for custom items:** Use custom heads with unique textures (base64 encoded).  
  - 🔑 **No crates required:** The box is a consumable item (can be a head or any item).  
  - ⚙️ **Rewards can be items or commands:** Specify both item rewards and command executions.  
  - ⚙️ **Fully configurable loot chances:** Set chances for common, rare, and legendary rewards.  
  - 💥 **Visual and sound effects** when opening the Mystery Box.  

## ⚙️ Installation  
  1. Download the latest version of the plugin.  
  2. Place the ``.jar`` file into the ``/plugins/`` directory of your server.  
  3. Restart your server to enable the plugin.  
  4. Configure your custom Mystery Boxes in the ``config.yml``.  

## 📋 Configuration  
The plugin is fully configurable via the ``config.yml`` file, allowing you to define as many custom Mystery Boxes as you like.  
  
Example ``config.yml``:  
```
mystery_boxes:
  basic_box:
    name: "&aBasic Mystery Box"
    material: PLAYER_HEAD
    texture: "base64_texture_code_here_just_a_minecraft_head_link" #example texture: "http://textures.minecraft.net/texture/cc23ae30848b1b58b75942f40ae8560c23be82dc9b3a777a0d3d63f4164cca68" (Source : https://minecraft-heads.com)
    lore:
      - "&7Right-click to open!"
      - "&fContains common rewards."
      - "&6★ &eWorth: &a11.99k"
    sound: "minecraft:entity.player.levelup"
    rewards:
      common:
        chance: 50
        items:
          - "IRON_INGOT:10"
          - "GOLD_INGOT:5"
        command:
          - "give %player% diamond 1"
        message:
          - "tell %player% &7★ &fYou have received a &aCommon &freward! &7★"
      rare:
        chance: 30
        items:
          - "DIAMOND:2"
        command:
          - "give %player% minecraft:diamond_sword 1"
        message:
          - "tell %player% &7★ &fYou have received a &aRare &freward! &7★"
      epic:
        chance: 15
        items:
          - "DIAMOND:2"
          - "ANCIENT_DEBRIS:4"
        command:
          - "give %player% minecraft:netherite_sword 1"
          - "give %player% minecraft:netherite_helmet 1"
        message:
          - "tell %player% &7★ &fYou have received a &aEpic &freward! &7★"
      legendary:
        chance: 5
        items:
          - "DIAMOND_BLOCK:10"
          - "NETHERITE_INGOT:1"
        command:
          - "give %player% minecraft:shulker_box 1"
          - "eco add %player% 20000"
        give:
          legendary_crown_of_immortality:
            material: NETHERITE_HELMET
            amount: 1
            custom_model_data: 00001
            display:
              name: '§6✪ Legendary Crown of Immortality'
              italic: false
              lore:
                - '§eWorn by the eternal kings.'
                - '§6Grants near-invincibility to its bearer.'
            enchantments:
              - id: PROTECTION
                level: 5
              - id: UNBREAKING
                level: 4
              - id: MENDING
                level: 1
              - id: RESPIRATION
                level: 3
            tags:
              Unbreakable: true
              HideFlags: 4
        message:
          - "tell %player% &7★ &fYou have received a &aLegendary &freward! &7★"
```
### Key Configuration Options:  
  - name: Name of the Mystery Box (can be customized).  
  - material: The item that represents the box (e.g., PLAYER_HEAD).  
  - texture: Base64-encoded texture for the custom head.  
  - lore: Description shown when viewing the Mystery Box.  
  - rewards: Configurable rewards with chances:  
      - items: List of items to give, can include quantity.  
      - command: List of commands to execute when opening the box.
  
## 🎁 Example of Random Loot  
  - **Common Reward:** 70% chance to get 10 Iron Ingots or 5 Gold Ingots, plus a message or a custom item like a helmet.  
  - **Rare Reward:** 30% chance to get 2 Diamonds and a special item (like a custom sword).  
  - **Epic Reward:** 10% chance to get 1 Netherite Ingot and a Netherite Sword with a server-wide announcement.  
 
## 🛠️ Commands  
  - ``/givemysterybox <player> <box_id>``  
    Give a specific Mystery Box to a player (e.g., ``basic_box``, ``epic_box``).  
    
## 🛡️ Permissions  
  - **mysterybox.use**: Allow a player to consume the Mystery Box (right-click to open).  
  - **mysterybox.give**: Allow a player (or staff) to give Mystery Boxes to others.
    
## 🚀 How it Works  
  1. **Creating Custom Mystery Boxes**:  
    You can easily add new Mystery Boxes to the ``config.yml`` file. Each box can have its own name, texture, lore, and reward configuration.  

  2. **Consuming the Mystery Box**:  
    When a player right-clicks the Mystery Box in their inventory (e.g., a custom head), it is consumed and gives the player a random reward based on the configured chances.  

  3. **Rewards**:  
    The rewards can be a mix of items and executed commands. The plugin gives you full control over how you want to reward players whether through physical loot or custom commands (e.g., give items, announce rewards in chat).  
## 📝 Customization  
This plugin gives you complete freedom to:  

  - Customize the appearance of the Mystery Box (via textures).  
  - Define loot chances and what rewards players receive.  
  - Execute custom commands to modify the server experience when opening boxes.  

You can adjust everything in ``config.yml`` without modifying any code, allowing you to easily add more boxes or change their content.  

## 🔗 Links & Resources  

  - **GitHub Repository**: [SkyX Mystery Box Plugin](https://github.com/XPaladiumyX/SkyX-Mystery-Box)
  - **Plugin Wiki**: [Wiki](https://github.com/XPaladiumyX/SkyX-Mystery-Box/wiki)  
  - **Discord**: Join our community for support and discussions!  
      - [Join Discord Server](https://discord.gg/pTErYjTh5h)
        
## 🎉 Credits & Acknowledgments  

  - Plugin developed by [SkyXNetwork](https://skyxnetwork.net/)
  - Base64 textures generated using [Texture URL Generator](https://www.base64-image.de/)

package skyxnetwork.skyXMysteryBox;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class SkyXMysteryBox extends JavaPlugin implements Listener {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.config = getConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("SkyX Mystery Box plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkyX Mystery Box plugin disabled!");
    }

    // Commande givemysterybox <player> <box_id>
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("givemysterybox")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /givemysterybox <player> <box_id>");
                return false;
            }

            String targetPlayerName = args[0];
            String boxId = args[1];

            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                sender.sendMessage("Player " + targetPlayerName + " is not online.");
                return false;
            }

            if (config.contains("mystery_boxes." + boxId)) {
                String boxName = config.getString("mystery_boxes." + boxId + ".name");
                String materialName = config.getString("mystery_boxes." + boxId + ".material", "PLAYER_HEAD");
                String textureUrl = config.getString("mystery_boxes." + boxId + ".texture");

                Material material = Material.matchMaterial(materialName);
                if (material == null) material = Material.PLAYER_HEAD;

                ItemStack boxItem = new ItemStack(material, 1);
                ItemMeta boxMeta = boxItem.getItemMeta();

                if (boxMeta != null) {
                    boxMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', boxName));
                    List<String> lore = config.getStringList("mystery_boxes." + boxId + ".lore");
                    List<String> coloredLore = new ArrayList<>();
                    for (String line : lore) {
                        coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
                    }
                    boxMeta.setLore(coloredLore);
                    boxMeta.getPersistentDataContainer().set(new NamespacedKey(this, "unrenamable"), PersistentDataType.BYTE, (byte) 1);
                    boxItem.setItemMeta(boxMeta);
                }

                if (material == Material.PLAYER_HEAD && textureUrl != null && !textureUrl.isEmpty()) {
                    String base64Texture = TextureUtils.getBase64FromURL(textureUrl);
                    applyTextureToItem(boxItem, base64Texture, textureUrl);
                }

                targetPlayer.getInventory().addItem(boxItem);
                sender.sendMessage("Gave " + targetPlayerName + " a " + boxName + "!");
            } else {
                sender.sendMessage("Box ID '" + boxId + "' not found in the config.");
            }
            return true;
        }

        // Correction : vérification de la commande /skyxmysterybox
        if (command.getName().equalsIgnoreCase("skyxmysterybox") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("skyxmysterybox.reload")) {
                reloadConfig();
                config = getConfig();
                sender.sendMessage("Config reloaded successfully!");
            } else {
                sender.sendMessage("You do not have permission to execute this command.");
            }
            return true;  // Important : retourne true pour confirmer que la commande a été traitée
        }

        return false; // Retour par défaut si aucune commande ne correspond
    }

    public static class TextureUtils {
        public static String getBase64FromURL(String textureUrl) {
            String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + textureUrl + "\"}}}";
            return Base64.getEncoder().encodeToString(json.getBytes());
        }
    }

    public static void applyTextureToItem(ItemStack item, String base64Texture, String textureUrl) {
        if (item.getType() != Material.PLAYER_HEAD) return;

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            UUID textureUUID = UUID.nameUUIDFromBytes(textureUrl.getBytes());
            com.mojang.authlib.GameProfile profile = new com.mojang.authlib.GameProfile(textureUUID, null);
            profile.getProperties().put("textures", new com.mojang.authlib.properties.Property("textures", base64Texture));

            try {
                java.lang.reflect.Field field = skullMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(skullMeta, profile);
                item.setItemMeta(skullMeta);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && event.getAction().toString().contains("RIGHT_CLICK") && item.getAmount() > 0) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String itemName = meta.getDisplayName();

                for (String boxId : config.getConfigurationSection("mystery_boxes").getKeys(false)) {
                    if (itemName.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', config.getString("mystery_boxes." + boxId + ".name")))) {
                        event.setCancelled(true);
                        consumeMysteryBox(player, item, boxId);
                        return;
                    }
                }
            }
        }
    }

    private void consumeMysteryBox(Player player, ItemStack item, String boxId) {
        item.setAmount(item.getAmount() - 1);
        giveReward(player, boxId);
    }

    private void giveReward(Player player, String boxId) {
        Random rand = new Random();
        int commonChance = config.getInt("mystery_boxes." + boxId + ".rewards.common.chance");
        int rareChance = config.getInt("mystery_boxes." + boxId + ".rewards.rare.chance");
        int totalChance = commonChance + rareChance;

        int roll = rand.nextInt(totalChance);

        if (roll < commonChance) {
            distributeRewards(player, boxId, "common");
        } else {
            distributeRewards(player, boxId, "rare");
        }
    }

    private void distributeRewards(Player player, String boxId, String rewardType) {
        List<String> items = config.getStringList("mystery_boxes." + boxId + ".rewards." + rewardType + ".items");
        for (String item : items) {
            String[] itemData = item.split(":");
            Material material = Material.valueOf(itemData[0]);
            int amount = Integer.parseInt(itemData[1]);
            player.getInventory().addItem(new ItemStack(material, amount));
        }

        List<String> commands = config.getStringList("mystery_boxes." + boxId + ".rewards." + rewardType + ".command");
        executeCommands(player, commands);
    }

    private void executeCommands(Player player, List<String> commands) {
        for (String command : commands) {
            command = command.replace("%player%", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    @EventHandler
    public void onAnvilRename(PrepareAnvilEvent event) {
        ItemStack item = event.getResult();
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.getPersistentDataContainer().has(new NamespacedKey(this, "unrenamable"), PersistentDataType.BYTE)) {
                event.setResult(null);
                event.getInventory().setRepairCost(0);
            }
        }
    }
}
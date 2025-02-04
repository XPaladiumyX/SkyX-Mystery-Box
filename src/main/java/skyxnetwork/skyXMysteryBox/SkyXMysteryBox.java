package skyxnetwork.skyXMysteryBox;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
    private static final String ANSI_MAGENTA = "\u001B[35m";
    private static final String ANSI_LIGHT_GRAY = "\u001B[37m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_LIGHT_GREEN = "\u001B[92m";
    private static final String ANSI_RED = "\u001B[31m";

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.config = getConfig();
        Bukkit.getLogger().info(ANSI_LIGHT_GRAY + "︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹");
        Bukkit.getLogger().info(ANSI_MAGENTA + " _______  ___   _  __   __    __   __    __    _  _______  _______ " + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + "|       ||   | | ||  | |  |  |  |_|  |  |  |  | ||       ||       |" + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + "|  _____||   |_| ||  |_|  |  |       |  |   |_| ||    ___||_     _|" + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + "| |_____ |      _||       |  |       |  |       ||   |___   |   |  " + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + "|_____  ||     |_ |_     _|   |     |   |  _    ||    ___|  |   |  " + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + " _____| ||    _  |  |   |    |   _   |  | | |   ||   |___   |   |  " + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + "|_______||___| |_|  |___|    |__| |__|  |_|  |__||_______|  |___|  " + ANSI_RESET);
        Bukkit.getLogger().info("   ");
        Bukkit.getLogger().info(ANSI_LIGHT_GREEN + "Plugin SkyXMysteryBox enabled !");
        Bukkit.getLogger().info(ANSI_LIGHT_GRAY + "︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getCommand("givemysterybox").setTabCompleter(new MysteryBoxTabCompleter(this));  // Pass the main plugin instance
        getCommand("skyxmysterybox").setTabCompleter(new SkyXTabCompleter());
        getLogger().info("SkyX Mystery Box plugin enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ANSI_LIGHT_GRAY + "︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹︹");
        Bukkit.getLogger().info(ANSI_MAGENTA + " _______  ___   _  __   __    __   __    __    _  _______  _______ " + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + "|       ||   | | ||  | |  |  |  |_|  |  |  |  | ||       ||       |" + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + "|  _____||   |_| ||  |_|  |  |       |  |   |_| ||    ___||_     _|" + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + "| |_____ |      _||       |  |       |  |       ||   |___   |   |  " + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + "|_____  ||     |_ |_     _|   |     |   |  _    ||    ___|  |   |  " + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + " _____| ||    _  |  |   |    |   _   |  | | |   ||   |___   |   |  " + ANSI_RESET);
        Bukkit.getLogger().info(ANSI_MAGENTA + "|_______||___| |_|  |___|    |__| |__|  |_|  |__||_______|  |___|  " + ANSI_RESET);
        Bukkit.getLogger().info("   ");
        Bukkit.getLogger().info(ANSI_RED + "  Plugin SkyXMysteryBox disabled !");
        Bukkit.getLogger().info(ANSI_LIGHT_GRAY + "︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺︺");
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

                // Nouvelle section ajoutée pour gérer la commande depuis le fichier YAML
                List<String> boxCommands = config.getStringList("mystery_boxes." + boxId + ".commands");
                for (String commandStr : boxCommands) {
                    if (commandStr != null && !commandStr.isEmpty()) {
                        executeCommand(commandStr, sender, targetPlayer);
                    }
                }

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

        if (item != null && event.getAction().toString().contains("RIGHT_CLICK")) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                for (String boxId : config.getConfigurationSection("mystery_boxes").getKeys(false)) {
                    if (meta.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', config.getString("mystery_boxes." + boxId + ".name")))) {
                        // Vérification si l'inventaire du joueur est plein
                        if (player.getInventory().firstEmpty() == -1) {
                            player.sendMessage(ChatColor.RED + "Your inventory is full! Cannot open the Consumable Item.");
                            event.setCancelled(true);
                            return;
                        }

                        // Lecture du son
                        String sound = config.getString("mystery_boxes." + boxId + ".sound");
                        if (sound != null && !sound.isEmpty()) {
                            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                        }

                        event.setCancelled(true);
                        consumeMysteryBox(player, item, boxId);
                        return;
                    }
                }
            }
        }
    }

    // Nouvelle méthode pour exécuter les commandes lues depuis le fichier YAML
    private void executeCommand(String commandStr, CommandSender sender, Player targetPlayer) {
        // Remplace %player% par le nom du joueur dans la commande
        String command = commandStr.replace("%player%", targetPlayer.getName());

        // Enlève les couleurs (si nécessaire) et exécute la commande
        command = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', command));

        try {
            // Assurez-vous que les commandes ne commencent pas par un "/"
            if (command.startsWith("/")) {
                command = command.substring(1);
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Failed to execute command: " + command);
            e.printStackTrace();
        }
    }

    private void consumeMysteryBox(Player player, ItemStack item, String boxId) {
        item.setAmount(item.getAmount() - 1);
        giveReward(player, boxId);
    }

    private void giveReward(Player player, String boxId) {
        Random rand = new Random();
        int roll = rand.nextInt(100) + 1;  // Valeur de 1 à 100
        int cumulativeChance = 0;

        List<String> rarities = Arrays.asList("legendary", "epic", "rare", "common");  // Vérifier de la rare à la plus commune

        for (String rarity : rarities) {
            if (config.contains("mystery_boxes." + boxId + ".rewards." + rarity)) {
                int chance = config.getInt("mystery_boxes." + boxId + ".rewards." + rarity + ".chance");
                cumulativeChance += chance;

                if (roll <= cumulativeChance) {  // Corrige la logique de comparaison
                    distributeRewards(player, boxId, rarity);
                    return;
                }
            }
        }
    }

    private void distributeRewards(Player player, String boxId, String rewardType) {
        List<String> items = config.getStringList("mystery_boxes." + boxId + ".rewards." + rewardType + ".items");
        List<String> commands = config.getStringList("mystery_boxes." + boxId + ".rewards." + rewardType + ".command");
        List<String> messages = config.getStringList("mystery_boxes." + boxId + ".rewards." + rewardType + ".message");

        // Envoi des messages de récompense
        for (String msg : messages) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("%player%", player.getName())));
        }

        // Gestion des items personnalisés via "give"
        ConfigurationSection rewardSection = config.getConfigurationSection("mystery_boxes." + boxId + ".rewards." + rewardType + ".give");

        // Combine les items, les commandes et les récompenses personnalisées pour une sélection aléatoire
        List<Object> allRewards = new ArrayList<>();
        allRewards.addAll(items);
        allRewards.addAll(commands);

        if (rewardSection != null) {
            for (String itemKey : rewardSection.getKeys(false)) {
                allRewards.add(rewardSection.getConfigurationSection(itemKey)); // Ajouter la config de l'item pour la randomisation
            }
        }

        if (!allRewards.isEmpty()) {
            Object reward = allRewards.get(new Random().nextInt(allRewards.size()));

            if (reward instanceof String) {
                String rewardStr = (String) reward;

                // Traitement des commandes JSON complexes avec /give
                if (rewardStr.toLowerCase().startsWith("give ") && rewardStr.contains("{")) {
                    executeGiveWithJson(player, rewardStr);
                }
                // Si la chaîne contient des espaces, on considère que c'est une commande à exécuter
                else if (rewardStr.contains(" ")) {
                    executeCommands(player, Collections.singletonList(rewardStr));
                }
                // Traitement des items Minecraft (avec ou sans namespace)
                else if (rewardStr.contains(":")) {
                    try {
                        String[] parts = rewardStr.split(":");
                        String materialName = parts[0].toUpperCase().replace("MINECRAFT:", "");
                        Material material = Material.matchMaterial(materialName);

                        if (material == null) {
                            player.sendMessage(ChatColor.RED + "Invalid material: " + parts[0]);
                            return;
                        }

                        int amount = (parts.length > 1) ? Integer.parseInt(parts[1]) : 1;
                        player.getInventory().addItem(new ItemStack(material, amount));
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Error while processing item reward: " + rewardStr);
                        e.printStackTrace();
                    }
                }
                // Si ce n'est ni une commande ni un item avec namespace, on vérifie s'il s'agit d'un simple item Minecraft
                else {
                    Material material = Material.matchMaterial(rewardStr.toUpperCase());
                    if (material != null) {
                        player.getInventory().addItem(new ItemStack(material, 1));
                    } else {
                        // Si ce n'est pas un item valide, on tente de l'exécuter comme une commande finale
                        executeCommands(player, Collections.singletonList(rewardStr));
                    }
                }
            }
            // Si la récompense est un item personnalisé (section de config)
            else if (reward instanceof ConfigurationSection) {
                ItemStack customItem = CustomItemBuilder.buildItem((ConfigurationSection) reward);
                player.getInventory().addItem(customItem);
            }
        }
    }


    private void executeGiveWithJson(Player player, String reward) {
        try {
            // Exécution de la commande /give avec des JSON complexes
            String command = reward.replace("%player%", player.getName());
            if (command.startsWith("/")) {
                command = command.substring(1); // Supprime le / initial
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Failed to execute give command with JSON: " + reward);
            e.printStackTrace();
        }
    }

    private void executeCommands(Player player, List<String> commands) {
        for (String command : commands) {
            command = command.replace("%player%", player.getName());
            command = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', command));

            try {
                // Assurez-vous que les commandes ne commencent pas par un "/"
                if (command.startsWith("/")) {
                    command = command.substring(1);
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Failed to execute command: " + command);
                e.printStackTrace();
            }
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
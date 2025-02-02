package skyxnetwork.skyXMysteryBox;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

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

            // Charger les informations de la Mystery Box depuis le config.yml
            if (config.contains("mystery_boxes." + boxId)) {
                String boxName = config.getString("mystery_boxes." + boxId + ".name");
                String texture = config.getString("mystery_boxes." + boxId + ".texture");
                ItemStack boxItem = new ItemStack(Material.PLAYER_HEAD, 1);
                ItemMeta boxMeta = boxItem.getItemMeta();

                // Appliquer le nom et le lore à l'item avec support des couleurs
                if (boxMeta != null) {
                    boxMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', boxName));

                    // Traduire les couleurs dans le lore
                    List<String> lore = config.getStringList("mystery_boxes." + boxId + ".lore");
                    for (int i = 0; i < lore.size(); i++) {
                        lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
                    }
                    boxMeta.setLore(lore);
                    boxItem.setItemMeta(boxMeta);
                }

                // Ajouter la texture (nécessite un code Base64 pour l'image)
                // Tu devras utiliser une librairie ou API pour appliquer la texture si nécessaire.

                // Donner la Mystery Box au joueur
                targetPlayer.getInventory().addItem(boxItem);
                sender.sendMessage("Gave " + targetPlayerName + " a " + boxName + "!");
            } else {
                sender.sendMessage("Box ID not found in the config.");
                return false;
            }
            return true;
        }

        // Commande reload pour recharger le config.yml
        if (command.getName().equalsIgnoreCase("skyxmysterybox") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("skyxmysterybox.reload")) {
                reloadConfig();
                config = getConfig();
                sender.sendMessage("Config reloaded successfully!");
                return true;
            } else {
                sender.sendMessage("You do not have permission to execute this command.");
                return false;
            }
        }

        return false;
    }

    // Événement pour consommer la box avec un clic droit
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Vérifier si l'item est une boîte mystère (par nom et matériau)
        if (item != null && item.getType() != Material.AIR && event.getAction().toString().contains("RIGHT_CLICK")) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String itemName = meta.getDisplayName();

                // Vérifier si l'item correspond à une boîte mystère définie dans le config.yml
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
        // Retirer la boîte mystère de l'inventaire
        player.getInventory().remove(item);

        // Choisir une récompense aléatoire en fonction des chances définies dans le config.yml
        giveReward(player, boxId);
    }

    private void giveReward(Player player, String boxId) {
        Random rand = new Random();

        // Chance de chaque type de récompense (commun, rare, légendaire)
        int commonChance = config.getInt("mystery_boxes." + boxId + ".rewards.common.chance");
        int rareChance = config.getInt("mystery_boxes." + boxId + ".rewards.rare.chance");

        // Calcul de la chance totale
        int totalChance = commonChance + rareChance;

        int roll = rand.nextInt(totalChance);

        if (roll < commonChance) {
            // Récompenses communes
            List<String> commonItems = config.getStringList("mystery_boxes." + boxId + ".rewards.common.items");
            for (String item : commonItems) {
                String[] itemData = item.split(":");
                Material material = Material.valueOf(itemData[0]);
                int amount = Integer.parseInt(itemData[1]);
                player.getInventory().addItem(new ItemStack(material, amount));
            }

            // Exécution des commandes communes
            List<String> commonCommands = config.getStringList("mystery_boxes." + boxId + ".rewards.common.command");
            executeCommands(player, commonCommands);

        } else {
            // Récompenses rares
            List<String> rareItems = config.getStringList("mystery_boxes." + boxId + ".rewards.rare.items");
            for (String item : rareItems) {
                String[] itemData = item.split(":");
                Material material = Material.valueOf(itemData[0]);
                int amount = Integer.parseInt(itemData[1]);
                player.getInventory().addItem(new ItemStack(material, amount));
            }

            // Exécution des commandes rares
            List<String> rareCommands = config.getStringList("mystery_boxes." + boxId + ".rewards.rare.command");
            executeCommands(player, rareCommands);
        }
    }

    private void executeCommands(Player player, List<String> commands) {
        for (String command : commands) {
            command = command.replace("%player%", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    // Événement pour interdire le renommage de la boîte mystère dans l'enclume
    @EventHandler
    public void onAnvilRename(PrepareAnvilEvent event) {
        ItemStack item = event.getResult();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String itemName = item.getItemMeta().getDisplayName();

            // Vérifier si l'item correspond à une boîte mystère
            for (String boxId : config.getConfigurationSection("mystery_boxes").getKeys(false)) {
                if (itemName.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', config.getString("mystery_boxes." + boxId + ".name")))) {
                    event.setResult(null); // Annuler le renommage
                    event.getInventory().setRepairCost(0);
                    return;
                }
            }
        }
    }
}
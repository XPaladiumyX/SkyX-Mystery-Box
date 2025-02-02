package skyxnetwork.skyXMysteryBox;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Vérifier si l'item est une boîte mystère (par nom et matériau)
        if (item != null && item.getType() != Material.AIR && event.getAction().toString().contains("RIGHT_CLICK")) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String itemName = meta.getDisplayName();
                // Vérifier si l'item est une boîte mystère, ici on assume "Basic Mystery Box"
                if (itemName.equalsIgnoreCase(config.getString("mystery_boxes.basic_box.name"))) {
                    event.setCancelled(true);
                    consumeMysteryBox(player, item);
                }
            }
        }
    }

    private void consumeMysteryBox(Player player, ItemStack item) {
        // Retirer la boîte mystère de l'inventaire
        player.getInventory().remove(item);

        // Choisir une récompense aléatoire en fonction des chances définies dans le config.yml
        giveReward(player);
    }

    private void giveReward(Player player) {
        Random rand = new Random();

        // Chance de chaque type de récompense (commun, rare, légendaire)
        int commonChance = config.getInt("mystery_boxes.basic_box.rewards.common.chance");
        int rareChance = config.getInt("mystery_boxes.basic_box.rewards.rare.chance");

        // Calcul de la chance totale
        int totalChance = commonChance + rareChance;

        int roll = rand.nextInt(totalChance);

        if (roll < commonChance) {
            // Récompenses communes
            List<String> commonItems = config.getStringList("mystery_boxes.basic_box.rewards.common.items");
            for (String item : commonItems) {
                String[] itemData = item.split(":");
                Material material = Material.valueOf(itemData[0]);
                int amount = Integer.parseInt(itemData[1]);
                player.getInventory().addItem(new ItemStack(material, amount));
            }

            // Exécution des commandes communes
            List<String> commonCommands = config.getStringList("mystery_boxes.basic_box.rewards.common.command");
            executeCommands(player, commonCommands);

        } else {
            // Récompenses rares
            List<String> rareItems = config.getStringList("mystery_boxes.basic_box.rewards.rare.items");
            for (String item : rareItems) {
                String[] itemData = item.split(":");
                Material material = Material.valueOf(itemData[0]);
                int amount = Integer.parseInt(itemData[1]);
                player.getInventory().addItem(new ItemStack(material, amount));
            }

            // Exécution des commandes rares
            List<String> rareCommands = config.getStringList("mystery_boxes.basic_box.rewards.rare.command");
            executeCommands(player, rareCommands);
        }
    }

    private void executeCommands(Player player, List<String> commands) {
        for (String command : commands) {
            command = command.replace("%player%", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}
package skyxnetwork.skyXMysteryBox;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class CustomItemBuilder {

    public static ItemStack buildItem(ConfigurationSection itemConfig) {
        Material material = Material.matchMaterial(itemConfig.getString("material", "STONE"));
        if (material == null) material = Material.STONE;

        int amount = itemConfig.getInt("amount", 1);
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        // Custom Model Data
        if (itemConfig.contains("custom_model_data")) {
            meta.setCustomModelData(itemConfig.getInt("custom_model_data"));
        }

        // Display Name & Lore
        if (itemConfig.contains("display")) {
            ConfigurationSection display = itemConfig.getConfigurationSection("display");
            if (display.contains("name")) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display.getString("name")));
            }
            if (display.contains("lore")) {
                List<String> lore = display.getStringList("lore");
                meta.setLore(lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).toList());
            }
        }

        // Enchantements
        if (itemConfig.contains("enchantments")) {
            List<Map<?, ?>> enchantments = itemConfig.getMapList("enchantments");
            for (Map<?, ?> enchantmentMap : enchantments) {
                String enchantId = ((String) enchantmentMap.get("id")).toLowerCase(); // Convertir en minuscule
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantId)); // Utilisation de getByKey

                if (enchantment != null) {
                    int level = (int) enchantmentMap.get("level");
                    meta.addEnchant(enchantment, level, true);
                } else {
                    Bukkit.getLogger().warning("Invalid enchantment ID: " + enchantId);
                }
            }
        }

        // Tags (NBT)
        if (itemConfig.contains("tags")) {
            ConfigurationSection tags = itemConfig.getConfigurationSection("tags");
            for (String key : tags.getKeys(false)) {
                if (key.equalsIgnoreCase("Unbreakable")) {
                    // Gestion spécifique pour le tag Unbreakable
                    meta.setUnbreakable(tags.getBoolean(key));
                } else {
                    // Pour les autres tags personnalisés
                    meta.getPersistentDataContainer().set(
                            new NamespacedKey(Bukkit.getPluginManager().getPlugin("SkyXMysteryBox"), key),
                            PersistentDataType.STRING,
                            tags.get(key).toString()
                    );
                }
            }
        }

        item.setItemMeta(meta);
        return item;
    }
}
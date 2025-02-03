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
                Enchantment enchantment = Enchantment.getByName((String) enchantmentMap.get("id"));
                int level = (int) enchantmentMap.get("level");
                if (enchantment != null) {
                    meta.addEnchant(enchantment, level, true);
                }
            }
        }

        // Tags (NBT)
        if (itemConfig.contains("tags")) {
            ConfigurationSection tags = itemConfig.getConfigurationSection("tags");
            for (String key : tags.getKeys(false)) {
                meta.getPersistentDataContainer().set(new NamespacedKey(Bukkit.getPluginManager().getPlugin("SkyXMysteryBox"), key),
                        PersistentDataType.STRING, tags.get(key).toString());
            }
        }

        item.setItemMeta(meta);
        return item;
    }
}
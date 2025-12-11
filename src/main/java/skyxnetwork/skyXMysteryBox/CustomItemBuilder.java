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

    public static ItemStack buildItem(ConfigurationSection section) {

        // Material sécurisé
        Material material = Material.matchMaterial(section.getString("material", "STONE"));
        if (material == null) {
            Bukkit.getLogger().warning("[SkyXMysteryBox] Invalid material: " + section.getString("material"));
            material = Material.STONE;
        }

        ItemStack item = new ItemStack(material, section.getInt("amount", 1));
        ItemMeta meta = item.getItemMeta();

        // Custom model data
        if (section.contains("custom_model_data")) {
            meta.setCustomModelData(section.getInt("custom_model_data"));
        }

        // Display
        ConfigurationSection display = section.getConfigurationSection("display");
        if (display != null) {

            if (display.contains("name"))
                meta.setDisplayName(color(display.getString("name")));

            if (display.contains("lore")) {
                List<String> lore = display.getStringList("lore")
                        .stream().map(CustomItemBuilder::color).toList();
                meta.setLore(lore);
            }
        }

        // Enchantements améliorés
        if (section.contains("enchantments")) {
            for (Map<?, ?> enchantMap : section.getMapList("enchantments")) {

                String id = ((String) enchantMap.get("id")).toLowerCase();
                if (id.contains(":")) id = id.split(":")[1];

                Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(id));
                if (ench == null) {
                    Bukkit.getLogger().warning("[SkyXMysteryBox] Invalid enchant ID: " + id);
                    continue;
                }

                int level = (int) enchantMap.get("level");
                meta.addEnchant(ench, level, true);
            }
        }

        // Tags avancés
        if (section.contains("tags")) {
            ConfigurationSection tags = section.getConfigurationSection("tags");

            for (String key : tags.getKeys(false)) {
                Object raw = tags.get(key);
                NamespacedKey nkey = new NamespacedKey("SkyXMysteryBox", key);

                switch (raw) {
                    case Integer v -> meta.getPersistentDataContainer().set(nkey, PersistentDataType.INTEGER, v);
                    case Double v -> meta.getPersistentDataContainer().set(nkey, PersistentDataType.DOUBLE, v);
                    case Boolean v ->
                            meta.getPersistentDataContainer().set(nkey, PersistentDataType.BYTE, (byte) (v ? 1 : 0));
                    default -> meta.getPersistentDataContainer().set(nkey, PersistentDataType.STRING, raw.toString());
                }
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    private static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
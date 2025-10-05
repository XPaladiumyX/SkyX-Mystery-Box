package skyxnetwork.skyXMysteryBox;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MysteryBoxTabCompleter implements TabCompleter {
    private final SkyXMysteryBox plugin;

    // Constructor to accept the main plugin instance
    public MysteryBoxTabCompleter(SkyXMysteryBox plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) { // Player argument
            if (sender instanceof Player) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    completions.add(onlinePlayer.getName());
                }
            }
        } else if (args.length == 2) { // Box ID argument
            List<String> boxIds = new ArrayList<>(plugin.getConfig().getConfigurationSection("mystery_boxes").getKeys(false));
            StringUtil.copyPartialMatches(args[1], boxIds, completions);
        }
        return completions;
    }
}

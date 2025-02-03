package skyxnetwork.skyXMysteryBox;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class SkyXTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return args.length == 1 ? List.of("reload") : List.of();
    }
}

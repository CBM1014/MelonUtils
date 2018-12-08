package red.cbm.CBMUtils;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public class Placeholders extends EZPlaceholderHook {

    public Placeholders(JavaPlugin plugin) {
        super(plugin, "CBMUtils");
    }

    @Override
    public String onPlaceholderRequest(Player player, String string) {
        if (string.equals("status")) {
            if(Main.isUsingPaperAPI){
                double d[] = Bukkit.getTPS();
                if(d[0]>19.5){
                    return Main.c("&7良好");
                }else if(d[0]>18){
                    return Main.c("&7平稳");
                }else if(d[0]>17){
                    return Main.c("&e一般");
                }else{
                    return Main.c("&c欠佳");
                }
            }else{
                return Main.c("&7未知");
            }
        }
        return null;
    }

}

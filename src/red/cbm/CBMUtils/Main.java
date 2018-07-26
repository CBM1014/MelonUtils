package red.cbm.CBMUtils;


import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;


public class Main extends JavaPlugin {

    protected static Map<String, Long> gclastused = new HashMap<>();
    protected static final String PREFIX = c("&3CBMUtils &f>> ");
    private final String NO_PERMISSION = PREFIX + c("&7您没有权限使用这条命令哦~");
    protected static boolean privateMode = false;

    @Override
    public void onEnable() {

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.getLogger().info(c("&3&l找到PlaceholderAPI,尝试注册变量。"));
            boolean b = new Placeholders(this).hook();
            if (b) {
                this.getLogger().info(c("&3&lPlaceholder注册成功。"));
            } else {
                this.getLogger().info(c("&3&lPlaceholder注册失败，放弃注册Placeholder。"));
            }
        } else {
            this.getLogger().info(c("&3&l未找到PlaceholderAPI，放弃注册Placeholder。"));

        }

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                System.runFinalization();
                System.gc();
            }
        }.runTaskTimer(this, 0L, 1200 * 20L);
        this.getLogger().info(c("&3&lCBMUtils已加载。"));
    }

    @Override
    public void onDisable() {
        this.getLogger().info(c("&3&lCBMUtils已关闭。"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("gc")) {
            if (!(sender instanceof Player)) {
                System.runFinalization();
                System.gc();
                sender.sendMessage(c("&7垃圾清理完成"));
            } else {
                if ((System.currentTimeMillis() - (gclastused.get(sender.getName()))) < 60000) {
                    sender.sendMessage(NOT_COOLDOWN(sender.getName()));
                } else {
                    System.runFinalization();
                    System.gc();
                    ((Player) sender).sendTitle("", c("&7垃圾清理完成"), 10, 15, 10);
                    gclastused.put(sender.getName(), System.currentTimeMillis());
                }
            }

        } else if(command.getName().equalsIgnoreCase("testmode")){
            if(sender.hasPermission("cbmserver.admin")){if(privateMode==true){
                privateMode=false;
                sender.sendMessage(PREFIX+"&7调试模式已关闭，再次使用 &3/testmode &7可以开启。");
            }else{
                privateMode=true;
                sender.sendMessage(PREFIX+"&7调试模式已开启，再次使用 &3/testmode &7可以关闭。");
            }}else{
                sender.sendMessage(NO_PERMISSION);
            }

        } else if(command.getName().equalsIgnoreCase("back")){
            if(sender.hasPermission("cbmserver.admin")){if(privateMode==true){
                privateMode=false;
                sender.sendMessage(PREFIX+"&7调试模式已关闭，再次使用 &3/testmode &7可以开启。");
            }else{
                privateMode=true;
                sender.sendMessage(PREFIX+"&7调试模式已开启，再次使用 &3/testmode &7可以关闭。");
            }}else{
                sender.sendMessage(NO_PERMISSION);
            }

        }
        return true;
    }

    private String NOT_COOLDOWN(String userName) {
        Long l = (System.currentTimeMillis() - (gclastused.get(userName)));
        return PREFIX + c("&7指令冷却中，还需" + (60 - (l / 1000)) + "秒才能再次使用。");
    }

    static String c(String in) {
        String result = in.replace("&", "§");
        result = result.replace("§§", "&");
        return result;
    }


}
package cat.melon.CBMUtils;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import cat.melon.CBMUtils.Utils.MelonLocation;

import java.util.*;


public class Main extends JavaPlugin {

    protected static Map<String, Long> gclastused = new HashMap<>();
    protected static Map<String, Long> backlastused = new HashMap<>();
    protected static Map<String, Map<Long, MelonLocation>> deathrecord = new HashMap();
    protected static Map<String, MelonLocation> latestdeath = new HashMap<>();
    protected static final String PREFIX = c("&3CBMUtils &f>> ");
    public static boolean isUsingPaperAPI = false;
    private final String ONLY_PLAYER_NOTICE = PREFIX + c("&7只有玩家可以执行此指令");
    private final String NO_DEATH_RECORD = PREFIX + c("&7没有找到您的死亡记录哦~");
    private final String DEATH_RECORD_TIMEOUT = PREFIX + c("&7您的死亡记录已超时，死亡记录最多会被保留五分钟。");
    private final String NO_PERMISSION = PREFIX + c("&7您没有权限使用这条命令哦~");
    protected static boolean privateMode = false;


    @Override
    public void onEnable() {
        if (this.getServer().getName().equalsIgnoreCase("Paper")) {
            isUsingPaperAPI = true;
            this.getLogger().info(c("&3&l找到PaperAPI,将会开启所有功能。"));
        } else {
            this.getLogger().info(c("&3&l未找到PaperAPI，放弃部分功能。"));
        }
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

        getServer().getPluginManager().registerEvents(new cat.melon.CBMUtils.EventListener(), this);


        new BukkitRunnable() {
            @Override
            public void run() {
                System.runFinalization();
                System.gc();
                for (Player x : Bukkit.getOnlinePlayers()) {
                    x.sendTitle("", c("&7△"), 10, 20, 10);
                }
            }
        }.runTaskTimer(this, 600 * 20L, 1200 * 20L);
        this.getLogger().info(c("&3&lCBMUtils已加载。"));

        if (isUsingPaperAPI) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    int cavecount = 0;
                    int rp = 0;
                    List<Entity> col = Bukkit.getWorld("world").getEntities();
                    for (Entity x : col) {
                        if (x.isCustomNameVisible()) {
                            continue;
                        }
                        switch (x.getType()) {
                            case ZOMBIE:
                            case CAVE_SPIDER:
                            case SPIDER:
                            case CREEPER:
                            case SKELETON:
                            case BAT:
                                if (x.getLocation().getBlock().getType() == Material.CAVE_AIR) {
                                    if (x.getLocation().getNearbyEntitiesByType(Player.class, 15D, 15D, 8D).isEmpty()) {
                                        x.remove();
                                        cavecount++;
                                    }
                                }
                                break;
                            case RABBIT:
                            case PARROT:
                                if (x.getLocation().distance(EventListener.spawnLocation) < 100) {
                                    if (x.getLocation().getNearbyEntitiesByType(Player.class, 40D, 40D, 40D).isEmpty()) {
                                        x.remove();
                                        rp++;
                                    }
                                }

                        }
                    }
                    Bukkit.broadcast(PREFIX + c("&7本次清理了" + cavecount + "个矿洞实体。"), "cbmserver.admin");
                    Bukkit.broadcast(PREFIX + c("&7本次清理了" + rp + "个小兔子和鹦鹉。"), "cbmserver.admin");
                    for (Player x :
                            Bukkit.getOnlinePlayers()) {
                        x.sendTitle("", c("&7☆"), 10, 20, 10);
                    }
                }
            }.runTaskTimer(this, 150 * 20L, 300 * 20L);
        }


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
                for (Player x : Bukkit.getOnlinePlayers()
                ) {
                    if (!x.getName().equalsIgnoreCase(sender.getName())) {
                        x.sendTitle("", c("&7△"), 10, 20, 10);
                    }

                }
                sender.sendMessage(c("&7垃圾清理完成"));
            } else {
                if ((System.currentTimeMillis() - (gclastused.get(sender.getName()))) < 60000) {
                    sender.sendMessage(GC_NOT_COOLDOWN(sender.getName()));
                } else {
                    /*if(((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/Runtime.getRuntime().totalMemory())<0.6){
                        sender.sendMessage(PREFIX+c("当前内存占用量一般，频繁调用垃圾清理器会导致服务器卡顿。请尽量只在内存占用大时使用此指令。"));
                    }*/
                    if (isUsingPaperAPI) {
                        if (getPing((Player) sender) > 300) {
                            sender.sendMessage(PREFIX + c("您的网络延迟较高，卡顿可能是网络问题。可以试试使用加速线路进入服务器。"));
                        }
                    }
                    for (Player x : Bukkit.getOnlinePlayers()
                    ) {
                        if (!x.getName().equalsIgnoreCase(sender.getName())) {
                            x.sendTitle("", c("&7△"), 10, 20, 10);
                        }

                    }
                    System.runFinalization();
                    System.gc();
                    ((Player) sender).sendTitle("", c("&7垃圾清理完成"), 10, 15, 10);
                    gclastused.put(sender.getName(), System.currentTimeMillis());
                }
            }

        } else if (command.getName().equalsIgnoreCase("testmode")) {
            if (sender.hasPermission("cbmserver.admin")) {
                if (privateMode == true) {
                    privateMode = false;
                    sender.sendMessage(PREFIX + c("&7调试模式已关闭，再次使用 &3/testmode &7可以开启。"));
                } else {
                    privateMode = true;
                    sender.sendMessage(PREFIX + c("&7调试模式已开启，再次使用 &3/testmode &7可以关闭。"));
                }
            } else {
                sender.sendMessage(NO_PERMISSION);
            }

        } else if (command.getName().equalsIgnoreCase("back")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ONLY_PLAYER_NOTICE);
                return true;
            }
            int recodenumbers = 0;
            boolean hasdeathrecord = false;
            boolean canTeleprot = false;
            long now = now();
            StringBuilder sb1 = new StringBuilder();
            ArrayList<Long> tmpl= new ArrayList<>();
            for (Long l:deathrecord.get(((Player) sender).getUniqueId().toString()).keySet()){
                tmpl.add(l);
            }
            Collections.reverse(tmpl);
            for (Long l : tmpl) {
                hasdeathrecord = true;
                if ((now - l) < 300000) {
                    sb1.append(deathrecord.get(((Player) sender).getUniqueId().toString()).get(l).toString() + "    §7" + ((now - l) / 1000) + "秒前\n");
                    recodenumbers++;
                }
            }
            if ((now - (backlastused.get(((Player) sender).getUniqueId().toString()))) > 180000) {
                canTeleprot = true;
            }
            if (hasdeathrecord) {
                if (recodenumbers == 0) {
                    sender.sendMessage(DEATH_RECORD_TIMEOUT);
                    return true;
                }
            } else {
                sender.sendMessage(NO_DEATH_RECORD);
                return true;
            }
            if (canTeleprot) {
                MelonLocation melonloc = latestdeath.get(((Player) sender).getUniqueId().toString());

                if (melonloc.y < 0) {
                    if (recodenumbers > 1) {
                        sender.sendMessage(sb1.toString());
                        sender.sendMessage(c("&7您上一次死于虚空，不能进行传送。以上是您的死亡记录:"));
                    } else {
                        sender.sendMessage(PREFIX + c("&7您上一次死于虚空，不能进行传送。&7您上一次的死亡位置:"));
                        sender.sendMessage(sb1.toString());
                    }
                } else {
                    Location location = new Location(Bukkit.getWorld(melonloc.world), melonloc.x, melonloc.y, melonloc.z, 0F, 0F);
                    if (recodenumbers > 1) {
                        sender.sendMessage(sb1.toString());
                        sender.sendMessage(c("&7以上是您的死亡记录。&7正在前往上一次的死亡地点..."));
                    } else {
                        sender.sendMessage(PREFIX + c("&7正在前往死亡地点..."));
                    }

                    ((Player) sender).sendTitle("", c("&7Teleporting..."), 10, 10, 10);
                    ((Player) sender).teleport(location);
                    backlastused.put(((Player) sender).getUniqueId().toString(), now);
                }

            } else {
                if (recodenumbers > 1) {
                    sender.sendMessage(sb1.toString());
                    sender.sendMessage(BACK_NOT_COOLDOWN(((Player) sender).getUniqueId().toString()));
                    sender.sendMessage(c("&7以上是您的死亡记录。"));
                } else {
                    sender.sendMessage(BACK_NOT_COOLDOWN(((Player) sender).getUniqueId().toString()));
                    sender.sendMessage(c("&7上一次死亡的位置:"));
                    sender.sendMessage(sb1.toString());
                }

            }
        } else if (command.getName().equalsIgnoreCase("help")) {
            sender.sendMessage(Constants.HELP_HEAD);
            try {
                try {
                    sender.sendMessage(Constants.HELP_LIST[Integer.parseInt(args[0]) - 1]);
                } catch (NumberFormatException e1) {
                    sender.sendMessage(Constants.HELP_LIST[0]);
                }

            } catch (IndexOutOfBoundsException e) {
                if (args.length < 1) {
                    sender.sendMessage(Constants.HELP_LIST[0]);
                } else {
                    sender.sendMessage(c("&7找不到第" + args[0] + "页，将会为您显示第1页。"));
                    sender.sendMessage(Constants.HELP_LIST[0]);
                }

            }
        } else if (command.getName().equalsIgnoreCase("hat")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ONLY_PLAYER_NOTICE);
                return true;
            }
            ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
            if (is.getType() == Material.AIR) {
                sender.sendMessage(PREFIX + c("&7你的手上没有物品哦~"));
                return true;
            }
            ((Player) sender).getInventory().setItemInMainHand(((Player) sender).getInventory().getHelmet());
            ((Player) sender).getInventory().setHelmet(is);
            sender.sendMessage(PREFIX + c("&7新帽子换好啦！"));
        }
        return true;
    }

    private String GC_NOT_COOLDOWN(String userName) {
        Long l = (System.currentTimeMillis() - (gclastused.get(userName)));
        return PREFIX + c("&7指令冷却中，还需" + (60 - (l / 1000)) + "秒才能再次使用。");
    }

    private String BACK_NOT_COOLDOWN(String userName) {
        Long l = (System.currentTimeMillis() - (backlastused.get(userName)));
        return PREFIX + c("&7传送冷却中，还需" + (180 - (l / 1000)) + "秒才能再次传送。\n您可以查找您在5分钟内的死亡位置。");
    }

    static String c(String in) {
        String result = in.replace("&", "§");
        result = result.replace("§§", "&");
        return result;
    }

    private int getPing(Player p) {
        return ((CraftPlayer) p).getHandle().ping;
    }

    protected static Long now() {
        return System.currentTimeMillis();
    }

}
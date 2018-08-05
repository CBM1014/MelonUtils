package red.cbm.CBMUtils;


import net.minecraft.server.v1_13_R1.EntityPlayer;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;
import red.cbm.CBMUtils.Utils.MelonLocation;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

public class EventListener implements Listener {
    private DecimalFormat fer = new DecimalFormat();
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
        Main.deathrecord.put(event.getPlayer().getUniqueId().toString(), new HashMap<>());
        Main.backlastused.put(event.getPlayer().getUniqueId().toString(), 0L);
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


        @EventHandler
        public void onPlayerAttack(EntityDamageByEntityEvent event) {
            Entity d = event.getDamager();

            if (d instanceof Player) {
                ((Player) d).playSound(d.getLocation(),Sound.BLOCK_WOOD_BREAK,100,0);
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

        if (m.equalsIgnoreCase("/ping")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Main.PREFIX+Main.c("&7pong! ("+((CraftPlayer)event.getPlayer()).getHandle().ping)+"ms)");
        }

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

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        /*
        Boolean b=false;
        int index = -1;
        int itemloc = -1;
        for(ItemStack i:event.getEntity().getInventory().getStorageContents()){
            if(i==null){
                continue;
            }
            index++;
            if(i.getItemMeta().getLore().get(0).equalsIgnoreCase(Main.c("&6永恒Ⅰ"))){
                b=true;
                itemloc=index;
            }
        }
        if(b==true){
            ItemStack is = event.getEntity().getInventory().getItem(itemloc);
            is.setAmount(is.getAmount()-1);
            event.setKeepInventory(true);
        }
        */
        Main.deathrecord.get(event.getEntity().getUniqueId().toString()).put(System.currentTimeMillis(), new MelonLocation(event.getEntity().getLocation()));
        Main.latestdeath.put(event.getEntity().getUniqueId().toString(), new MelonLocation(event.getEntity().getLocation()));
    }


    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent event) {
        Long time = Main.now();
        Boolean b = false;
        for (Long l : Main.deathrecord.get(event.getPlayer().getUniqueId().toString()).keySet()) {
            if ((time - l) < 300000) {
                b = true;
            }
        }
        if (b == true) {
            event.getPlayer().sendMessage(Main.PREFIX + Main.c("&7您的死亡地点已被记录，您可以使用 &3/back &7返回死亡地点。"));
        }


    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        switch (event.getEntityType()) {
            case PLAYER:
            case WITCH:
            case VILLAGER:
                if (event.getEntity() instanceof LivingEntity) {
                    if (((LivingEntity) event.getEntity()).isSwimming()) {
                        event.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, event.getEntity().getLocation(), 120, 0D, 0D, 0D, Material.REDSTONE_BLOCK.createBlockData());

                    } else {
                        event.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, event.getEntity().getLocation(), 100, 0D, 1D, 0D, Material.REDSTONE_BLOCK.createBlockData());
                        event.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, event.getEntity().getLocation(), 80, 0D, 1.2D, 0D, Material.REDSTONE_BLOCK.createBlockData());
                    }
                }
                break;

            case SPIDER:
            case SHEEP:
            case PIG:
            case COW:
            case HORSE:
            case SILVERFISH:
            case TROPICAL_FISH:
            case DOLPHIN:
            case CHICKEN:
            case BAT:
            case PARROT:
            case RABBIT:
            case WOLF:
            case SQUID:

                event.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, event.getEntity().getLocation(), 100, 0D, 0.5D, 0D, Material.REDSTONE_BLOCK.createBlockData());
                break;
            case ZOMBIE:
            case ZOMBIE_VILLAGER:
            case ZOMBIE_HORSE:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 25, 0D, 1D, 0D, Material.CYAN_CONCRETE);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 35, 0D, 1D, 0D, Material.REDSTONE_BLOCK);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 35, 0D, 1.2D, 0D, Material.REDSTONE_BLOCK);
                break;
            case SKELETON:
            case SKELETON_HORSE:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 50, 0D, 1D, 0D, Material.BONE_BLOCK);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 50, 0D, 1.2D, 0D, Material.BONE_BLOCK);
                break;
            case CREEPER:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 50, 0D, 1D, 0D, Material.LIME_CONCRETE);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 50, 0D, 1.2D, 0D, Material.LIME_CONCRETE);
                break;
        }

    }

    private void spawnParticle(Particle particle, Entity entity,int i,double v,double v1,double v2,Material material){
        entity.getWorld().spawnParticle(particle,entity.getLocation(),i,v,v1,v2,material.createBlockData());
    }


}

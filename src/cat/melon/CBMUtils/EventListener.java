package cat.melon.CBMUtils;


import org.bukkit.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import cat.melon.CBMUtils.Utils.MelonLocation;

import java.text.DecimalFormat;
import java.util.*;

public class EventListener implements Listener {
    protected static Location spawnLocation = new Location(Bukkit.getWorld("world"), -12, 69, 278, 0F, 0F);
    private DecimalFormat fer = new DecimalFormat();
    private Random random = new Random();

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
        if(Main.isUsingPaperAPI){
            Entity d = event.getDamager();
            Entity e = event.getEntity();
            switch (d.getType()) {
                case PLAYER:
                    ((Player) d).playSound(d.getLocation(), Sound.BLOCK_WOOD_BREAK, 100, 0);
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
                    break;
                case ARROW:
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

                    } else if (((Arrow) d).getShooter() instanceof Skeleton) {
                        if (isNearSpawnPoint(((Skeleton) ((Arrow) d).getShooter()).getLocation())) {
                            event.setCancelled(true);
                            ((Skeleton) ((Arrow) d).getShooter()).getWorld().spawnParticle(Particle.VILLAGER_HAPPY, ((Skeleton) ((Arrow) d).getShooter()).getLocation(), 50, 0D, 1D, 0D);
                            ((Skeleton) ((Arrow) d).getShooter()).getWorld().spawnEntity(((Skeleton) ((Arrow) d).getShooter()).getLocation(), EntityType.RABBIT);
                            ((Skeleton) ((Arrow) d).getShooter()).remove();
                            d.remove();
                        }
                    }
                    break;
                case TRIDENT:
                    if (((Trident) d).getShooter() instanceof Player) {
                        if (e instanceof LivingEntity) {
                            double h = ((LivingEntity) e).getHealth();
                            double remain;
                            if ((h - event.getDamage()) >= 0) {
                                remain = h - event.getDamage();
                            } else {
                                remain = 0;
                            }
                            ((Player) ((Trident) d).getShooter()).sendActionBar(Main.c("&7- " + fer.format(event.getDamage()) + " &c❤  " + "&f|" + "  &7" + fer.format(remain) + " &c❤"));
                        }
                    } else if (((Trident) d).getShooter() instanceof Drowned) {
                        if (isNearSpawnPoint(((Skeleton) ((Trident) d).getShooter()).getLocation())) {
                            event.setDamage((event.getDamage() / 10));
                            ((Drowned) ((Trident) d).getShooter()).getWorld().spawnParticle(Particle.VILLAGER_HAPPY, ((Skeleton) ((Arrow) d).getShooter()).getLocation(), 50, 0D, 1D, 0D);
                            ((Drowned) ((Trident) d).getShooter()).getWorld().spawnEntity(((Drowned) ((Trident) d).getShooter()).getLocation(), EntityType.RABBIT);
                            ((Drowned) ((Trident) d).getShooter()).remove();
                            d.remove();
                        }
                    }
                    break;
                case ZOMBIE:
                case ZOMBIE_VILLAGER:
                case SPIDER:
                case CAVE_SPIDER:
                case ENDERMAN:
                    if (e.getType() != EntityType.PLAYER) {
                        return;
                    }
                    if (isNearSpawnPoint(d.getLocation())) {
                        event.setDamage((event.getDamage() / 10));
                        d.getWorld().spawnEntity(d.getLocation(), EntityType.RABBIT);
                        d.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, d.getLocation(), 100, 0D, 1D, 0D);
                        d.remove();
                    }
                    break;
                case PHANTOM:
                    if (e.getType() != EntityType.PLAYER) {
                        return;
                    }
                    if (isNearSpawnPoint(d.getLocation())) {
                        d.getWorld().spawnEntity(d.getLocation(), EntityType.PARROT);
                        d.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, d.getLocation(), 100, 0D, 0D, 0D);
                        d.remove();
                    }
                    break;
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
            event.getPlayer().sendMessage(Main.PREFIX + Main.c("&7pong! (" + ((CraftPlayer) event.getPlayer()).getHandle().ping) + "ms)");
        }

        if (m.equalsIgnoreCase("/spawn")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Main.PREFIX + Main.c("&7您距离重生点有" + fer.format(event.getPlayer().getLocation().distance(spawnLocation))) + "格。");
        }

    }


    @EventHandler
    public void onFireSpread(BlockSpreadEvent event) {
        if (event.getSource().getType() == org.bukkit.Material.FIRE) {
            event.setCancelled(true);
        }
    }

    /*
        @EventHandler
        public void onPlayerChat(AsyncPlayerChatEvent event) {
            String m = event.getMessage();
            event.setMessage(Main.c(m));


            {
                System.out.println("[CHAT]<"+event.getPlayer().getName()+"> "+m);
                if (p.hasPermission("cbmserver.admin")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.c("tellraw @a [\"\",{\"text\":\"<&3" + p.getName() + "&f>\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg " + p.getName() + " \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"&3" + p.getName() + " is Operator.&f\nClick to /msg " + p.getName() + "\"}]}}},{\"text\":\" " + m + " \"}]"));
                } else {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.c("tellraw @a [\"\",{\"text\":\"<" + p.getName() + ">\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg " + p.getName() + " \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"/msg " + p.getName() + "\"}]}}},{\"text\":\" " + m + " \"}]"));
                }
            }


        }//已包含在MelonWhitelist中
    */
    @EventHandler
    public void signEvent(SignChangeEvent event) {
        for (int l = 0; l <= 3; l++) {
            String line = Main.c(event.getLine(l));
            event.setLine(l, line);
        }
    }

/*
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getInventory().getType() != InventoryType.ANVIL) {
            return;
        }
        if (event.getSlotType() == InventoryType.SlotType.RESULT) {

        }
    }*/


    @EventHandler
    public void creeperEvent(EntityExplodeEvent event) {
        if (event.getEntityType() == EntityType.CREEPER) {
            event.setCancelled(true);
            if(Main.isUsingPaperAPI){
                Collection<Player> col = event.getLocation().getNearbyEntitiesByType(Player.class, 4.0D, 3.0D);
                PotionEffect[] potionEffects = {new PotionEffect(PotionEffectType.SLOW, 20, 1), new PotionEffect(PotionEffectType.UNLUCK, 200, 10), new PotionEffect(PotionEffectType.WEAKNESS, 200, 1)};
                for (Player p : col) {
                    for (PotionEffect e : potionEffects) {
                        p.addPotionEffect(e);
                    }
                }
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
        event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
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
    public void onMobSpawn(EntitySpawnEvent event) {
        if (event.isCancelled() == true) {
            return;
        }
        if (event.getLocation().getWorld().getName().equalsIgnoreCase("world")) {
            if (event.getLocation().distance(spawnLocation) < 20) {
                switch (event.getEntityType()) {
                    case ZOMBIE:
                    case CREEPER:
                    case SPIDER:
                    case CAVE_SPIDER:
                    case ZOMBIE_VILLAGER:
                    case ENDERMAN:
                        event.setCancelled(true);
                }

            }
        }
        if(Main.isUsingPaperAPI){
            if (event.getLocation().getBlock().getType() == Material.CAVE_AIR) {
                if (random.nextInt(3) == 1) {
                    if (event.getLocation().getNearbyEntitiesByType(Player.class, 15D, 15D, 8D).isEmpty()) {
                        event.setCancelled(true);
                    }
                }
            }
        }


    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Material mat = randomColorConcrete();
        switch (event.getEntityType()) {
            case PLAYER:
                if (event.getEntity() instanceof LivingEntity) {
                    if (((LivingEntity) event.getEntity()).isSwimming()) {
                        spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 120, 0D, 0D, 0D, mat);

                    } else {
                        spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 100, 0D, 1D, 0D, mat);
                        spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 80, 0D, 1.2D, 0D, mat);
                    }
                }
                break;
            case WITCH:
            case VILLAGER:
            case BLAZE:
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
            case ENDERMITE:
            case MUSHROOM_COW:
            case OCELOT:
            case SALMON:
            case MULE:
            case TURTLE:
            case DONKEY:
            case GHAST:
            case POLAR_BEAR:
            case PUFFERFISH:
            case LLAMA:
            case CAVE_SPIDER:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 100, 0D, 0.5D, 0D, randomColorConcrete());
                break;
            case ZOMBIE:
            case DROWNED:
            case ZOMBIE_VILLAGER:
            case ZOMBIE_HORSE:
            case PIG_ZOMBIE:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 25, 0D, 1D, 0D, Material.CYAN_CONCRETE);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 35, 0D, 1D, 0D, Material.RED_CONCRETE);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 35, 0D, 1.2D, 0D, Material.RED_CONCRETE);
                break;
            case SKELETON:
            case SKELETON_HORSE:
            case STRAY:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 50, 0D, 1D, 0D, Material.BONE_BLOCK);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 50, 0D, 1.2D, 0D, Material.BONE_BLOCK);
                break;
            case CREEPER:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 60, 0D, 1D, 0D, Material.LIME_CONCRETE);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 60, 0D, 1.2D, 0D, Material.LIME_CONCRETE);
                break;
            case SHULKER:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 100, 0D, 0.5D, 0D, Material.PURPLE_CONCRETE);
            case GUARDIAN:
            case ELDER_GUARDIAN:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 60, 0D, 0.5D, 0D, Material.ORANGE_CONCRETE);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 60, 0D, 0.5D, 0D, Material.RED_CONCRETE);
            case WITHER:
            case WITHER_SKELETON:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 50, 0D, 1D, 0D, Material.COAL_BLOCK);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 50, 0D, 1.2D, 0D, Material.COAL_BLOCK);
                break;
            case ENDERMAN:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 25, 0D, 1D, 0D, Material.COAL_BLOCK);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 30, 0D, 1D, 0D, Material.RED_CONCRETE);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 30, 0D, 1.5D, 0D, Material.RED_CONCRETE);
                break;
            case HUSK:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 25, 0D, 1D, 0D, Material.STRIPPED_OAK_WOOD);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 35, 0D, 1D, 0D, Material.RED_CONCRETE);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 35, 0D, 1.2D, 0D, Material.RED_CONCRETE);
                break;
            case VINDICATOR:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 25, 0D, 1D, 0D, Material.BONE_BLOCK);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 35, 0D, 1D, 0D, Material.REDSTONE_BLOCK);
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 35, 0D, 1.2D, 0D, Material.REDSTONE_BLOCK);
                break;
            case VEX:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 50, 0D, 0.5D, 0D, Material.BONE_BLOCK);
                break;
            case ENDER_DRAGON:
                spawnParticle(Particle.BLOCK_DUST, event.getEntity(), 120, 0D, 2D, 0D, mat);
        }

    }

    private void spawnParticle(Particle particle, Entity entity, int i, double v, double v1, double v2, Material material) {
        entity.getWorld().spawnParticle(particle, entity.getLocation(), i, v, v1, v2, material.createBlockData());
    }

    private boolean isNearSpawnPoint(Location loc) {
        if (loc.getWorld().getName().equalsIgnoreCase("world")) {
            if (loc.distance(spawnLocation) < 55) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private Material randomColorConcrete() {
        switch (random.nextInt(8)) {
            case 0:
                return Material.PINK_CONCRETE;
            case 1:
                return Material.ORANGE_CONCRETE;
            case 2:
                return Material.YELLOW_CONCRETE;
            case 3:
                return Material.LIME_CONCRETE;
            case 4:
                return Material.CYAN_CONCRETE;
            case 5:
                return Material.LIGHT_BLUE_CONCRETE;
            case 6:
                return Material.PURPLE_CONCRETE;
        }
        return Material.MAGENTA_CONCRETE;
    }


}

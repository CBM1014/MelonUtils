package cat.melon.helloween;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {
    private ItemStack pie;
    private ItemStack la;
    private List<String> lore = new ArrayList<>();
    private List<String> lorela = new ArrayList<>();
    private Random ran = new Random();


    static String c(String in) {
        String result = in.replace("&", "§");
        result = result.replace("§§", "&");
        return result;

    }

    private ItemStack getPie() {
        ItemStack is = new ItemStack(Material.PUMPKIN_PIE);
        is.setLore(lore);
        is.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        is.getItemMeta().setLocalizedName(c("&6万圣节南瓜派"));
        is.getItemMeta().setDisplayName(c("&6万圣节南瓜派"));
        return is;
    }

    private ItemStack getla(){
        ItemStack is = new ItemStack(Material.JACK_O_LANTERN);
        is.setLore(lore);
        is.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        is.getItemMeta().setLocalizedName(c("&6万圣节南瓜灯"));
        is.getItemMeta().setDisplayName(c("&6万圣节南瓜灯"));
        return is;
    }

    @Override
    public void onEnable() {
        lore.add(c("&7恐吓 I"));
        lore.add("");
        lore.add(c("&3四周目活动物品"));
        lore.add(c("&3不给糖就捣蛋！"));

        lorela.add(c("&7恐吓 I"));
        lorela.add("");
        lorela.add(c("&3四周目活动物品"));
        lorela.add(c("&3超凶！"));
        pie = this.getPie();
        la = this.getla();
        this.getServer().getPluginManager().registerEvents(this, this);
        while(true){}
    }


    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        EntityType ep = event.getEntityType();
        switch (ep) {
            case PLAYER:
            case BOAT:
            case MINECART:
            case MINECART_TNT:
            case MINECART_CHEST:
            case MINECART_COMMAND:
            case MINECART_HOPPER:
            case MINECART_FURNACE:
            case MINECART_MOB_SPAWNER:
                return;
        }
        if (event.getEntity().getType() == EntityType.ENDERMAN && event.getEntity().getWorld().getName().equalsIgnoreCase("world_the_end")) {
            return;
        }
        if(event.getEntity().getLocation().getNearbyEntitiesByType(Player.class,10,10,10).isEmpty()){
            return;
        }
        if (event.getEntity().getType() == EntityType.BAT) {
            ItemStack is1 = pie.clone();
            is1.setAmount(3);
            event.getDrops().add(is1);
        } else if (event.getEntity().getType() == EntityType.WITCH) {
            event.getDrops().add(pie.clone());
        } else {
            if (ran.nextInt(3) == 1) {
                event.getDrops().add(pie.clone());
            }
        }

    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (event.getEntity().getType() == EntityType.ENDERMAN && event.getEntity().getWorld().getName().equalsIgnoreCase("world_the_end")) {
            return;
        }
        if (event.getLocation().getBlock().getType() == Material.CAVE_AIR) {
            return;
        }
        switch (event.getEntityType()) {
            case ZOMBIE:
            case SKELETON:
            case SPIDER:
            case VILLAGER:
            case ZOMBIE_VILLAGER:
                if (ran.nextInt(2) == 1) {
                    Location l = event.getLocation();
                    l.setX(l.getBlockX()+ran.nextInt(5)-2.5);
                    l.setZ(l.getBlockZ()+ran.nextInt(5)-2.5);
                    while(l.getBlock().getType()!=Material.AIR){
                        l.setY(l.getY()+2);
                    }
                    event.getEntity().getWorld().spawnEntity(l, EntityType.WITCH);
                }
        }
        if(!(event.getEntity() instanceof CraftItem)){
            EntityEquipment eq = ((LivingEntity)event.getEntity()).getEquipment();
            eq.setHelmet(la.clone());
            eq.setHelmetDropChance(0.07F);
            ((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED,0xFFFFFFFF,0x1),true);
            if(event.getEntityType()==EntityType.ZOMBIE){
                eq.setItemInMainHand(new ItemStack(Material.STONE_SWORD));
                eq.setItemInMainHandDropChance(0);
            }
        }

    }

    @EventHandler
    public void onRightClick(BlockPlaceEvent event){
        if(event.getItemInHand().getLore()!=null){if(event.getItemInHand().getLore().get(0).equalsIgnoreCase(c("&7恐吓 I"))){
            event.setCancelled(true);
        }}
    }

    @EventHandler
    public void onFish(PlayerFishEvent event){
        event.getPlayer().sendMessage(c("Called PlayerFishEvent.&5"+event.getState().name()));
    }


}


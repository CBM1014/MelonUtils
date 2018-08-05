package red.cbm.CBMUtils.Utils;


import org.bukkit.Location;


public class MelonLocation {
    public String world;
    public Double x;
    public Double y;
    public Double z;
    public Float yaw;
    public Float pitch;
    public MelonLocation(Location location){
        this.world=location.getWorld().getName();
        this.x=location.getX();
        this.y=location.getY();
        this.z=location.getZ();
        this.yaw=location.getYaw();
        this.pitch=location.getPitch();
    }
    public MelonLocation(String world, Double x, Double y, Double z, Float yaw, Float pitch){
        this.world=world;
        this.x=x;
        this.y=y;
        this.z=z;
        this.yaw=yaw;
        this.pitch=pitch;
    }

    public MelonLocation(){}

    @Override
    public String toString() {

        return "§7x: §3§l"+x.intValue()+"   §7y: §3§l"+y.intValue()+"   §7z: §3§l"+z.intValue()+"   §3§l"+world;
    }
}

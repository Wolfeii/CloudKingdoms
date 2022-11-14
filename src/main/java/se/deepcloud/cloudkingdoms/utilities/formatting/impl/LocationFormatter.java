package se.deepcloud.cloudkingdoms.utilities.formatting.impl;


import org.bukkit.Location;
import se.deepcloud.cloudkingdoms.utilities.formatting.IFormatter;

public class LocationFormatter implements IFormatter<Location> {

    private static final LocationFormatter INSTANCE = new LocationFormatter();

    public static LocationFormatter getInstance() {
        return INSTANCE;
    }

    private LocationFormatter() {

    }

    @Override
    public String format(Location value) {
        String worldName = value.getWorld() == null ? "null" : value.getWorld().getName();
        return worldName + ", " + value.getBlockX() + ", " + value.getBlockY() + ", " + value.getBlockZ();
    }
}
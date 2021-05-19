package me.zombiestriker.customitemframework;

import org.bukkit.Location;
import org.bukkit.Material;

public class CustomBlock extends CustomEntityType{
    public CustomBlock(String internalName, Material material, String displayname, String modelpath, int custommodeldata) {
        super(internalName, material, displayname, modelpath, custommodeldata);
    }

    public static CustomEntity placeCustomBlockAt(Location location, CustomBlock customBlock){
        CustomEntity e = new CustomEntity(location,customBlock);
        return e;
    }
}

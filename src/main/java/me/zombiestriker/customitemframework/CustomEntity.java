package me.zombiestriker.customitemframework;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.yaml.snakeyaml.Yaml;

import javax.accessibility.AccessibleComponent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomEntity implements ConfigurationSerializable {

    private static HashMap<CustomEntity, ArmorStand> entities = new HashMap<>();

    private static File file;
    private static FileConfiguration configuration;

    private ArmorStand baseEntity;
    private CustomEntityType type;

    public static void init(File entitydata){
        file = entitydata;
        configuration = YamlConfiguration.loadConfiguration(file);
        configuration.get("data");
    }
    public static void save(){
        configuration.set("data",entities.keySet());
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CustomEntity(HashMap<String, Object> map){
        Location loc = (Location) map.get("location");
        if(!loc.isWorldLoaded()){
            loc.getChunk().load();
        }
        for(Entity e : loc.getWorld().getNearbyEntities(loc,1,1,1)){
            if(e.getUniqueId().toString().equals(map.get("uuid"))){
                baseEntity = (ArmorStand) e;
            }
        }
        type = (CustomEntityType) CustomItemFramework.getCustomItem((String) map.get("type"));
        entities.put(this,baseEntity);
    }

    public CustomEntity(Location location, CustomEntityType type){
        this.type = type;
        baseEntity = location.getWorld().spawn(location.clone().add(0.5,0,0.5),ArmorStand.class);
        baseEntity.getEquipment().setHelmet(type.asItemStack());
        baseEntity.setSmall(true);
        baseEntity.setVisible(false);
        baseEntity.setInvulnerable(true);
        entities.put(this,baseEntity);
    }

    public static Set<CustomEntity> getCustomEntities(){
        return entities.keySet();
    }

    public static CustomEntity getCustomEntity(Entity e){
        for(Map.Entry<CustomEntity, ArmorStand> c :  entities.entrySet()){
            if(c.getValue().getUniqueId().equals(e.getUniqueId())){
                return c.getKey();
            }}
        return null;
    }

    public static void remove(CustomEntity ent) {
        entities.remove(ent);
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> s = new HashMap<>();
        s.put("world",baseEntity.getWorld().getName());
        s.put("location",baseEntity.getLocation());
        s.put("uuid",baseEntity.getUniqueId().toString());
        s.put("type",type.getInternalName());
        return s;
    }

    public Entity getEntity() {
        return baseEntity;
    }
    public CustomEntityType getType(){
        return type;
    }
}

package me.zombiestriker.customitemframework.resourcepack;

import me.zombiestriker.customitemframework.CustomItem;
import me.zombiestriker.customitemframework.CustomItemFramework;
import me.zombiestriker.customitemframework.utils.FileUtils;
import me.zombiestriker.customitemframework.utils.JsonHandler;
import me.zombiestriker.customitemframework.utils.ZipUtility;
import org.bukkit.Material;
import sun.net.www.protocol.jar.JarURLConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.zip.GZIPOutputStream;

public class ResourcepackCreator {

    public static void compile(File file, File outputFile) throws IOException {
        ZipUtility zip = new ZipUtility();
        System.out.println(outputFile.getPath());
        zip.zip(Arrays.asList(file.listFiles()), outputFile.getPath());
    }

    public static void generateResourcepackStructure(URL jarFile, File output) throws MalformedURLException {
        try {
            FileUtils.copyJarResourcesRecursively(output, new JarURLConnection(jarFile, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateSoundsJSON(File resourcepackFile) throws IOException {
        File dir = new File(resourcepackFile, "assets\\minecraft");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File soundsjsonfile = new File(dir, "sounds.json");
        JsonHandler soundsjson = new JsonHandler(soundsjsonfile);
        HashMap<String, Object> sounds = new HashMap<>();
        for (File f : new File(dir,"sounds").listFiles()) {
            HashMap<String, Object> inside = new HashMap<>();
            String name = f.getName().split("\\.")[0];
            inside.put("sounds", Collections.singletonList(name));
            inside.put("replace", false);
            sounds.put(name, inside);
        }
        soundsjson.writeJsonStream(sounds);

    }

    public static void generateModelJSON(File resourcepackFile) throws IOException {
        File dir = new File(resourcepackFile, "assets\\minecraft\\models\\item");
        HashMap<Material,List<CustomItem>> materialsAndCustomItems = new HashMap<>();
        for(CustomItem c : CustomItemFramework.getCustomItems()){
            if(materialsAndCustomItems.containsKey(c.getMaterial())){
                materialsAndCustomItems.get(c.getMaterial()).add(c);
            }else{
                List<CustomItem> linkedlist = new LinkedList<>();
                linkedlist.add(c);
                materialsAndCustomItems.put(c.getMaterial(),linkedlist);
            }
        }
        for(Map.Entry<Material, List<CustomItem>> entry : materialsAndCustomItems.entrySet()){
            File itemToReplace = new File(dir,entry.getKey().name().toLowerCase()+".json");
            JsonHandler jsonHandler = new JsonHandler(itemToReplace);
            HashMap<String, Object> map = new HashMap<>();
            map.put("parent", "item/generated");
            HashMap<String,Object> layer0 = new HashMap<>();
            layer0.put("layer0","item/"+entry.getKey().name().toLowerCase());
            map.put("textures",layer0);
            List<HashMap<String,Object>> overrides = new LinkedList<>();
            for(CustomItem c : entry.getValue()){
                HashMap<String, Object> data = new HashMap();
                HashMap<String,Object> custommodeldata = new HashMap<>();
                custommodeldata.put("custom_model_data",c.getCustomModelData());
                System.out.println("Custom Model Data  "+custommodeldata);
                data.put("predicate",custommodeldata);
                data.put("model",c.getModel());
                overrides.add(data);
            }
            map.put("overrides",overrides);
            jsonHandler.writeJsonStream(map);
        }
    }
}

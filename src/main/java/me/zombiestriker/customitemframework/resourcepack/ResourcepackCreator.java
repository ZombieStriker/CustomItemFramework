package me.zombiestriker.customitemframework.resourcepack;

import me.zombiestriker.customitemframework.CustomBlock;
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
        zip.zip(Arrays.asList(file.listFiles()), outputFile.getPath());
    }

    public static void generateResourcepackStructure(URL jarFile, File output) throws MalformedURLException {
        try {
            FileUtils.copyJarResourcesRecursively(output, new JarURLConnection(jarFile, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateNoteBlockBlockStateJson(File resourcepackFile) throws IOException {
        /**
         * {
         *   "variants": {
         *     "instrument=harp,note=0,powered=false": {
         *       "model": "minecraft:block/harp/0"
         *     },
         */

        File dir = new File(resourcepackFile, "assets\\minecraft\\blockstates");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File noteblock = new File(dir, "note_block.json");
        JsonHandler noteblockjson = new JsonHandler(noteblock);
        HashMap<String, Object> variants = new HashMap<>();
        HashMap<String, Object> instrumentdata = new HashMap<>();
        for(CustomBlock customBlock : CustomItemFramework.getCustomBlocks()) {
            HashMap<String, Object> models = new HashMap<>();
            models.put("model", customBlock.getModel());
            instrumentdata.put("instrument="+customBlock.getInstrument().name().toLowerCase()+",note="+customBlock.getNote().getId()+",powered=true",models);
        }
        variants.put("variants",instrumentdata);
        noteblockjson.writeJsonStream(variants);
    }

    public static void generateSingleFacedBlock(File resourcepackFile, String modelname, String texturepath) throws IOException {
        File dir = new File(resourcepackFile, "assets\\minecraft\\models\\block");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File materialfile = new File(dir, modelname+".json");

        JsonHandler materialjson = new JsonHandler(materialfile);
        HashMap<String, Object> overview = new HashMap<>();
        HashMap<String, Object> textures = new HashMap<>();
        textures.put("all",texturepath);
        overview.put("parent","minecraft:block/cube_all");
        overview.put("textures",textures);
        materialjson.writeJsonStream(overview);
    }

    public static void genereateMultifacedBlock(File resourcepackFile, String modelname, String westFaceTexturePath, String eastFaceTexturePath,
                                                String northFaceTexturePath, String southFaceTexturePath,
                                                String upFaceTexturePath, String downFaceTexturePath,String particleTexturePath) throws IOException {
        File dir = new File(resourcepackFile, "assets\\minecraft\\models\\block");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File materialfile = new File(dir, modelname+".json");

        JsonHandler materialjson = new JsonHandler(materialfile);
        HashMap<String, Object> overview = new HashMap<>();
        HashMap<String, Object> textures = new HashMap<>();
        textures.put("particle",particleTexturePath);
        textures.put("north",northFaceTexturePath);
        textures.put("south",southFaceTexturePath);
        textures.put("east",eastFaceTexturePath);
        textures.put("west",westFaceTexturePath);
        textures.put("up",upFaceTexturePath);
        textures.put("down",downFaceTexturePath);
        overview.put("parent","minecraft:block/cube");
        overview.put("textures",textures);
        materialjson.writeJsonStream(overview);
    }
    public static void generateSoundsJSON(File resourcepackFile) throws IOException {
        File dir = new File(resourcepackFile, "assets\\minecraft");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File soundsjsonfile = new File(dir, "sounds.json");
        JsonHandler soundsjson = new JsonHandler(soundsjsonfile);
        HashMap<String, Object> sounds = new HashMap<>();
        if (!new File(dir, "sounds").exists()) {
            new File(dir, "sounds").mkdirs();
        }
        for (File f : new File(dir, "sounds").listFiles()) {
            HashMap<String, Object> inside = new HashMap<>();
            String name = f.getName().split("\\.")[0];
            inside.put("sounds", Collections.singletonList(name));
            inside.put("replace", false);
            sounds.put(name, inside);
        }
        soundsjson.writeJsonStream(sounds);

    }

    public static void generateModelJSON(File resourcepackFile) throws IOException {
        HashMap<Material, List<CustomItem>> materialsAndCustomItems = new HashMap<>();
        for (CustomItem c : CustomItemFramework.getCustomItems()) {
            if (materialsAndCustomItems.containsKey(c.getMaterial())) {
                materialsAndCustomItems.get(c.getMaterial()).add(c);
            } else {
                List<CustomItem> linkedlist = new LinkedList<>();
                linkedlist.add(c);
                materialsAndCustomItems.put(c.getMaterial(), linkedlist);
            }
        }
        for (Map.Entry<Material, List<CustomItem>> entry : materialsAndCustomItems.entrySet()) {
            File dir;

            if (entry.getKey().isBlock()) {
               // dir = new File(resourcepackFile, "assets\\minecraft\\models\\block");
                dir = new File(resourcepackFile, "assets\\minecraft\\models\\item");
            } else {
                dir = new File(resourcepackFile, "assets\\minecraft\\models\\item");
            }
            if(!dir.exists())
                dir.mkdirs();
            File itemToReplace = new File(dir, entry.getKey().name().toLowerCase() + ".json");
            JsonHandler jsonHandler = new JsonHandler(itemToReplace);
            HashMap<String, Object> map = new HashMap<>();
            if (entry.getKey().isBlock()) {
                map.put("parent", "minecraft:block/cube_all");
                HashMap<String, Object> all = new HashMap<>();
                all.put("all", "minecraft:block/" + entry.getKey().name().toLowerCase());
                map.put("textures", all);
            } else {
                map.put("parent", "item/generated");
                HashMap<String, Object> layer0 = new HashMap<>();
                if (entry.getKey() == Material.CROSSBOW) {
                    layer0.put("layer0", "item/crossbow_standby");
                    map.put("textures", layer0);
                } else {
                    layer0.put("layer0", "item/" + entry.getKey().name().toLowerCase());
                    map.put("textures", layer0);
                }
            }


            /**
             *
             * {
             *     "parent": "item/generated",
             *     "textures": {
             *         "layer0": "item/crossbow_standby"
             *     },
             *     "display": {
             *         "thirdperson_righthand": {
             *             "rotation": [ -90, 0, -60 ],
             *             "translation": [ 2, 0.1, -3 ],
             *             "scale": [ 0.9, 0.9, 0.9 ]
             *         },
             *         "thirdperson_lefthand": {
             *             "rotation": [ -90, 0, 30 ],
             *             "translation": [ 2, 0.1, -3 ],
             *             "scale": [ 0.9, 0.9, 0.9 ]
             *         },
             *         "firstperson_righthand": {
             *             "rotation": [ -90, 0, -55 ],
             *             "translation": [ 1.13, 3.2, 1.13],
             *             "scale": [ 0.68, 0.68, 0.68 ]
             *         },
             *         "firstperson_lefthand": {
             *             "rotation": [ -90, 0, 35 ],
             *             "translation": [ 1.13, 3.2, 1.13],
             *             "scale": [ 0.68, 0.68, 0.68 ]
             *         }
             *     },
             *     "overrides": [
             *         {
             *             "predicate": {
             *                 "pulling": 1
             *             },
             *             "model": "item/crossbow_pulling_0"
             *         },
             *         {
             *             "predicate": {
             *                 "pulling": 1,
             *                 "pull": 0.58
             *             },
             *             "model": "item/crossbow_pulling_1"
             *         },
             *         {
             *             "predicate": {
             *                 "pulling": 1,
             *                 "pull": 1.0
             *             },
             *             "model": "item/crossbow_pulling_2"
             *         },
             *         {
             *             "predicate": {
             *                 "charged": 1
             *             },
             *             "model": "item/crossbow_arrow"
             *         },
             *         {
             *             "predicate": {
             *                 "charged": 1,
             *                 "firework": 1
             *             },
             *             "model": "item/crossbow_firework"
             *         },
             */


            List<HashMap<String, Object>> overrides = new LinkedList<>();

            if (entry.getKey() == Material.CROSSBOW) {
                HashMap<String, Object> data1 = new HashMap();
                HashMap<String, Object> predicate1 = new HashMap<>();
                predicate1.put("pulling", 1);
                data1.put("predicate", predicate1);
                data1.put("model", "item/crossbow_pulling_0");
                overrides.add(data1);


                HashMap<String, Object> data2 = new HashMap();
                HashMap<String, Object> predicate2 = new HashMap<>();
                predicate2.put("pulling", 1);
                predicate2.put("pull", 0.58);
                data2.put("predicate", predicate2);
                data2.put("model", "item/crossbow_pulling_1");
                overrides.add(data2);

                HashMap<String, Object> data3 = new HashMap();
                HashMap<String, Object> predicate3 = new HashMap<>();
                predicate3.put("pulling", 1);
                predicate3.put("pull", 1.0);
                data3.put("predicate", predicate3);
                data3.put("model", "item/crossbow_pulling_2");
                overrides.add(data3);


                HashMap<String, Object> data4 = new HashMap();
                HashMap<String, Object> predicate4 = new HashMap<>();
                predicate4.put("charged", 1);
                data4.put("predicate", predicate4);
                data4.put("model", "item/crossbow_arrow");
                overrides.add(data4);
            }

            List<CustomItem> customitems = entry.getValue();
            Collections.sort(customitems);
            for (CustomItem c : customitems) {
                HashMap<String, Object> data = new HashMap();
                HashMap<String, Object> custommodeldata = new HashMap<>();
                custommodeldata.put("custom_model_data", c.getCustomModelData());
                data.put("predicate", custommodeldata);
                data.put("model", c.getModel());
                overrides.add(data);
            }
            map.put("overrides", overrides);
            jsonHandler.writeJsonStream(map);
        }
    }
}

package me.zombiestriker.customitemframework;

import me.zombiestriker.customitemframework.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import sun.net.www.protocol.jar.JarURLConnection;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomItemFramework {

    private static List<CustomItem> customitems = new ArrayList<>();

    public static CustomItem getCustomItem(String internalname) {
        for (CustomItem c : customitems) {
            if (c.getInternalName().equals(internalname))
                return c;
        }
        return null;
    }

    public static CustomItem getCustomItem(ItemStack itemstack) {
        for (CustomItem customItem : customitems) {
            if (customItem.isSimilar(itemstack))
                return customItem;
        }
        return null;
    }

    public static void registerCustomItem(CustomItem customItem) {
        customitems.add(customItem);
    }


    public static void setResourcepackVersion(int version){
        Main.getInstance().setVersion(version);
    }
    public static void incrementResourcepackVersion(int version){
        Main.getInstance().setVersion(Main.getInstance().getVersion()+version);
    }


    public static void registerCustomModel(File modeljson){
        FileUtils.copyFile(modeljson,new File(Main.getInstance().getModelFolder(),modeljson.getName()));
    }
    public static void registerCustomSound(File soundogg){
        FileUtils.copyFile(soundogg,new File(Main.getInstance().getModelFolder(),soundogg.getName()));
    }
    public static void registerCustomTexture(File texturepng){
        FileUtils.copyFile(texturepng,new File(Main.getInstance().getModelFolder(),texturepng.getName()));
    }

    /**
     * Registers the custom model from within the jar.
     *
     * Example: registerCustomModel(Main.class.getResource("/path/to/model/model.json"),"test_model");
     * @param modeljson
     *  The path to the .json model file in the jar.
     * @param name
     *  The name of the file for the model, without the .json at the end.
     */
    public static void registerCustomModel(URL modeljson, String name){
        try {
            FileUtils.copyJarResourcesRecursively(new File(Main.getInstance().getModelFolder(),name+".json"), new JarURLConnection(modeljson, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Registers the custom sound from within the jar.
     *
     * Example: registerCustomSound(Main.class.getResource("/path/to/sound/sound.ogg"),"test_sound");
     * @param soundogg
     *  The path to the .ogg sound file in the jar.
     * @param name
     *  The name of the file for the sound, without the .ogg at the end.
     */
    public static void registerCustomSound(URL soundogg, String name){
        try {
            FileUtils.copyJarResourcesRecursively(new File(Main.getInstance().getModelFolder(),name+".ogg"), new JarURLConnection(soundogg, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers the custom texture from within the jar.
     *
     * Example: registerCustomTexture(Main.class.getResource("/path/to/texture/texture.ogg"),"test_texture");
     * @param texturepng
     *  The path to the .png texture file in the jar.
     * @param name
     *  The name of the file for the texture, without the .png at the end.
     */
    public static void registerCustomTexture(URL texturepng, String name){
        try {
            FileUtils.copyJarResourcesRecursively(new File(Main.getInstance().getModelFolder(),name+".png"), new JarURLConnection(texturepng, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Registers the custom texture from within the jar.
     *
     * Example: registerResourcepack(Main.class.getResource("/resourcepack/"));
     * @param resourcepackFolder
     *  The URL to the resourcepack folder
     */
    public static void registerResourcepack(URL resourcepackFolder){
        try {
            FileUtils.copyJarResourcesRecursively(Main.getInstance().getResourcepackFolder(), new JarURLConnection(resourcepackFolder, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<CustomItem> getCustomItems() {
        return customitems;
    }
}

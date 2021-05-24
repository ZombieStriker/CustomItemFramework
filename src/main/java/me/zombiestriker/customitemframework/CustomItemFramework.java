package me.zombiestriker.customitemframework;

import me.zombiestriker.customitemframework.resourcepack.ResourcepackCreator;
import me.zombiestriker.customitemframework.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;
import sun.net.www.protocol.jar.JarURLConnection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomItemFramework {

    private static List<CustomItem> customitems = new ArrayList<>();
    private static List<CustomBlock> customBlocks = new ArrayList<>();

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

    public static CustomBlock getCustomBlock(String internalName) {
        for (CustomBlock c : customBlocks) {
            if (c.getInternalName().equals(internalName))
                return c;
        }
        return null;
    }

    public static CustomBlock getCustomBlock(Block block) {
        for (CustomBlock c : customBlocks) {
            if (block.getType() == Material.NOTE_BLOCK) {
                NoteBlock data = (NoteBlock) block.getBlockData();
                if (data.getInstrument() == c.getInstrument() && data.getNote() == c.getNote())
                    return c;
            }
        }
        return null;
    }

    /**
     * Generates a custom block json file with the texture applied to all faces of the cube
     * @param name
     *  The name of the file
     * @param texturepath
     *  The texture to be applied on all faces
     * @return the path to the model
     */
    public static String generateCustomBlockModel(String name,String texturepath){
        try {
            ResourcepackCreator.generateSingleFacedBlock(Main.getInstance().getResourcepackFolder(),name,texturepath);
            return "block/"+name;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generates a custom block json file with all the textures specified.
     * @param name
     *  Name of the file
     * @param westFace
     * @param eastFace
     * @param northFace
     * @param southFace
     * @param upFace
     * @param downFace
     * @return the path to the model
     */
    public static String generateMultifacedCustomBlockModel(String name,String westFace, String eastFace, String northFace, String southFace, String upFace, String downFace){
        try {
            ResourcepackCreator.genereateMultifacedBlock(Main.getInstance().getResourcepackFolder(),name,westFace,eastFace,northFace,southFace,upFace,downFace,downFace);
            return "block/"+name;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void registerCustomItem(CustomItem customItem) {
        customitems.add(customItem);
    }

    public static void registerCustomBlock(CustomBlock customBlock){
        customBlocks.add(customBlock);
    }

    public static void setResourcepackVersion(int version) {
        Main.getInstance().setVersion(version);
    }

    public static void incrementResourcepackVersion(int version) {
        Main.getInstance().setVersion(Main.getInstance().getVersion() + version);
    }


    public static void registerCustomModel(File modeljson) {
        FileUtils.copyFile(modeljson, new File(Main.getInstance().getModelFolder(), modeljson.getName()));
    }

    public static void registerCustomSound(File soundogg) {
        FileUtils.copyFile(soundogg, new File(Main.getInstance().getModelFolder(), soundogg.getName()));
    }

    public static void registerCustomTexture(File texturepng) {
        FileUtils.copyFile(texturepng, new File(Main.getInstance().getModelFolder(), texturepng.getName()));
    }

    /**
     * Registers the custom model from within the jar.
     * <p>
     * Example: registerCustomModel(Main.class.getResource("/path/to/model/model.json"),"test_model");
     *
     * @param modeljson The path to the .json model file in the jar.
     * @param name      The name of the file for the model, without the .json at the end.
     */
    public static void registerCustomModel(URL modeljson, String name) {
        try {
            FileUtils.copyJarResourcesRecursively(new File(Main.getInstance().getModelFolder(), name + ".json"), new JarURLConnection(modeljson, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers the custom sound from within the jar.
     * <p>
     * Example: registerCustomSound(Main.class.getResource("/path/to/sound/sound.ogg"),"test_sound");
     *
     * @param soundogg The path to the .ogg sound file in the jar.
     * @param name     The name of the file for the sound, without the .ogg at the end.
     */
    public static void registerCustomSound(URL soundogg, String name) {
        try {
            FileUtils.copyJarResourcesRecursively(new File(Main.getInstance().getModelFolder(), name + ".ogg"), new JarURLConnection(soundogg, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers the custom texture from within the jar.
     * <p>
     * Example: registerCustomTexture(Main.class.getResource("/path/to/texture/texture.ogg"),"test_texture");
     *
     * @param texturepng The path to the .png texture file in the jar.
     * @param name       The name of the file for the texture, without the .png at the end.
     */
    public static void registerCustomTexture(URL texturepng, String name) {
        try {
            FileUtils.copyJarResourcesRecursively(new File(Main.getInstance().getModelFolder(), name + ".png"), new JarURLConnection(texturepng, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers the custom texture from within the jar.
     * <p>
     * Example: registerResourcepack(Main.class.getResource("/resourcepack/"));
     *
     * @param resourcepackFolder The URL to the resourcepack folder
     */
    public static void registerResourcepack(URL resourcepackFolder) {
        try {
            FileUtils.copyJarResourcesRecursively(Main.getInstance().getResourcepackFolder(), new JarURLConnection(resourcepackFolder, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<CustomItem> getCustomItems() {
        return customitems;
    }

    public static List<CustomBlock> getCustomBlocks() {
        return customBlocks;
    }

    public static CustomBlock getCustomBlockAt(Location location) {
        for(CustomBlock customBlock : getCustomBlocks()){
            if(location.getBlock().getType()==customBlock.getMaterial()){
                if(customBlock.getMaterial()==Material.NOTE_BLOCK){
                    NoteBlock noteblock = (NoteBlock) location.getBlock().getBlockData();
                    if(noteblock.getNote().getTone()==customBlock.getNote().getTone()&&noteblock.getNote().getOctave()==customBlock.getNote().getOctave()&&noteblock.getInstrument()==customBlock.getInstrument())
                        return customBlock;
                }
            }
        }
        return null;
    }
    public static CustomBlock getCustomBlockByBlockData(BlockData blockData) {
        for(CustomBlock customBlock : getCustomBlocks()){
            if(blockData.getMaterial()==customBlock.getMaterial()){
                if(customBlock.getMaterial()==Material.NOTE_BLOCK){
                    NoteBlock noteblock = (NoteBlock) blockData;
                    if(noteblock.getNote().getTone()==customBlock.getNote().getTone()&&noteblock.getNote().getOctave()==customBlock.getNote().getOctave()&&noteblock.getInstrument()==customBlock.getInstrument())
                        return customBlock;
                }
            }
        }
        return null;
    }
}

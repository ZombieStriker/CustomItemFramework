package me.zombiestriker.customitemframework;

import me.zombiestriker.customitemframework.listeners.CIFListener;
import me.zombiestriker.customitemframework.resourcepack.ResourcepackCreator;
import me.zombiestriker.customitemframework.resourcepack.ResourcepackDaemon;
import me.zombiestriker.customitemframework.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Main extends JavaPlugin {

    private File resourcepackFolder;
    private File resourcepackFile;
    private File customItemYmlFolder;

    private String resourcepackFileOutput = "resourcepack.zip";

    private static Main main;
    private int version = 1;


    public void setVersion(int version){
        this.version = version;
    }
    public int getVersion(){
        return version;
    }
    protected static Main getInstance(){
        return main;
    }
    public File getResourcepackFolder(){
        return resourcepackFolder;
    }
    public File getModelFolder(){
        return new File(resourcepackFolder,"/assets/minecraft/models/item");
    }
    public File getTextureFolder(){
        return new File(resourcepackFolder,"/assets/minecraft/textures/item");
    }
    public File getSoundsFolder(){
        return new File(resourcepackFolder,"/assets/minecraft/sounds");
    }


    private int port;
    private String ip;

    private ResourcepackDaemon daemon;


    private static final String IP_URL = "https://api.ipify.org";

    public static String getIp() {
        try {
            URL url = new URL(IP_URL);
            InputStream stream = url.openStream();
            Scanner s = new Scanner(stream, "UTF-8").useDelimiter("\\A");
            String ip = s.next();
            s.close();
            stream.close();
            return ip;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onEnable() {
        Main.main=this;
        Bukkit.getPluginManager().registerEvents(new CIFListener(this), this);

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        customItemYmlFolder = new File(getDataFolder(), "customitems");
        if (!customItemYmlFolder.exists()) {
            customItemYmlFolder.mkdirs();
        }


        resourcepackFolder = new File(getDataFolder(), "resourcepack");
        if (!resourcepackFolder.exists()) {
            resourcepackFolder.mkdirs();
        }
        try {
            ResourcepackCreator.generateResourcepackStructure(Main.class.getResource("/defaultcustomitems/"), customItemYmlFolder);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            ResourcepackCreator.generateResourcepackStructure(Main.class.getResource("/defaultresourcepack/"), resourcepackFolder);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        resourcepackFile = new File(getDataFolder(), resourcepackFileOutput);

        try {
            ResourcepackCreator.generateSoundsJSON(resourcepackFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CustomItemFramework.registerCustomItem(new CustomItem("test", Material.STONE,"TEST","item/test",1));
        CustomItemFramework.registerCustomItem(new CustomItem("uranium", Material.STONE,"Uranium","item/uranium",2));
        CustomItemFramework.registerCustomItem(new CustomItem("dynamite", Material.STONE,"Dynamite","item/dynamite", 3));

        CustomItemFramework.registerCustomBlock(new CustomBlock("testblock", Instrument.BELL, Note.natural(0, Note.Tone.E),"block/stone",CustomItemFramework.getCustomItem("test")));
        CustomItemFramework.registerCustomBlock(new CustomBlock("testblock2", Instrument.BELL, Note.natural(0, Note.Tone.A),"block/dirt",CustomItemFramework.getCustomItem("uranium")));
        CustomItemFramework.registerCustomBlock(new CustomBlock("testblock3", Instrument.BELL, Note.natural(0, Note.Tone.B),"block/crafting_table",CustomItemFramework.getCustomItem("dynamite")));

        if(!getConfig().contains("enableResourcepackDaemon")){
            getConfig().set("enableResourcepackDaemon",true);
            saveConfig();
        }

        if(getConfig().getBoolean("enableResourcepackDaemon")) {
            new BukkitRunnable() {
                public void run() {


                    try {
                        ResourcepackCreator.generateModelJSON(resourcepackFolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        ResourcepackCreator.generateNoteBlockBlockStateJson(resourcepackFolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    try {
                        ResourcepackCreator.compile(resourcepackFolder, resourcepackFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!getConfig().contains("port")) {
                        getConfig().set("port", 8080);
                        saveConfig();
                    }
                    port = getConfig().getInt("port");
                    if (true) {
                        ip = "localhost";
                    } else {
                        ip = getIp();
                    }
                    try {
                        daemon = new ResourcepackDaemon(port, resourcepackFile);
                        daemon.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    CustomEntity.init(new File(getDataFolder(), "entitydata.yml"));

                }
            }.runTaskLater(this, 0);
        }
    }

    @Override
    public void onDisable() {
        if(daemon!=null)
        daemon.running = false;
        CustomEntity.save();
    }

    public void a (List<String> tabs, String args, String compareTo){
        if(compareTo.toLowerCase().startsWith(args.toLowerCase())){
            tabs.add(compareTo);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();
        if(args.length==1){
            a(tab,args[0],"give");
            return tab;
        }
        if(args[0].equalsIgnoreCase("give")){
            if(args.length==2) {
                for (CustomItem c : CustomItemFramework.getCustomItems()) {
                    a(tab, args[1], c.getInternalName());
                }
            }
        }
            return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()){
            return true;
        }
        if(args.length==0){
            sender.sendMessage("<CustomItemFramework>:");
            sender.sendMessage("/cif give <itemname>  - Gives you the item");
            return true;
        }
        if(args[0].equals("give")){
            if(args.length==1){
                for(CustomItem c : CustomItemFramework.getCustomItems()){
                    sender.sendMessage(c.getInternalName());
                }
                return true;
            }
            String internalName = args[1];
            CustomItem customitem = CustomItemFramework.getCustomItem(internalName);
            ((Player)sender).getInventory().addItem(customitem.asItemStack());
            return true;
        }
        return true;
    }

    /**
     * Send a resource pack to the desired player
     *
     * @param player The player to send it to, note that they must have the right permission to download the pack
     */
    public void sendResourcePack(Player player) {
        player.setResourcePack("http://" + ip + ":" + port + "/" +"version"+version+"/"+resourcepackFileOutput);
    }
}

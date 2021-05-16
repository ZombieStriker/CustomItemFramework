package me.zombiestriker.customitemframework;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomItem {


    private String internalName;
    private Material material;
    private String displayname;
    private int custommodeldata;
    private String modelpath = "item/flatitem";
    private List<String> lore;
    private ItemEvents itemeventtype;

    public CustomItem(String internalName, Material material, String displayname, String modelpath, int custommodeldata) {
        this(internalName, material, displayname, modelpath,custommodeldata, null, ItemEvents.DEFAULT);
    }

    public CustomItem(String internalName, Material material, String displayname, String modelpath, int custommodeldata, List<String> lore) {
        this(internalName, material, displayname, modelpath,custommodeldata, lore, ItemEvents.DEFAULT);
    }

    public CustomItem(String internalName, Material material, String displayname, String modelpath, int custommodeldata, ItemEvents type) {
        this(internalName, material, displayname,modelpath,custommodeldata, null, type);
    }

    public CustomItem(String internalName, Material material, String displayname,String modelpath,  int custommodeldata, List<String> lore, ItemEvents type) {
        this.material = material;
        this.displayname = displayname;
        this.internalName = internalName;
        this.custommodeldata = custommodeldata;
        this.lore = lore;
        this.itemeventtype = type;
        this.modelpath = modelpath;
    }

    public ItemStack asItemStack() {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.RESET+displayname);
        if (lore != null) {
            im.setLore(lore);
        }
        im.setCustomModelData(custommodeldata);
        is.setItemMeta(im);
        return is;
    }

    public String getInternalName() {
        return internalName;
    }

    public boolean isSimilar(ItemStack base) {
        if (base.getType() == material) {
            if(base.getItemMeta().hasCustomModelData() && base.getItemMeta().getCustomModelData() == custommodeldata) {
                //TODO: You may want to also check fore lore if you want items to be lore specific, but I don't think you need to.
                return true;
            }
        }
        return false;
    }

    public ItemEvents getItemEvents() {
        return itemeventtype;
    }

    public Material getMaterial() {
        return material;
    }
    public int getCustomModelData(){
        return custommodeldata;
    }
    public void setModelpath(String path){
        this.modelpath = path;
    }

    public Object getModel() {
        return modelpath;
    }
}

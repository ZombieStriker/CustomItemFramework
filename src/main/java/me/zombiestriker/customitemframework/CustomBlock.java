package me.zombiestriker.customitemframework;

import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CustomBlock{

    private Material material;
    private Instrument instrument;
    private Note note;
    private String internalname;

    private CustomItem item;
    private String modelpath;

    private List<ItemStack> drops = null;


    public CustomBlock(String internalName, Instrument instrument, Note note, String modelpath, CustomItem item) {
        this.material = Material.NOTE_BLOCK;
        this.instrument = instrument;
        this.note = note;
        this.internalname= internalName;
        this.item = item;
        this.modelpath = modelpath;
    }
    public CustomBlock(String internalName, Instrument instrument, Note note, String modelpath, String customItem) {
        this.material = Material.NOTE_BLOCK;
        this.instrument = instrument;
        this.note = note;
        this.internalname= internalName;
        this.modelpath = modelpath;
        this.item = CustomItemFramework.getCustomItem(customItem);
    }

    public void setDrops(ItemStack... itemstacks){
        this.drops = Arrays.asList(itemstacks);
    }

    public String getInternalName(){
        return internalname;
    }
    public Material getMaterial(){
        return material;
    }

    public void placeAt(Location loc){
        loc.getBlock().setType(material);

        if(material == Material.NOTE_BLOCK){
            NoteBlock noteblock = (NoteBlock) loc.getBlock().getBlockData();
            noteblock.setNote(note);
            noteblock.setInstrument(instrument);
        }
    }

    public Note getNote() {
        return note;
    }

    public CustomItem getItem(){
        return item;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public String getModel() {
        return modelpath;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }
}

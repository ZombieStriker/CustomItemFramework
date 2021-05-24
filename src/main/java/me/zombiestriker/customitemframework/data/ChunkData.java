package me.zombiestriker.customitemframework.data;

import me.zombiestriker.customitemframework.CustomBlock;
import me.zombiestriker.customitemframework.CustomItemFramework;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class ChunkData {
    private static HashMap<ChunkLocation, ChunkData> chunksAndTheirCustomBlocks = new HashMap<>();
    private HashMap<Location, CustomBlock> blocks = new HashMap<>();

    public static ChunkData getChunkAt(Chunk chunk){
        for(Map.Entry<ChunkLocation, ChunkData> c : chunksAndTheirCustomBlocks.entrySet()){
            if(c.getKey().getX()==chunk.getX()&&c.getKey().getZ()==chunk.getZ()){
                return c.getValue();
            }
        }
        ChunkData chunkData = new ChunkData();
        chunksAndTheirCustomBlocks.put(new ChunkLocation(chunk),chunkData);
        return chunkData;
    }

    public static void addCustomBlock(Location location, CustomBlock customBlock){
     ChunkData chunkData = getChunkAt(location.getChunk());
     chunkData.blocks.put(location, customBlock);
    }
    public static CustomBlock getCustomBlockAt(Location location){
        ChunkData chunkData = getChunkAt(location.getChunk());
        return chunkData.blocks.get(location);
    }
    public static void removeCustomBlock(Location location){
        ChunkData chunkData = getChunkAt(location.getChunk());
        chunkData.blocks.remove(location);
    }
}

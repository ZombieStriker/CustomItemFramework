package me.zombiestriker.customitemframework.data;

import org.bukkit.Chunk;

import java.util.Objects;

public class ChunkLocation {

    private int x;
    private int z;

    public ChunkLocation(Chunk chunk){
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    public int getX(){
        return x;
    }
    public int getZ(){
        return z;
    }
}

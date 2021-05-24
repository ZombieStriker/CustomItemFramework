package me.zombiestriker.customitemframework.events;

import me.zombiestriker.customitemframework.CustomBlock;
import me.zombiestriker.customitemframework.CustomEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeftClickCustomBlockEvent extends Event {

    private Player player;
    private CustomBlock block;
    private Location location;

    public PlayerLeftClickCustomBlockEvent(Player hitter, CustomBlock block, Location location){
        this.player = hitter;
        this.block = block;
        this.location = location;
    }
    public CustomBlock getCustomBlock(){
        return block;
    }
    public Location getBlockLocation(){
        return location;
    }
    public Player getPlayer(){
        return player;
    }
    private static final HandlerList handlers = new HandlerList();


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

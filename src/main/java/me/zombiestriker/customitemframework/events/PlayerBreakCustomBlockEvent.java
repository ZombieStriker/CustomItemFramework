package me.zombiestriker.customitemframework.events;

import me.zombiestriker.customitemframework.CustomBlock;
import me.zombiestriker.customitemframework.CustomEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBreakCustomBlockEvent extends Event implements Cancellable {

    private Player player;
    private CustomBlock entity;
    private Location location;
    private boolean cancel = false;

    public PlayerBreakCustomBlockEvent(Player player, CustomBlock customBlock, Location location) {
        this.location = location;
        this.player = player;
        this.entity=customBlock;
    }

    public CustomBlock getBlockEntity(){
        return entity;
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

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
this.cancel = cancel;
    }
}

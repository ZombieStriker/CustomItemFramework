package me.zombiestriker.customitemframework.events;

import me.zombiestriker.customitemframework.CustomEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPlaceCustomBlockEvent extends Event implements Cancellable {

    private Player player;
    private CustomEntity entity;
    private boolean cancel = false;

    public PlayerPlaceCustomBlockEvent(Player hitter, CustomEntity entity){
        this.player = hitter;
        this.entity = entity;
    }
    public CustomEntity getBlockEntity(){
        return entity;
    }
    public Location getBlockLocation(){
        return entity.getEntity().getLocation();
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

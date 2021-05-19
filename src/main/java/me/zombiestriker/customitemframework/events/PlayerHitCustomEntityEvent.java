package me.zombiestriker.customitemframework.events;

import me.zombiestriker.customitemframework.CustomEntity;
import me.zombiestriker.customitemframework.CustomEntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerHitCustomEntityEvent extends Event {

    private Player player;
    private CustomEntity entity;

    public PlayerHitCustomEntityEvent(Player hitter, CustomEntity entity){
        this.player = hitter;
        this.entity = entity;
    }
    public CustomEntity getEntity(){
        return entity;
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

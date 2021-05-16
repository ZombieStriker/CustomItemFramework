package me.zombiestriker.customitemframework;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public abstract class ItemEvents {

    public static final ItemEvents DEFAULT = new ItemEvents() {
        @Override
        public void onLeftCLick(PlayerInteractEvent event) {
        }

        @Override
        public void onRightClick(PlayerInteractEvent event) {
        }

        @Override
        public void onRightClickEntity(PlayerInteractEntityEvent event) {
        }

        @Override
        public void onConsume(PlayerItemConsumeEvent event) {
        }

        @Override
        public void onDrop(PlayerDropItemEvent event) {
        }
    };

    public abstract void onLeftCLick(PlayerInteractEvent event);

    public abstract void onRightClick(PlayerInteractEvent event);

    public abstract void onRightClickEntity(PlayerInteractEntityEvent event);

    public abstract void onConsume(PlayerItemConsumeEvent event);

    public abstract void onDrop(PlayerDropItemEvent event);
}
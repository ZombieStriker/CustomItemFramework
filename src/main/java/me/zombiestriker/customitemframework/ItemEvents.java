package me.zombiestriker.customitemframework;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public abstract class ItemEvents {

    public static final ItemEvents DEFAULT = new ItemEvents() {
        @Override
        public void onLeftCLick(PlayerInteractEvent event, CustomItem item) {
        }

        @Override
        public void onRightClick(PlayerInteractEvent event, CustomItem item) {
        }

        @Override
        public void onRightClickEntity(PlayerInteractEntityEvent event, CustomItem item) {
        }

        @Override
        public void onConsume(PlayerItemConsumeEvent event, CustomItem item) {
        }

        @Override
        public void onDrop(PlayerDropItemEvent event, CustomItem item) {
        }
    };

    public abstract void onLeftCLick(PlayerInteractEvent event, CustomItem item);

    public abstract void onRightClick(PlayerInteractEvent event, CustomItem item);

    public abstract void onRightClickEntity(PlayerInteractEntityEvent event, CustomItem item);

    public abstract void onConsume(PlayerItemConsumeEvent event, CustomItem item);

    public abstract void onDrop(PlayerDropItemEvent event, CustomItem item);
}
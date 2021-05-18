package me.zombiestriker.customitemframework.listeners;

import me.zombiestriker.customitemframework.CustomItem;
import me.zombiestriker.customitemframework.CustomItemFramework;
import me.zombiestriker.customitemframework.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CIFListener implements Listener {

    private Main main;

    public CIFListener(Main main) {
    this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        new BukkitRunnable(){
            public void run(){
                main.sendResourcePack(e.getPlayer());
            }
        }.runTaskLater(main,5);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        CustomItem customItem = CustomItemFramework.getCustomItem(event.getItem());
        if (customItem != null) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                customItem.getItemEvents().onLeftCLick(event,customItem);
            } else {
                customItem.getItemEvents().onRightClick(event,customItem);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        CustomItem customItem = CustomItemFramework.getCustomItem(event.getItemDrop().getItemStack());
        if (customItem != null) {
            customItem.getItemEvents().onDrop(event,customItem);
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        CustomItem customItem = CustomItemFramework.getCustomItem(event.getItem());
        if (customItem != null) {
            customItem.getItemEvents().onConsume(event,customItem);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event) {
        CustomItem customItem = CustomItemFramework.getCustomItem(event.getPlayer().getInventory().getItemInMainHand());
        if (customItem != null) {
            customItem.getItemEvents().onRightClickEntity(event,customItem);
        }
    }


    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        for (int i = 0; i < 9; i++) {
            if (CustomItemFramework.getCustomItem(e.getInventory().getMatrix()[i]) != null) {
                //TODO: Add variant where you can only craft an item with a custom item
                e.getInventory().setResult(new ItemStack(Material.AIR));
                break;
            }
        }
    }
}

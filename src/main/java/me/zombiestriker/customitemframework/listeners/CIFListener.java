package me.zombiestriker.customitemframework.listeners;

import me.zombiestriker.customitemframework.*;
import me.zombiestriker.customitemframework.events.PlayerBreakCustomBlockEvent;
import me.zombiestriker.customitemframework.events.PlayerHitCustomEntityEvent;
import me.zombiestriker.customitemframework.events.PlayerPlaceCustomBlockEvent;
import me.zombiestriker.customitemframework.events.PlayerRightClickCustomEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class CIFListener implements Listener {

    private Main main;

    public CIFListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        new BukkitRunnable() {
            public void run() {
                main.sendResourcePack(e.getPlayer());
            }
        }.runTaskLater(main, 5);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        CustomItem customItem = CustomItemFramework.getCustomItem(e.getItem());
        if (customItem != null) {
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                customItem.getItemEvents().onLeftCLick(e, customItem);
            } else {
                customItem.getItemEvents().onRightClick(e, customItem);
            }
        }
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null)
                if (e.getClickedBlock().getType() == Material.BARRIER) {
                    for (CustomEntity ent : new ArrayList<>(CustomEntity.getCustomEntities())) {
                        if (ent.getType() instanceof CustomBlock) {
                            if (ent.getEntity().getLocation().getBlock().getLocation().equals(e.getClickedBlock().getLocation())) {
                                e.getClickedBlock().setType(ent.getType().getMaterial(), false);
                                break;
                            }
                        }
                    }
                }
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            CustomEntity entity = CustomEntity.getCustomEntity(e.getEntity());
            if (entity != null) {
                Bukkit.getPluginManager().callEvent(new PlayerHitCustomEntityEvent((Player) e.getDamager(), entity));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void armorstandManipulateEvent(PlayerArmorStandManipulateEvent e) {
        CustomEntity entity = CustomEntity.getCustomEntity(e.getRightClicked());
        if (entity != null) {
            Bukkit.getPluginManager().callEvent(new PlayerRightClickCustomEntityEvent(e.getPlayer(), entity));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void armorstandManipulateEvent(PlayerInteractEntityEvent e) {
        CustomEntity entity = CustomEntity.getCustomEntity(e.getRightClicked());
        if (entity != null) {
            Bukkit.getPluginManager().callEvent(new PlayerRightClickCustomEntityEvent(e.getPlayer(), entity));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void armorstandManipulateEvent(PlayerInteractAtEntityEvent e) {
        CustomEntity entity = CustomEntity.getCustomEntity(e.getRightClicked());
        if (entity != null) {
            Bukkit.getPluginManager().callEvent(new PlayerRightClickCustomEntityEvent(e.getPlayer(), entity));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        for(Block b : event.blockList()){
            for (CustomEntity ent : new ArrayList<>(CustomEntity.getCustomEntities())) {
                if (ent.getType() instanceof CustomBlock) {
                    if (ent.getEntity().getLocation().getBlock().getLocation().equals(b.getLocation())) {
                        CustomEntity.remove(ent);
                        ent.getEntity().remove();
                    }
                }
            }
        }
    }
    @EventHandler
    public void onExplode(BlockExplodeEvent event){
        for(Block b : event.blockList()){
            for (CustomEntity ent : new ArrayList<>(CustomEntity.getCustomEntities())) {
                if (ent.getType() instanceof CustomBlock) {
                    if (ent.getEntity().getLocation().getBlock().getLocation().equals(b.getLocation())) {
                       CustomEntity.remove(ent);
                       ent.getEntity().remove();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlace(BlockPlaceEvent e) {
        CustomItem item = CustomItemFramework.getCustomItem(e.getItemInHand());
        if (item != null && item instanceof CustomBlock) {
            CustomBlock block = (CustomBlock) item;
            CustomEntity customEntity = CustomBlock.placeCustomBlockAt(e.getBlock().getLocation(), block);
            Bukkit.getPluginManager().callEvent(new PlayerPlaceCustomBlockEvent(e.getPlayer(), customEntity));
            e.getBlock().setType(Material.BARRIER);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent e) {
        for (CustomEntity ent : new ArrayList<>(CustomEntity.getCustomEntities())) {
            if (ent.getType() instanceof CustomBlock) {
                if (ent.getEntity().getLocation().getBlock().getLocation().equals(e.getBlock().getLocation())) {
                    PlayerBreakCustomBlockEvent event = new PlayerBreakCustomBlockEvent(e.getPlayer(), ent);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        e.setCancelled(true);
                    } else {
                        ent.getEntity().remove();
                        CustomEntity.remove(ent);
                        e.setDropItems(false);
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5), ent.getType().asItemStack());
                    }
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        CustomItem customItem = CustomItemFramework.getCustomItem(event.getItemDrop().getItemStack());
        if (customItem != null) {
            customItem.getItemEvents().onDrop(event, customItem);
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        CustomItem customItem = CustomItemFramework.getCustomItem(event.getItem());
        if (customItem != null) {
            customItem.getItemEvents().onConsume(event, customItem);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event) {
        CustomItem customItem = CustomItemFramework.getCustomItem(event.getPlayer().getInventory().getItemInMainHand());
        if (customItem != null) {
            customItem.getItemEvents().onRightClickEntity(event, customItem);
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

package me.zombiestriker.customitemframework.listeners;

import me.zombiestriker.customitemframework.*;
import me.zombiestriker.customitemframework.data.ChunkData;
import me.zombiestriker.customitemframework.data.ChunkLocation;
import me.zombiestriker.customitemframework.events.PlayerBreakCustomBlockEvent;
import me.zombiestriker.customitemframework.events.PlayerHitCustomEntityEvent;
import me.zombiestriker.customitemframework.events.PlayerRightClickCustomBlockEvent;
import me.zombiestriker.customitemframework.events.PlayerRightClickCustomEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CIFListener implements Listener {

    private Main main;

    private HashMap<Location, BukkitTask> mineable = new HashMap<>();

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
    public void onPhysics(BlockPhysicsEvent event){
        CustomBlock customBlock = ChunkData.getCustomBlockAt(event.getBlock().getLocation());
        if(customBlock!=null){
            event.setCancelled(true);
        }
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
            if (e.getClickedBlock() != null) {
                //TODO: Set required mining type
            }

        } else {
            if (e.getHand() == EquipmentSlot.HAND) {
                if (e.getClickedBlock() != null) {
                    CustomBlock block = CustomItemFramework.getCustomBlockAt(e.getClickedBlock().getLocation());
                    if (block != null) {
                        PlayerRightClickCustomBlockEvent event = new PlayerRightClickCustomBlockEvent(e.getPlayer(), block, e.getClickedBlock().getLocation());
                        Bukkit.getPluginManager().callEvent(event);
                        e.setCancelled(true);
                    }
                }
            } else {
                if (e.getClickedBlock() != null) {
                    CustomBlock block = CustomItemFramework.getCustomBlockAt(e.getClickedBlock().getLocation());
                    if (block != null) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNoteblock(NotePlayEvent e) {
        if (CustomItemFramework.getCustomBlockAt(e.getBlock().getLocation()) != null) {
            new BukkitRunnable() {
                public void run() {
                    NoteBlock noteBlock = (NoteBlock) e.getBlock().getBlockData();
                    noteBlock.setPowered(false);
                    e.getBlock().setBlockData(noteBlock);
                }
            }.runTaskLater(main, 1);
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

    @EventHandler(priority = EventPriority.LOW)
    public void onPlace(BlockPlaceEvent e) {
        CustomItem item = CustomItemFramework.getCustomItem(e.getItemInHand());

        if (e.getBlockPlaced().getType() == Material.REDSTONE_TORCH || e.getBlockPlaced().getType() == Material.REDSTONE_WALL_TORCH || e.getBlockPlaced().getType() == Material.REDSTONE_BLOCK) {
            BlockFace[] directions = new BlockFace[]{BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN};
            for (BlockFace blockFace : directions) {
                Block block = e.getBlockPlaced().getRelative(blockFace);
                if (CustomItemFramework.getCustomBlockAt(block.getLocation()) != null) {
                    e.setCancelled(true);
                    return;
                }
            }
        }


        if (item != null && item.getMaterial().isBlock()) {
            for (CustomBlock customBlock : CustomItemFramework.getCustomBlocks()) {
                if (customBlock.getItem() == item) {
                    e.getBlockPlaced().setType(customBlock.getMaterial());
                    if (customBlock.getMaterial() == Material.NOTE_BLOCK) {
                        NoteBlock noteblock = (NoteBlock) e.getBlockPlaced().getBlockData();
                        noteblock.setInstrument(customBlock.getInstrument());
                        noteblock.setNote(customBlock.getNote());
                        noteblock.setPowered(true);
                        e.getBlockPlaced().setBlockData(noteblock);
                        ChunkData.addCustomBlock(e.getBlockPlaced().getLocation(),customBlock);
                        break;
                    }
                }
            }
            /*CustomBlock block = (CustomBlock) item;
            CustomEntity customEntity = CustomBlock.placeCustomBlockAt(e.getBlock().getLocation(), block);
            Bukkit.getPluginManager().callEvent(new PlayerPlaceCustomBlockEvent(e.getPlayer(), customEntity));
            e.getBlock().setType(Material.BARRIER);*/
        }
    }

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent event) {
        CustomBlock customBlock = CustomItemFramework.getCustomBlockByBlockData(event.getBlockState().getBlockData());
        if (customBlock != null) {
            if (event.getPlayer() == null || event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                List<ItemStack> drops = customBlock.getDrops();
                event.setCancelled(true);
                if (drops == null) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(0.5, 0.5, 0.5), customBlock.getItem().asItemStack());
                } else {
                    for (ItemStack is : drops) {
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(0.5, 0.5, 0.5), is);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent e) {
        CustomBlock customBlock = CustomItemFramework.getCustomBlockAt(e.getBlock().getLocation());
        if (customBlock != null) {
            PlayerBreakCustomBlockEvent event = new PlayerBreakCustomBlockEvent(e.getPlayer(), customBlock, e.getBlock().getLocation());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                e.setCancelled(true);
            }else{
                ChunkData.removeCustomBlock(e.getBlock().getLocation());
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
        for (int i = 0; i < e.getInventory().getMatrix().length; i++) {
            if (CustomItemFramework.getCustomItem(e.getInventory().getMatrix()[i]) != null) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
                break;
            }
        }
    }
}

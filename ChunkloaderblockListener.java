package su.mcdev.minecraft.spigot.mcitems.item.component.chunkloaderblock;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import me.socrum.advanced.ini.Initer;
import me.socrum.advanced.util.UtilA;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.SoundAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.advanced.delay.DelayAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.ItemM;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import su.mcdev.chunk_loader.ChunkLoader;
import su.mcdev.minecraft.spigot.mcitems.Main;
import su.mcdev.minecraft.spigot.mcitems.commons.ChunkHelper;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.EmitterApi;
import su.mcdev.minecraft.spigot.mcitems.util.U;

public class ChunkloaderblockListener implements Initer, Listener {
   @NotNull
   private ChunkloaderblockManager chunkloaderblockManager;
   @NotNull
   private DelayAdvanced clickDelayAdvanced;
   @NotNull
   private EmitterApi emitterApi;

   public ChunkloaderblockListener(@NotNull ChunkloaderblockManager chunkloaderblockManager, @NotNull EmitterApi emitterApi) {
      this.chunkloaderblockManager = chunkloaderblockManager;
      this.clickDelayAdvanced = UtilM.delay();
      this.emitterApi = emitterApi;
   }

   public void init() {
      PluginManager pluginManager = Bukkit.getPluginManager();
      pluginManager.registerEvents(this, Main.instance);
   }

   public void destroy() {
      HandlerList.unregisterAll(this);
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onBlockPlaceEvent(@NotNull BlockPlaceEvent event) {
      if (!event.isCancelled()) {
         Player player = event.getPlayer();
         GameMode gameMode = player.getGameMode();
         if (gameMode == GameMode.CREATIVE) {
            ItemStack itemInHand = player.getItemInHand();
            ItemM itemm = ItemMAPI.getOrNull(itemInHand);
            ChunkloaderblockComponent chunkloaderblockComponent = (ChunkloaderblockComponent)ItemMAPI.getFirstItemMComponent(itemInHand, ChunkloaderblockComponent.class, true);
            if (itemm != null && chunkloaderblockComponent != null) {
               event.setCancelled(true);
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onPlayerInteractEvent1(@NotNull PlayerInteractEvent event) {
      if (!event.isCancelled()) {
         Action action = event.getAction();
         if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            String playerName = player.getName();
            Location playerLocation = player.getLocation();
            Location pasteLocation = event.getClickedBlock().getLocation().clone().add(0.0D, 1.0D, 0.0D);
            Block pasteLocationBlock = pasteLocation.getBlock();
            if (pasteLocationBlock == null || pasteLocationBlock.getType() == Material.AIR) {
               ItemStack inHandItemStack = player.getItemInHand();
               ItemM itemm = ItemMAPI.getOrNull(inHandItemStack);
               ChunkloaderblockComponent chunkloaderblockComponent = (ChunkloaderblockComponent)ItemMAPI.getFirstItemMComponent(inHandItemStack, ChunkloaderblockComponent.class, true);
               if (itemm != null && chunkloaderblockComponent != null) {
                  String itemmId = itemm.getId();
                  event.setCancelled(true);
                  World playerWorld = player.getWorld();
                  if (!chunkloaderblockComponent.isWorldAllowed(playerWorld)) {
                     chunkloaderblockComponent.sendUseDenyWorldMessage(player);
                  } else if (U.canBuildHere(player, pasteLocation)) {
                     Region affectedRegion = new Region(pasteLocation, pasteLocation);
                     if (chunkloaderblockComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
                        chunkloaderblockComponent.sendIsIntersectingWithOneOfWorldGuardDenyRegionMessage(player);
                     } else if (this.clickDelayAdvanced.hasDelay(player)) {
                        event.setCancelled(true);
                     } else {
                        this.clickDelayAdvanced.delay(player, 1);
                        double minDistanceBetweenChunkloaderblockBlocks = Main.instance.getConfig().getDouble("min_distance_between_chunkloaderblock_blocks");
                        Chunkloaderblock nearChunkloaderblock = (Chunkloaderblock)this.chunkloaderblockManager.getChunkloaderblockList().stream().filter((chunkloaderblockx) -> {
                           return UtilM.distance(chunkloaderblockx.getLocation(), pasteLocation) <= minDistanceBetweenChunkloaderblockBlocks;
                        }).findFirst().orElse((Object)null);
                        if (nearChunkloaderblock != null) {
                           UtilM.sendMessageFromConfigurationPath(Main.instance, player, "message.you_can_not_place_other_chunkloaderblock", Map.of("%blocks%", UtilA.number(minDistanceBetweenChunkloaderblockBlocks)));
                           event.setCancelled(true);
                        } else {
                           ItemStack itemStack = inHandItemStack.clone();
                           itemStack.setAmount(1);
                           UtilM.delItem(player, itemStack);
                           Chunkloaderblock chunkloaderblock = new Chunkloaderblock(playerName, pasteLocation, itemmId, this.emitterApi);
                           this.chunkloaderblockManager.create(chunkloaderblock, player);
                           (new SoundAdvanced("DIG_STONE")).play(pasteLocation);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onBlockBreakEvent(@NotNull BlockBreakEvent event) {
      if (!event.isCancelled()) {
         Player player = event.getPlayer();
         Block block = event.getBlock();
         Location blockLocation = block.getLocation();
         World blockWorld = block.getWorld();
         List<Chunkloaderblock> chunkloaderblockList = this.chunkloaderblockManager.getChunkloaderblockList();
         Iterator var7 = chunkloaderblockList.iterator();

         while(var7.hasNext()) {
            Chunkloaderblock chunkloaderblock = (Chunkloaderblock)var7.next();
            Location chunkloaderblockLocation = chunkloaderblock.getLocation();
            World chunkloaderblockWorld = chunkloaderblockLocation.getWorld();
            if (chunkloaderblockWorld.equals(blockWorld) && chunkloaderblockLocation.equals(blockLocation)) {
               this.chunkloaderblockManager.delete(player, chunkloaderblock);
               break;
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onPlayerInteractEvent2(@NotNull PlayerInteractEvent event) {
      if (!event.isCancelled()) {
         Action action = event.getAction();
         if (action == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();
            this.chunkloaderblockManager.getChunkloaderblockList().stream().filter((chunkloaderblock) -> {
               return chunkloaderblock.getLocation().getBlock().equals(clickedBlock);
            }).findFirst().ifPresent((chunkloaderblock) -> {
               event.setCancelled(true);
               this.chunkloaderblockManager.delete(player, chunkloaderblock);
               (new SoundAdvanced("ITEM_BREAK")).play(chunkloaderblock.getLocation());
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onChunkUnloadEvent(@NotNull ChunkUnloadEvent event) {
      Chunk chunk = event.getChunk();
      Iterator var3 = this.chunkloaderblockManager.getChunkloaderblockList().iterator();

      List chunkList;
      do {
         if (!var3.hasNext()) {
            return;
         }

         Chunkloaderblock chunkloaderblock = (Chunkloaderblock)var3.next();
         ChunkLoader chunkLoader = chunkloaderblock.getChunkLoader();
         chunkList = chunkLoader.getChunkList();
      } while(!chunkList.contains(chunk));

      ChunkHelper.loadChunk(Main.instance, chunk);
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onEntityChangeBlockEvent(@NotNull EntityChangeBlockEvent event) {
      if (!event.isCancelled()) {
         Block block = event.getBlock();
         Location blockLocation = block.getLocation();
         this.chunkloaderblockManager.getChunkloaderblockList().stream().filter((chunkloaderblock) -> {
            return chunkloaderblock.getLocation().equals(blockLocation);
         }).findFirst().ifPresent((chunkloaderblock) -> {
            event.setCancelled(true);
         });
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onEntityExplodeEvent(@NotNull EntityExplodeEvent event) {
      if (!event.isCancelled()) {
         List<Block> blockList = event.blockList();
         List<Chunkloaderblock> chunkloaderblockList = this.chunkloaderblockManager.getChunkloaderblockList();
         Iterator var4 = chunkloaderblockList.iterator();

         while(var4.hasNext()) {
            Chunkloaderblock chunkloaderblock = (Chunkloaderblock)var4.next();
            Location chunkloaderblockLocation = chunkloaderblock.getLocation();
            Block chunkloaderblockLocationBlock = chunkloaderblockLocation.getBlock();
            if (blockList.contains(chunkloaderblockLocationBlock)) {
               blockList.remove(chunkloaderblockLocationBlock);
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   private void onBlockExplodeEvent(@NotNull BlockExplodeEvent event) {
      if (!event.isCancelled()) {
         Block block = event.getBlock();
         Location blockLocation = block.getLocation();
         this.chunkloaderblockManager.getChunkloaderblockList().stream().filter((chunkloaderblock) -> {
            return chunkloaderblock.getLocation().equals(blockLocation);
         }).findFirst().ifPresent((chunkloaderblock) -> {
            event.setCancelled(true);
         });
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   private void onBlockPistonRetractEvent(@NotNull BlockPistonRetractEvent event) {
      if (!event.isCancelled()) {
         List<Block> blockList = event.getBlocks();
         List<Chunkloaderblock> chunkloaderblockList = this.chunkloaderblockManager.getChunkloaderblockList();
         Iterator var4 = chunkloaderblockList.iterator();

         Block damageblockLocationBlock;
         do {
            if (!var4.hasNext()) {
               return;
            }

            Chunkloaderblock chunkloaderblock = (Chunkloaderblock)var4.next();
            Location damageblockLocation = chunkloaderblock.getLocation();
            damageblockLocationBlock = damageblockLocation.getBlock();
         } while(!blockList.contains(damageblockLocationBlock));

         event.setCancelled(true);
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   private void onBlockPistonExtendEvent(@NotNull BlockPistonExtendEvent event) {
      if (!event.isCancelled()) {
         List<Block> blockList = event.getBlocks();
         List<Chunkloaderblock> chunkloaderblockList = this.chunkloaderblockManager.getChunkloaderblockList();
         Iterator var4 = chunkloaderblockList.iterator();

         Block chunkloaderblockLocationBlock;
         do {
            if (!var4.hasNext()) {
               return;
            }

            Chunkloaderblock chunkloaderblock = (Chunkloaderblock)var4.next();
            Location chunkloaderblockLocation = chunkloaderblock.getLocation();
            chunkloaderblockLocationBlock = chunkloaderblockLocation.getBlock();
         } while(!blockList.contains(chunkloaderblockLocationBlock));

         event.setCancelled(true);
      }
   }

   @NotNull
   @Generated
   public ChunkloaderblockManager getChunkloaderblockManager() {
      return this.chunkloaderblockManager;
   }

   @NotNull
   @Generated
   public DelayAdvanced getClickDelayAdvanced() {
      return this.clickDelayAdvanced;
   }

   @NotNull
   @Generated
   public EmitterApi getEmitterApi() {
      return this.emitterApi;
   }

   @Generated
   public void setChunkloaderblockManager(@NotNull ChunkloaderblockManager chunkloaderblockManager) {
      this.chunkloaderblockManager = chunkloaderblockManager;
   }

   @Generated
   public void setClickDelayAdvanced(@NotNull DelayAdvanced clickDelayAdvanced) {
      this.clickDelayAdvanced = clickDelayAdvanced;
   }

   @Generated
   public void setEmitterApi(@NotNull EmitterApi emitterApi) {
      this.emitterApi = emitterApi;
   }
}

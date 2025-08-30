package su.mcdev.minecraft.spigot.mcitems.item.component.damageblock;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.Main;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.EmitterApi;
import su.mcdev.minecraft.spigot.mcitems.util.U;

public class DamageblockListener implements Initer, Listener {
   @NotNull
   private DamageblockManager damageBlockManager;
   @NotNull
   private DelayAdvanced clickDelayAdvanced;
   @NotNull
   private EmitterApi emitterApi;

   public DamageblockListener(@NotNull DamageblockManager damageBlockManager, @NotNull EmitterApi emitterApi) {
      this.damageBlockManager = damageBlockManager;
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
            DamageblockComponent damageblockComponent = (DamageblockComponent)ItemMAPI.getFirstItemMComponent(itemInHand, DamageblockComponent.class, true);
            if (itemm != null && damageblockComponent != null) {
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
               DamageblockComponent damageblockComponent = (DamageblockComponent)ItemMAPI.getFirstItemMComponent(inHandItemStack, DamageblockComponent.class, true);
               if (itemm != null && damageblockComponent != null) {
                  String itemmId = itemm.getId();
                  event.setCancelled(true);
                  World playerWorld = player.getWorld();
                  if (!damageblockComponent.isWorldAllowed(playerWorld)) {
                     damageblockComponent.sendUseDenyWorldMessage(player);
                  } else if (U.canBuildHere(player, pasteLocation)) {
                     Region affectedRegion = new Region(pasteLocation, pasteLocation);
                     if (damageblockComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
                        damageblockComponent.sendIsIntersectingWithOneOfWorldGuardDenyRegionMessage(player);
                     } else if (this.clickDelayAdvanced.hasDelay(player)) {
                        event.setCancelled(true);
                     } else {
                        this.clickDelayAdvanced.delay(player, 1);
                        double minDistanceBetweenDamagerblockBlocks = Main.instance.getConfig().getDouble("min_distance_between_damagerblock_blocks");
                        Damageblock nearDamageblock = (Damageblock)this.damageBlockManager.getDamageblockList().stream().filter((damageblock) -> {
                           return UtilM.distance(damageblock.getLocation(), pasteLocation) <= minDistanceBetweenDamagerblockBlocks;
                        }).findFirst().orElse((Object)null);
                        if (nearDamageblock != null) {
                           UtilM.sendMessageFromConfigurationPath(Main.instance, player, "message.you_can_not_place_other_damageblock", Map.of("%blocks%", UtilA.number(minDistanceBetweenDamagerblockBlocks)));
                           event.setCancelled(true);
                        } else {
                           ItemStack itemStack = inHandItemStack.clone();
                           itemStack.setAmount(1);
                           UtilM.delItem(player, itemStack);
                           Damageblock damageBlock = new Damageblock(playerName, pasteLocation, itemmId, this.emitterApi);
                           this.damageBlockManager.create(damageBlock, player);
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
         List<Damageblock> damageBlockList = this.damageBlockManager.getDamageblockList();
         Iterator var7 = damageBlockList.iterator();

         while(var7.hasNext()) {
            Damageblock damageBlock = (Damageblock)var7.next();
            Location damageBlockLocation = damageBlock.getLocation();
            World damageBlockWorld = damageBlockLocation.getWorld();
            if (damageBlockWorld.equals(blockWorld) && damageBlockLocation.equals(blockLocation)) {
               this.damageBlockManager.delete(player, damageBlock);
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
            this.damageBlockManager.getDamageblockList().stream().filter((damageblock) -> {
               return damageblock.getLocation().getBlock().equals(clickedBlock);
            }).findFirst().ifPresent((damageblock) -> {
               event.setCancelled(true);
               this.damageBlockManager.delete(player, damageblock);
               (new SoundAdvanced("ITEM_BREAK")).play(damageblock.getLocation());
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onEntityChangeBlockEvent(@NotNull EntityChangeBlockEvent event) {
      if (!event.isCancelled()) {
         Block block = event.getBlock();
         Location blockLocation = block.getLocation();
         this.damageBlockManager.getDamageblockList().stream().filter((damageblock) -> {
            return damageblock.getLocation().equals(blockLocation);
         }).findFirst().ifPresent((damageblock) -> {
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
         List<Damageblock> damageblockList = this.damageBlockManager.getDamageblockList();
         Iterator var4 = damageblockList.iterator();

         while(var4.hasNext()) {
            Damageblock damageblock = (Damageblock)var4.next();
            Location damageblockLocation = damageblock.getLocation();
            Block damageblockBlock = damageblockLocation.getBlock();
            if (blockList.contains(damageblockBlock)) {
               blockList.remove(damageblockBlock);
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
         this.damageBlockManager.getDamageblockList().stream().filter((damageblock) -> {
            return damageblock.getLocation().equals(blockLocation);
         }).findFirst().ifPresent((damageblock) -> {
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
         List<Damageblock> damageblockList = this.damageBlockManager.getDamageblockList();
         Iterator var4 = damageblockList.iterator();

         Block damageblockBlock;
         do {
            if (!var4.hasNext()) {
               return;
            }

            Damageblock damageblock = (Damageblock)var4.next();
            Location damageblockLocation = damageblock.getLocation();
            damageblockBlock = damageblockLocation.getBlock();
         } while(!blockList.contains(damageblockBlock));

         event.setCancelled(true);
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   private void onBlockPistonExtendEvent(@NotNull BlockPistonExtendEvent event) {
      if (!event.isCancelled()) {
         List<Block> blockList = event.getBlocks();
         List<Damageblock> damageblockList = this.damageBlockManager.getDamageblockList();
         Iterator var4 = damageblockList.iterator();

         Block damageblockBlock;
         do {
            if (!var4.hasNext()) {
               return;
            }

            Damageblock damageblock = (Damageblock)var4.next();
            Location damageblockLocation = damageblock.getLocation();
            damageblockBlock = damageblockLocation.getBlock();
         } while(!blockList.contains(damageblockBlock));

         event.setCancelled(true);
      }
   }

   @NotNull
   @Generated
   public DamageblockManager getDamageBlockManager() {
      return this.damageBlockManager;
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
   public void setDamageBlockManager(@NotNull DamageblockManager damageBlockManager) {
      this.damageBlockManager = damageBlockManager;
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

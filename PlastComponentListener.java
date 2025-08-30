package su.mcdev.minecraft.spigot.mcitems.item.component.plast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.advanced.delay.DelayAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.Main;

public class PlastComponentListener extends ItemMComponentListener {
   @NotNull
   protected List<Plast> plastList = new ArrayList();
   @NotNull
   protected DelayAdvanced clickDelayAdvanced = UtilM.delay();
   @NotNull
   protected BukkitRunnable bukkitRunnable = new BukkitRunnable() {
      public void run() {
         for(int i = 0; i < PlastComponentListener.this.plastList.size(); ++i) {
            Plast plast = (Plast)PlastComponentListener.this.plastList.get(i);
            plast.update();
            int tempTtlTicks = plast.getTempTtlTicks();
            if (tempTtlTicks <= 0) {
               plast.despawn();
               PlastComponentListener.this.plastList.remove(plast);
               --i;
            }
         }

      }
   };

   public void init() {
      this.bukkitRunnable.runTaskTimer(Main.instance, 0L, 1L);
   }

   public void destroy() {
      this.bukkitRunnable.cancel();

      for(int i = 0; i < this.plastList.size(); ++i) {
         Plast plast = (Plast)this.plastList.get(i);
         plast.despawn();
         this.plastList.remove(i);
         --i;
      }

   }

   @EventHandler
   private void onPlayerInteractEvent(@NotNull PlayerInteractEvent event) {
      Action action = event.getAction();
      if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
         Player player = event.getPlayer();
         ItemStack inHandItemStack = player.getItemInHand();
         PlastComponent plastComponent = (PlastComponent)ItemMAPI.getFirstItemMComponent(inHandItemStack, PlastComponent.class, true);
         if (plastComponent != null) {
            event.setCancelled(true);
            World playerWorld = player.getWorld();
            if (!plastComponent.isWorldAllowed(playerWorld)) {
               plastComponent.sendUseDenyWorldMessage(player);
            } else if (!this.clickDelayAdvanced.hasDelay(player)) {
               this.clickDelayAdvanced.delay(player, 1);
               if (!plastComponent.hasDelayIfHasSendUseDelayMessage(player)) {
                  BlockIterator blockIterator = new BlockIterator(player, 3);

                  Block block;
                  for(block = null; blockIterator.hasNext(); block = blockIterator.next()) {
                  }

                  Location pasteLocation = block.getLocation();
                  Plast plast = new Plast(player, pasteLocation, plastComponent);
                  if (plast.spawn()) {
                     ItemStack itemStack = inHandItemStack.clone();
                     itemStack.setAmount(1);
                     UtilM.delItem(player, itemStack);
                     this.plastList.add(plast);
                  }
               }
            }
         }
      }
   }

   @EventHandler
   private void onEntityDamageEvent(@NotNull EntityDamageEvent event) {
      Entity entity = event.getEntity();
      if (entity instanceof Player) {
         Player player = (Player)entity;
         DamageCause damageCause = event.getCause();
         if (damageCause == DamageCause.FALL) {
            boolean isPlayerInvulnerableToFallDamage = this.plastList.stream().anyMatch((plast) -> {
               return plast.getSummonerPlayer() == player && plast.isPlayerInvulnerableToFallDamage();
            });
            if (isPlayerInvulnerableToFallDamage) {
               event.setDamage(0.0D);
               event.setCancelled(true);
            }
         }
      }
   }

   @EventHandler
   private void onBlockBreakEvent(@NotNull BlockBreakEvent event) {
      Player player = event.getPlayer();
      Block block = event.getBlock();
      Iterator var4 = this.plastList.iterator();

      while(var4.hasNext()) {
         Plast plast = (Plast)var4.next();
         PlastComponent plastComponent = plast.getPlastComponent();
         boolean opCanBreakUnbreakableWallRegionWhileItSpawned = plastComponent.isOpCanBreakUnbreakableWallRegionWhileItSpawned();
         if (!opCanBreakUnbreakableWallRegionWhileItSpawned || !player.isOp()) {
            Region region = plast.getClipboardMRegion();
            if (region.isInRegion(block)) {
               event.setCancelled(true);
               break;
            }
         }
      }

   }

   @EventHandler
   private void onBlockPistonExtendEvent(@NotNull BlockPistonExtendEvent event) {
      List<Block> blockList = event.getBlocks();
      Iterator var3 = blockList.iterator();

      Block block;
      do {
         if (!var3.hasNext()) {
            return;
         }

         block = (Block)var3.next();
      } while(this.plastList.stream().noneMatch((plast) -> {
         return plast.getClipboardMRegion().isInRegion(block);
      }));

      event.setCancelled(true);
   }

   @EventHandler
   private void onBlockPistonRetractEvent(@NotNull BlockPistonRetractEvent event) {
      List<Block> blockList = event.getBlocks();
      Iterator var3 = blockList.iterator();

      Block block;
      do {
         if (!var3.hasNext()) {
            return;
         }

         block = (Block)var3.next();
      } while(this.plastList.stream().noneMatch((plast) -> {
         return plast.getClipboardMRegion().isInRegion(block);
      }));

      event.setCancelled(true);
   }

   @EventHandler
   private void onBlockExplodeEvent(@NotNull BlockExplodeEvent event) {
      List<Block> blockList = event.blockList();

      for(int i = 0; i < blockList.size(); ++i) {
         Block block = (Block)blockList.get(i);
         if (!this.plastList.stream().noneMatch((plast) -> {
            return plast.getClipboardMRegion().isInRegion(block);
         })) {
            blockList.remove(i);
            --i;
         }
      }

   }

   @EventHandler
   private void onEntityExplodeEvent(@NotNull EntityExplodeEvent event) {
      List<Block> blockList = event.blockList();

      for(int i = 0; i < blockList.size(); ++i) {
         Block block = (Block)blockList.get(i);
         if (!this.plastList.stream().noneMatch((plast) -> {
            return plast.getClipboardMRegion().isInRegion(block);
         })) {
            blockList.remove(i);
            --i;
         }
      }

   }

   @EventHandler
   private void onEntityChangeBlockEvent(@NotNull EntityChangeBlockEvent event) {
      Block block = event.getBlock();
      if (!this.plastList.stream().noneMatch((plast) -> {
         return plast.getClipboardMRegion().isInRegion(block);
      })) {
         event.setCancelled(true);
      }
   }

   @NotNull
   @Generated
   public List<Plast> getPlastList() {
      return this.plastList;
   }

   @NotNull
   @Generated
   public DelayAdvanced getClickDelayAdvanced() {
      return this.clickDelayAdvanced;
   }

   @NotNull
   @Generated
   public BukkitRunnable getBukkitRunnable() {
      return this.bukkitRunnable;
   }

   @Generated
   public void setPlastList(@NotNull List<Plast> plastList) {
      this.plastList = plastList;
   }

   @Generated
   public void setClickDelayAdvanced(@NotNull DelayAdvanced clickDelayAdvanced) {
      this.clickDelayAdvanced = clickDelayAdvanced;
   }

   @Generated
   public void setBukkitRunnable(@NotNull BukkitRunnable bukkitRunnable) {
      this.bukkitRunnable = bukkitRunnable;
   }
}

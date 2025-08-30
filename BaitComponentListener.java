package su.mcdev.minecraft.spigot.mcitems.item.component.bait;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.advanced.delay.DelayAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.Main;

public class BaitComponentListener extends ItemMComponentListener {
   @NotNull
   protected DelayAdvanced clickDelayAdvanced = UtilM.delay();
   @NotNull
   protected List<BaitItem> baitItemList = new ArrayList();
   @NotNull
   protected BukkitRunnable bukkitRunnable = new BukkitRunnable() {
      public void run() {
         List<BaitItem> baitItemList = BaitComponentListener.this.baitItemList;

         for(int i = 0; i < baitItemList.size(); ++i) {
            BaitItem baitItem = (BaitItem)baitItemList.get(i);
            Item item = baitItem.getItem();
            if (item.isDead() || !item.isValid()) {
               item.remove();
               baitItemList.remove(i);
               --i;
            }
         }

      }
   };

   public void init() {
      this.bukkitRunnable.runTaskTimer(Main.instance, 20L, 20L);
   }

   public void destroy() {
      for(int i = 0; i < this.baitItemList.size(); ++i) {
         BaitItem baitItem = (BaitItem)this.baitItemList.get(i);
         Item item = baitItem.getItem();
         item.remove();
         this.baitItemList.remove(i);
         --i;
      }

      this.bukkitRunnable.cancel();
   }

   @EventHandler
   public void onPlayerDropItemEvent(@NotNull PlayerDropItemEvent event) {
      Player player = event.getPlayer();
      Location playerLocation = player.getLocation();
      Item item = event.getItemDrop();
      ItemStack itemStack = item.getItemStack();
      BaitComponent baitComponent = (BaitComponent)ItemMAPI.getFirstItemMComponent(itemStack, BaitComponent.class, true);
      if (baitComponent != null) {
         World playerWorld = player.getWorld();
         if (!baitComponent.isWorldAllowed(playerWorld)) {
            baitComponent.sendUseDenyWorldMessage(player);
         } else {
            Region affectedRegion = new Region(playerLocation, playerLocation);
            if (baitComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
               baitComponent.sendIsIntersectingWithOneOfWorldGuardDenyRegionMessage(player);
            } else {
               BaitItem baitItem = new BaitItem(player, item);
               this.baitItemList.add(baitItem);
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerPickupItemEvent(@NotNull PlayerPickupItemEvent event) {
      Player player = event.getPlayer();
      Location playerLocation = player.getLocation();
      World playerLocationWorld = playerLocation.getWorld();
      Item item = event.getItem();
      ItemStack itemStack = item.getItemStack();
      BaitComponent baitComponent = (BaitComponent)ItemMAPI.getFirstItemMComponent(itemStack, BaitComponent.class, true);
      if (baitComponent != null) {
         World playerWorld = player.getWorld();
         if (baitComponent.isWorldAllowed(playerWorld)) {
            Region affectedRegion = new Region(playerLocation, playerLocation);
            if (!baitComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
               BaitItem baitItem = (BaitItem)this.baitItemList.stream().filter((baitItem1) -> {
                  return baitItem1.getItem().equals(item);
               }).findFirst().orElse((Object)null);
               if (baitItem != null) {
                  Player baitOwnerPlayer = baitItem.getPlayer();
                  if (!baitOwnerPlayer.equals(player)) {
                     event.setCancelled(true);
                     item.remove();
                     float power = baitComponent.getExplosionPower();
                     boolean setFire = false;
                     playerLocationWorld.createExplosion(playerLocation, power, setFire);
                     baitComponent.sendBaitWorkedMessage(baitOwnerPlayer);
                     baitComponent.sendYouTookBaitMessage(player);
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void onPlayerQuitEvent(@NotNull PlayerQuitEvent event) {
      Player player = event.getPlayer();

      for(int i = 0; i < this.baitItemList.size(); ++i) {
         BaitItem baitItem = (BaitItem)this.baitItemList.get(i);
         Player player1 = baitItem.getPlayer();
         if (player1 == player) {
            Item item = baitItem.getItem();
            item.remove();
            this.baitItemList.remove(i);
            --i;
         }
      }

   }

   @NotNull
   @Generated
   public DelayAdvanced getClickDelayAdvanced() {
      return this.clickDelayAdvanced;
   }

   @NotNull
   @Generated
   public List<BaitItem> getBaitItemList() {
      return this.baitItemList;
   }

   @NotNull
   @Generated
   public BukkitRunnable getBukkitRunnable() {
      return this.bukkitRunnable;
   }

   @Generated
   public void setClickDelayAdvanced(@NotNull DelayAdvanced clickDelayAdvanced) {
      this.clickDelayAdvanced = clickDelayAdvanced;
   }

   @Generated
   public void setBaitItemList(@NotNull List<BaitItem> baitItemList) {
      this.baitItemList = baitItemList;
   }

   @Generated
   public void setBukkitRunnable(@NotNull BukkitRunnable bukkitRunnable) {
      this.bukkitRunnable = bukkitRunnable;
   }
}

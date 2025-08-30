package su.mcdev.minecraft.spigot.mcitems.item.component.disorientation;

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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.Main;

public class DisorientationComponentListener extends ItemMComponentListener {
   @NotNull
   protected List<Disorientation> disorientationList = new ArrayList();
   @NotNull
   protected DelayAdvanced clickDelayAdvanced = UtilM.delay();
   @NotNull
   protected BukkitRunnable bukkitRunnable = new BukkitRunnable() {
      public void run() {
         for(int i = 0; i < DisorientationComponentListener.this.disorientationList.size(); ++i) {
            Disorientation disorientation = (Disorientation)DisorientationComponentListener.this.disorientationList.get(i);
            disorientation.update();
            int tempTtlTicks = disorientation.getTempTtlTicks();
            if (tempTtlTicks <= 0) {
               disorientation.despawn();
               DisorientationComponentListener.this.disorientationList.remove(disorientation);
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

      for(int i = 0; i < this.disorientationList.size(); ++i) {
         Disorientation disorientation = (Disorientation)this.disorientationList.get(i);
         disorientation.despawn();
         this.disorientationList.remove(i);
         --i;
      }

   }

   @EventHandler
   private void onPlayerInteractEvent(@NotNull PlayerInteractEvent event) {
      Action action = event.getAction();
      if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
         Player player = event.getPlayer();
         Location playerLocation = player.getLocation();
         ItemStack inHandItemStack = player.getItemInHand();
         DisorientationComponent disorientationComponent = (DisorientationComponent)ItemMAPI.getFirstItemMComponent(inHandItemStack, DisorientationComponent.class, true);
         if (disorientationComponent != null) {
            event.setCancelled(true);
            World playerWorld = player.getWorld();
            if (!disorientationComponent.isWorldAllowed(playerWorld)) {
               disorientationComponent.sendUseDenyWorldMessage(player);
            } else {
               Region affectedRegion = new Region(playerLocation, playerLocation);
               if (disorientationComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
                  disorientationComponent.sendIsIntersectingWithOneOfWorldGuardDenyRegionMessage(player);
               } else if (!this.clickDelayAdvanced.hasDelay(player)) {
                  this.clickDelayAdvanced.delay(player, 1);
                  if (!disorientationComponent.hasDelayIfHasSendUseDelayMessage(player)) {
                     ItemStack itemStack = inHandItemStack.clone();
                     itemStack.setAmount(1);
                     UtilM.delItem(player, itemStack);
                     Location pasteLocation = playerLocation.clone();
                     Disorientation disorientation = new Disorientation(player, pasteLocation, disorientationComponent);
                     this.disorientationList.add(disorientation);
                     disorientation.spawn();
                  }
               }
            }
         }
      }
   }

   @NotNull
   @Generated
   public List<Disorientation> getDisorientationList() {
      return this.disorientationList;
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
   public void setDisorientationList(@NotNull List<Disorientation> disorientationList) {
      this.disorientationList = disorientationList;
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

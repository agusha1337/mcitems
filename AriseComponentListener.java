package su.mcdev.minecraft.spigot.mcitems.item.component.arise;

import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.advanced.delay.DelayAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.item.component.arise.entity.AriseEntityManager;

public class AriseComponentListener extends ItemMComponentListener {
   @NotNull
   private DelayAdvanced clickDelayAdvanced = UtilM.delay();
   @NotNull
   private AriseEntityManager ariseEntityManager = new AriseEntityManager();

   public void init() {
      this.ariseEntityManager.init();
   }

   public void destroy() {
      this.ariseEntityManager.destroy();
   }

   @EventHandler
   private void onPlayerInteractEvent(@NotNull PlayerInteractEvent event) {
      Action action = event.getAction();
      if (action == Action.RIGHT_CLICK_BLOCK) {
         Player player = event.getPlayer();
         Block clickedBlock = event.getClickedBlock();
         Location clickedBlockLocation = clickedBlock.getLocation();
         Location summonLocation = clickedBlockLocation.clone().add(0.5D, 1.0D, 0.5D);
         ItemStack inHandItemStack = player.getItemInHand();
         AriseComponent ariseComponent = (AriseComponent)ItemMAPI.getFirstItemMComponent(inHandItemStack, AriseComponent.class, true);
         if (ariseComponent != null) {
            event.setCancelled(true);
            World playerWorld = player.getWorld();
            if (!ariseComponent.isWorldAllowed(playerWorld)) {
               ariseComponent.sendUseDenyWorldMessage(player);
            } else if (!this.clickDelayAdvanced.hasDelay(player)) {
               this.clickDelayAdvanced.delay(player, 5);
               if (!ariseComponent.hasDelayIfHasSendUseDelayMessage(player)) {
                  int arisenEntitiesCount = this.ariseEntityManager.getArisenEntitiesCount(player, ariseComponent);
                  int arisenEntityLimit = ariseComponent.getArisenEntityLimit();
                  int arisenEntitySpawnAmount = ariseComponent.getArisenEntitySpawnAmount();
                  if (arisenEntitiesCount + 1 > arisenEntityLimit) {
                     ariseComponent.sendArisenEntityLimitReachedMessage(player);
                  } else {
                     int allowedArisenEntitySpawnAmount = arisenEntityLimit - arisenEntitiesCount;
                     int calculatedArisenEntitySpawnAmount = Math.min(arisenEntitySpawnAmount, allowedArisenEntitySpawnAmount);
                     ItemStack itemStack = inHandItemStack.clone();
                     itemStack.setAmount(1);
                     UtilM.delItem(player, itemStack);
                     this.ariseEntityManager.spawn(player, summonLocation, EntityType.WITHER_SKELETON, ariseComponent, () -> {
                        ariseComponent.sendArisenMessage(player);
                     }, () -> {
                        ariseComponent.sendDespawnedMessage(player);
                     }, ariseComponent.getTtlSeconds(), calculatedArisenEntitySpawnAmount, ariseComponent.getGlowingColor());
                  }
               }
            }
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
   public AriseEntityManager getAriseEntityManager() {
      return this.ariseEntityManager;
   }

   @Generated
   public void setClickDelayAdvanced(@NotNull DelayAdvanced clickDelayAdvanced) {
      this.clickDelayAdvanced = clickDelayAdvanced;
   }

   @Generated
   public void setAriseEntityManager(@NotNull AriseEntityManager ariseEntityManager) {
      this.ariseEntityManager = ariseEntityManager;
   }
}

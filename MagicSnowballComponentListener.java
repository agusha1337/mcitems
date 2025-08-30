package su.mcdev.minecraft.spigot.mcitems.item.component.magic_snowball;

import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.advanced.delay.DelayAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.bulletm.BulletAAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MagicSnowballComponentListener extends ItemMComponentListener {
   @NotNull
   protected DelayAdvanced clickDelayAdvanced = UtilM.delay();

   @EventHandler
   private void onPlayerInteractEvent(@NotNull PlayerInteractEvent event) {
      Player player = event.getPlayer();
      Location playerLocation = player.getLocation();
      World playerWorld = player.getWorld();
      ItemStack inHandItemStack = player.getItemInHand();
      Action action = event.getAction();
      if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
         MagicSnowballComponent magicSnowballComponent = (MagicSnowballComponent)ItemMAPI.getFirstItemMComponent(inHandItemStack, MagicSnowballComponent.class, true);
         if (magicSnowballComponent != null) {
            event.setCancelled(true);
            if (!magicSnowballComponent.isWorldAllowed(playerWorld)) {
               magicSnowballComponent.sendUseDenyWorldMessage(player);
            } else {
               Region affectedRegion = new Region(playerLocation, playerLocation);
               if (magicSnowballComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
                  magicSnowballComponent.sendIsIntersectingWithOneOfWorldGuardDenyRegionMessage(player);
               } else if (!this.clickDelayAdvanced.hasDelay(player)) {
                  this.clickDelayAdvanced.delay(player, 1);
                  if (!magicSnowballComponent.hasDelayIfHasSendUseDelayMessage(player)) {
                     ItemStack itemStack = inHandItemStack.clone();
                     itemStack.setAmount(1);
                     UtilM.delItem(player, itemStack);
                     magicSnowballComponent.playThrowSound(player.getLocation().clone().add(0.0D, 2.0D, 0.0D));
                     BulletAAPI.shoot(1, 0, Snowball.class, player, (entity) -> {
                        if (entity instanceof LivingEntity) {
                           LivingEntity livingEntity = (LivingEntity)entity;
                           magicSnowballComponent.addAllDisorientationEffectsToLivingEntity(livingEntity);
                           magicSnowballComponent.playSuccessHitSound(player);
                           magicSnowballComponent.sendSuccessHitMessage(player);
                        }
                     });
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

   @Generated
   public void setClickDelayAdvanced(@NotNull DelayAdvanced clickDelayAdvanced) {
      this.clickDelayAdvanced = clickDelayAdvanced;
   }
}

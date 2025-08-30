package su.mcdev.minecraft.spigot.mcitems.item.component.flash_snowball;

import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.advanced.delay.DelayAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.bulletm.BulletAAPI;
import me.socrum.minecraft.spigot.plugin.utilm.bulletm.bullet.Bullet;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FlashSnowballComponentListener extends ItemMComponentListener {
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
         FlashSnowballComponent flashSnowballComponent = (FlashSnowballComponent)ItemMAPI.getFirstItemMComponent(inHandItemStack, FlashSnowballComponent.class, true);
         if (flashSnowballComponent != null) {
            event.setCancelled(true);
            if (!flashSnowballComponent.isWorldAllowed(playerWorld)) {
               flashSnowballComponent.sendUseDenyWorldMessage(player);
            } else {
               Region affectedRegion = new Region(playerLocation, playerLocation);
               if (flashSnowballComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
                  flashSnowballComponent.sendIsIntersectingWithOneOfWorldGuardDenyRegionMessage(player);
               } else if (!this.clickDelayAdvanced.hasDelay(player)) {
                  this.clickDelayAdvanced.delay(player, 1);
                  if (!flashSnowballComponent.hasDelayIfHasSendUseDelayMessage(player)) {
                     ItemStack itemStack = inHandItemStack.clone();
                     itemStack.setAmount(1);
                     UtilM.delItem(player, itemStack);
                     flashSnowballComponent.playThrowSound(player.getLocation().clone().add(0.0D, 2.0D, 0.0D));
                     Bullet bullet = BulletAAPI.shoot(1, 0, Snowball.class, player, (entity) -> {
                        if (entity instanceof LivingEntity) {
                           LivingEntity livingEntity = (LivingEntity)entity;
                           flashSnowballComponent.addAllDisorientationEffectsToLivingEntity(livingEntity);
                           flashSnowballComponent.playSuccessHitSound(player);
                           flashSnowballComponent.sendSuccessHitMessage(player);
                           Location livingEntityLocation = livingEntity.getLocation();
                           World world = livingEntityLocation.getWorld();
                           Location particleSpawnLocation = livingEntityLocation.clone().add(0.0D, 1.0D, 0.0D);
                           world.spawnParticle(Particle.FLASH, particleSpawnLocation, 1);
                           world.playSound(particleSpawnLocation, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0F, 2.0F);
                        }
                     });
                     Projectile projectile = bullet.getProjectile();
                     projectile.setGlowing(true);
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

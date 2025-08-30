package su.mcdev.minecraft.spigot.mcitems.item.component.explosion_damage_reducer;

import me.socrum.advanced.sum.Sum;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExplosionDamageReducerComponentListener extends ItemMComponentListener {
   @Nullable
   private ExplosionDamageReducerComponent findFirstExplosionDamageReducerComponent(@NotNull ItemStack[] itemStacks) {
      ItemStack[] var3 = itemStacks;
      int var4 = itemStacks.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ItemStack itemStack = var3[var5];
         ExplosionDamageReducerComponent explosionDamageReducerComponent = (ExplosionDamageReducerComponent)ItemMAPI.getFirstItemMComponent(itemStack, ExplosionDamageReducerComponent.class, false);
         if (explosionDamageReducerComponent != null) {
            return explosionDamageReducerComponent;
         }
      }

      return null;
   }

   @Nullable
   private ExplosionDamageReducerComponent findFirstExplosionDamageReducerComponent(@NotNull Player player) {
      PlayerInventory playerInventory = player.getInventory();
      ItemStack[] armorContents = playerInventory.getArmorContents();
      ItemStack[] contents = playerInventory.getContents();
      ExplosionDamageReducerComponent explosionDamageReducerComponent = this.findFirstExplosionDamageReducerComponent(armorContents);
      if (explosionDamageReducerComponent != null) {
         return explosionDamageReducerComponent;
      } else {
         explosionDamageReducerComponent = this.findFirstExplosionDamageReducerComponent(contents);
         return explosionDamageReducerComponent;
      }
   }

   @EventHandler
   public void onEntityDamageEvent(@NotNull EntityDamageEvent event) {
      Entity entity = event.getEntity();
      DamageCause damageCause = event.getCause();
      if (damageCause == DamageCause.ENTITY_EXPLOSION || damageCause == DamageCause.BLOCK_EXPLOSION) {
         if (entity instanceof Player) {
            Player player = (Player)entity;
            Location playerLocation = player.getLocation();
            double damage = event.getDamage();
            ExplosionDamageReducerComponent firstExplosionDamageReducerComponent = this.findFirstExplosionDamageReducerComponent(player);
            if (firstExplosionDamageReducerComponent != null) {
               World playerWorld = player.getWorld();
               if (firstExplosionDamageReducerComponent.isWorldAllowed(playerWorld)) {
                  Region affectedRegion = new Region(playerLocation, playerLocation);
                  if (!firstExplosionDamageReducerComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
                     double explosionDamageReductionPercent = firstExplosionDamageReducerComponent.getExplosionDamageReductionPercent();
                     double finalDamage = Sum.subtract(damage, Sum.multiply(Sum.divide(damage, 100.0D), explosionDamageReductionPercent));
                     event.setDamage(finalDamage);
                     firstExplosionDamageReducerComponent.sendDamageReducedMessage(player, damage, finalDamage);
                  }
               }
            }
         }
      }
   }
}

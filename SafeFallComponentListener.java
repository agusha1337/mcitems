package su.mcdev.minecraft.spigot.mcitems.item.component.unfallable;

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

public class SafeFallComponentListener extends ItemMComponentListener {
   @Nullable
   private SafeFallComponent findFirstSafeFallComponent(@NotNull ItemStack[] itemStacks) {
      ItemStack[] var3 = itemStacks;
      int var4 = itemStacks.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ItemStack itemStack = var3[var5];
         SafeFallComponent safeFallComponent = (SafeFallComponent)ItemMAPI.getFirstItemMComponent(itemStack, SafeFallComponent.class, false);
         if (safeFallComponent != null) {
            return safeFallComponent;
         }
      }

      return null;
   }

   @Nullable
   private SafeFallComponent findFirstSafeFallComponent(@NotNull Player player) {
      PlayerInventory playerInventory = player.getInventory();
      ItemStack[] armorContents = playerInventory.getArmorContents();
      ItemStack[] contents = playerInventory.getContents();
      SafeFallComponent safeFallComponent = this.findFirstSafeFallComponent(armorContents);
      if (safeFallComponent != null) {
         return safeFallComponent;
      } else {
         safeFallComponent = this.findFirstSafeFallComponent(contents);
         return safeFallComponent;
      }
   }

   @EventHandler
   private void onEntityDamageEvent(@NotNull EntityDamageEvent event) {
      Entity entity = event.getEntity();
      if (entity instanceof Player) {
         Player player = (Player)entity;
         DamageCause damageCause = event.getCause();
         if (damageCause == DamageCause.FALL) {
            SafeFallComponent firstSafeFallComponent = this.findFirstSafeFallComponent(player);
            if (firstSafeFallComponent != null) {
               Location playerLocation = player.getLocation();
               World playerWorld = player.getWorld();
               if (firstSafeFallComponent.isWorldAllowed(playerWorld)) {
                  Region affectedRegion = new Region(playerLocation, playerLocation);
                  if (!firstSafeFallComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
                     event.setDamage(0.0D);
                     event.setCancelled(true);
                  }
               }
            }
         }
      }
   }
}

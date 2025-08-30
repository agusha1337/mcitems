package su.mcdev.minecraft.spigot.mcitems.item.component.neutralize;

import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.Main;

public class NeutralizeComponentListener extends ItemMComponentListener {
   @NotNull
   protected BukkitRunnable bukkitRunnable = new BukkitRunnable() {
      public void run() {
         Iterator var1 = Bukkit.getOnlinePlayers().iterator();

         while(true) {
            Player player;
            Location playerLocation;
            World playerWorld;
            NeutralizeComponent neutralizeComponent;
            do {
               if (!var1.hasNext()) {
                  return;
               }

               player = (Player)var1.next();
               playerLocation = player.getLocation();
               playerWorld = player.getWorld();
               PlayerInventory playerInventory = player.getInventory();
               ItemStack[] armorContentItemStacks = playerInventory.getArmorContents();
               ItemStack[] contentItemStacks = playerInventory.getContents();
               neutralizeComponent = null;
               ItemStack[] var9 = armorContentItemStacks;
               int var10 = armorContentItemStacks.length;

               int var11;
               ItemStack contentItemStack;
               for(var11 = 0; var11 < var10; ++var11) {
                  contentItemStack = var9[var11];
                  neutralizeComponent = (NeutralizeComponent)ItemMAPI.getFirstItemMComponent(contentItemStack, NeutralizeComponent.class, true);
                  if (neutralizeComponent != null) {
                     break;
                  }
               }

               if (neutralizeComponent == null) {
                  var9 = contentItemStacks;
                  var10 = contentItemStacks.length;

                  for(var11 = 0; var11 < var10; ++var11) {
                     contentItemStack = var9[var11];
                     neutralizeComponent = (NeutralizeComponent)ItemMAPI.getFirstItemMComponent(contentItemStack, NeutralizeComponent.class, true);
                     if (neutralizeComponent != null) {
                        break;
                     }
                  }
               }
            } while(neutralizeComponent == null);

            if (!neutralizeComponent.isWorldAllowed(playerWorld)) {
               return;
            }

            Region affectedRegion = new Region(playerLocation, playerLocation);
            if (neutralizeComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
               return;
            }

            boolean hasNeutralizablePotionEffectType = false;
            List<PotionEffectType> neutralizablePotionEffectTypeList = neutralizeComponent.getNeutralizablePotionEffectTypeList();

            PotionEffectType neutralizablePotionEffectType;
            for(Iterator var17 = neutralizablePotionEffectTypeList.iterator(); var17.hasNext(); player.removePotionEffect(neutralizablePotionEffectType)) {
               neutralizablePotionEffectType = (PotionEffectType)var17.next();
               if (player.hasPotionEffect(neutralizablePotionEffectType)) {
                  hasNeutralizablePotionEffectType = true;
               }
            }

            if (hasNeutralizablePotionEffectType) {
               neutralizeComponent.sendNeutralizedMessage(player);
            }
         }
      }
   };

   public void init() {
      this.bukkitRunnable.runTaskTimer(Main.instance, 20L, 20L);
   }

   public void destroy() {
      this.bukkitRunnable.cancel();
   }

   @NotNull
   @Generated
   public BukkitRunnable getBukkitRunnable() {
      return this.bukkitRunnable;
   }

   @Generated
   public void setBukkitRunnable(@NotNull BukkitRunnable bukkitRunnable) {
      this.bukkitRunnable = bukkitRunnable;
   }
}

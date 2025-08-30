package su.mcdev.minecraft.spigot.mcitems.item.component.kamikaze;

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
import org.jetbrains.annotations.NotNull;

public class KamikazeComponentListener extends ItemMComponentListener {
   @NotNull
   protected DelayAdvanced clickDelayAdvanced = UtilM.delay();

   @EventHandler
   private void onPlayerInteractEvent(@NotNull PlayerInteractEvent event) {
      Action action = event.getAction();
      if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
         Player player = event.getPlayer();
         Location playerLocation = player.getLocation();
         World playerWorld = player.getWorld();
         ItemStack inHandItemStack = player.getItemInHand();
         KamikazeComponent kamikazeComponent = (KamikazeComponent)ItemMAPI.getFirstItemMComponent(inHandItemStack, KamikazeComponent.class, true);
         if (kamikazeComponent != null) {
            event.setCancelled(true);
            if (!kamikazeComponent.isWorldAllowed(playerWorld)) {
               kamikazeComponent.sendUseDenyWorldMessage(player);
            } else {
               Region affectedRegion = new Region(playerLocation, playerLocation);
               if (kamikazeComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
                  kamikazeComponent.sendIsIntersectingWithOneOfWorldGuardDenyRegionMessage(player);
               } else if (!this.clickDelayAdvanced.hasDelay(player)) {
                  this.clickDelayAdvanced.delay(player, 1);
                  if (!kamikazeComponent.hasDelayIfHasSendUseDelayMessage(player)) {
                     ItemStack itemStack = inHandItemStack.clone();
                     itemStack.setAmount(1);
                     UtilM.delItem(player, itemStack);
                     World playerLocationWorld = playerLocation.getWorld();
                     float power = kamikazeComponent.getExplosionPower();
                     boolean setFire = false;
                     playerLocationWorld.createExplosion(playerLocation, power, setFire);
                     kamikazeComponent.sendExplodeMessage(player);
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

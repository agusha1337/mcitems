package su.mcdev.minecraft.spigot.mcitems.item.component.flash;

import lombok.Generated;
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

public class FlashComponentListener extends ItemMComponentListener {
   @NotNull
   protected DelayAdvanced clickDelayAdvanced = UtilM.delay();

   @EventHandler
   private void onPlayerInteractEvent(@NotNull PlayerInteractEvent event) {
      Action action = event.getAction();
      if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
         Player player = event.getPlayer();
         Location playerLocation = player.getLocation();
         ItemStack inHandItemStack = player.getItemInHand();
         FlashComponent flashComponent = (FlashComponent)ItemMAPI.getFirstItemMComponent(inHandItemStack, FlashComponent.class, true);
         if (flashComponent != null) {
            event.setCancelled(true);
            World playerWorld = player.getWorld();
            if (!flashComponent.isWorldAllowed(playerWorld)) {
               flashComponent.sendUseDenyWorldMessage(player);
            } else if (!this.clickDelayAdvanced.hasDelay(player)) {
               this.clickDelayAdvanced.delay(player, 1);
               if (!flashComponent.hasDelayIfHasSendUseDelayMessage(player)) {
                  ItemStack itemStack = inHandItemStack.clone();
                  itemStack.setAmount(1);
                  UtilM.delItem(player, itemStack);
                  Flash flash = new Flash(player, playerLocation.clone(), flashComponent);
                  flash.spawn();
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

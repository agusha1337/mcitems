package su.mcdev.minecraft.spigot.mcitems.item.component.poison;

import java.util.Random;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PoisonComponentListener extends ItemMComponentListener {
   public boolean isWin(double chance) {
      if (chance <= 0.0D) {
         return false;
      } else {
         return (new Random()).nextDouble() * 100.0D <= chance;
      }
   }

   @EventHandler
   public void onEntityDamageByEntityEvent(@NotNull EntityDamageByEntityEvent event) {
      Entity defenderEntity = event.getEntity();
      Entity attackerEntity = event.getDamager();
      if (attackerEntity instanceof Player && defenderEntity instanceof Player) {
         Player defenderPlayer = (Player)defenderEntity;
         Player attackerPlayer = (Player)attackerEntity;
         EntityEquipment attackerPlayerEquipment = attackerPlayer.getEquipment();
         ItemStack itemInHand = attackerPlayerEquipment.getItemInHand();
         PoisonComponent poisonComponent = (PoisonComponent)ItemMAPI.getFirstItemMComponent(itemInHand, PoisonComponent.class, true);
         if (poisonComponent != null) {
            Location attackerPlayerLocation = attackerPlayer.getLocation();
            World attackerPlayerWorld = attackerPlayer.getWorld();
            if (poisonComponent.isWorldAllowed(attackerPlayerWorld)) {
               Region affectedRegion = new Region(attackerPlayerLocation, attackerPlayerLocation);
               if (!poisonComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
                  double triggerChance = poisonComponent.getTriggerChance();
                  if (this.isWin(triggerChance)) {
                     poisonComponent.addAllDisorientationEffectsToLivingEntity(defenderPlayer);
                     poisonComponent.sendYouPoisonedPlayerMessage(attackerPlayer, defenderPlayer);
                     poisonComponent.sendYouWerePoisonedMessage(attackerPlayer, defenderPlayer);
                  }
               }
            }
         }
      }
   }
}

package su.mcdev.minecraft.spigot.mcitems.item.component.regeneration_shield;

import java.util.Random;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class RegenerationShieldComponentListener extends ItemMComponentListener {
   protected boolean isWin(double chance) {
      if (chance <= 0.0D) {
         return false;
      } else {
         return (new Random()).nextDouble() * 100.0D <= chance;
      }
   }

   protected void playerDefendByShield(@NotNull Player defenderPlayer, @NotNull ItemStack shieldItemStack) {
      RegenerationShieldComponent regenerationShieldComponent = (RegenerationShieldComponent)ItemMAPI.getFirstItemMComponent(shieldItemStack, RegenerationShieldComponent.class, true);
      if (regenerationShieldComponent != null) {
         World playerWorld = defenderPlayer.getWorld();
         if (regenerationShieldComponent.isWorldAllowed(playerWorld)) {
            double abilityChancePercent = regenerationShieldComponent.getAbilityChancePercent();
            if (this.isWin(abilityChancePercent)) {
               regenerationShieldComponent.addAllDisorientationEffectsToLivingEntity(defenderPlayer);
               regenerationShieldComponent.sendRegenerationHitMessage(defenderPlayer);
            }
         }
      }
   }

   @EventHandler
   protected void onEntityDamageByEntityEvent(@NotNull EntityDamageByEntityEvent event) {
      Entity entity = event.getEntity();
      if (entity instanceof Player) {
         Player player = (Player)event.getEntity();
         if (player.isBlocking()) {
            PlayerInventory playerInventory = player.getInventory();
            ItemStack itemInMainHand = playerInventory.getItemInMainHand();
            ItemStack itemInOffHand = playerInventory.getItemInOffHand();
            if (itemInMainHand.getType() == Material.SHIELD && player.isHandRaised()) {
               this.playerDefendByShield(player, itemInMainHand);
            } else if (itemInOffHand.getType() == Material.SHIELD && player.isHandRaised()) {
               this.playerDefendByShield(player, itemInOffHand);
            }
         }
      }
   }
}

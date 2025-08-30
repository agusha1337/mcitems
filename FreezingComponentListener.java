package su.mcdev.minecraft.spigot.mcitems.item.component.freezing;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.advanced.delay.DelayAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.Main;

public class FreezingComponentListener extends ItemMComponentListener {
   @NotNull
   protected List<FreezePlayer> freezePlayerList = new ArrayList();
   @NotNull
   protected DelayAdvanced clickDelayAdvanced = UtilM.delay();
   @NotNull
   protected BukkitRunnable bukkitRunnable = new BukkitRunnable() {
      public void run() {
         for(int i = 0; i < FreezingComponentListener.this.freezePlayerList.size(); ++i) {
            FreezePlayer freezePlayer = (FreezePlayer)FreezingComponentListener.this.freezePlayerList.get(i);
            Player player = freezePlayer.getPlayer();
            int tempFreezeSeconds = freezePlayer.getTempFreezeSeconds();
            --tempFreezeSeconds;
            if (tempFreezeSeconds > 0) {
               freezePlayer.setTempFreezeSeconds(tempFreezeSeconds);
               return;
            }

            FreezingComponentListener.this.resetPlayerSpeed(player);
            FreezingComponentListener.this.freezePlayerList.remove(i);
            --i;
         }

      }
   };

   public void init() {
      this.bukkitRunnable.runTaskTimer(Main.instance, 20L, 20L);
   }

   public void resetPlayerSpeed(@NotNull Player player) {
      player.setWalkSpeed(0.2F);
      player.setFlySpeed(0.1F);
   }

   public void destroy() {
      for(int i = 0; i < this.freezePlayerList.size(); ++i) {
         FreezePlayer freezePlayer = (FreezePlayer)this.freezePlayerList.get(i);
         Player player = freezePlayer.getPlayer();
         this.resetPlayerSpeed(player);
         this.freezePlayerList.remove(i);
         --i;
      }

      this.bukkitRunnable.cancel();
   }

   public void freezePlayer(@NotNull Player player, int seconds) {
      FreezePlayer freezePlayer = new FreezePlayer(player, seconds);
      this.freezePlayerList.add(freezePlayer);
      player.setWalkSpeed(0.0F);
      player.setFlySpeed(0.0F);
   }

   @EventHandler
   private void onPlayerQuitEvent(@NotNull PlayerQuitEvent event) {
      Player player = event.getPlayer();
      this.freezePlayerList.stream().filter((freezePlayer) -> {
         return freezePlayer.getPlayer() == player;
      }).findFirst().ifPresent((freezePlayer) -> {
         this.freezePlayerList.remove(freezePlayer);
         this.resetPlayerSpeed(player);
      });
   }

   @EventHandler
   private void onPlayerRespawnEvent(@NotNull PlayerRespawnEvent event) {
      Player player = event.getPlayer();
      this.freezePlayerList.stream().filter((freezePlayer) -> {
         return freezePlayer.getPlayer() == player;
      }).findFirst().ifPresent((freezePlayer) -> {
         this.freezePlayerList.remove(freezePlayer);
         this.resetPlayerSpeed(player);
      });
   }

   @EventHandler
   public void onPlayerMove(@NotNull PlayerMoveEvent event) {
      if (!event.isCancelled()) {
         Player player = event.getPlayer();
         double fromY = event.getFrom().getY();
         double toY = event.getTo().getY();
         if (!this.freezePlayerList.stream().noneMatch((freezePlayer) -> {
            return freezePlayer.getPlayer() == player;
         })) {
            if (!(toY <= fromY)) {
               event.setCancelled(true);
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   private void onEntityDamageByEntityEvent(@NotNull EntityDamageByEntityEvent event) {
      if (!event.isCancelled()) {
         Entity defenderEntity = event.getEntity();
         Entity attackerEntity = event.getDamager();
         if (attackerEntity instanceof Player && defenderEntity instanceof Player) {
            Player defenderPlayer = (Player)defenderEntity;
            Player attackerPlayer = (Player)attackerEntity;
            EntityEquipment attackerLivingEntityEquipment = attackerPlayer.getEquipment();
            ItemStack itemInHand = attackerLivingEntityEquipment.getItemInHand();
            FreezingComponent freezingComponent = (FreezingComponent)ItemMAPI.getFirstItemMComponent(itemInHand, FreezingComponent.class, true);
            if (freezingComponent != null) {
               Location attackerPlayerLocation = attackerPlayer.getLocation();
               World attackerPlayerWorld = attackerPlayer.getWorld();
               if (!freezingComponent.isWorldAllowed(attackerPlayerWorld)) {
                  freezingComponent.sendUseDenyWorldMessage(attackerPlayer);
               } else {
                  Region affectedRegion = new Region(attackerPlayerLocation, attackerPlayerLocation);
                  if (freezingComponent.isIntersectingWithOneOfWorldGuardDenyRegion(affectedRegion)) {
                     freezingComponent.sendIsIntersectingWithOneOfWorldGuardDenyRegionMessage(attackerPlayer);
                  } else if (!this.clickDelayAdvanced.hasDelay(attackerPlayer)) {
                     this.clickDelayAdvanced.delay(attackerPlayer, 1);
                     if (!freezingComponent.hasDelayIfHasSendUseDelayMessage(attackerPlayer)) {
                        int durationSeconds = freezingComponent.getDurationSeconds();
                        FreezePlayer freezePlayer = (FreezePlayer)this.freezePlayerList.stream().filter((freezePlayer1) -> {
                           return freezePlayer1.getPlayer() == defenderPlayer;
                        }).findFirst().orElse((Object)null);
                        if (freezePlayer == null) {
                           this.freezePlayer(defenderPlayer, durationSeconds);
                           freezingComponent.sendTargetIsFrozenMessage(attackerPlayer, defenderPlayer);
                           freezingComponent.sendYouAreFrozenMessage(attackerPlayer, defenderPlayer);
                           Location defenderEntityLocation = defenderEntity.getLocation();
                           freezingComponent.playFreezeSound(defenderEntityLocation);
                           freezingComponent.playFreezeEffect(defenderEntityLocation.clone().add(0.0D, 1.0D, 0.0D));
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @NotNull
   @Generated
   public List<FreezePlayer> getFreezePlayerList() {
      return this.freezePlayerList;
   }

   @NotNull
   @Generated
   public DelayAdvanced getClickDelayAdvanced() {
      return this.clickDelayAdvanced;
   }

   @NotNull
   @Generated
   public BukkitRunnable getBukkitRunnable() {
      return this.bukkitRunnable;
   }

   @Generated
   public void setFreezePlayerList(@NotNull List<FreezePlayer> freezePlayerList) {
      this.freezePlayerList = freezePlayerList;
   }

   @Generated
   public void setClickDelayAdvanced(@NotNull DelayAdvanced clickDelayAdvanced) {
      this.clickDelayAdvanced = clickDelayAdvanced;
   }

   @Generated
   public void setBukkitRunnable(@NotNull BukkitRunnable bukkitRunnable) {
      this.bukkitRunnable = bukkitRunnable;
   }
}

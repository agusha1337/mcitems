package su.mcdev.minecraft.spigot.mcitems.particle.emitter;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import me.socrum.advanced.util.Destructible;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.Emitter;

public class EmitterScheduler implements Destructible {
   @NotNull
   private Plugin plugin;
   @NotNull
   private EmitterManager emitterManager;
   @NotNull
   private BukkitRunnable bukkitRunnable;

   public EmitterScheduler(@NotNull Plugin plugin, @NotNull EmitterManager emitterDataManager) {
      this.plugin = plugin;
      this.emitterManager = emitterDataManager;
      this.bukkitRunnable = new BukkitRunnable() {
         public void run() {
            EmitterScheduler.this.update();
         }
      };
      this.bukkitRunnable.runTaskTimerAsynchronously(this.plugin, 1L, 1L);
   }

   public void destruct() {
      this.bukkitRunnable.cancel();
   }

   private void update() {
      EmitterManager emitterManager = this.emitterManager;
      CopyOnWriteArrayList<Emitter> emitterCopyOnWriteArrayList = emitterManager.getEmitterCopyOnWriteArrayList();
      Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
      Iterator var4 = emitterCopyOnWriteArrayList.iterator();

      while(var4.hasNext()) {
         Emitter emitter = (Emitter)var4.next();
         if (this.isEmitterNearToAnyPlayer(emitter, onlinePlayerCollection)) {
            emitter.spawnParticle();
         }
      }

   }

   private boolean isEmitterNearToAnyPlayer(@NotNull Emitter emitter, @NotNull Collection<? extends Player> playerCollection) {
      Location emitterLocation = emitter.getLocation();
      Iterator var4 = playerCollection.iterator();

      Location playerLocation;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         Player player = (Player)var4.next();
         playerLocation = player.getLocation();
      } while(!(UtilM.distance(playerLocation, emitterLocation) < 32.0D));

      return true;
   }

   @NotNull
   @Generated
   public Plugin getPlugin() {
      return this.plugin;
   }

   @NotNull
   @Generated
   public EmitterManager getEmitterManager() {
      return this.emitterManager;
   }

   @NotNull
   @Generated
   public BukkitRunnable getBukkitRunnable() {
      return this.bukkitRunnable;
   }

   @Generated
   public void setPlugin(@NotNull Plugin plugin) {
      this.plugin = plugin;
   }

   @Generated
   public void setEmitterManager(@NotNull EmitterManager emitterManager) {
      this.emitterManager = emitterManager;
   }

   @Generated
   public void setBukkitRunnable(@NotNull BukkitRunnable bukkitRunnable) {
      this.bukkitRunnable = bukkitRunnable;
   }
}

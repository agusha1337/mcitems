package su.mcdev.minecraft.spigot.mcitems.item.component.arise.entity;

import lombok.Generated;
import me.socrum.advanced.ini.Initer;
import me.socrum.minecraft.spigot.plugin.utilm.Main;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class AriseEntityScheduler implements Initer {
   @NotNull
   private AriseEntityManager ariseEntityManager;
   @NotNull
   private BukkitRunnable bukkitRunnable;

   public AriseEntityScheduler(@NotNull AriseEntityManager ariseEntityManager) {
      this.ariseEntityManager = ariseEntityManager;
      this.bukkitRunnable = new BukkitRunnable() {
         public void run() {
            AriseEntityScheduler.this.ariseEntityManager.update();
         }
      };
   }

   public void init() {
      this.bukkitRunnable.runTaskTimer(Main.instance, 1L, 1L);
   }

   public void destroy() {
      this.bukkitRunnable.cancel();
   }

   @NotNull
   @Generated
   public AriseEntityManager getAriseEntityManager() {
      return this.ariseEntityManager;
   }

   @NotNull
   @Generated
   public BukkitRunnable getBukkitRunnable() {
      return this.bukkitRunnable;
   }

   @Generated
   public void setAriseEntityManager(@NotNull AriseEntityManager ariseEntityManager) {
      this.ariseEntityManager = ariseEntityManager;
   }

   @Generated
   public void setBukkitRunnable(@NotNull BukkitRunnable bukkitRunnable) {
      this.bukkitRunnable = bukkitRunnable;
   }
}

package su.mcdev.chunk_loader.scheduler;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import su.mcdev.chunk_loader.ChunkLoaderApi;

public class BukkitSchedulerImplementation implements ISchedulerImplementation {
   public static final BukkitSchedulerImplementation INSTANCE = new BukkitSchedulerImplementation();

   private BukkitSchedulerImplementation() {
   }

   public boolean isRegionScheduler() {
      return false;
   }

   public ScheduledTask scheduleTask(World world, int chunkX, int chunkZ, Runnable task, long delay) {
      return this.scheduleTask(task, delay);
   }

   public ScheduledTask scheduleTask(Entity unused, Runnable task, long delay) {
      return this.scheduleTask(task, delay);
   }

   public ScheduledTask scheduleTask(Runnable task, long delay) {
      Objects.requireNonNull(ChunkLoaderApi.plugin);
      return delay <= 0L ? new BukkitSchedulerImplementation.BukkitScheduledTask(Bukkit.getScheduler().runTask(ChunkLoaderApi.plugin, task)) : new BukkitSchedulerImplementation.BukkitScheduledTask(Bukkit.getScheduler().runTaskLater(ChunkLoaderApi.plugin, task, delay));
   }

   public ScheduledTask scheduleAsyncTask(Runnable task, long delay) {
      Objects.requireNonNull(ChunkLoaderApi.plugin);
      return delay <= 0L ? new BukkitSchedulerImplementation.BukkitScheduledTask(Bukkit.getScheduler().runTaskAsynchronously(ChunkLoaderApi.plugin, task)) : new BukkitSchedulerImplementation.BukkitScheduledTask(Bukkit.getScheduler().runTaskLaterAsynchronously(ChunkLoaderApi.plugin, task, delay));
   }

   private static class BukkitScheduledTask implements ScheduledTask {
      private int taskId;

      BukkitScheduledTask(BukkitTask bukkitTask) {
         this(bukkitTask.getTaskId());
      }

      BukkitScheduledTask(int taskId) {
         this.taskId = taskId;
      }

      public void cancel() {
         if (Bukkit.getScheduler().isCurrentlyRunning(this.taskId)) {
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.taskId = -1;
         }

      }
   }
}

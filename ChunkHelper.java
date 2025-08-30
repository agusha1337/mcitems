package su.mcdev.minecraft.spigot.mcitems.commons;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class ChunkHelper {
   public static void loadChunk(@NotNull JavaPlugin javaPlugin, @NotNull Chunk chunk) {
      final int chunkX = chunk.getX();
      final int chunkZ = chunk.getZ();
      final World world = chunk.getWorld();
      <undefinedtype> bukkitRunnable = new BukkitRunnable() {
         public void run() {
            try {
               world.setChunkForceLoaded(chunkX, chunkZ, true);
            } catch (Throwable var5) {
            } finally {
               world.loadChunk(chunkX, chunkZ);
            }

         }
      };
      bukkitRunnable.runTaskLater(javaPlugin, 1L);
   }
}

package su.mcdev.chunk_loader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Generated;
import me.socrum.advanced.util.Destructible;
import me.socrum.advanced.util.Password;
import me.socrum.minecraft.spigot.plugin.utilm.Version;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.mcdev.chunk_loader.scheduler.Scheduler;
import su.mcdev.chunk_loader.v1_16_R3.NmsAdapter_v1_16_R3;

public class ChunkLoaderApi implements Destructible {
   @Nullable
   public static Plugin plugin;
   @NotNull
   private final NmsAdapter nmsAdapter;
   @NotNull
   private final Map<ChunkLoader, NpcChunkLoader> chunkLoaderNpcChunkLoaderMap;
   @NotNull
   private final ChunkLoaderListener chunkLoaderListener;

   public ChunkLoaderApi(@NotNull Plugin plugin) throws Exception {
      ChunkLoaderApi.plugin = plugin;
      this.nmsAdapter = this.calcNmsAdapter();
      this.chunkLoaderNpcChunkLoaderMap = new HashMap();
      this.chunkLoaderListener = new ChunkLoaderListener(ChunkLoaderApi.plugin, this);
   }

   public void destruct() {
      this.chunkLoaderListener.destruct();
   }

   public void createLoader(@NotNull ChunkLoader chunkLoader) {
      List<Chunk> chunkList = chunkLoader.getChunkList();
      Runnable runnable = () -> {
         this.nmsAdapter.setChunksForcedForLoader(chunkList, true);
      };
      if (Scheduler.isRegionScheduler()) {
         Scheduler.runTask(runnable);
      } else {
         runnable.run();
      }

      UUID generatedUniqueNpcUuid = this.generateUniqueNpcUuid();
      String generatedUniqueNpcName = this.generateUniqueNpcName();
      NpcChunkLoader npcChunkLoader = this.nmsAdapter.createNpcChunkLoader(chunkLoader, generatedUniqueNpcUuid, generatedUniqueNpcName);
      Player player = npcChunkLoader.getPlayer();
      player.setMetadata("NPC", new FixedMetadataValue(plugin, true));
      this.chunkLoaderNpcChunkLoaderMap.put(chunkLoader, npcChunkLoader);
   }

   public void removeLoader(@NotNull ChunkLoader chunkLoader) {
      List<Chunk> chunkList = chunkLoader.getChunkList();
      NpcChunkLoader chunkLoaderNPC = (NpcChunkLoader)this.chunkLoaderNpcChunkLoaderMap.get(chunkLoader);
      if (chunkLoaderNPC != null) {
         Player player = chunkLoaderNPC.getPlayer();
         player.removeMetadata("NPC", plugin);
         chunkLoaderNPC.die();
         this.chunkLoaderNpcChunkLoaderMap.remove(chunkLoader);
      }

      Runnable runnable = () -> {
         this.nmsAdapter.setChunksForcedForLoader(chunkList, false);
      };
      if (Scheduler.isRegionScheduler()) {
         Scheduler.runTask(runnable);
      } else {
         runnable.run();
      }

   }

   @NotNull
   private NmsAdapter calcNmsAdapter() throws Exception {
      Server server = Bukkit.getServer();
      Version version = Version.getServerVersion(server);
      NmsAdapter nmsAdapter = null;
      if (List.of(Version.v1_16_R3, Version.v1_16_R2, Version.v1_16_R1).contains(version)) {
         nmsAdapter = new NmsAdapter_v1_16_R3();
      }

      if (nmsAdapter == null) {
         String message = "Не удалось получить NMS адаптер для этой версии сервера '" + version.name() + "'.";
         throw new Exception(message);
      } else {
         return nmsAdapter;
      }
   }

   @NotNull
   private UUID generateUniqueNpcUuid() {
      UUID uuid;
      for(uuid = UUID.randomUUID(); this.getNpcChunkLoaderByNpcUuid(uuid) != null; uuid = UUID.randomUUID()) {
      }

      return uuid;
   }

   @NotNull
   private String generateUniqueNpcName() {
      String npcName;
      for(npcName = Password.generateRandomPassword(16); this.getNpcChunkLoaderByNpcName(npcName) != null; npcName = Password.generateRandomPassword(16)) {
      }

      return npcName;
   }

   @Nullable
   private NpcChunkLoader getNpcChunkLoaderByNpcUuid(@NotNull UUID npcUuid) {
      Iterator var2 = this.chunkLoaderNpcChunkLoaderMap.values().iterator();

      NpcChunkLoader npcChunkLoader;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         npcChunkLoader = (NpcChunkLoader)var2.next();
      } while(!npcChunkLoader.getNpcUuid().equals(npcUuid));

      return npcChunkLoader;
   }

   @Nullable
   private NpcChunkLoader getNpcChunkLoaderByNpcName(@NotNull String npcName) {
      Iterator var2 = this.chunkLoaderNpcChunkLoaderMap.values().iterator();

      NpcChunkLoader npcChunkLoader;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         npcChunkLoader = (NpcChunkLoader)var2.next();
      } while(!npcChunkLoader.getNpcName().equals(npcName));

      return npcChunkLoader;
   }

   @NotNull
   @Generated
   public NmsAdapter getNmsAdapter() {
      return this.nmsAdapter;
   }

   @NotNull
   @Generated
   public Map<ChunkLoader, NpcChunkLoader> getChunkLoaderNpcChunkLoaderMap() {
      return this.chunkLoaderNpcChunkLoaderMap;
   }

   @NotNull
   @Generated
   public ChunkLoaderListener getChunkLoaderListener() {
      return this.chunkLoaderListener;
   }
}

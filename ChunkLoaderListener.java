package su.mcdev.chunk_loader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import me.socrum.advanced.util.Destructible;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ChunkLoaderListener implements Destructible, Listener {
   @NotNull
   private final Plugin plugin;
   @NotNull
   private final ChunkLoaderApi chunkLoaderApi;

   public ChunkLoaderListener(@NotNull Plugin plugin, @NotNull ChunkLoaderApi chunkLoaderApi) {
      this.plugin = plugin;
      this.chunkLoaderApi = chunkLoaderApi;
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
   }

   public void destruct() {
      HandlerList.unregisterAll(this);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onPlayerJoinEvent(@NotNull PlayerJoinEvent event) {
      Player player = event.getPlayer();
      Map<ChunkLoader, NpcChunkLoader> chunkLoaderNpcChunkLoaderMap = this.chunkLoaderApi.getChunkLoaderNpcChunkLoaderMap();
      Iterator var4 = chunkLoaderNpcChunkLoaderMap.values().iterator();

      while(var4.hasNext()) {
         NpcChunkLoader npcChunkLoader = (NpcChunkLoader)var4.next();
         Player npcChunkLoaderPlayer = npcChunkLoader.getPlayer();
         player.hidePlayer(npcChunkLoaderPlayer);
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onBlockPlaceEvent(@NotNull BlockPlaceEvent event) {
      if (!event.isCancelled()) {
         Block block = event.getBlock();
         Chunk chunk = block.getChunk();
         Material blockMaterial = block.getType();
         NmsAdapter nmsAdapter = this.chunkLoaderApi.getNmsAdapter();
         Material spawnerMaterial = nmsAdapter.getSpawnerMaterial();
         if (blockMaterial == spawnerMaterial) {
            Map<ChunkLoader, NpcChunkLoader> chunkLoaderNpcChunkLoaderMap = this.chunkLoaderApi.getChunkLoaderNpcChunkLoaderMap();
            Iterator var8 = chunkLoaderNpcChunkLoaderMap.keySet().iterator();

            List chunkList;
            do {
               if (!var8.hasNext()) {
                  return;
               }

               ChunkLoader chunkLoader = (ChunkLoader)var8.next();
               chunkList = chunkLoader.getChunkList();
            } while(!chunkList.contains(chunk));

            nmsAdapter.setChunksForcedForLoader(new ArrayList(List.of(chunk)), true);
         }
      }
   }

   @NotNull
   @Generated
   public Plugin getPlugin() {
      return this.plugin;
   }

   @NotNull
   @Generated
   public ChunkLoaderApi getChunkLoaderApi() {
      return this.chunkLoaderApi;
   }
}

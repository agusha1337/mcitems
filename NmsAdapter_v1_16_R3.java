package su.mcdev.chunk_loader.v1_16_R3;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.v1_16_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_16_R3.MobSpawnerAbstract;
import net.minecraft.server.v1_16_R3.TileEntity;
import net.minecraft.server.v1_16_R3.TileEntityMobSpawner;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.mcdev.chunk_loader.ChunkLoader;
import su.mcdev.chunk_loader.NmsAdapter;
import su.mcdev.chunk_loader.NpcChunkLoader;

public class NmsAdapter_v1_16_R3 implements NmsAdapter {
   @NotNull
   public NpcChunkLoader createNpcChunkLoader(@NotNull ChunkLoader chunkLoader, @NotNull UUID npcUuid, @NotNull String npcName) {
      Location chunkLoaderLocation = chunkLoader.getLocation();
      return new NpcChunkLoader_v1_16_R3(chunkLoaderLocation, npcUuid, npcName);
   }

   public void setChunksForcedForLoader(@NotNull List<Chunk> chunkList, boolean forced) {
      if (!chunkList.isEmpty()) {
         Chunk chunk1 = (Chunk)chunkList.get(0);
         World world = chunk1.getWorld();
         CraftWorld craftWorld = (CraftWorld)world;
         WorldServer worldServer = craftWorld.getHandle();
         int requiredPlayerRange = forced ? -1 : 16;
         Iterator var8 = chunkList.iterator();

         while(var8.hasNext()) {
            Chunk chunk = (Chunk)var8.next();
            CraftChunk craftChunk = (CraftChunk)chunk;
            net.minecraft.server.v1_16_R3.Chunk chunk_v1_16_R3 = craftChunk.getHandle();
            Iterator var12 = chunk_v1_16_R3.tileEntities.values().iterator();

            while(var12.hasNext()) {
               TileEntity tileEntity = (TileEntity)var12.next();
               if (tileEntity instanceof TileEntityMobSpawner) {
                  TileEntityMobSpawner tileEntityMobSpawner = (TileEntityMobSpawner)tileEntity;
                  MobSpawnerAbstract mobSpawnerAbstract = tileEntityMobSpawner.getSpawner();
                  mobSpawnerAbstract.requiredPlayerRange = requiredPlayerRange;
               }
            }

            ChunkCoordIntPair chunkCoordIntPair = chunk_v1_16_R3.getPos();
            worldServer.setForceLoaded(chunkCoordIntPair.x, chunkCoordIntPair.z, forced);
         }

      }
   }

   @Nullable
   public Material getSpawnerMaterial() {
      return Material.SPAWNER;
   }
}

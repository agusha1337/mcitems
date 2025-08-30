package su.mcdev.chunk_loader;

import java.util.List;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.jetbrains.annotations.NotNull;

public class ChunkLoader {
   @NotNull
   private Location location;
   @NotNull
   private List<Chunk> chunkList;
   @NotNull
   private World world;
   private WorldServer worldServer;

   public ChunkLoader(@NotNull Location location, @NotNull List<Chunk> chunkList) {
      this.location = location;
      this.chunkList = chunkList;
      this.world = (World)Objects.requireNonNull(this.location.getWorld());
      CraftWorld craftWorld = (CraftWorld)this.world;
      WorldServer worldServer = craftWorld.getHandle();
      this.worldServer = worldServer;
   }

   @NotNull
   @Generated
   public Location getLocation() {
      return this.location;
   }

   @NotNull
   @Generated
   public List<Chunk> getChunkList() {
      return this.chunkList;
   }

   @NotNull
   @Generated
   public World getWorld() {
      return this.world;
   }

   @Generated
   public WorldServer getWorldServer() {
      return this.worldServer;
   }

   @Generated
   public void setLocation(@NotNull Location location) {
      this.location = location;
   }

   @Generated
   public void setChunkList(@NotNull List<Chunk> chunkList) {
      this.chunkList = chunkList;
   }

   @Generated
   public void setWorld(@NotNull World world) {
      this.world = world;
   }

   @Generated
   public void setWorldServer(WorldServer worldServer) {
      this.worldServer = worldServer;
   }
}

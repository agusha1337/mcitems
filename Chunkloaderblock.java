package su.mcdev.minecraft.spigot.mcitems.item.component.chunkloaderblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.BlockM;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.ItemM;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.mcdev.chunk_loader.ChunkLoader;
import su.mcdev.minecraft.spigot.mcitems.Main;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.EmitterApi;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.Emitter;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.EmitterData;

@SerializableAs("mcItems_Chunkloaderblock")
public class Chunkloaderblock implements ConfigurationSerializable {
   @NotNull
   private String creatorPlayerName;
   @NotNull
   private Location location;
   @NotNull
   private String itemmItemIdUsedToCreateThisChunkloaderblock;
   @NotNull
   private EmitterApi emitterApi;
   @Nullable
   private ChunkloaderblockComponent chunkloaderblockComponent;
   @NotNull
   private ChunkLoader chunkLoader;
   @Nullable
   private Emitter emitter;

   public Chunkloaderblock(@NotNull String creatorPlayerName, @NotNull Location location, @NotNull String itemmItemIdUsedToCreateThisChunkloaderblock, @NotNull EmitterApi emitterApi) {
      this.creatorPlayerName = creatorPlayerName;
      this.location = location;
      this.itemmItemIdUsedToCreateThisChunkloaderblock = itemmItemIdUsedToCreateThisChunkloaderblock;
      this.emitterApi = emitterApi;
      this.chunkloaderblockComponent = this.calcChunkloaderblockComponent();
      List<Chunk> chunkList = this.calcCoveredChunkList();
      this.chunkLoader = new ChunkLoader(this.location, chunkList);
      if (this.chunkloaderblockComponent != null) {
         EmitterData emitterData = this.chunkloaderblockComponent.getEmitterData();
         if (emitterData != null) {
            double emitterSpawnOffsetX = this.chunkloaderblockComponent.getEmitterSpawnOffsetX();
            double emitterSpawnOffsetY = this.chunkloaderblockComponent.getEmitterSpawnOffsetY();
            double emitterSpawnOffsetZ = this.chunkloaderblockComponent.getEmitterSpawnOffsetZ();
            Location emitterSpawnLocation = location.clone().add(emitterSpawnOffsetX, emitterSpawnOffsetY, emitterSpawnOffsetZ);
            this.emitter = emitterData.createEmitter(emitterSpawnLocation);
         }
      }

   }

   @Nullable
   public ChunkloaderblockComponent calcChunkloaderblockComponent() {
      ItemM itemm = ItemMAPI.getOrNull(this.itemmItemIdUsedToCreateThisChunkloaderblock);
      return itemm == null ? null : (ChunkloaderblockComponent)ItemMAPI.getFirstItemMComponent(itemm.getItemStack(), ChunkloaderblockComponent.class, false);
   }

   protected void recreateEmitterAfterRestart() {
      if (this.emitter != null) {
         this.emitterApi.attachEmitter(this.emitter);
      }

   }

   public void place(@NotNull Player initiator) {
      ItemM itemm = ItemMAPI.getOrNull(this.itemmItemIdUsedToCreateThisChunkloaderblock);
      if (itemm != null) {
         ItemStack itemStack = itemm.getItemStack();
         ChunkloaderblockComponent chunkloaderblockComponent = (ChunkloaderblockComponent)ItemMAPI.getFirstItemMComponent(itemStack, ChunkloaderblockComponent.class, false);
         if (chunkloaderblockComponent != null) {
            BlockM toPlaceBlockM = chunkloaderblockComponent.getToPlaceBlockM();
            Block block = this.location.getBlock();
            toPlaceBlockM.place(block);
            chunkloaderblockComponent.sendPlacedMessage(initiator);
            if (this.emitter != null) {
               this.emitterApi.attachEmitter(this.emitter);
            }

         }
      }
   }

   private void removeBlock() {
      Block block = this.location.getBlock();
      block.setType(Material.AIR);
   }

   private void dropItemUsedToCreateThisDamageblock() {
      ItemM itemm = ItemMAPI.getOrNull(this.itemmItemIdUsedToCreateThisChunkloaderblock);
      if (itemm != null) {
         ItemStack itemStackToDrop = itemm.getItemStack();
         World world = this.location.getWorld();
         Location dropLocation = this.location.clone();
         world.dropItemNaturally(dropLocation, itemStackToDrop);
      }
   }

   public void removeBlock(@NotNull Player initiator, boolean dropItem) {
      if (this.emitter != null) {
         this.emitterApi.detachEmitter(this.emitter);
      }

      this.removeBlock();
      if (dropItem) {
         this.dropItemUsedToCreateThisDamageblock();
      }

      ItemM itemm = ItemMAPI.getOrNull(this.itemmItemIdUsedToCreateThisChunkloaderblock);
      if (itemm != null) {
         ItemStack itemStack = itemm.getItemStack();
         ChunkloaderblockComponent chunkloaderblockComponent = (ChunkloaderblockComponent)ItemMAPI.getFirstItemMComponent(itemStack, ChunkloaderblockComponent.class, false);
         if (chunkloaderblockComponent != null) {
            chunkloaderblockComponent.sendBrokenMessage(initiator);
         }
      }
   }

   @NotNull
   private List<Chunk> calcCoveredChunkList() {
      ArrayList<Chunk> chunkList = new ArrayList();
      Block block = this.location.getBlock();
      ChunkloaderblockComponent chunkloaderblockComponent = this.calcChunkloaderblockComponent();
      if (chunkloaderblockComponent == null) {
         return chunkList;
      } else {
         int radius = chunkloaderblockComponent.getRadius();
         if (radius <= 0) {
            chunkList.add(block.getChunk());
            return chunkList;
         } else {
            World world = block.getWorld();
            Chunk centerChunk = block.getChunk();
            int centerX = centerChunk.getX();
            int centerZ = centerChunk.getZ();

            for(int x = centerX - radius; x <= centerX + radius; ++x) {
               for(int z = centerZ - radius; z <= centerZ + radius; ++z) {
                  Chunk chunk = world.getChunkAt(x, z);
                  chunkList.add(chunk);
               }
            }

            return chunkList;
         }
      }
   }

   @NotNull
   public static Chunkloaderblock deserialize(@NotNull Map<String, Object> stringObjectMap) {
      EmitterApi emitterApi = Main.emitterModule.getEmitterApi();
      return new Chunkloaderblock(String.valueOf(stringObjectMap.getOrDefault("creator_player_name", (Object)null)), (Location)stringObjectMap.getOrDefault("location", (Object)null), String.valueOf(stringObjectMap.getOrDefault("itemm_item_id_used_to_create_this_damageblock", (Object)null)), emitterApi);
   }

   public Map<String, Object> serialize() {
      return Map.of("creator_player_name", this.creatorPlayerName, "location", this.location, "itemm_item_id_used_to_create_this_damageblock", this.itemmItemIdUsedToCreateThisChunkloaderblock);
   }

   @NotNull
   @Generated
   public String getCreatorPlayerName() {
      return this.creatorPlayerName;
   }

   @NotNull
   @Generated
   public Location getLocation() {
      return this.location;
   }

   @NotNull
   @Generated
   public String getItemmItemIdUsedToCreateThisChunkloaderblock() {
      return this.itemmItemIdUsedToCreateThisChunkloaderblock;
   }

   @NotNull
   @Generated
   public EmitterApi getEmitterApi() {
      return this.emitterApi;
   }

   @Nullable
   @Generated
   public ChunkloaderblockComponent getChunkloaderblockComponent() {
      return this.chunkloaderblockComponent;
   }

   @NotNull
   @Generated
   public ChunkLoader getChunkLoader() {
      return this.chunkLoader;
   }

   @Nullable
   @Generated
   public Emitter getEmitter() {
      return this.emitter;
   }

   @Generated
   public void setCreatorPlayerName(@NotNull String creatorPlayerName) {
      this.creatorPlayerName = creatorPlayerName;
   }

   @Generated
   public void setLocation(@NotNull Location location) {
      this.location = location;
   }

   @Generated
   public void setItemmItemIdUsedToCreateThisChunkloaderblock(@NotNull String itemmItemIdUsedToCreateThisChunkloaderblock) {
      this.itemmItemIdUsedToCreateThisChunkloaderblock = itemmItemIdUsedToCreateThisChunkloaderblock;
   }

   @Generated
   public void setEmitterApi(@NotNull EmitterApi emitterApi) {
      this.emitterApi = emitterApi;
   }

   @Generated
   public void setChunkloaderblockComponent(@Nullable ChunkloaderblockComponent chunkloaderblockComponent) {
      this.chunkloaderblockComponent = chunkloaderblockComponent;
   }

   @Generated
   public void setChunkLoader(@NotNull ChunkLoader chunkLoader) {
      this.chunkLoader = chunkLoader;
   }

   @Generated
   public void setEmitter(@Nullable Emitter emitter) {
      this.emitter = emitter;
   }
}

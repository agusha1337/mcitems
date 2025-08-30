package su.mcdev.minecraft.spigot.mcitems.item.component.chunkloaderblock;

import java.io.File;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.EmitterApi;

public class ChunkloaderblockModule extends ItemMComponentListener {
   @NotNull
   private File databaseYamlFile;
   @NotNull
   private EmitterApi emitterApi;
   @NotNull
   private Plugin plugin;
   @NotNull
   private ChunkloaderblockManager chunkloaderblockManager;
   @NotNull
   private ChunkloaderblockListener chunkloaderblockListener;

   public ChunkloaderblockModule(@NotNull File databaseYamlFile, @NotNull EmitterApi emitterApi, @NotNull Plugin plugin) {
      this.databaseYamlFile = databaseYamlFile;
      this.emitterApi = emitterApi;
      this.plugin = plugin;
      ConfigurationSerialization.registerClass(Chunkloaderblock.class);
      this.chunkloaderblockManager = new ChunkloaderblockManager(this.databaseYamlFile, this.plugin);
      this.chunkloaderblockListener = new ChunkloaderblockListener(this.chunkloaderblockManager, this.emitterApi);
   }

   public void init() {
      this.chunkloaderblockManager.init();
      this.chunkloaderblockListener.init();
   }

   public void destroy() {
      this.chunkloaderblockListener.destroy();
      this.chunkloaderblockManager.destroy();
      ConfigurationSerialization.unregisterClass(Chunkloaderblock.class);
   }

   @NotNull
   @Generated
   public File getDatabaseYamlFile() {
      return this.databaseYamlFile;
   }

   @NotNull
   @Generated
   public EmitterApi getEmitterApi() {
      return this.emitterApi;
   }

   @NotNull
   @Generated
   public Plugin getPlugin() {
      return this.plugin;
   }

   @NotNull
   @Generated
   public ChunkloaderblockManager getChunkloaderblockManager() {
      return this.chunkloaderblockManager;
   }

   @NotNull
   @Generated
   public ChunkloaderblockListener getChunkloaderblockListener() {
      return this.chunkloaderblockListener;
   }

   @Generated
   public void setDatabaseYamlFile(@NotNull File databaseYamlFile) {
      this.databaseYamlFile = databaseYamlFile;
   }

   @Generated
   public void setEmitterApi(@NotNull EmitterApi emitterApi) {
      this.emitterApi = emitterApi;
   }

   @Generated
   public void setPlugin(@NotNull Plugin plugin) {
      this.plugin = plugin;
   }

   @Generated
   public void setChunkloaderblockManager(@NotNull ChunkloaderblockManager chunkloaderblockManager) {
      this.chunkloaderblockManager = chunkloaderblockManager;
   }

   @Generated
   public void setChunkloaderblockListener(@NotNull ChunkloaderblockListener chunkloaderblockListener) {
      this.chunkloaderblockListener = chunkloaderblockListener;
   }
}

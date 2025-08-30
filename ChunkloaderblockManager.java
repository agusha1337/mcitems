package su.mcdev.minecraft.spigot.mcitems.item.component.chunkloaderblock;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import me.socrum.advanced.ini.Initer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.mcdev.chunk_loader.ChunkLoaderApi;

public class ChunkloaderblockManager implements Initer {
   @NotNull
   private File databaseYamlFile;
   @NotNull
   private Plugin plugin;
   @Nullable
   private ChunkLoaderApi chunkLoaderApi;
   @NotNull
   private YamlConfiguration databaseConfig;
   @NotNull
   private List<Chunkloaderblock> chunkloaderblockList;

   public ChunkloaderblockManager(@NotNull File databaseYamlFile, @NotNull Plugin plugin) {
      this.databaseYamlFile = databaseYamlFile;
      this.plugin = plugin;

      try {
         this.chunkLoaderApi = new ChunkLoaderApi(this.plugin);
      } catch (Exception var5) {
         Bukkit.getLogger().warning(var5.getMessage());
      }

      this.databaseConfig = YamlConfiguration.loadConfiguration(this.databaseYamlFile);
      this.chunkloaderblockList = this.databaseConfig.getList("chunkloaderblock_list", new ArrayList());

      while(this.chunkloaderblockList.contains((Object)null)) {
         this.chunkloaderblockList.remove((Object)null);
      }

      Chunkloaderblock chunkloaderblock;
      for(Iterator var3 = this.chunkloaderblockList.iterator(); var3.hasNext(); chunkloaderblock.recreateEmitterAfterRestart()) {
         chunkloaderblock = (Chunkloaderblock)var3.next();
         if (this.chunkLoaderApi != null) {
            this.chunkLoaderApi.createLoader(chunkloaderblock.getChunkLoader());
         }
      }

   }

   public void destroy() {
      try {
         if (this.chunkLoaderApi != null) {
            Iterator var1 = this.chunkloaderblockList.iterator();

            while(var1.hasNext()) {
               Chunkloaderblock chunkloaderblock = (Chunkloaderblock)var1.next();
               this.chunkLoaderApi.removeLoader(chunkloaderblock.getChunkLoader());
            }
         }

         this.databaseConfig.set("chunkloaderblock_list", this.chunkloaderblockList);
         this.databaseConfig.save(this.databaseYamlFile);
         if (this.chunkLoaderApi != null) {
            this.chunkLoaderApi.destruct();
         }

      } catch (Throwable var3) {
         throw var3;
      }
   }

   public void create(@NotNull Chunkloaderblock chunkloaderblock, @NotNull Player initiator) {
      chunkloaderblock.place(initiator);
      if (this.chunkLoaderApi != null) {
         this.chunkLoaderApi.createLoader(chunkloaderblock.getChunkLoader());
      }

      this.chunkloaderblockList.add(chunkloaderblock);
   }

   public void delete(@NotNull Player destroyer, @NotNull Chunkloaderblock chunkloaderblock) {
      GameMode gameMode = destroyer.getGameMode();
      chunkloaderblock.removeBlock(destroyer, gameMode != GameMode.CREATIVE);
      if (this.chunkLoaderApi != null) {
         this.chunkLoaderApi.removeLoader(chunkloaderblock.getChunkLoader());
      }

      this.chunkloaderblockList.remove(chunkloaderblock);
   }

   @NotNull
   @Generated
   public File getDatabaseYamlFile() {
      return this.databaseYamlFile;
   }

   @NotNull
   @Generated
   public Plugin getPlugin() {
      return this.plugin;
   }

   @Nullable
   @Generated
   public ChunkLoaderApi getChunkLoaderApi() {
      return this.chunkLoaderApi;
   }

   @NotNull
   @Generated
   public YamlConfiguration getDatabaseConfig() {
      return this.databaseConfig;
   }

   @NotNull
   @Generated
   public List<Chunkloaderblock> getChunkloaderblockList() {
      return this.chunkloaderblockList;
   }

   @Generated
   public void setDatabaseYamlFile(@NotNull File databaseYamlFile) {
      this.databaseYamlFile = databaseYamlFile;
   }

   @Generated
   public void setPlugin(@NotNull Plugin plugin) {
      this.plugin = plugin;
   }

   @Generated
   public void setChunkLoaderApi(@Nullable ChunkLoaderApi chunkLoaderApi) {
      this.chunkLoaderApi = chunkLoaderApi;
   }

   @Generated
   public void setDatabaseConfig(@NotNull YamlConfiguration databaseConfig) {
      this.databaseConfig = databaseConfig;
   }

   @Generated
   public void setChunkloaderblockList(@NotNull List<Chunkloaderblock> chunkloaderblockList) {
      this.chunkloaderblockList = chunkloaderblockList;
   }
}

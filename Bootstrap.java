package su.mcdev.minecraft.spigot.mcitems;

import java.io.File;
import lombok.Generated;
import me.socrum.advanced.ini.Initer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.command.CommandManager;
import su.mcdev.minecraft.spigot.mcitems.item.ItemManager;
import su.mcdev.minecraft.spigot.mcitems.item.component.chunkloaderblock.ChunkloaderblockModule;
import su.mcdev.minecraft.spigot.mcitems.item.component.damageblock.DamageblockModule;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.EmitterApi;

public class Bootstrap implements Initer {
   @NotNull
   private Plugin plugin;
   @NotNull
   private DamageblockModule damageblockModule;
   @NotNull
   private ChunkloaderblockModule chunkloaderblockModule;

   public Bootstrap(@NotNull Plugin plugin) {
      this.plugin = plugin;
      EmitterApi emitterApi = Main.emitterModule.getEmitterApi();
      File dataFolder = Main.instance.getDataFolder();
      Main.ini.put(new ItemManager(emitterApi));
      Main.ini.put(new CommandManager());
      this.damageblockModule = new DamageblockModule(new File(dataFolder, "damageblock-database.yaml"), emitterApi);
      this.chunkloaderblockModule = new ChunkloaderblockModule(new File(dataFolder, "chunkloaderblock-database.yaml"), emitterApi, this.plugin);
   }

   public void init() {
      this.damageblockModule.init();
      this.chunkloaderblockModule.init();
   }

   public void destroy() {
      this.chunkloaderblockModule.destroy();
      this.damageblockModule.destroy();
   }

   @NotNull
   @Generated
   public Plugin getPlugin() {
      return this.plugin;
   }

   @NotNull
   @Generated
   public DamageblockModule getDamageblockModule() {
      return this.damageblockModule;
   }

   @NotNull
   @Generated
   public ChunkloaderblockModule getChunkloaderblockModule() {
      return this.chunkloaderblockModule;
   }

   @Generated
   public void setPlugin(@NotNull Plugin plugin) {
      this.plugin = plugin;
   }

   @Generated
   public void setDamageblockModule(@NotNull DamageblockModule damageblockModule) {
      this.damageblockModule = damageblockModule;
   }

   @Generated
   public void setChunkloaderblockModule(@NotNull ChunkloaderblockModule chunkloaderblockModule) {
      this.chunkloaderblockModule = chunkloaderblockModule;
   }
}

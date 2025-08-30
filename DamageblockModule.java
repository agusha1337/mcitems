package su.mcdev.minecraft.spigot.mcitems.item.component.damageblock;

import java.io.File;
import lombok.Generated;
import me.socrum.advanced.ini.Initer;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.EmitterApi;

public class DamageblockModule implements Initer {
   @NotNull
   private File databaseYamlFile;
   @NotNull
   private EmitterApi emitterApi;
   @NotNull
   private DamageblockManager damageBlockManager;
   @NotNull
   private DamageblockScheduler damageBlockScheduler;
   @NotNull
   private DamageblockListener damageBlockListener;

   public DamageblockModule(@NotNull File databaseYamlFile, @NotNull EmitterApi emitterApi) {
      ConfigurationSerialization.registerClass(Damageblock.class);
      this.databaseYamlFile = databaseYamlFile;
      this.emitterApi = emitterApi;
      this.damageBlockManager = new DamageblockManager(this.databaseYamlFile);
      this.damageBlockScheduler = new DamageblockScheduler(this.damageBlockManager);
      this.damageBlockListener = new DamageblockListener(this.damageBlockManager, this.emitterApi);
   }

   public void init() {
      this.damageBlockManager.init();
      this.damageBlockScheduler.init();
      this.damageBlockListener.init();
   }

   public void destroy() {
      this.damageBlockListener.destroy();
      this.damageBlockScheduler.destroy();
      this.damageBlockManager.destroy();
      ConfigurationSerialization.unregisterClass(Damageblock.class);
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
   public DamageblockManager getDamageBlockManager() {
      return this.damageBlockManager;
   }

   @NotNull
   @Generated
   public DamageblockScheduler getDamageBlockScheduler() {
      return this.damageBlockScheduler;
   }

   @NotNull
   @Generated
   public DamageblockListener getDamageBlockListener() {
      return this.damageBlockListener;
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
   public void setDamageBlockManager(@NotNull DamageblockManager damageBlockManager) {
      this.damageBlockManager = damageBlockManager;
   }

   @Generated
   public void setDamageBlockScheduler(@NotNull DamageblockScheduler damageBlockScheduler) {
      this.damageBlockScheduler = damageBlockScheduler;
   }

   @Generated
   public void setDamageBlockListener(@NotNull DamageblockListener damageBlockListener) {
      this.damageBlockListener = damageBlockListener;
   }
}

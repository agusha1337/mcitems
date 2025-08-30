package su.mcdev.minecraft.spigot.mcitems.particle.emitter;

import lombok.Generated;
import me.socrum.advanced.util.Destructible;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class EmitterModule implements Destructible {
   @NotNull
   private Plugin plugin;
   @NotNull
   private EmitterManager emitterManager;
   @NotNull
   private EmitterApi emitterApi;

   public EmitterModule(@NotNull Plugin plugin) {
      this.plugin = plugin;
      this.emitterManager = new EmitterManager(this.plugin);
      this.emitterApi = new EmitterApi(this.emitterManager);
   }

   @NotNull
   @Generated
   public Plugin getPlugin() {
      return this.plugin;
   }

   @NotNull
   @Generated
   public EmitterManager getEmitterManager() {
      return this.emitterManager;
   }

   @NotNull
   @Generated
   public EmitterApi getEmitterApi() {
      return this.emitterApi;
   }

   @Generated
   public void setPlugin(@NotNull Plugin plugin) {
      this.plugin = plugin;
   }

   @Generated
   public void setEmitterManager(@NotNull EmitterManager emitterManager) {
      this.emitterManager = emitterManager;
   }

   @Generated
   public void setEmitterApi(@NotNull EmitterApi emitterApi) {
      this.emitterApi = emitterApi;
   }
}

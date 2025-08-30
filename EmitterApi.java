package su.mcdev.minecraft.spigot.mcitems.particle.emitter;

import me.socrum.advanced.util.Destructible;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.Emitter;

public class EmitterApi implements Destructible {
   @NotNull
   private EmitterManager emitterManager;

   public EmitterApi(@NotNull EmitterManager emitterManager) {
      this.emitterManager = emitterManager;
   }

   public void attachEmitter(@NotNull Emitter emitter) {
      this.emitterManager.attach(emitter);
   }

   public void detachEmitter(@NotNull Emitter emitter) {
      this.emitterManager.detach(emitter);
   }
}

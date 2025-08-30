package su.mcdev.minecraft.spigot.mcitems.particle.emitter.object;

import lombok.Generated;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public abstract class Emitter {
   @NotNull
   protected EmitterData emitterData;
   @NotNull
   protected Location location;

   public Emitter(@NotNull EmitterData emitterData, @NotNull Location location) {
      this.emitterData = emitterData;
      this.location = location;
   }

   public abstract void spawnParticle();

   @NotNull
   @Generated
   public EmitterData getEmitterData() {
      return this.emitterData;
   }

   @NotNull
   @Generated
   public Location getLocation() {
      return this.location;
   }

   @Generated
   public void setEmitterData(@NotNull EmitterData emitterData) {
      this.emitterData = emitterData;
   }

   @Generated
   public void setLocation(@NotNull Location location) {
      this.location = location;
   }
}

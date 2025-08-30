package su.mcdev.minecraft.spigot.mcitems.particle.emitter.object;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public abstract class EmitterData {
   public EmitterData(@NotNull ConfigurationSection config) {
   }

   public abstract EmitterData newInstance(@NotNull ConfigurationSection var1);

   public abstract Emitter createEmitter(@NotNull Location var1);

   @Generated
   public EmitterData() {
   }
}

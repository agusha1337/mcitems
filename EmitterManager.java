package su.mcdev.minecraft.spigot.mcitems.particle.emitter;

import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import me.socrum.advanced.util.Destructible;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.Emitter;

public class EmitterManager implements Destructible {
   @NotNull
   private Plugin plugin;
   @NotNull
   private CopyOnWriteArrayList<Emitter> emitterCopyOnWriteArrayList;
   @NotNull
   private EmitterScheduler emitterScheduler;

   public EmitterManager(@NotNull Plugin plugin) {
      this.plugin = plugin;
      this.emitterCopyOnWriteArrayList = new CopyOnWriteArrayList();
      this.emitterScheduler = new EmitterScheduler(this.plugin, this);
   }

   public void destruct() {
      this.emitterScheduler.destruct();
   }

   public void attach(@NotNull Emitter emitter) {
      this.emitterCopyOnWriteArrayList.add(emitter);
   }

   public void detach(@NotNull Emitter emitter) {
      this.emitterCopyOnWriteArrayList.remove(emitter);
   }

   @NotNull
   @Generated
   public Plugin getPlugin() {
      return this.plugin;
   }

   @NotNull
   @Generated
   public CopyOnWriteArrayList<Emitter> getEmitterCopyOnWriteArrayList() {
      return this.emitterCopyOnWriteArrayList;
   }

   @NotNull
   @Generated
   public EmitterScheduler getEmitterScheduler() {
      return this.emitterScheduler;
   }

   @Generated
   public void setPlugin(@NotNull Plugin plugin) {
      this.plugin = plugin;
   }

   @Generated
   public void setEmitterCopyOnWriteArrayList(@NotNull CopyOnWriteArrayList<Emitter> emitterCopyOnWriteArrayList) {
      this.emitterCopyOnWriteArrayList = emitterCopyOnWriteArrayList;
   }

   @Generated
   public void setEmitterScheduler(@NotNull EmitterScheduler emitterScheduler) {
      this.emitterScheduler = emitterScheduler;
   }
}

package su.mcdev.minecraft.spigot.mcitems.item.component.arise.entity;

import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.item.component.arise.entity.helper.UpdateLoopSuppressor;

public class AriseEntityDespawner {
   private int ttlSeconds;
   @NotNull
   private UpdateLoopSuppressor updateLoopSuppressor;

   protected AriseEntityDespawner(int ttlSeconds) {
      this.ttlSeconds = ttlSeconds;
      this.updateLoopSuppressor = new UpdateLoopSuppressor(20, this::realUpdate);
   }

   private void realUpdate() {
      if (this.ttlSeconds > 0) {
         --this.ttlSeconds;
      }
   }

   protected void updatePerTick() {
      this.updateLoopSuppressor.updatePerTick();
   }

   protected boolean isReadyForDespawn() {
      boolean readyForDespawn = this.ttlSeconds <= 0;
      return readyForDespawn;
   }

   protected int getTtlSeconds() {
      return this.ttlSeconds;
   }
}

package su.mcdev.damager;

import java.util.UUID;
import lombok.Generated;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public abstract class Damager {
   @NotNull
   private final World world;
   @NotNull
   private final UUID uniquePlayerUuid;
   @NotNull
   private final String uniquePlayerName;

   public Damager(@NotNull World world, @NotNull UUID uniquePlayerUuid, @NotNull String uniquePlayerName) {
      this.world = world;
      this.uniquePlayerUuid = uniquePlayerUuid;
      this.uniquePlayerName = uniquePlayerName;
   }

   public abstract void simulateDamageByPlayer(@NotNull LivingEntity var1, float var2);

   protected abstract void runTemporaryDisableAnnounceAdvancementsRunnable(@NotNull Runnable var1);

   @NotNull
   @Generated
   public World getWorld() {
      return this.world;
   }

   @NotNull
   @Generated
   public UUID getUniquePlayerUuid() {
      return this.uniquePlayerUuid;
   }

   @NotNull
   @Generated
   public String getUniquePlayerName() {
      return this.uniquePlayerName;
   }
}

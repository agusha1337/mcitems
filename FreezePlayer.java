package su.mcdev.minecraft.spigot.mcitems.item.component.freezing;

import lombok.Generated;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FreezePlayer {
   @NotNull
   protected Player player;
   protected int tempFreezeSeconds;

   public FreezePlayer(@NotNull Player player, int tempFreezeSeconds) {
      this.player = player;
      this.tempFreezeSeconds = tempFreezeSeconds;
   }

   @NotNull
   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @Generated
   public int getTempFreezeSeconds() {
      return this.tempFreezeSeconds;
   }

   @Generated
   public void setPlayer(@NotNull Player player) {
      this.player = player;
   }

   @Generated
   public void setTempFreezeSeconds(int tempFreezeSeconds) {
      this.tempFreezeSeconds = tempFreezeSeconds;
   }
}

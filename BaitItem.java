package su.mcdev.minecraft.spigot.mcitems.item.component.bait;

import lombok.Generated;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BaitItem {
   @NotNull
   protected Player player;
   @NotNull
   protected Item item;

   public BaitItem(@NotNull Player player, @NotNull Item item) {
      this.player = player;
      this.item = item;
   }

   @NotNull
   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @NotNull
   @Generated
   public Item getItem() {
      return this.item;
   }

   @Generated
   public void setPlayer(@NotNull Player player) {
      this.player = player;
   }

   @Generated
   public void setItem(@NotNull Item item) {
      this.item = item;
   }
}

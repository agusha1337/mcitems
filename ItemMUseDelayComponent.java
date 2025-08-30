package su.mcdev.minecraft.spigot.mcitems.commons;

import java.util.Map;
import lombok.Generated;
import me.socrum.advanced.util.UtilA;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.advanced.delay.DelayAdvanced;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ItemMUseDelayComponent extends ItemMDisableWorldListComponent {
   @NotNull
   private ConfigurationSection config;
   @NotNull
   private ConfigurationSection messageConfig;
   @NotNull
   private DelayAdvanced delayAdvanced;
   @NotNull
   private Boolean enableUseDelay;
   @NotNull
   private Integer delayUseTicks;

   public ItemMUseDelayComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfig = config.getConfigurationSection("message");
      this.delayAdvanced = UtilM.delay();
      this.enableUseDelay = this.config.getBoolean("enable_use_delay");
      this.delayUseTicks = this.config.getInt("delay_use_ticks");
   }

   public void sendUseDelayMessage(@NotNull Player player, @NotNull Integer useDelayTicks) {
      double useDelaySeconds = (double)useDelayTicks / 20.0D;
      Map<String, String> placeholders = Map.of("%use_delay_seconds%", UtilA.number(useDelaySeconds));
      UtilM.sendMessageFromConfigurationPath(this.messageConfig, player, "use_delay", placeholders);
   }

   public boolean hasDelayIfHasSendUseDelayMessage(@NotNull Player player) {
      if (!this.enableUseDelay) {
         return false;
      } else if (this.delayAdvanced.hasDelayIfHas(player, (delayUseTicks) -> {
         this.sendUseDelayMessage(player, delayUseTicks);
      })) {
         return true;
      } else {
         this.delayAdvanced.delay(player, this.delayUseTicks);
         return false;
      }
   }

   public void resetDelay(@NotNull Player player) {
      this.delayAdvanced.reset(player);
   }

   @NotNull
   @Generated
   public ConfigurationSection getConfig() {
      return this.config;
   }

   @NotNull
   @Generated
   public ConfigurationSection getMessageConfig() {
      return this.messageConfig;
   }

   @NotNull
   @Generated
   public DelayAdvanced getDelayAdvanced() {
      return this.delayAdvanced;
   }

   @NotNull
   @Generated
   public Boolean getEnableUseDelay() {
      return this.enableUseDelay;
   }

   @NotNull
   @Generated
   public Integer getDelayUseTicks() {
      return this.delayUseTicks;
   }

   @Generated
   public void setConfig(@NotNull ConfigurationSection config) {
      this.config = config;
   }

   @Generated
   public void setMessageConfig(@NotNull ConfigurationSection messageConfig) {
      this.messageConfig = messageConfig;
   }

   @Generated
   public void setDelayAdvanced(@NotNull DelayAdvanced delayAdvanced) {
      this.delayAdvanced = delayAdvanced;
   }

   @Generated
   public void setEnableUseDelay(@NotNull Boolean enableUseDelay) {
      this.enableUseDelay = enableUseDelay;
   }

   @Generated
   public void setDelayUseTicks(@NotNull Integer delayUseTicks) {
      this.delayUseTicks = delayUseTicks;
   }
}

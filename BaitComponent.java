package su.mcdev.minecraft.spigot.mcitems.item.component.bait;

import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMDisableWorldListComponent;

public class BaitComponent extends ItemMDisableWorldListComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   protected float explosionPower;

   public BaitComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = config.getConfigurationSection("message");
      this.explosionPower = (float)config.getDouble("explosion_power");
   }

   public void sendBaitWorkedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "the_bait_worked");
   }

   public void sendYouTookBaitMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "you_took_the_bait");
   }

   @NotNull
   @Generated
   public ConfigurationSection getConfig() {
      return this.config;
   }

   @NotNull
   @Generated
   public ConfigurationSection getMessageConfigurationSection() {
      return this.messageConfigurationSection;
   }

   @Generated
   public float getExplosionPower() {
      return this.explosionPower;
   }

   @Generated
   public void setConfig(@NotNull ConfigurationSection config) {
      this.config = config;
   }

   @Generated
   public void setMessageConfigurationSection(@NotNull ConfigurationSection messageConfigurationSection) {
      this.messageConfigurationSection = messageConfigurationSection;
   }

   @Generated
   public void setExplosionPower(float explosionPower) {
      this.explosionPower = explosionPower;
   }
}

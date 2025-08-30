package su.mcdev.minecraft.spigot.mcitems.item.component.unfallable;

import lombok.Generated;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMDisableWorldListComponent;

public class SafeFallComponent extends ItemMDisableWorldListComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;

   public SafeFallComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
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
   public void setConfig(@NotNull ConfigurationSection config) {
      this.config = config;
   }

   @Generated
   public void setMessageConfigurationSection(@NotNull ConfigurationSection messageConfigurationSection) {
      this.messageConfigurationSection = messageConfigurationSection;
   }
}

package su.mcdev.minecraft.spigot.mcitems.item.component.explosion_damage_reducer;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import me.socrum.advanced.util.UtilA;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMDisableWorldListComponent;

public class ExplosionDamageReducerComponent extends ItemMDisableWorldListComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   protected double explosionDamageReductionPercent;

   public ExplosionDamageReducerComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = config.getConfigurationSection("message");
      this.explosionDamageReductionPercent = config.getDouble("explosion_damage_reduction_percent");
   }

   public void sendDamageReducedMessage(@NotNull Player player, double sourceDamage, double finalDamage) {
      Map<String, String> placeholders = new HashMap();
      placeholders.put("%source_damage%", UtilA.number(sourceDamage));
      placeholders.put("%final_damage%", UtilA.number(finalDamage));
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "damage_reduced", placeholders);
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
   public double getExplosionDamageReductionPercent() {
      return this.explosionDamageReductionPercent;
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
   public void setExplosionDamageReductionPercent(double explosionDamageReductionPercent) {
      this.explosionDamageReductionPercent = explosionDamageReductionPercent;
   }
}

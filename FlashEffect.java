package su.mcdev.minecraft.spigot.mcitems.item.component.flash;

import lombok.Generated;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class FlashEffect {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected PotionEffectType potionEffectType;
   protected int durationSeconds;
   protected int durationTicks;
   protected int romanAmplifier;
   protected int bukkitAmplifier;

   public FlashEffect(@NotNull ConfigurationSection config) {
      this.config = config;
      String potionEffectType = this.config.getString("potion_effect_type");
      String potionEffectTypeUpperCase = potionEffectType.toUpperCase();
      this.potionEffectType = PotionEffectType.getByName(potionEffectTypeUpperCase);
      int durationSeconds = this.config.getInt("duration_seconds");
      this.durationTicks = durationSeconds * 20;
      this.romanAmplifier = this.config.getInt("amplifier");
      this.bukkitAmplifier = this.romanAmplifier - 1;
   }

   @NotNull
   public PotionEffect createPotionEffect() {
      PotionEffect potionEffect = new PotionEffect(this.potionEffectType, this.durationTicks, this.bukkitAmplifier, false, false);
      return potionEffect;
   }

   @NotNull
   @Generated
   public ConfigurationSection getConfig() {
      return this.config;
   }

   @NotNull
   @Generated
   public PotionEffectType getPotionEffectType() {
      return this.potionEffectType;
   }

   @Generated
   public int getDurationSeconds() {
      return this.durationSeconds;
   }

   @Generated
   public int getDurationTicks() {
      return this.durationTicks;
   }

   @Generated
   public int getRomanAmplifier() {
      return this.romanAmplifier;
   }

   @Generated
   public int getBukkitAmplifier() {
      return this.bukkitAmplifier;
   }

   @Generated
   public void setConfig(@NotNull ConfigurationSection config) {
      this.config = config;
   }

   @Generated
   public void setPotionEffectType(@NotNull PotionEffectType potionEffectType) {
      this.potionEffectType = potionEffectType;
   }

   @Generated
   public void setDurationSeconds(int durationSeconds) {
      this.durationSeconds = durationSeconds;
   }

   @Generated
   public void setDurationTicks(int durationTicks) {
      this.durationTicks = durationTicks;
   }

   @Generated
   public void setRomanAmplifier(int romanAmplifier) {
      this.romanAmplifier = romanAmplifier;
   }

   @Generated
   public void setBukkitAmplifier(int bukkitAmplifier) {
      this.bukkitAmplifier = bukkitAmplifier;
   }
}

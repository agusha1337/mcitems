package su.mcdev.minecraft.spigot.mcitems.item.component.arise;

import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMUseDelayComponent;

public class AriseComponent extends ItemMUseDelayComponent {
   @NotNull
   private ConfigurationSection config;
   @NotNull
   private ConfigurationSection messageConfig;
   private int arisenEntityLimit;
   private double maxHealth;
   private int arisenEntitySpawnAmount;
   @NotNull
   private ChatColor glowingColor;
   private int ttlSeconds;
   @NotNull
   private String healthDisplayStringFormat;
   @NotNull
   private String ttlSecondsDisplayStringFormat;
   @NotNull
   private String customNameFormat;
   @NotNull
   private ItemStack helmet;
   @NotNull
   private ItemStack chestplate;
   @NotNull
   private ItemStack leggings;
   @NotNull
   private ItemStack boots;

   public AriseComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfig = this.config.getConfigurationSection("message");
      this.arisenEntityLimit = this.config.getInt("arisen_entity_limit");
      this.maxHealth = this.config.getDouble("max_health");
      this.arisenEntitySpawnAmount = this.config.getInt("arisen_entity_spawn_amount");
      this.glowingColor = ChatColor.valueOf(this.config.getString("glowing_color").toUpperCase());
      this.ttlSeconds = this.config.getInt("time_to_live_seconds");
      this.healthDisplayStringFormat = this.config.getString("health_display_string_format");
      this.ttlSecondsDisplayStringFormat = this.config.getString("time_to_live_seconds_display_string_format");
      this.customNameFormat = this.config.getString("custom_name_format");
      ConfigurationSection armorConfig = this.config.getConfigurationSection("armor");
      this.helmet = UtilM.getItemStackFromConfigurationPath(armorConfig.getConfigurationSection("helmet"));
      this.chestplate = UtilM.getItemStackFromConfigurationPath(armorConfig.getConfigurationSection("chestplate"));
      this.leggings = UtilM.getItemStackFromConfigurationPath(armorConfig.getConfigurationSection("leggings"));
      this.boots = UtilM.getItemStackFromConfigurationPath(armorConfig.getConfigurationSection("boots"));
   }

   public void sendArisenMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfig, player, "arisen");
   }

   public void sendDespawnedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfig, player, "despawned");
   }

   public void sendArisenEntityLimitReachedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfig, player, "arisen_entity_limit_reached");
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

   @Generated
   public int getArisenEntityLimit() {
      return this.arisenEntityLimit;
   }

   @Generated
   public double getMaxHealth() {
      return this.maxHealth;
   }

   @Generated
   public int getArisenEntitySpawnAmount() {
      return this.arisenEntitySpawnAmount;
   }

   @NotNull
   @Generated
   public ChatColor getGlowingColor() {
      return this.glowingColor;
   }

   @Generated
   public int getTtlSeconds() {
      return this.ttlSeconds;
   }

   @NotNull
   @Generated
   public String getHealthDisplayStringFormat() {
      return this.healthDisplayStringFormat;
   }

   @NotNull
   @Generated
   public String getTtlSecondsDisplayStringFormat() {
      return this.ttlSecondsDisplayStringFormat;
   }

   @NotNull
   @Generated
   public String getCustomNameFormat() {
      return this.customNameFormat;
   }

   @NotNull
   @Generated
   public ItemStack getHelmet() {
      return this.helmet;
   }

   @NotNull
   @Generated
   public ItemStack getChestplate() {
      return this.chestplate;
   }

   @NotNull
   @Generated
   public ItemStack getLeggings() {
      return this.leggings;
   }

   @NotNull
   @Generated
   public ItemStack getBoots() {
      return this.boots;
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
   public void setArisenEntityLimit(int arisenEntityLimit) {
      this.arisenEntityLimit = arisenEntityLimit;
   }

   @Generated
   public void setMaxHealth(double maxHealth) {
      this.maxHealth = maxHealth;
   }

   @Generated
   public void setArisenEntitySpawnAmount(int arisenEntitySpawnAmount) {
      this.arisenEntitySpawnAmount = arisenEntitySpawnAmount;
   }

   @Generated
   public void setGlowingColor(@NotNull ChatColor glowingColor) {
      this.glowingColor = glowingColor;
   }

   @Generated
   public void setTtlSeconds(int ttlSeconds) {
      this.ttlSeconds = ttlSeconds;
   }

   @Generated
   public void setHealthDisplayStringFormat(@NotNull String healthDisplayStringFormat) {
      this.healthDisplayStringFormat = healthDisplayStringFormat;
   }

   @Generated
   public void setTtlSecondsDisplayStringFormat(@NotNull String ttlSecondsDisplayStringFormat) {
      this.ttlSecondsDisplayStringFormat = ttlSecondsDisplayStringFormat;
   }

   @Generated
   public void setCustomNameFormat(@NotNull String customNameFormat) {
      this.customNameFormat = customNameFormat;
   }

   @Generated
   public void setHelmet(@NotNull ItemStack helmet) {
      this.helmet = helmet;
   }

   @Generated
   public void setChestplate(@NotNull ItemStack chestplate) {
      this.chestplate = chestplate;
   }

   @Generated
   public void setLeggings(@NotNull ItemStack leggings) {
      this.leggings = leggings;
   }

   @Generated
   public void setBoots(@NotNull ItemStack boots) {
      this.boots = boots;
   }
}

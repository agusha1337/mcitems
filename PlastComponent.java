package su.mcdev.minecraft.spigot.mcitems.item.component.plast;

import java.io.File;
import java.util.List;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.SoundAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.Main;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMUseDelayComponent;

public class PlastComponent extends ItemMUseDelayComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   @NotNull
   protected File schematicFile_0_deg;
   @NotNull
   protected File schematicFile_45_deg;
   @NotNull
   protected File schematicFile_90_deg;
   @NotNull
   protected File schematicFile_bottom;
   @NotNull
   protected File schematicFile_minus_45_deg;
   protected int ttlSeconds;
   protected int particleSpawnRateTicks;
   @NotNull
   protected List<String> particleNameToSpawnList;
   protected boolean isOpCanBreakUnbreakableWallRegionWhileItSpawned;
   protected boolean disableSpawnInsideWorldGuardRegions;
   protected boolean disableSpawnInsideMcRegionRegions;
   private int playerInvulnerableToFallDamageAfterPlastSpawnedSeconds;

   public PlastComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = config.getConfigurationSection("message");
      File dataFolder = Main.instance.getDataFolder();
      File schematicDirectory = new File(dataFolder, "schematics");
      this.schematicFile_0_deg = new File(schematicDirectory, config.getString("schematic_0_deg"));
      this.schematicFile_45_deg = new File(schematicDirectory, config.getString("schematic_45_deg"));
      this.schematicFile_90_deg = new File(schematicDirectory, config.getString("schematic_90_deg"));
      this.schematicFile_bottom = new File(schematicDirectory, config.getString("schematic_bottom"));
      this.schematicFile_minus_45_deg = new File(schematicDirectory, config.getString("schematic_minus_45_deg"));
      this.ttlSeconds = config.getInt("time_to_live_seconds");
      this.particleSpawnRateTicks = this.configurationSection.getInt("particle_spawn_rate_ticks");
      this.particleNameToSpawnList = this.configurationSection.getStringList("particle_name_to_spawn_list");
      this.isOpCanBreakUnbreakableWallRegionWhileItSpawned = config.getBoolean("is_op_can_break_unbreakable_wall_region_while_it_spawned");
      this.disableSpawnInsideWorldGuardRegions = this.config.getBoolean("disable_spawn_inside_worldguard_regions");
      this.disableSpawnInsideMcRegionRegions = this.config.getBoolean("disable_spawn_inside_mcregion_regions");
      this.playerInvulnerableToFallDamageAfterPlastSpawnedSeconds = this.config.getInt("player_invulnerable_to_fall_damage_after_plast_spawned_seconds");
   }

   public void sendSpawnedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "spawned");
   }

   public void sendDespawnedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "despawned");
   }

   public void playSpawnedSound(@NotNull Location location) {
      ConfigurationSection config = this.config.getConfigurationSection("sound.spawned");
      SoundAdvanced soundAdvanced = UtilM.getSoundAdvancedFromConfigurationPath(config);
      soundAdvanced.play(location);
   }

   public void playDespawnedSound(@NotNull Location location) {
      ConfigurationSection config = this.config.getConfigurationSection("sound.despawned");
      SoundAdvanced soundAdvanced = UtilM.getSoundAdvancedFromConfigurationPath(config);
      soundAdvanced.play(location);
   }

   public void sendSpawnIsNotAllowedBecauseOfPluginRegionMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "spawn_is_not_allowed_because_of_plugin_region");
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

   @NotNull
   @Generated
   public File getSchematicFile_0_deg() {
      return this.schematicFile_0_deg;
   }

   @NotNull
   @Generated
   public File getSchematicFile_45_deg() {
      return this.schematicFile_45_deg;
   }

   @NotNull
   @Generated
   public File getSchematicFile_90_deg() {
      return this.schematicFile_90_deg;
   }

   @NotNull
   @Generated
   public File getSchematicFile_bottom() {
      return this.schematicFile_bottom;
   }

   @NotNull
   @Generated
   public File getSchematicFile_minus_45_deg() {
      return this.schematicFile_minus_45_deg;
   }

   @Generated
   public int getTtlSeconds() {
      return this.ttlSeconds;
   }

   @Generated
   public int getParticleSpawnRateTicks() {
      return this.particleSpawnRateTicks;
   }

   @NotNull
   @Generated
   public List<String> getParticleNameToSpawnList() {
      return this.particleNameToSpawnList;
   }

   @Generated
   public boolean isOpCanBreakUnbreakableWallRegionWhileItSpawned() {
      return this.isOpCanBreakUnbreakableWallRegionWhileItSpawned;
   }

   @Generated
   public boolean isDisableSpawnInsideWorldGuardRegions() {
      return this.disableSpawnInsideWorldGuardRegions;
   }

   @Generated
   public boolean isDisableSpawnInsideMcRegionRegions() {
      return this.disableSpawnInsideMcRegionRegions;
   }

   @Generated
   public int getPlayerInvulnerableToFallDamageAfterPlastSpawnedSeconds() {
      return this.playerInvulnerableToFallDamageAfterPlastSpawnedSeconds;
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
   public void setSchematicFile_0_deg(@NotNull File schematicFile_0_deg) {
      this.schematicFile_0_deg = schematicFile_0_deg;
   }

   @Generated
   public void setSchematicFile_45_deg(@NotNull File schematicFile_45_deg) {
      this.schematicFile_45_deg = schematicFile_45_deg;
   }

   @Generated
   public void setSchematicFile_90_deg(@NotNull File schematicFile_90_deg) {
      this.schematicFile_90_deg = schematicFile_90_deg;
   }

   @Generated
   public void setSchematicFile_bottom(@NotNull File schematicFile_bottom) {
      this.schematicFile_bottom = schematicFile_bottom;
   }

   @Generated
   public void setSchematicFile_minus_45_deg(@NotNull File schematicFile_minus_45_deg) {
      this.schematicFile_minus_45_deg = schematicFile_minus_45_deg;
   }

   @Generated
   public void setTtlSeconds(int ttlSeconds) {
      this.ttlSeconds = ttlSeconds;
   }

   @Generated
   public void setParticleSpawnRateTicks(int particleSpawnRateTicks) {
      this.particleSpawnRateTicks = particleSpawnRateTicks;
   }

   @Generated
   public void setParticleNameToSpawnList(@NotNull List<String> particleNameToSpawnList) {
      this.particleNameToSpawnList = particleNameToSpawnList;
   }

   @Generated
   public void setOpCanBreakUnbreakableWallRegionWhileItSpawned(boolean isOpCanBreakUnbreakableWallRegionWhileItSpawned) {
      this.isOpCanBreakUnbreakableWallRegionWhileItSpawned = isOpCanBreakUnbreakableWallRegionWhileItSpawned;
   }

   @Generated
   public void setDisableSpawnInsideWorldGuardRegions(boolean disableSpawnInsideWorldGuardRegions) {
      this.disableSpawnInsideWorldGuardRegions = disableSpawnInsideWorldGuardRegions;
   }

   @Generated
   public void setDisableSpawnInsideMcRegionRegions(boolean disableSpawnInsideMcRegionRegions) {
      this.disableSpawnInsideMcRegionRegions = disableSpawnInsideMcRegionRegions;
   }

   @Generated
   public void setPlayerInvulnerableToFallDamageAfterPlastSpawnedSeconds(int playerInvulnerableToFallDamageAfterPlastSpawnedSeconds) {
      this.playerInvulnerableToFallDamageAfterPlastSpawnedSeconds = playerInvulnerableToFallDamageAfterPlastSpawnedSeconds;
   }
}

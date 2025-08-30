package su.mcdev.minecraft.spigot.mcitems.item.component.arise.entity;

import java.util.Map;
import java.util.UUID;
import lombok.Generated;
import me.socrum.advanced.util.Password;
import me.socrum.advanced.util.UtilA;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.mcdev.minecraft.spigot.mcitems.item.component.arise.AriseComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.arise.entity.helper.UpdateLoopSuppressor;
import su.mcdev.minecraft.spigot.mcitems.lib.com.iridium.iridiumcolorapi.IridiumColorAPI;

public class AriseEntity {
   public static final int TARGET_DISTANCE = 10;
   @NotNull
   private AriseEntityHolder ariseEntityHolder;
   @NotNull
   private AriseComponent ariseComponent;
   @NotNull
   private EntityType entityType;
   @Nullable
   private Monster monster;
   @NotNull
   private UpdateLoopSuppressor particleUpdateLoopSuppressor;
   @Nullable
   private Team glowingTeam;
   @NotNull
   private Runnable onSpawnRunnable;
   @NotNull
   private Runnable onDespawnRunnable;
   @NotNull
   private AriseEntityDespawner ariseEntityDespawner;
   @NotNull
   private ChatColor glowingColor;

   public AriseEntity(@NotNull AriseEntityHolder ariseEntityHolder, @NotNull AriseComponent ariseComponent, @NotNull EntityType entityType, @NotNull Runnable onSpawnRunnable, @NotNull Runnable onDespawnRunnable, int ttlSeconds, @NotNull ChatColor glowingColor) {
      this.ariseEntityHolder = ariseEntityHolder;
      this.ariseComponent = ariseComponent;
      this.entityType = entityType;
      this.monster = null;
      this.particleUpdateLoopSuppressor = new UpdateLoopSuppressor(3, this::calculations);
      this.onSpawnRunnable = onSpawnRunnable;
      this.onDespawnRunnable = onDespawnRunnable;
      this.ariseEntityDespawner = new AriseEntityDespawner(ttlSeconds);
      this.glowingColor = glowingColor;
   }

   private void playAmbientParticles() {
      Location location = this.monster.getLocation();
      World world = location.getWorld();
      Location randomLocation = UtilM.randomLocation(location.clone().add(0.0D, 1.0D, 0.0D), 1.0D);
      Particle particle = null;
      double random = UtilM.UTILM_RANDOM.nextDouble();
      if (random < 0.75D) {
         particle = Particle.SOUL_FIRE_FLAME;
      }

      if (particle == null && random < 0.85D) {
         particle = Particle.SMOKE_NORMAL;
      }

      if (particle == null) {
         particle = Particle.SMOKE_LARGE;
      }

      world.spawnParticle(particle, randomLocation, 1, 0.1D, 0.1D, 0.1D, 0.01D);
   }

   @NotNull
   private String formatHealth(double health) {
      String formattedHealth = health < 1.0D ? UtilA.number(health, 2) : UtilA.number(health, 0);
      return formattedHealth;
   }

   @NotNull
   private String calculateHealthDisplayString() {
      double health = this.monster.getHealth();
      String formattedHealth = this.formatHealth(health);
      String healthDisplayStringFormat = this.ariseComponent.getHealthDisplayStringFormat();
      String healthDisplayString = UtilA.placeholders(healthDisplayStringFormat, Map.of("%health%", formattedHealth));
      return healthDisplayString;
   }

   @NotNull
   private String formatTtlSeconds(int ttlSeconds) {
      String formattedTtlSeconds = UtilA.number(ttlSeconds);
      return formattedTtlSeconds;
   }

   @NotNull
   private String calculateTtlSecondsDisplayString() {
      int ttlSeconds = this.ariseEntityDespawner.getTtlSeconds();
      String formattedTtlSeconds = this.formatTtlSeconds(ttlSeconds);
      String ttlSecondsDisplayStringFormat = this.ariseComponent.getTtlSecondsDisplayStringFormat();
      String ttlSecondsDisplayString = UtilA.placeholders(ttlSecondsDisplayStringFormat, Map.of("%time_to_live_seconds%", formattedTtlSeconds));
      return ttlSecondsDisplayString;
   }

   @NotNull
   private String calculateCustomName() {
      Player summoner = this.ariseEntityHolder.getSummoner();
      String summonerName = summoner.getName();
      String formattedSummonerName = IridiumColorAPI.process("<GRADIENT:55FFFF>" + summonerName + "</GRADIENT:FFFFFF>");
      String calculatedHealthDisplayString = this.calculateHealthDisplayString();
      String calculatedTtlSecondsDisplayString = this.calculateTtlSecondsDisplayString();
      String customNameFormat = this.ariseComponent.getCustomNameFormat();
      Map<String, String> placeholders = Map.of("%formatted_summoner_name%", formattedSummonerName, "%formatted_health_display_string%", calculatedHealthDisplayString, "%formatted_time_to_live_seconds_display_string%", calculatedTtlSecondsDisplayString);
      String customName = UtilA.placeholders(customNameFormat, placeholders);
      String colorizedCustomName = UtilM.colorize(customName);
      return colorizedCustomName;
   }

   private void updateCustomName() {
      String calculatedCustomName = this.calculateCustomName();
      this.monster.setCustomName(calculatedCustomName);
      this.monster.setCustomNameVisible(true);
   }

   private void calculations() {
      this.playAmbientParticles();
      this.updateCustomName();
   }

   protected void update() {
      this.particleUpdateLoopSuppressor.updatePerTick();
      this.monster.setFireTicks(0);
   }

   private void applyArmor() {
      EntityEquipment equipment = this.monster.getEquipment();
      equipment.setHelmet(this.ariseComponent.getHelmet());
      equipment.setChestplate(this.ariseComponent.getChestplate());
      equipment.setLeggings(this.ariseComponent.getLeggings());
      equipment.setBoots(this.ariseComponent.getBoots());
   }

   private void createGloving() {
      ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
      Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
      String randomString = Password.generateRandomPassword(16);
      this.glowingTeam = scoreboard.registerNewTeam(randomString);
      this.glowingTeam.setColor(this.glowingColor);
      UUID uniqueUuid = this.monster.getUniqueId();
      String uniqueUuidString = uniqueUuid.toString();
      this.glowingTeam.addEntry(uniqueUuidString);
      this.monster.setGlowing(true);
   }

   private void removeGloving() {
      UUID uniqueUuid = this.monster.getUniqueId();
      String uniqueUuidString = uniqueUuid.toString();
      this.glowingTeam.removeEntry(uniqueUuidString);
      this.glowingTeam.unregister();
   }

   private void playSpawnParticles() {
      Location monsterLocation = this.monster.getLocation();
      World world = monsterLocation.getWorld();

      for(int i = 0; i < 20; ++i) {
         Location particleLocation = UtilM.randomLocation(monsterLocation.clone().add(0.0D, 1.0D, 0.0D), 1.0D);
         Particle particle = null;
         double random = UtilM.UTILM_RANDOM.nextDouble();
         if (random < 0.5D) {
            particle = Particle.SMOKE_NORMAL;
         }

         if (particle == null) {
            particle = Particle.SMOKE_LARGE;
         }

         world.spawnParticle(particle, particleLocation, 1, 0.0D, 0.0D, 0.0D, 0.1D);
      }

   }

   private void playSpawnSound() {
      Location location = this.monster.getLocation();
      World world = location.getWorld();
      Location soundLocation = location.clone().add(0.0D, 1.0D, 0.0D);
      world.playSound(soundLocation, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
      world.playSound(soundLocation, Sound.ENTITY_VEX_CHARGE, 1.0F, 0.0F);
   }

   private void applyHealth() {
      double health = this.ariseComponent.getMaxHealth();
      this.monster.setMaxHealth(health);
      this.monster.setHealth(health);
   }

   protected void spawn(@NotNull Location location) {
      World world = location.getWorld();
      this.monster = (Monster)world.spawnEntity(location, this.entityType);
      this.playSpawnParticles();
      this.playSpawnSound();
      this.applyArmor();
      this.applyHealth();
      this.updateCustomName();
      this.createGloving();
      this.onSpawnRunnable.run();
   }

   protected void despawn() {
      this.removeGloving();
      this.monster.remove();
      this.onDespawnRunnable.run();
   }

   @NotNull
   @Generated
   public AriseEntityHolder getAriseEntityHolder() {
      return this.ariseEntityHolder;
   }

   @NotNull
   @Generated
   public AriseComponent getAriseComponent() {
      return this.ariseComponent;
   }

   @NotNull
   @Generated
   public EntityType getEntityType() {
      return this.entityType;
   }

   @Nullable
   @Generated
   public Monster getMonster() {
      return this.monster;
   }

   @NotNull
   @Generated
   public UpdateLoopSuppressor getParticleUpdateLoopSuppressor() {
      return this.particleUpdateLoopSuppressor;
   }

   @Nullable
   @Generated
   public Team getGlowingTeam() {
      return this.glowingTeam;
   }

   @NotNull
   @Generated
   public Runnable getOnSpawnRunnable() {
      return this.onSpawnRunnable;
   }

   @NotNull
   @Generated
   public Runnable getOnDespawnRunnable() {
      return this.onDespawnRunnable;
   }

   @NotNull
   @Generated
   public AriseEntityDespawner getAriseEntityDespawner() {
      return this.ariseEntityDespawner;
   }

   @NotNull
   @Generated
   public ChatColor getGlowingColor() {
      return this.glowingColor;
   }

   @Generated
   public void setAriseEntityHolder(@NotNull AriseEntityHolder ariseEntityHolder) {
      this.ariseEntityHolder = ariseEntityHolder;
   }

   @Generated
   public void setAriseComponent(@NotNull AriseComponent ariseComponent) {
      this.ariseComponent = ariseComponent;
   }

   @Generated
   public void setEntityType(@NotNull EntityType entityType) {
      this.entityType = entityType;
   }

   @Generated
   public void setMonster(@Nullable Monster monster) {
      this.monster = monster;
   }

   @Generated
   public void setParticleUpdateLoopSuppressor(@NotNull UpdateLoopSuppressor particleUpdateLoopSuppressor) {
      this.particleUpdateLoopSuppressor = particleUpdateLoopSuppressor;
   }

   @Generated
   public void setGlowingTeam(@Nullable Team glowingTeam) {
      this.glowingTeam = glowingTeam;
   }

   @Generated
   public void setOnSpawnRunnable(@NotNull Runnable onSpawnRunnable) {
      this.onSpawnRunnable = onSpawnRunnable;
   }

   @Generated
   public void setOnDespawnRunnable(@NotNull Runnable onDespawnRunnable) {
      this.onDespawnRunnable = onDespawnRunnable;
   }

   @Generated
   public void setAriseEntityDespawner(@NotNull AriseEntityDespawner ariseEntityDespawner) {
      this.ariseEntityDespawner = ariseEntityDespawner;
   }

   @Generated
   public void setGlowingColor(@NotNull ChatColor glowingColor) {
      this.glowingColor = glowingColor;
   }
}

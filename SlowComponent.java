package su.mcdev.minecraft.spigot.mcitems.item.component.slow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMUseDelayComponent;
import xyz.xenondevs.particle.ParticleEffect;

public class SlowComponent extends ItemMUseDelayComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   protected int ttlSeconds;
   protected int radius;
   protected int particleSpawnRateTicks;
   protected int slowParticleCount;
   protected int secondParticleCount;
   @NotNull
   protected ParticleEffect slowParticleEffect;
   @NotNull
   protected ParticleEffect secondParticleEffect;
   @NotNull
   protected List<SlowEffect> slowEffectList;

   public SlowComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = this.config.getConfigurationSection("message");
      this.ttlSeconds = this.config.getInt("time_to_live_seconds");
      this.radius = this.config.getInt("radius");
      this.particleSpawnRateTicks = this.config.getInt("particle_spawn_rate_ticks");
      this.slowParticleCount = this.config.getInt("slow_particle_count");
      this.secondParticleCount = this.config.getInt("second_particle_count");
      String particleEffect = this.config.getString("slow_particle_effect");
      String particleEffectUpperCase = particleEffect.toUpperCase();
      this.slowParticleEffect = ParticleEffect.valueOf(particleEffectUpperCase);
      particleEffect = this.config.getString("second_particle_effect");
      particleEffectUpperCase = particleEffect.toUpperCase();
      this.secondParticleEffect = ParticleEffect.valueOf(particleEffectUpperCase);
      this.slowEffectList = new ArrayList();
      ConfigurationSection disorientationEffectListConfig = this.config.getConfigurationSection("slow_effect_list");
      Set<String> disorientationEffectIdSet = disorientationEffectListConfig.getKeys(false);
      Iterator var4 = disorientationEffectIdSet.iterator();

      while(var4.hasNext()) {
         String disorientationEffectId = (String)var4.next();
         ConfigurationSection disorientationEffectConfig = disorientationEffectListConfig.getConfigurationSection(disorientationEffectId);
         SlowEffect slowEffect = new SlowEffect(disorientationEffectConfig);
         this.slowEffectList.add(slowEffect);
      }

   }

   public void sendUsedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "used");
   }

   public void sendYouWereSlowedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "you_were_slowed");
   }

   public void sendYouCantUseTeleportationItemsHere(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "you_cant_use_teleportation_items_here");
   }

   public void addAllSlowEffectsToPlayer(@NotNull Player player) {
      Iterator var2 = this.slowEffectList.iterator();

      while(var2.hasNext()) {
         SlowEffect slowEffect = (SlowEffect)var2.next();
         PotionEffect potionEffect = slowEffect.createPotionEffect();
         player.addPotionEffect(potionEffect);
      }

      this.sendYouWereSlowedMessage(player);
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
   public int getTtlSeconds() {
      return this.ttlSeconds;
   }

   @Generated
   public int getRadius() {
      return this.radius;
   }

   @Generated
   public int getParticleSpawnRateTicks() {
      return this.particleSpawnRateTicks;
   }

   @Generated
   public int getSlowParticleCount() {
      return this.slowParticleCount;
   }

   @Generated
   public int getSecondParticleCount() {
      return this.secondParticleCount;
   }

   @NotNull
   @Generated
   public ParticleEffect getSlowParticleEffect() {
      return this.slowParticleEffect;
   }

   @NotNull
   @Generated
   public ParticleEffect getSecondParticleEffect() {
      return this.secondParticleEffect;
   }

   @NotNull
   @Generated
   public List<SlowEffect> getSlowEffectList() {
      return this.slowEffectList;
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
   public void setTtlSeconds(int ttlSeconds) {
      this.ttlSeconds = ttlSeconds;
   }

   @Generated
   public void setRadius(int radius) {
      this.radius = radius;
   }

   @Generated
   public void setParticleSpawnRateTicks(int particleSpawnRateTicks) {
      this.particleSpawnRateTicks = particleSpawnRateTicks;
   }

   @Generated
   public void setSlowParticleCount(int slowParticleCount) {
      this.slowParticleCount = slowParticleCount;
   }

   @Generated
   public void setSecondParticleCount(int secondParticleCount) {
      this.secondParticleCount = secondParticleCount;
   }

   @Generated
   public void setSlowParticleEffect(@NotNull ParticleEffect slowParticleEffect) {
      this.slowParticleEffect = slowParticleEffect;
   }

   @Generated
   public void setSecondParticleEffect(@NotNull ParticleEffect secondParticleEffect) {
      this.secondParticleEffect = secondParticleEffect;
   }

   @Generated
   public void setSlowEffectList(@NotNull List<SlowEffect> slowEffectList) {
      this.slowEffectList = slowEffectList;
   }
}

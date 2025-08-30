package su.mcdev.minecraft.spigot.mcitems.item.component.disorientation;

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

public class DisorientationComponent extends ItemMUseDelayComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   protected int ttlSeconds;
   protected int radius;
   protected int particleSpawnRateTicks;
   @NotNull
   protected ParticleEffect particleEffect;
   @NotNull
   protected List<DisorientationEffect> disorientationEffectList;

   public DisorientationComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = this.config.getConfigurationSection("message");
      this.ttlSeconds = this.config.getInt("time_to_live_seconds");
      this.radius = this.config.getInt("radius");
      this.particleSpawnRateTicks = this.config.getInt("particle_spawn_rate_ticks");
      String particleEffect = this.config.getString("particle_effect");
      String particleEffectUpperCase = particleEffect.toUpperCase();
      this.particleEffect = ParticleEffect.valueOf(particleEffectUpperCase);
      this.disorientationEffectList = new ArrayList();
      ConfigurationSection disorientationEffectListConfig = this.config.getConfigurationSection("disorientation_effect_list");
      Set<String> disorientationEffectIdSet = disorientationEffectListConfig.getKeys(false);
      Iterator var6 = disorientationEffectIdSet.iterator();

      while(var6.hasNext()) {
         String disorientationEffectId = (String)var6.next();
         ConfigurationSection disorientationEffectConfig = disorientationEffectListConfig.getConfigurationSection(disorientationEffectId);
         DisorientationEffect disorientationEffect = new DisorientationEffect(disorientationEffectConfig);
         this.disorientationEffectList.add(disorientationEffect);
      }

   }

   public void sendUsedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "used");
   }

   public void sendYouWereDisorientedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "you_were_disoriented");
   }

   public void addAllDisorientationEffectsToPlayer(@NotNull Player player) {
      Iterator var2 = this.disorientationEffectList.iterator();

      while(var2.hasNext()) {
         DisorientationEffect disorientationEffect = (DisorientationEffect)var2.next();
         PotionEffect potionEffect = disorientationEffect.createPotionEffect();
         player.addPotionEffect(potionEffect);
      }

      this.sendYouWereDisorientedMessage(player);
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

   @NotNull
   @Generated
   public ParticleEffect getParticleEffect() {
      return this.particleEffect;
   }

   @NotNull
   @Generated
   public List<DisorientationEffect> getDisorientationEffectList() {
      return this.disorientationEffectList;
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
   public void setParticleEffect(@NotNull ParticleEffect particleEffect) {
      this.particleEffect = particleEffect;
   }

   @Generated
   public void setDisorientationEffectList(@NotNull List<DisorientationEffect> disorientationEffectList) {
      this.disorientationEffectList = disorientationEffectList;
   }
}

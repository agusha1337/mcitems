package su.mcdev.minecraft.spigot.mcitems.item.component.flash;

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

public class FlashComponent extends ItemMUseDelayComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   protected int radius;
   protected int flashParticleCount;
   protected int secondParticleCount;
   @NotNull
   protected ParticleEffect flashParticleEffect;
   @NotNull
   protected ParticleEffect secondParticleEffect;
   @NotNull
   protected List<FlashEffect> flashEffectList;

   public FlashComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = this.config.getConfigurationSection("message");
      this.radius = this.config.getInt("radius");
      this.flashParticleCount = this.config.getInt("flash_particle_count");
      this.secondParticleCount = this.config.getInt("second_particle_count");
      String particleEffect = this.config.getString("flash_particle_effect");
      String particleEffectUpperCase = particleEffect.toUpperCase();
      this.flashParticleEffect = ParticleEffect.valueOf(particleEffectUpperCase);
      particleEffect = this.config.getString("second_particle_effect");
      particleEffectUpperCase = particleEffect.toUpperCase();
      this.secondParticleEffect = ParticleEffect.valueOf(particleEffectUpperCase);
      this.flashEffectList = new ArrayList();
      ConfigurationSection disorientationEffectListConfig = this.config.getConfigurationSection("flash_effect_list");
      Set<String> disorientationEffectIdSet = disorientationEffectListConfig.getKeys(false);
      Iterator var4 = disorientationEffectIdSet.iterator();

      while(var4.hasNext()) {
         String disorientationEffectId = (String)var4.next();
         ConfigurationSection disorientationEffectConfig = disorientationEffectListConfig.getConfigurationSection(disorientationEffectId);
         FlashEffect flashEffect = new FlashEffect(disorientationEffectConfig);
         this.flashEffectList.add(flashEffect);
      }

   }

   public void sendUsedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "used");
   }

   public void sendYouWereFlashedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "you_were_flashed");
   }

   public void addAllFlashEffectsToPlayer(@NotNull Player player) {
      Iterator var2 = this.flashEffectList.iterator();

      while(var2.hasNext()) {
         FlashEffect flashEffect = (FlashEffect)var2.next();
         PotionEffect potionEffect = flashEffect.createPotionEffect();
         player.addPotionEffect(potionEffect);
      }

      this.sendYouWereFlashedMessage(player);
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
   public int getRadius() {
      return this.radius;
   }

   @Generated
   public int getFlashParticleCount() {
      return this.flashParticleCount;
   }

   @Generated
   public int getSecondParticleCount() {
      return this.secondParticleCount;
   }

   @NotNull
   @Generated
   public ParticleEffect getFlashParticleEffect() {
      return this.flashParticleEffect;
   }

   @NotNull
   @Generated
   public ParticleEffect getSecondParticleEffect() {
      return this.secondParticleEffect;
   }

   @NotNull
   @Generated
   public List<FlashEffect> getFlashEffectList() {
      return this.flashEffectList;
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
   public void setRadius(int radius) {
      this.radius = radius;
   }

   @Generated
   public void setFlashParticleCount(int flashParticleCount) {
      this.flashParticleCount = flashParticleCount;
   }

   @Generated
   public void setSecondParticleCount(int secondParticleCount) {
      this.secondParticleCount = secondParticleCount;
   }

   @Generated
   public void setFlashParticleEffect(@NotNull ParticleEffect flashParticleEffect) {
      this.flashParticleEffect = flashParticleEffect;
   }

   @Generated
   public void setSecondParticleEffect(@NotNull ParticleEffect secondParticleEffect) {
      this.secondParticleEffect = secondParticleEffect;
   }

   @Generated
   public void setFlashEffectList(@NotNull List<FlashEffect> flashEffectList) {
      this.flashEffectList = flashEffectList;
   }
}

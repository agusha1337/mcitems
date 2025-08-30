package su.mcdev.minecraft.spigot.mcitems.item.component.freezing;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.SoundAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMUseDelayComponent;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class FreezingComponent extends ItemMUseDelayComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   protected int durationSeconds;
   @NotNull
   protected ParticleEffect freezeParticleEffect;

   public FreezingComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = config.getConfigurationSection("message");
      this.durationSeconds = config.getInt("duration_seconds");
      String freezeParticleEffect = config.getString("freeze_particle_effect");
      String freezeParticleEffectUpperCase = freezeParticleEffect.toUpperCase();
      this.freezeParticleEffect = ParticleEffect.valueOf(freezeParticleEffectUpperCase);
   }

   public void sendTargetIsFrozenMessage(@NotNull Player attackerPlayer, @NotNull Player defenderPlayer) {
      String defenderPlayerName = defenderPlayer.getName();
      Map<String, String> placeholders = new HashMap();
      placeholders.put("%duration_seconds%", String.valueOf(this.durationSeconds));
      placeholders.put("%player_name%", defenderPlayerName);
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, attackerPlayer, "target_is_frozen", placeholders);
   }

   public void sendYouAreFrozenMessage(@NotNull Player attackerPlayer, @NotNull Player defenderPlayer) {
      String attackerPlayerName = attackerPlayer.getName();
      Map<String, String> placeholders = new HashMap();
      placeholders.put("%duration_seconds%", String.valueOf(this.durationSeconds));
      placeholders.put("%player_name%", attackerPlayerName);
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, defenderPlayer, "you_are_frozen", placeholders);
   }

   public void playFreezeSound(@NotNull Location location) {
      ConfigurationSection config = this.config.getConfigurationSection("sound.freeze");
      SoundAdvanced soundAdvanced = UtilM.getSoundAdvancedFromConfigurationPath(config);
      soundAdvanced.play(location);
   }

   public void playFreezeEffect(@NotNull Location location) {
      ParticleBuilder particleBuilder = new ParticleBuilder(this.freezeParticleEffect);
      particleBuilder = particleBuilder.setOffset(0.0F, 0.0F, 0.0F).setAmount(1).setSpeed(0.0F);

      for(int i = 0; i < 20; ++i) {
         particleBuilder.setLocation(UtilM.randomLocation(location, 2.0D));
         particleBuilder.display();
      }

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
   public int getDurationSeconds() {
      return this.durationSeconds;
   }

   @NotNull
   @Generated
   public ParticleEffect getFreezeParticleEffect() {
      return this.freezeParticleEffect;
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
   public void setDurationSeconds(int durationSeconds) {
      this.durationSeconds = durationSeconds;
   }

   @Generated
   public void setFreezeParticleEffect(@NotNull ParticleEffect freezeParticleEffect) {
      this.freezeParticleEffect = freezeParticleEffect;
   }
}

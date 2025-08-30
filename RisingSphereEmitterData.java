package su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.impl.rising_sphere;

import java.awt.Color;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.Emitter;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.EmitterData;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class RisingSphereEmitterData extends EmitterData {
   private double radius;
   private int density;
   private int animationDurationTicks;
   @NotNull
   private Color particleColor;
   @NotNull
   private ParticleBuilder particleBuilder;

   public RisingSphereEmitterData(double radius, int density, int animationDurationTicks, @NotNull Color particleColor) {
      this.radius = radius;
      this.density = density;
      this.animationDurationTicks = animationDurationTicks;
      this.particleColor = particleColor;
      this.particleBuilder = new ParticleBuilder(ParticleEffect.REDSTONE);
      this.particleBuilder.setSpeed(0.0F);
      this.particleBuilder.setOffsetX(0.0F);
      this.particleBuilder.setOffsetY(0.0F);
      this.particleBuilder.setOffsetZ(0.0F);
      this.particleBuilder.setColor(this.particleColor);
   }

   public RisingSphereEmitterData(@NotNull ConfigurationSection config) {
      this(config.getDouble("radius"), config.getInt("density"), config.getInt("animation_duration_ticks"), Color.decode(config.getString("particle_hex_color")));
   }

   @NotNull
   public EmitterData newInstance(@NotNull ConfigurationSection config) {
      return new RisingSphereEmitterData(config);
   }

   @NotNull
   public Emitter createEmitter(@NotNull Location location) {
      return new RisingSphereEmitter(this, location);
   }

   @Generated
   public double getRadius() {
      return this.radius;
   }

   @Generated
   public int getDensity() {
      return this.density;
   }

   @Generated
   public int getAnimationDurationTicks() {
      return this.animationDurationTicks;
   }

   @NotNull
   @Generated
   public Color getParticleColor() {
      return this.particleColor;
   }

   @NotNull
   @Generated
   public ParticleBuilder getParticleBuilder() {
      return this.particleBuilder;
   }

   @Generated
   public void setRadius(double radius) {
      this.radius = radius;
   }

   @Generated
   public void setDensity(int density) {
      this.density = density;
   }

   @Generated
   public void setAnimationDurationTicks(int animationDurationTicks) {
      this.animationDurationTicks = animationDurationTicks;
   }

   @Generated
   public void setParticleColor(@NotNull Color particleColor) {
      this.particleColor = particleColor;
   }

   @Generated
   public void setParticleBuilder(@NotNull ParticleBuilder particleBuilder) {
      this.particleBuilder = particleBuilder;
   }

   @Generated
   public RisingSphereEmitterData() {
   }
}

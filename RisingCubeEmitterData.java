package su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.impl.rising_cube;

import java.awt.Color;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.Emitter;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.EmitterData;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class RisingCubeEmitterData extends EmitterData {
   private double radius;
   private int pointsPerSide;
   private int animationDurationTicks;
   @NotNull
   private Color particleColor;
   @NotNull
   private ParticleBuilder particleBuilder;

   public RisingCubeEmitterData(double radius, int pointsPerSide, int animationDurationTicks, @NotNull Color particleColor) {
      this.radius = radius;
      this.pointsPerSide = pointsPerSide;
      this.animationDurationTicks = animationDurationTicks;
      this.particleColor = particleColor;
      this.particleBuilder = new ParticleBuilder(ParticleEffect.REDSTONE);
      this.particleBuilder.setSpeed(0.0F);
      this.particleBuilder.setOffsetX(0.0F);
      this.particleBuilder.setOffsetY(0.0F);
      this.particleBuilder.setOffsetZ(0.0F);
      this.particleBuilder.setColor(this.particleColor);
   }

   public RisingCubeEmitterData(@NotNull ConfigurationSection config) {
      this(config.getDouble("radius"), config.getInt("points_per_side"), config.getInt("animation_duration_ticks"), Color.decode(config.getString("particle_hex_color")));
   }

   @NotNull
   public EmitterData newInstance(@NotNull ConfigurationSection config) {
      return new RisingCubeEmitterData(config);
   }

   @NotNull
   public Emitter createEmitter(@NotNull Location location) {
      return new RisingCubeEmitter(this, location);
   }

   @Generated
   public double getRadius() {
      return this.radius;
   }

   @Generated
   public int getPointsPerSide() {
      return this.pointsPerSide;
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
   public void setPointsPerSide(int pointsPerSide) {
      this.pointsPerSide = pointsPerSide;
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
   public RisingCubeEmitterData() {
   }
}

package su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.impl.rising_sphere;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.Emitter;
import xyz.xenondevs.particle.ParticleBuilder;

public class RisingSphereEmitter extends Emitter {
   @NotNull
   private RisingSphereEmitterData risingSphereEmitterData;
   private final List<Location> allSpherePoints;
   private int currentAnimationStep = 0;
   private final int totalAnimationSteps;
   private final int pointsPerLayer;

   public RisingSphereEmitter(@NotNull RisingSphereEmitterData risingSphereEmitterData, @NotNull Location center) {
      super(risingSphereEmitterData, center);
      this.risingSphereEmitterData = risingSphereEmitterData;
      this.allSpherePoints = generateSpherePoints(center, this.risingSphereEmitterData.getRadius(), this.risingSphereEmitterData.getDensity());
      int animationDurationTicks = this.risingSphereEmitterData.getAnimationDurationTicks();
      this.totalAnimationSteps = animationDurationTicks;
      this.pointsPerLayer = (int)Math.ceil((double)this.allSpherePoints.size() / (double)animationDurationTicks);
   }

   public void spawnParticle() {
      if (this.currentAnimationStep >= this.totalAnimationSteps) {
         this.currentAnimationStep = 0;
      }

      int startIndex = this.currentAnimationStep * this.pointsPerLayer;
      int endIndex = Math.min(startIndex + this.pointsPerLayer, this.allSpherePoints.size());
      World world = ((Location)this.allSpherePoints.get(0)).getWorld();

      for(int i = startIndex; i < endIndex; ++i) {
         Location point = (Location)this.allSpherePoints.get(i);
         ParticleBuilder particleBuilder = this.risingSphereEmitterData.getParticleBuilder();
         particleBuilder.setLocation(point);
         particleBuilder.display();
      }

      ++this.currentAnimationStep;
   }

   public static List<Location> generateSpherePoints(Location center, double radius, int pointsCount) {
      List<Location> points = new ArrayList();
      World world = center.getWorld();
      double xCenter = center.getX();
      double yCenter = center.getY();
      double zCenter = center.getZ();
      List<Location> unsortedPoints = new ArrayList();
      double goldenRatio = (1.0D + Math.sqrt(5.0D)) / 2.0D;
      double angleIncrement = 6.283185307179586D * goldenRatio;

      for(int i = 0; i < pointsCount; ++i) {
         double y = 1.0D - (double)i / (double)(pointsCount - 1) * 2.0D;
         double radiusAtY = Math.sqrt(1.0D - y * y);
         double theta = angleIncrement * (double)i;
         double x = Math.cos(theta) * radiusAtY;
         double z = Math.sin(theta) * radiusAtY;
         double pointX = xCenter + x * radius;
         double pointY = yCenter + y * radius;
         double pointZ = zCenter + z * radius;
         unsortedPoints.add(new Location(world, pointX, pointY, pointZ));
      }

      unsortedPoints.sort((loc1, loc2) -> {
         return Double.compare(loc1.getY(), loc2.getY());
      });
      points.addAll(unsortedPoints);
      return points;
   }

   @NotNull
   @Generated
   public RisingSphereEmitterData getRisingSphereEmitterData() {
      return this.risingSphereEmitterData;
   }

   @Generated
   public List<Location> getAllSpherePoints() {
      return this.allSpherePoints;
   }

   @Generated
   public int getCurrentAnimationStep() {
      return this.currentAnimationStep;
   }

   @Generated
   public int getTotalAnimationSteps() {
      return this.totalAnimationSteps;
   }

   @Generated
   public int getPointsPerLayer() {
      return this.pointsPerLayer;
   }

   @Generated
   public void setRisingSphereEmitterData(@NotNull RisingSphereEmitterData risingSphereEmitterData) {
      this.risingSphereEmitterData = risingSphereEmitterData;
   }

   @Generated
   public void setCurrentAnimationStep(int currentAnimationStep) {
      this.currentAnimationStep = currentAnimationStep;
   }
}

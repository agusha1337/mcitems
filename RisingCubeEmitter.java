package su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.impl.rising_cube;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.Emitter;
import xyz.xenondevs.particle.ParticleBuilder;

public class RisingCubeEmitter extends Emitter {
   @NotNull
   private RisingCubeEmitterData risingCubeEmitterData;
   private final List<Location> allSpherePoints;
   private int currentAnimationStep = 0;
   private final int totalAnimationSteps;
   private final int pointsPerLayer;
   private int currentLayer = 0;

   public RisingCubeEmitter(@NotNull RisingCubeEmitterData risingCubeEmitterData, @NotNull Location center) {
      super(risingCubeEmitterData, center);
      this.risingCubeEmitterData = risingCubeEmitterData;
      this.allSpherePoints = generateCubePoints(center, this.risingCubeEmitterData.getRadius(), this.risingCubeEmitterData.getPointsPerSide());
      int animationDurationTicks = this.risingCubeEmitterData.getAnimationDurationTicks();
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
         ParticleBuilder particleBuilder = this.risingCubeEmitterData.getParticleBuilder();
         particleBuilder.setLocation(point);
         particleBuilder.display();
      }

      ++this.currentAnimationStep;
   }

   public static List<Location> generateCubePoints(Location center, double sideLength, int pointsPerSide) {
      List<Location> points = new ArrayList();
      World world = center.getWorld();
      double halfSide = sideLength / 2.0D;
      double step = sideLength / (double)(pointsPerSide - 1);
      generateFace(center, points, halfSide, step, pointsPerSide, 0);
      generateFace(center, points, halfSide, step, pointsPerSide, 1);
      generateFace(center, points, halfSide, step, pointsPerSide, 2);
      generateFace(center, points, halfSide, step, pointsPerSide, 3);
      points.sort((loc1, loc2) -> {
         return Double.compare(loc1.getY(), loc2.getY());
      });
      return points;
   }

   private static void generateFace(Location center, List<Location> points, double halfSide, double step, int pointsPerSide, int face) {
      for(int i = 0; i < pointsPerSide; ++i) {
         for(int j = 0; j < pointsPerSide; ++j) {
            double u = -halfSide + (double)i * step;
            double v = -halfSide + (double)j * step;
            Location point;
            switch(face) {
            case 0:
               point = center.clone().add(u, v, halfSide);
               break;
            case 1:
               point = center.clone().add(u, v, -halfSide);
               break;
            case 2:
               point = center.clone().add(halfSide, v, u);
               break;
            case 3:
               point = center.clone().add(-halfSide, v, u);
               break;
            case 4:
               point = center.clone().add(u, halfSide, v);
               break;
            case 5:
               point = center.clone().add(u, -halfSide, v);
               break;
            default:
               continue;
            }

            points.add(point);
         }
      }

   }

   private static List<List<Location>> generateCubeHeightLayers(Location center, double sideLength, int pointsPerSide) {
      Map<Double, List<Location>> heightMap = new TreeMap();
      double halfSide = sideLength / 2.0D;
      double step = sideLength / (double)(pointsPerSide - 1);

      for(int face = 0; face < 6; ++face) {
         for(int i = 0; i < pointsPerSide; ++i) {
            for(int j = 0; j < pointsPerSide; ++j) {
               double u = -halfSide + (double)i * step;
               double v = -halfSide + (double)j * step;
               Location point;
               switch(face) {
               case 0:
                  point = center.clone().add(u, v, halfSide);
                  break;
               case 1:
                  point = center.clone().add(u, v, -halfSide);
                  break;
               case 2:
                  point = center.clone().add(halfSide, v, u);
                  break;
               case 3:
                  point = center.clone().add(-halfSide, v, u);
                  break;
               case 4:
                  point = center.clone().add(u, halfSide, v);
                  break;
               case 5:
                  point = center.clone().add(u, -halfSide, v);
                  break;
               default:
                  point = center.clone();
               }

               double y = point.getY();
               if (!heightMap.containsKey(y)) {
                  heightMap.put(y, new ArrayList());
               }

               ((List)heightMap.get(y)).add(point);
            }
         }
      }

      return new ArrayList(heightMap.values());
   }

   @NotNull
   @Generated
   public RisingCubeEmitterData getRisingCubeEmitterData() {
      return this.risingCubeEmitterData;
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
   public int getCurrentLayer() {
      return this.currentLayer;
   }

   @Generated
   public void setRisingCubeEmitterData(@NotNull RisingCubeEmitterData risingCubeEmitterData) {
      this.risingCubeEmitterData = risingCubeEmitterData;
   }

   @Generated
   public void setCurrentAnimationStep(int currentAnimationStep) {
      this.currentAnimationStep = currentAnimationStep;
   }

   @Generated
   public void setCurrentLayer(int currentLayer) {
      this.currentLayer = currentLayer;
   }
}

package su.mcdev.minecraft.spigot.mcitems.item.component.plast;

public class PlastUtil {
   public static float normalizeYaw(float angle) {
      angle %= 360.0F;
      if (angle >= 180.0F) {
         angle -= 360.0F;
      } else if (angle < -180.0F) {
         angle += 360.0F;
      }

      return angle;
   }

   public static float roundToNearestYaw(float angle) {
      float[] possibleAngles = new float[]{0.0F, 45.0F, 90.0F, 135.0F, 180.0F, -45.0F, -90.0F, -135.0F, -180.0F};
      angle = normalizeYaw(angle);
      float nearestAngle = possibleAngles[0];
      float minDifference = Math.abs(angle - nearestAngle);
      float[] var4 = possibleAngles;
      int var5 = possibleAngles.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         float possibleAngle = var4[var6];
         float difference = Math.abs(angle - possibleAngle);
         if (difference < minDifference) {
            minDifference = difference;
            nearestAngle = possibleAngle;
         }
      }

      return nearestAngle;
   }

   public static float normalizePitch(float pitch) {
      pitch %= 180.0F;
      if (pitch > 90.0F) {
         pitch -= 180.0F;
      } else if (pitch < -90.0F) {
         pitch += 180.0F;
      }

      return pitch;
   }

   public static float roundToNearestPitch(float pitch) {
      float[] possiblePitches = new float[]{-90.0F, -45.0F, 0.0F, 45.0F, 90.0F};
      pitch = normalizePitch(pitch);
      float nearestPitch = possiblePitches[0];
      float minDifference = Math.abs(pitch - nearestPitch);
      float[] var4 = possiblePitches;
      int var5 = possiblePitches.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         float possiblePitch = var4[var6];
         float difference = Math.abs(pitch - possiblePitch);
         if (difference < minDifference) {
            minDifference = difference;
            nearestPitch = possiblePitch;
         }
      }

      return nearestPitch;
   }
}

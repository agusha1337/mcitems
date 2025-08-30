package su.mcdev.minecraft.spigot.mcitems.item.component.chunkloaderblock;

import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.BlockM;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMDisableWorldListComponent;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.EmitterType;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.EmitterData;

public class ChunkloaderblockComponent extends ItemMDisableWorldListComponent {
   @NotNull
   private ConfigurationSection config;
   @NotNull
   private BlockM toPlaceBlockM;
   private int radius;
   @Nullable
   private EmitterData emitterData;
   private double emitterSpawnOffsetX;
   private double emitterSpawnOffsetY;
   private double emitterSpawnOffsetZ;

   public ChunkloaderblockComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.toPlaceBlockM = new BlockM(this.getConfig().getConfigurationSection("to_place_blockm"));
      this.radius = this.getConfig().getInt("radius");
      if (this.config.contains("particle_emitter")) {
         ConfigurationSection emitterConfig = this.config.getConfigurationSection("particle_emitter");
         EmitterType emitterType = EmitterType.valueOf(emitterConfig.getString("type").toUpperCase());
         this.emitterData = emitterType.newInstance(emitterConfig);
         this.emitterSpawnOffsetX = emitterConfig.getDouble("spawn_offset_x");
         this.emitterSpawnOffsetY = emitterConfig.getDouble("spawn_offset_y");
         this.emitterSpawnOffsetZ = emitterConfig.getDouble("spawn_offset_z");
      }

   }

   public void sendPlacedMessage(@NotNull Player receiver) {
      ConfigurationSection messageConfig = this.getConfig().getConfigurationSection("message");
      UtilM.sendMessageFromConfigurationPath(messageConfig, receiver, "placed");
   }

   public void sendBrokenMessage(@NotNull Player receiver) {
      ConfigurationSection messageConfig = this.getConfig().getConfigurationSection("message");
      UtilM.sendMessageFromConfigurationPath(messageConfig, receiver, "broken");
   }

   @NotNull
   @Generated
   public ConfigurationSection getConfig() {
      return this.config;
   }

   @NotNull
   @Generated
   public BlockM getToPlaceBlockM() {
      return this.toPlaceBlockM;
   }

   @Generated
   public int getRadius() {
      return this.radius;
   }

   @Nullable
   @Generated
   public EmitterData getEmitterData() {
      return this.emitterData;
   }

   @Generated
   public double getEmitterSpawnOffsetX() {
      return this.emitterSpawnOffsetX;
   }

   @Generated
   public double getEmitterSpawnOffsetY() {
      return this.emitterSpawnOffsetY;
   }

   @Generated
   public double getEmitterSpawnOffsetZ() {
      return this.emitterSpawnOffsetZ;
   }

   @Generated
   public void setConfig(@NotNull ConfigurationSection config) {
      this.config = config;
   }

   @Generated
   public void setToPlaceBlockM(@NotNull BlockM toPlaceBlockM) {
      this.toPlaceBlockM = toPlaceBlockM;
   }

   @Generated
   public void setRadius(int radius) {
      this.radius = radius;
   }

   @Generated
   public void setEmitterData(@Nullable EmitterData emitterData) {
      this.emitterData = emitterData;
   }

   @Generated
   public void setEmitterSpawnOffsetX(double emitterSpawnOffsetX) {
      this.emitterSpawnOffsetX = emitterSpawnOffsetX;
   }

   @Generated
   public void setEmitterSpawnOffsetY(double emitterSpawnOffsetY) {
      this.emitterSpawnOffsetY = emitterSpawnOffsetY;
   }

   @Generated
   public void setEmitterSpawnOffsetZ(double emitterSpawnOffsetZ) {
      this.emitterSpawnOffsetZ = emitterSpawnOffsetZ;
   }
}

package su.mcdev.minecraft.spigot.mcitems.item.component.plast;

import java.io.File;
import java.util.List;
import java.util.Objects;
import lombok.Generated;
import me.socrum.advanced.util.UtilA;
import me.socrum.minecraft.spigot.plugin.utilm.ClipboardM;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.SchematicPasteOperation;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.WorldEditM;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.mcdev.minecraft.spigot.mcitems.util.U;

public class Plast {
   @NotNull
   protected Player summonerPlayer;
   @NotNull
   protected Location pasteLocation;
   @NotNull
   protected PlastComponent plastComponent;
   @NotNull
   protected ClipboardM clipboardM;
   @NotNull
   protected Region clipboardMRegion;
   @Nullable
   protected SchematicPasteOperation schematicPasteOperation;
   protected int tempTtlTicks;
   protected int tempParticleSpawnDelayTicks;
   protected long spawnedTimestamp;

   public Plast(@NotNull Player summonerPlayer, @NotNull Location pasteLocation, @NotNull PlastComponent plastComponent) {
      this.summonerPlayer = summonerPlayer;
      this.pasteLocation = pasteLocation;
      this.plastComponent = plastComponent;
      this.schematicPasteOperation = null;
      int ttlSeconds = plastComponent.getTtlSeconds();
      this.tempTtlTicks = ttlSeconds * 20;
      this.tempParticleSpawnDelayTicks = plastComponent.getParticleSpawnRateTicks();
   }

   public boolean spawn() {
      if (!this.summonerPlayer.isOnline()) {
         return false;
      } else {
         World world = (World)Objects.requireNonNull(this.pasteLocation.getWorld());
         Location summonerPlayerLocation = this.summonerPlayer.getLocation();
         float yaw = summonerPlayerLocation.getYaw();
         float roundedToNearestYaw = PlastUtil.roundToNearestYaw(yaw);
         File schematicFile = null;
         if (roundedToNearestYaw == 0.0F || roundedToNearestYaw == 180.0F || roundedToNearestYaw == -180.0F) {
            schematicFile = this.plastComponent.getSchematicFile_0_deg();
         }

         if (roundedToNearestYaw == 90.0F || roundedToNearestYaw == -90.0F) {
            schematicFile = this.plastComponent.getSchematicFile_90_deg();
         }

         if (roundedToNearestYaw == 45.0F || roundedToNearestYaw == -135.0F) {
            schematicFile = this.plastComponent.getSchematicFile_45_deg();
         }

         if (roundedToNearestYaw == -45.0F || roundedToNearestYaw == 135.0F) {
            schematicFile = this.plastComponent.getSchematicFile_minus_45_deg();
         }

         float pitch = summonerPlayerLocation.getPitch();
         float roundedPitch = PlastUtil.roundToNearestPitch(pitch);
         if (roundedPitch == 45.0F || roundedPitch == 90.0F) {
            schematicFile = this.plastComponent.getSchematicFile_bottom();
         }

         if (roundedPitch == -45.0F || roundedPitch == -90.0F) {
            schematicFile = this.plastComponent.getSchematicFile_bottom();
         }

         this.clipboardM = WorldEditM.loadSchematic(schematicFile, world);
         this.clipboardMRegion = this.clipboardM.toRegion(this.pasteLocation);
         if (this.plastComponent.isDisableSpawnInsideWorldGuardRegions() && U.isIntersectingAnyWorldGuardRegion(this.clipboardMRegion)) {
            this.plastComponent.sendSpawnIsNotAllowedBecauseOfPluginRegionMessage(this.summonerPlayer);
            return false;
         } else if (this.plastComponent.isDisableSpawnInsideMcRegionRegions() && U.isIntersectingAnyMcRegionRegion(this.clipboardMRegion)) {
            this.plastComponent.sendSpawnIsNotAllowedBecauseOfPluginRegionMessage(this.summonerPlayer);
            return false;
         } else if (this.plastComponent.isIntersectingWithOneOfWorldGuardDenyRegion(this.clipboardMRegion)) {
            this.plastComponent.sendIsIntersectingWithOneOfWorldGuardDenyRegionMessage(this.summonerPlayer);
            return false;
         } else {
            this.schematicPasteOperation = WorldEditM.pasteSchematic(this.clipboardM, this.pasteLocation, true, true);
            this.plastComponent.sendSpawnedMessage(this.summonerPlayer);
            this.plastComponent.playSpawnedSound(this.pasteLocation);
            this.spawnedTimestamp = UtilA.timestamp();
            return true;
         }
      }
   }

   public void despawn() {
      if (this.schematicPasteOperation != null) {
         this.schematicPasteOperation.undo();
         if (this.summonerPlayer.isOnline()) {
            this.plastComponent.sendDespawnedMessage(this.summonerPlayer);
            this.plastComponent.playDespawnedSound(this.pasteLocation);
         }
      }
   }

   public void spawnParticle() {
      if (--this.tempParticleSpawnDelayTicks <= 0) {
         this.tempParticleSpawnDelayTicks = this.plastComponent.getParticleSpawnRateTicks();
         List<String> particleNameToSpawnList = this.plastComponent.getParticleNameToSpawnList();
         int counter = 0;

         for(double x = this.clipboardMRegion.getMinX(); x <= this.clipboardMRegion.getMaxX(); ++x) {
            for(double y = this.clipboardMRegion.getMinY(); y <= this.clipboardMRegion.getMaxY(); ++y) {
               for(double z = this.clipboardMRegion.getMinZ(); z <= this.clipboardMRegion.getMaxZ(); ++z) {
                  ++counter;
                  if (counter % 2 != 0) {
                     Location minLocation = this.clipboardMRegion.getMinLocation();
                     World world = minLocation.getWorld();
                     Block block = world.getBlockAt((int)x, (int)y, (int)z);
                     Location blockLocation = block.getLocation();
                     Location particleLocation = blockLocation.clone().add(UtilM.UTILM_RANDOM.nextBoolean() ? 1.0D : 0.0D, UtilM.UTILM_RANDOM.nextBoolean() ? 1.0D : 0.0D, UtilM.UTILM_RANDOM.nextBoolean() ? 1.0D : 0.0D);
                     int randomIndex = UtilM.UTILM_RANDOM.nextInt(particleNameToSpawnList.size());
                     String randomParticleName = (String)particleNameToSpawnList.get(randomIndex);
                     UtilM.spawnParticle(randomParticleName, particleLocation, 1, 1.0D, 1.0D, 1.0D, 0.1D);
                  }
               }
            }
         }

      }
   }

   public void update() {
      if (this.tempTtlTicks > 0) {
         this.spawnParticle();
         --this.tempTtlTicks;
      }
   }

   public boolean isPlayerInvulnerableToFallDamage() {
      int playerInvulnerableToFallDamageAfterPlastSpawnedSeconds = this.plastComponent.getPlayerInvulnerableToFallDamageAfterPlastSpawnedSeconds();
      long secondsSincePlastSpawned = UtilA.timestamp() - this.spawnedTimestamp;
      return secondsSincePlastSpawned <= (long)playerInvulnerableToFallDamageAfterPlastSpawnedSeconds;
   }

   @NotNull
   @Generated
   public Player getSummonerPlayer() {
      return this.summonerPlayer;
   }

   @NotNull
   @Generated
   public Location getPasteLocation() {
      return this.pasteLocation;
   }

   @NotNull
   @Generated
   public PlastComponent getPlastComponent() {
      return this.plastComponent;
   }

   @NotNull
   @Generated
   public ClipboardM getClipboardM() {
      return this.clipboardM;
   }

   @NotNull
   @Generated
   public Region getClipboardMRegion() {
      return this.clipboardMRegion;
   }

   @Nullable
   @Generated
   public SchematicPasteOperation getSchematicPasteOperation() {
      return this.schematicPasteOperation;
   }

   @Generated
   public int getTempTtlTicks() {
      return this.tempTtlTicks;
   }

   @Generated
   public int getTempParticleSpawnDelayTicks() {
      return this.tempParticleSpawnDelayTicks;
   }

   @Generated
   public long getSpawnedTimestamp() {
      return this.spawnedTimestamp;
   }

   @Generated
   public void setSummonerPlayer(@NotNull Player summonerPlayer) {
      this.summonerPlayer = summonerPlayer;
   }

   @Generated
   public void setPasteLocation(@NotNull Location pasteLocation) {
      this.pasteLocation = pasteLocation;
   }

   @Generated
   public void setPlastComponent(@NotNull PlastComponent plastComponent) {
      this.plastComponent = plastComponent;
   }

   @Generated
   public void setClipboardM(@NotNull ClipboardM clipboardM) {
      this.clipboardM = clipboardM;
   }

   @Generated
   public void setClipboardMRegion(@NotNull Region clipboardMRegion) {
      this.clipboardMRegion = clipboardMRegion;
   }

   @Generated
   public void setSchematicPasteOperation(@Nullable SchematicPasteOperation schematicPasteOperation) {
      this.schematicPasteOperation = schematicPasteOperation;
   }

   @Generated
   public void setTempTtlTicks(int tempTtlTicks) {
      this.tempTtlTicks = tempTtlTicks;
   }

   @Generated
   public void setTempParticleSpawnDelayTicks(int tempParticleSpawnDelayTicks) {
      this.tempParticleSpawnDelayTicks = tempParticleSpawnDelayTicks;
   }

   @Generated
   public void setSpawnedTimestamp(long spawnedTimestamp) {
      this.spawnedTimestamp = spawnedTimestamp;
   }
}

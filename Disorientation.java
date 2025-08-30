package su.mcdev.minecraft.spigot.mcitems.item.component.disorientation;

import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.SchematicPasteOperation;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class Disorientation {
   @NotNull
   protected Player summonerPlayer;
   @NotNull
   protected Location spawnLocation;
   @NotNull
   protected DisorientationComponent disorientationComponent;
   @Nullable
   protected SchematicPasteOperation schematicPasteOperation;
   protected int tempTtlTicks;
   protected int tempParticleSpawnRateTicks;

   public Disorientation(@NotNull Player summonerPlayer, @NotNull Location spawnLocation, @NotNull DisorientationComponent disorientationComponent) {
      this.summonerPlayer = summonerPlayer;
      this.spawnLocation = spawnLocation;
      this.disorientationComponent = disorientationComponent;
      int ttlSeconds = this.disorientationComponent.getTtlSeconds();
      this.tempTtlTicks = ttlSeconds * 20;
      this.tempParticleSpawnRateTicks = this.disorientationComponent.getParticleSpawnRateTicks();
   }

   protected void playEffect() {
      int disorientationComponentRadius = this.disorientationComponent.getRadius();
      ParticleEffect particleEffect = this.disorientationComponent.getParticleEffect();
      ParticleBuilder particleBuilder = new ParticleBuilder(particleEffect);
      World world = this.spawnLocation.getWorld();

      for(int x = 0; x < disorientationComponentRadius * 2; ++x) {
         for(int z = 0; z < disorientationComponentRadius * 2; ++z) {
            for(int y = 0; y < 3; ++y) {
               double x1 = (double)(this.spawnLocation.getBlockX() - disorientationComponentRadius + x) + 0.5D;
               double y1 = (double)(this.spawnLocation.getBlockY() + y) + 0.5D;
               double z1 = (double)(this.spawnLocation.getBlockZ() - disorientationComponentRadius + z) + 0.5D;
               Location location = new Location(world, x1, y1, z1);
               particleBuilder.setLocation(location).setOffset(0.0F, 0.0F, 0.0F).setSpeed(0.0F).setAmount(1).display();
            }
         }
      }

   }

   protected void applyPotionEffectToPlayersNearby() {
      int disorientationComponentRadius = this.disorientationComponent.getRadius();
      World world = this.spawnLocation.getWorld();
      List<Player> playerList = world.getPlayers();
      Iterator var4 = playerList.iterator();

      while(var4.hasNext()) {
         Player player = (Player)var4.next();
         Location playerLocation = player.getLocation();
         if (!(UtilM.distance(playerLocation, this.spawnLocation) > (double)disorientationComponentRadius) && !player.equals(this.summonerPlayer)) {
            this.disorientationComponent.addAllDisorientationEffectsToPlayer(player);
         }
      }

   }

   public void spawn() {
      if (this.summonerPlayer.isOnline()) {
         this.playEffect();
         this.applyPotionEffectToPlayersNearby();
         this.disorientationComponent.sendUsedMessage(this.summonerPlayer);
      }
   }

   public void despawn() {
      if (this.schematicPasteOperation != null) {
         this.schematicPasteOperation.undo();
      }
   }

   public void update() {
      if (this.tempTtlTicks > 0) {
         --this.tempTtlTicks;
         if (--this.tempParticleSpawnRateTicks <= 0) {
            this.tempParticleSpawnRateTicks = this.disorientationComponent.getParticleSpawnRateTicks();
            this.playEffect();
         }

      }
   }

   @NotNull
   @Generated
   public Player getSummonerPlayer() {
      return this.summonerPlayer;
   }

   @NotNull
   @Generated
   public Location getSpawnLocation() {
      return this.spawnLocation;
   }

   @NotNull
   @Generated
   public DisorientationComponent getDisorientationComponent() {
      return this.disorientationComponent;
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
   public int getTempParticleSpawnRateTicks() {
      return this.tempParticleSpawnRateTicks;
   }

   @Generated
   public void setSummonerPlayer(@NotNull Player summonerPlayer) {
      this.summonerPlayer = summonerPlayer;
   }

   @Generated
   public void setSpawnLocation(@NotNull Location spawnLocation) {
      this.spawnLocation = spawnLocation;
   }

   @Generated
   public void setDisorientationComponent(@NotNull DisorientationComponent disorientationComponent) {
      this.disorientationComponent = disorientationComponent;
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
   public void setTempParticleSpawnRateTicks(int tempParticleSpawnRateTicks) {
      this.tempParticleSpawnRateTicks = tempParticleSpawnRateTicks;
   }
}

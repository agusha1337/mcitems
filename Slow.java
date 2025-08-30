package su.mcdev.minecraft.spigot.mcitems.item.component.slow;

import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class Slow {
   @NotNull
   protected Player summonerPlayer;
   @NotNull
   protected Location spawnLocation;
   @NotNull
   protected SlowComponent slowComponent;
   protected int tempTtlTicks;
   protected int tempParticleSpawnRateTicks;

   public Slow(@NotNull Player summonerPlayer, @NotNull Location spawnLocation, @NotNull SlowComponent slowComponent) {
      this.summonerPlayer = summonerPlayer;
      this.spawnLocation = spawnLocation;
      this.slowComponent = slowComponent;
      int ttlSeconds = this.slowComponent.getTtlSeconds();
      this.tempTtlTicks = ttlSeconds * 20;
      this.tempParticleSpawnRateTicks = this.slowComponent.getParticleSpawnRateTicks();
   }

   @NotNull
   protected Location generateRandomParticleSpawnLocation() {
      int flashComponentRadius = this.slowComponent.getRadius();
      Location location = this.spawnLocation.clone();
      location.setX(location.getX() + (double)flashComponentRadius * UtilM.UTILM_RANDOM.nextDouble() * (double)(UtilM.UTILM_RANDOM.nextBoolean() ? 1 : -1));
      location.setY(location.getY() + (double)flashComponentRadius * UtilM.UTILM_RANDOM.nextDouble());
      location.setZ(location.getZ() + (double)flashComponentRadius * UtilM.UTILM_RANDOM.nextDouble() * (double)(UtilM.UTILM_RANDOM.nextBoolean() ? 1 : -1));
      return location;
   }

   protected void playSlowParticleEffect() {
      int slowParticleCount = this.slowComponent.getSlowParticleCount();
      ParticleEffect slowParticleEffect = this.slowComponent.getSlowParticleEffect();
      ParticleBuilder slowParticleBuilder = new ParticleBuilder(slowParticleEffect);

      for(int i = 0; i < slowParticleCount; ++i) {
         Location particleSpawnLocation = this.generateRandomParticleSpawnLocation();
         slowParticleBuilder.setLocation(particleSpawnLocation).setOffset(0.0F, 0.0F, 0.0F).setSpeed(0.0F).setAmount(1).display();
      }

   }

   protected void playSecondParticleEffect() {
      int secondParticleCount = this.slowComponent.getSecondParticleCount();
      ParticleEffect secondParticleEffect = this.slowComponent.getSecondParticleEffect();
      ParticleBuilder secondParticleBuilder = new ParticleBuilder(secondParticleEffect);

      for(int i = 0; i < secondParticleCount; ++i) {
         Location particleSpawnLocation = this.generateRandomParticleSpawnLocation();
         secondParticleBuilder.setLocation(particleSpawnLocation).setOffset(0.0F, 0.0F, 0.0F).setSpeed(0.0F).setAmount(1).display();
      }

   }

   protected void playEffect() {
      this.playSlowParticleEffect();
      this.playSecondParticleEffect();
   }

   protected void applyPotionEffectToPlayersNearby() {
      int disorientationComponentRadius = this.slowComponent.getRadius();
      World world = this.spawnLocation.getWorld();
      List<Player> playerList = world.getPlayers();
      Iterator var4 = playerList.iterator();

      while(var4.hasNext()) {
         Player player = (Player)var4.next();
         Location playerLocation = player.getLocation();
         if (!(UtilM.distance(playerLocation, this.spawnLocation) > (double)disorientationComponentRadius) && !player.equals(this.summonerPlayer)) {
            this.slowComponent.addAllSlowEffectsToPlayer(player);
         }
      }

   }

   public void spawn() {
      if (this.summonerPlayer.isOnline()) {
         this.playEffect();
         this.applyPotionEffectToPlayersNearby();
         this.slowComponent.sendUsedMessage(this.summonerPlayer);
      }
   }

   public void despawn() {
   }

   public void update() {
      if (this.tempTtlTicks > 0) {
         --this.tempTtlTicks;
         if (--this.tempParticleSpawnRateTicks <= 0) {
            this.tempParticleSpawnRateTicks = this.slowComponent.getParticleSpawnRateTicks();
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
   public SlowComponent getSlowComponent() {
      return this.slowComponent;
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
   public void setSlowComponent(@NotNull SlowComponent slowComponent) {
      this.slowComponent = slowComponent;
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

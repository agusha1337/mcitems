package su.mcdev.minecraft.spigot.mcitems.item.component.flash;

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

public class Flash {
   @NotNull
   protected Player summonerPlayer;
   @NotNull
   protected Location spawnLocation;
   @NotNull
   protected FlashComponent flashComponent;

   public Flash(@NotNull Player summonerPlayer, @NotNull Location spawnLocation, @NotNull FlashComponent flashComponent) {
      this.summonerPlayer = summonerPlayer;
      this.spawnLocation = spawnLocation;
      this.flashComponent = flashComponent;
   }

   @NotNull
   protected Location generateRandomParticleSpawnLocation() {
      int flashComponentRadius = this.flashComponent.getRadius();
      Location location = this.spawnLocation.clone();
      location.setX(location.getX() + (double)flashComponentRadius * UtilM.UTILM_RANDOM.nextDouble() * (double)(UtilM.UTILM_RANDOM.nextBoolean() ? 1 : -1));
      location.setY(location.getY() + (double)flashComponentRadius * UtilM.UTILM_RANDOM.nextDouble());
      location.setZ(location.getZ() + (double)flashComponentRadius * UtilM.UTILM_RANDOM.nextDouble() * (double)(UtilM.UTILM_RANDOM.nextBoolean() ? 1 : -1));
      return location;
   }

   protected void playFlashParticleEffect() {
      int flashParticleCount = this.flashComponent.getFlashParticleCount();
      ParticleEffect flashParticleEffect = this.flashComponent.getFlashParticleEffect();
      ParticleBuilder flashParticleBuilder = new ParticleBuilder(flashParticleEffect);

      for(int i = 0; i < flashParticleCount; ++i) {
         Location particleSpawnLocation = this.generateRandomParticleSpawnLocation();
         flashParticleBuilder.setLocation(particleSpawnLocation).setOffset(0.0F, 0.0F, 0.0F).setSpeed(0.0F).setAmount(1).display();
      }

   }

   protected void playSecondParticleEffect() {
      int secondParticleCount = this.flashComponent.getSecondParticleCount();
      ParticleEffect secondParticleEffect = this.flashComponent.getSecondParticleEffect();
      ParticleBuilder secondParticleBuilder = new ParticleBuilder(secondParticleEffect);

      for(int i = 0; i < secondParticleCount; ++i) {
         Location particleSpawnLocation = this.generateRandomParticleSpawnLocation();
         secondParticleBuilder.setLocation(particleSpawnLocation).setOffset(0.0F, 0.0F, 0.0F).setSpeed(0.0F).setAmount(1).display();
      }

   }

   protected void playEffect() {
      this.playFlashParticleEffect();
      this.playSecondParticleEffect();
   }

   protected void applyPotionEffectToPlayersNearby() {
      int disorientationComponentRadius = this.flashComponent.getRadius();
      World world = this.spawnLocation.getWorld();
      List<Player> playerList = world.getPlayers();
      Iterator var4 = playerList.iterator();

      while(var4.hasNext()) {
         Player player = (Player)var4.next();
         Location playerLocation = player.getLocation();
         if (!(UtilM.distance(playerLocation, this.spawnLocation) > (double)disorientationComponentRadius) && !player.equals(this.summonerPlayer)) {
            this.flashComponent.addAllFlashEffectsToPlayer(player);
         }
      }

   }

   public void spawn() {
      if (this.summonerPlayer.isOnline()) {
         this.playEffect();
         this.applyPotionEffectToPlayersNearby();
         this.flashComponent.sendUsedMessage(this.summonerPlayer);
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
   public FlashComponent getFlashComponent() {
      return this.flashComponent;
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
   public void setFlashComponent(@NotNull FlashComponent flashComponent) {
      this.flashComponent = flashComponent;
   }
}

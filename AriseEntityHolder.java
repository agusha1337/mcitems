package su.mcdev.minecraft.spigot.mcitems.item.component.arise.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.Updatable;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.item.component.arise.AriseComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.arise.entity.helper.UpdateLoopSuppressor;

public class AriseEntityHolder implements Updatable {
   @NotNull
   private Player summoner;
   @NotNull
   private List<AriseEntity> ariseEntityList;
   @NotNull
   private UpdateLoopSuppressor calculationsUpdateLoopSuppressor;

   public void despawn(@NotNull AriseEntity ariseEntity) {
      ariseEntity.despawn();
      this.ariseEntityList.remove(ariseEntity);
   }

   private void removeInvalidAriseEntities() {
      for(int i = 0; i < this.ariseEntityList.size(); ++i) {
         AriseEntity ariseEntity = (AriseEntity)this.ariseEntityList.get(i);
         Monster monster = ariseEntity.getMonster();
         if (monster.isDead() || !monster.isValid()) {
            this.despawn(ariseEntity);
            --i;
         }
      }

   }

   @NotNull
   private List<LivingEntity> calculatePotentialTargetLivingEntityList() {
      List<LivingEntity> livingEntityList = new ArrayList();
      Location location = this.summoner.getLocation();
      World world = location.getWorld();
      List<LivingEntity> livingEntityList1 = world.getLivingEntities();
      Iterator var5 = livingEntityList1.iterator();

      while(true) {
         LivingEntity livingEntity1;
         double distance;
         do {
            do {
               if (!var5.hasNext()) {
                  return livingEntityList;
               }

               livingEntity1 = (LivingEntity)var5.next();
               Location livingEntity1Location = livingEntity1.getLocation();
               distance = UtilM.distance(livingEntity1Location, location);
            } while(distance > 10.0D);
         } while(livingEntity1.equals(this.summoner));

         boolean flag = false;
         Iterator var11 = this.ariseEntityList.iterator();

         while(var11.hasNext()) {
            AriseEntity ariseEntity = (AriseEntity)var11.next();
            Monster monster = ariseEntity.getMonster();
            if (monster.equals(livingEntity1)) {
               flag = true;
               break;
            }
         }

         if (!flag) {
            livingEntityList.add(livingEntity1);
         }
      }
   }

   private void updateTarget() {
      List<LivingEntity> calculatedPotentialTargetLivingEntityList = this.calculatePotentialTargetLivingEntityList();
      Iterator var2 = this.ariseEntityList.iterator();

      while(var2.hasNext()) {
         AriseEntity ariseEntity = (AriseEntity)var2.next();
         Monster monster = ariseEntity.getMonster();
         monster.setTarget((LivingEntity)null);
         Location monsterLocation = monster.getLocation();
         Location summonerLocation = this.summoner.getLocation();
         double distance = UtilM.distance(monsterLocation, summonerLocation);
         if (distance > 10.0D) {
            monster.setTarget(this.summoner);
         }

         if (!calculatedPotentialTargetLivingEntityList.isEmpty()) {
            LivingEntity calculatedPotentialTargetLivingEntity = (LivingEntity)calculatedPotentialTargetLivingEntityList.get(0);
            monster.setTarget(calculatedPotentialTargetLivingEntity);
         }
      }

   }

   private void calculations() {
      this.removeInvalidAriseEntities();
      this.updateTarget();
   }

   public AriseEntityHolder(@NotNull Player summoner, @NotNull List<AriseEntity> ariseEntityList) {
      this.summoner = summoner;
      this.ariseEntityList = ariseEntityList;
      this.calculationsUpdateLoopSuppressor = new UpdateLoopSuppressor(20, this::calculations);
   }

   public void update() {
      this.calculationsUpdateLoopSuppressor.updatePerTick();

      for(int i = 0; i < this.ariseEntityList.size(); ++i) {
         AriseEntity ariseEntity = (AriseEntity)this.ariseEntityList.get(i);
         AriseEntityDespawner ariseEntityDespawner = ariseEntity.getAriseEntityDespawner();
         ariseEntityDespawner.updatePerTick();
         if (ariseEntityDespawner.isReadyForDespawn()) {
            this.despawn(ariseEntity);
            --i;
         } else {
            ariseEntity.update();
         }
      }

   }

   @NotNull
   public List<AriseEntity> spawn(@NotNull Location location, @NotNull EntityType entityType, @NotNull AriseComponent ariseComponent, @NotNull Runnable onSpawnRunnable, @NotNull Runnable onDespawnRunnable, int ttlSeconds, int spawnAmount, @NotNull ChatColor glowingColor) {
      ArrayList spawnedAriseEntityList;
      if (spawnAmount == 1) {
         AriseEntity ariseEntity = new AriseEntity(this, ariseComponent, entityType, onSpawnRunnable, onDespawnRunnable, ttlSeconds, glowingColor);
         ariseEntity.spawn(location);
         this.ariseEntityList.add(ariseEntity);
         spawnedAriseEntityList = new ArrayList();
         spawnedAriseEntityList.add(ariseEntity);
         return spawnedAriseEntityList;
      } else if (spawnAmount <= 1) {
         return new ArrayList();
      } else {
         List<Location> standingLocationList = UtilM.getStandingLocationList(location, 10, 10, true);
         spawnedAriseEntityList = new ArrayList();

         for(int i = 0; i < spawnAmount; ++i) {
            AriseEntity ariseEntity = new AriseEntity(this, ariseComponent, entityType, onSpawnRunnable, onDespawnRunnable, ttlSeconds, glowingColor);
            ariseEntity.spawn((Location)standingLocationList.get(UtilM.UTILM_RANDOM.nextInt(standingLocationList.size())));
            this.ariseEntityList.add(ariseEntity);
            spawnedAriseEntityList.add(ariseEntity);
         }

         return spawnedAriseEntityList;
      }
   }

   public void despawnAll() {
      for(int i = 0; i < this.ariseEntityList.size(); ++i) {
         AriseEntity ariseEntity = (AriseEntity)this.ariseEntityList.get(i);
         ariseEntity.despawn();
         this.ariseEntityList.remove(i);
         --i;
      }

   }

   @NotNull
   @Generated
   public Player getSummoner() {
      return this.summoner;
   }

   @NotNull
   @Generated
   public List<AriseEntity> getAriseEntityList() {
      return this.ariseEntityList;
   }

   @NotNull
   @Generated
   public UpdateLoopSuppressor getCalculationsUpdateLoopSuppressor() {
      return this.calculationsUpdateLoopSuppressor;
   }

   @Generated
   public void setSummoner(@NotNull Player summoner) {
      this.summoner = summoner;
   }

   @Generated
   public void setAriseEntityList(@NotNull List<AriseEntity> ariseEntityList) {
      this.ariseEntityList = ariseEntityList;
   }

   @Generated
   public void setCalculationsUpdateLoopSuppressor(@NotNull UpdateLoopSuppressor calculationsUpdateLoopSuppressor) {
      this.calculationsUpdateLoopSuppressor = calculationsUpdateLoopSuppressor;
   }
}

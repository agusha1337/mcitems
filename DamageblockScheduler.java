package su.mcdev.minecraft.spigot.mcitems.item.component.damageblock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import me.socrum.advanced.ini.Initer;
import me.socrum.minecraft.spigot.plugin.utilm.SoundAdvanced;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import su.mcdev.damager.Damager;
import su.mcdev.damager.DamagerManager;
import su.mcdev.damager.ex.UnsupportedServerVersionException;
import su.mcdev.minecraft.spigot.mcitems.Main;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class DamageblockScheduler implements Initer {
   @NotNull
   private DamageblockManager damageBlockManager;
   @NotNull
   private DamagerManager damagerManager;
   @NotNull
   private Damager damager;
   @NotNull
   private BukkitRunnable bukkitRunnable;

   public DamageblockScheduler(@NotNull DamageblockManager damageBlockManager) {
      this.damageBlockManager = damageBlockManager;
      this.damagerManager = new DamagerManager();
      this.bukkitRunnable = new BukkitRunnable() {
         public void run() {
            List<World> worldList = Bukkit.getWorlds();
            HashMap<World, List<LivingEntity>> worldLivingEntityListHashMap = new HashMap();
            Iterator var3 = worldList.iterator();

            while(var3.hasNext()) {
               World worldx = (World)var3.next();
               List<LivingEntity> livingEntityList = worldx.getLivingEntities();
               worldLivingEntityListHashMap.put(worldx, livingEntityList);
            }

            DamageblockManager damageBlockManager = DamageblockScheduler.this.damageBlockManager;
            List<Damageblock> damageBlockList = damageBlockManager.getDamageblockList();
            Iterator var22 = damageBlockList.iterator();

            label60:
            while(true) {
               Damageblock damagerBlock;
               DamageblockComponent damagerBlockComponent;
               do {
                  if (!var22.hasNext()) {
                     return;
                  }

                  damagerBlock = (Damageblock)var22.next();
                  damagerBlockComponent = damagerBlock.calcDamageblockComponent();
               } while(damagerBlockComponent == null);

               int radius = damagerBlockComponent.getRadius();
               int damage = damagerBlockComponent.getDamage();
               Location damageBlockLocation = damagerBlock.getLocation();
               World damageBlockWorld = damageBlockLocation.getWorld();
               List<LivingEntity> livingEntityListx = (List)worldLivingEntityListHashMap.get(damageBlockWorld);
               Location damageBlockCenteredLocation = damageBlockLocation.clone().add(0.5D, 0.5D, 0.5D);
               Iterator var14 = livingEntityListx.iterator();

               while(true) {
                  LivingEntity livingEntity;
                  Location livingEntityLocation;
                  GameMode gameMode;
                  do {
                     do {
                        do {
                           do {
                              if (!var14.hasNext()) {
                                 continue label60;
                              }

                              livingEntity = (LivingEntity)var14.next();
                              livingEntityLocation = livingEntity.getLocation();
                           } while(livingEntityLocation.distance(damageBlockCenteredLocation) > (double)radius);
                        } while(livingEntity.isDead());
                     } while(!livingEntity.isValid());

                     if (!(livingEntity instanceof Player)) {
                        break;
                     }

                     Player player = (Player)livingEntity;
                     gameMode = player.getGameMode();
                  } while(gameMode == GameMode.CREATIVE);

                  try {
                     World world = livingEntity.getWorld();
                     Damager damager = DamageblockScheduler.this.damagerManager.getOrCreateIfDoesNotExistsByWorld(world);
                     damager.simulateDamageByPlayer(livingEntity, (float)damage);
                  } catch (UnsupportedServerVersionException var19) {
                     throw new RuntimeException(var19);
                  }

                  (new SoundAdvanced("HURT_FLESH")).play(livingEntityLocation);
                  (new ParticleBuilder(ParticleEffect.EXPLOSION_LARGE)).setLocation(livingEntityLocation).setAmount(1).setSpeed(0.0F).setOffset(0.0F, 0.0F, 0.0F).display();
               }
            }
         }
      };
   }

   public void init() {
      this.bukkitRunnable.runTaskTimer(Main.instance, 20L, 20L);
   }

   public void destroy() {
      this.bukkitRunnable.cancel();
      this.damagerManager.destruct();
   }

   @NotNull
   @Generated
   public DamageblockManager getDamageBlockManager() {
      return this.damageBlockManager;
   }

   @NotNull
   @Generated
   public DamagerManager getDamagerManager() {
      return this.damagerManager;
   }

   @NotNull
   @Generated
   public Damager getDamager() {
      return this.damager;
   }

   @NotNull
   @Generated
   public BukkitRunnable getBukkitRunnable() {
      return this.bukkitRunnable;
   }

   @Generated
   public void setDamageBlockManager(@NotNull DamageblockManager damageBlockManager) {
      this.damageBlockManager = damageBlockManager;
   }

   @Generated
   public void setDamagerManager(@NotNull DamagerManager damagerManager) {
      this.damagerManager = damagerManager;
   }

   @Generated
   public void setDamager(@NotNull Damager damager) {
      this.damager = damager;
   }

   @Generated
   public void setBukkitRunnable(@NotNull BukkitRunnable bukkitRunnable) {
      this.bukkitRunnable = bukkitRunnable;
   }
}

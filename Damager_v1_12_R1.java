package su.mcdev.damager;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class Damager_v1_12_R1 extends Damager {
   @NotNull
   private final DamageSource damageSource;

   public Damager_v1_12_R1(@NotNull World world, @NotNull UUID uniquePlayerUuid, @NotNull String uniquePlayerName) {
      super(world, uniquePlayerUuid, uniquePlayerName);
      WorldServer worldServer = ((CraftWorld)this.getWorld()).getHandle();
      GameProfile gameProfile = new GameProfile(this.getUniquePlayerUuid(), this.getUniquePlayerName());
      PlayerInteractManager playerInteractManager = new PlayerInteractManager(worldServer);
      CraftServer craftServer = (CraftServer)Bukkit.getServer();
      EntityPlayer entityPlayer = new EntityPlayer(craftServer.getServer(), worldServer, gameProfile, playerInteractManager);
      CraftPlayer craftPlayer = new CraftPlayer(craftServer, entityPlayer);
      this.damageSource = DamageSource.playerAttack(craftPlayer.getHandle());
   }

   public void simulateDamageByPlayer(@NotNull LivingEntity livingEntity, float damage) {
      Runnable runnable = () -> {
         EntityLiving entityLiving = ((CraftLivingEntity)livingEntity).getHandle();
         entityLiving.damageEntity(this.damageSource, damage);
      };
      this.runTemporaryDisableAnnounceAdvancementsRunnable(runnable);
   }

   protected void runTemporaryDisableAnnounceAdvancementsRunnable(@NotNull Runnable disableAnnounceAdvancementsRunnable) {
      World world = this.getWorld();
      String gameRuleValue = "announceAdvancements";
      boolean savedValue = Boolean.parseBoolean(String.valueOf(world.getGameRuleValue(gameRuleValue)));
      world.setGameRuleValue(gameRuleValue, String.valueOf(false));
      disableAnnounceAdvancementsRunnable.run();
      world.setGameRuleValue(gameRuleValue, String.valueOf(savedValue));
   }
}

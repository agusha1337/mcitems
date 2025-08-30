package su.mcdev.minecraft.spigot.mcitems.item.component.arise.entity;

import java.util.List;
import java.util.Map;
import lombok.Generated;
import me.socrum.advanced.ini.Initer;
import me.socrum.minecraft.spigot.plugin.utilm.Main;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class AriseEntityListener implements Initer, Listener {
   @NotNull
   private AriseEntityManager ariseEntityManager;

   public AriseEntityListener(@NotNull AriseEntityManager ariseEntityManager) {
      this.ariseEntityManager = ariseEntityManager;
   }

   public void init() {
      Server server = Bukkit.getServer();
      PluginManager pluginManager = server.getPluginManager();
      pluginManager.registerEvents(this, Main.instance);
   }

   @EventHandler
   private void onPlayerJoinEvent(@NotNull PlayerJoinEvent event) {
      Player player = event.getPlayer();
      this.ariseEntityManager.create(player);
   }

   @EventHandler
   private void onPlayerQuitEvent(@NotNull PlayerQuitEvent event) {
      Player player = event.getPlayer();
      this.ariseEntityManager.remove(player);
   }

   @EventHandler
   private void onEntityTargetLivingEntityEvent(@NotNull EntityTargetLivingEntityEvent event) {
      Entity entity = event.getEntity();
      LivingEntity target = event.getTarget();
      Map<Player, AriseEntityHolder> playerAriseSummonerMap = this.ariseEntityManager.getPlayerAriseSummonerMap();
      if (target instanceof Player) {
         Player targetPlayer = (Player)target;
         AriseEntityHolder ariseEntityHolder = (AriseEntityHolder)playerAriseSummonerMap.get(targetPlayer);
         if (ariseEntityHolder != null) {
            List<AriseEntity> ariseEntityList = ariseEntityHolder.getAriseEntityList();
            if (!ariseEntityList.stream().noneMatch((ariseEntity) -> {
               return ariseEntity.getMonster().equals(entity);
            })) {
               Location entityLocation = entity.getLocation();
               Location targetPlayerLocation = targetPlayer.getLocation();
               double distance = UtilM.distance(entityLocation, targetPlayerLocation);
               if (!(distance > 10.0D)) {
                  event.setCancelled(true);
               }
            }
         }
      }
   }

   @EventHandler
   private void onPlayerDeathEvent(@NotNull PlayerDeathEvent event) {
      Player player = event.getEntity();
      Map<Player, AriseEntityHolder> playerAriseSummonerMap = this.ariseEntityManager.getPlayerAriseSummonerMap();
      AriseEntityHolder ariseEntityHolder = (AriseEntityHolder)playerAriseSummonerMap.get(player);
      if (ariseEntityHolder != null) {
         ariseEntityHolder.despawnAll();
      }
   }

   @NotNull
   @Generated
   public AriseEntityManager getAriseEntityManager() {
      return this.ariseEntityManager;
   }

   @Generated
   public void setAriseEntityManager(@NotNull AriseEntityManager ariseEntityManager) {
      this.ariseEntityManager = ariseEntityManager;
   }
}

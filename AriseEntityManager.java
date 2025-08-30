package su.mcdev.minecraft.spigot.mcitems.item.component.arise.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import me.socrum.advanced.ini.Initer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.item.component.arise.AriseComponent;

public class AriseEntityManager implements Initer {
   @NotNull
   private Map<Player, AriseEntityHolder> playerAriseSummonerMap = new HashMap();
   @NotNull
   private AriseEntityListener ariseEntityListener = new AriseEntityListener(this);
   @NotNull
   private AriseEntityScheduler ariseEntityScheduler = new AriseEntityScheduler(this);

   @NotNull
   protected AriseEntityHolder create(@NotNull Player player) {
      AriseEntityHolder ariseSummoner = new AriseEntityHolder(player, new ArrayList());
      this.playerAriseSummonerMap.put(player, ariseSummoner);
      return ariseSummoner;
   }

   protected void remove(@NotNull Player player) {
      AriseEntityHolder ariseSummoner = (AriseEntityHolder)this.playerAriseSummonerMap.get(player);
      if (ariseSummoner != null) {
         ariseSummoner.despawnAll();
      }

      this.playerAriseSummonerMap.remove(player);
   }

   public void init() {
      this.ariseEntityListener.init();
      this.ariseEntityScheduler.init();
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         this.create(player);
      }

   }

   public void destroy() {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         this.remove(player);
      }

      this.ariseEntityScheduler.destroy();
      this.ariseEntityListener.destroy();
   }

   protected void update() {
      Iterator var1 = this.playerAriseSummonerMap.values().iterator();

      while(var1.hasNext()) {
         AriseEntityHolder ariseSummoner = (AriseEntityHolder)var1.next();
         ariseSummoner.update();
      }

   }

   @NotNull
   public List<AriseEntity> spawn(@NotNull Player summoner, @NotNull Location summonLocation, @NotNull EntityType entityType, @NotNull AriseComponent ariseComponent, @NotNull Runnable onSpawnRunnable, @NotNull Runnable onDespawnRunnable, int ttlSeconds, int spawnAmount, @NotNull ChatColor glowingColor) {
      AriseEntityHolder ariseSummoner = (AriseEntityHolder)this.playerAriseSummonerMap.get(summoner);
      List<AriseEntity> ariseEntity = ariseSummoner.spawn(summonLocation, entityType, ariseComponent, onSpawnRunnable, onDespawnRunnable, ttlSeconds, spawnAmount, glowingColor);
      return ariseEntity;
   }

   public int getArisenEntitiesCount(@NotNull Player holder, @NotNull AriseComponent ariseComponent) {
      AriseEntityHolder ariseEntityHolder = (AriseEntityHolder)this.playerAriseSummonerMap.get(holder);
      if (ariseEntityHolder == null) {
         return 0;
      } else {
         List<AriseEntity> ariseEntityList = ariseEntityHolder.getAriseEntityList();
         int count = (int)ariseEntityList.stream().filter((ariseEntity) -> {
            return ariseEntity.getAriseComponent() == ariseComponent;
         }).count();
         return count;
      }
   }

   @NotNull
   @Generated
   public Map<Player, AriseEntityHolder> getPlayerAriseSummonerMap() {
      return this.playerAriseSummonerMap;
   }

   @NotNull
   @Generated
   public AriseEntityListener getAriseEntityListener() {
      return this.ariseEntityListener;
   }

   @NotNull
   @Generated
   public AriseEntityScheduler getAriseEntityScheduler() {
      return this.ariseEntityScheduler;
   }

   @Generated
   public void setPlayerAriseSummonerMap(@NotNull Map<Player, AriseEntityHolder> playerAriseSummonerMap) {
      this.playerAriseSummonerMap = playerAriseSummonerMap;
   }

   @Generated
   public void setAriseEntityListener(@NotNull AriseEntityListener ariseEntityListener) {
      this.ariseEntityListener = ariseEntityListener;
   }

   @Generated
   public void setAriseEntityScheduler(@NotNull AriseEntityScheduler ariseEntityScheduler) {
      this.ariseEntityScheduler = ariseEntityScheduler;
   }
}

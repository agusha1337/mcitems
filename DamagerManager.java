package su.mcdev.damager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import me.socrum.advanced.util.Destructible;
import me.socrum.advanced.util.Password;
import me.socrum.minecraft.spigot.plugin.utilm.Version;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.mcdev.damager.ex.UnsupportedServerVersionException;

public class DamagerManager implements Destructible {
   @NotNull
   private final List<Damager> damagerList = new ArrayList();

   @NotNull
   public Damager getOrCreateIfDoesNotExistsByWorld(@NotNull World world) throws UnsupportedServerVersionException {
      Damager damager = (Damager)this.damagerList.stream().filter((damager1) -> {
         return damager1.getWorld().equals(world);
      }).findAny().orElse((Object)null);
      if (damager == null) {
         damager = this.create(world);
      }

      return damager;
   }

   @NotNull
   private Damager create(@NotNull World world) throws UnsupportedServerVersionException {
      UUID uniqueDamagerPlayerUuid = this.generateUniqueDamagerPlayerUuid();
      String uniqueDamagerPlayerName = this.generateUniqueDamagerPlayerName();
      Server server = Bukkit.getServer();
      Version version = Version.getServerVersion(server);
      Damager damager = null;
      if (version == Version.v1_8_R3) {
         damager = new Damager_v1_8_R3(world, uniqueDamagerPlayerUuid, uniqueDamagerPlayerName);
      }

      if (version == Version.v1_12_R1) {
         damager = new Damager_v1_12_R1(world, uniqueDamagerPlayerUuid, uniqueDamagerPlayerName);
      }

      if (version == Version.v1_16_R3) {
         damager = new Damager_v1_16_R3(world, uniqueDamagerPlayerUuid, uniqueDamagerPlayerName);
      }

      if (damager == null) {
         String message = "Не удалось получить NMS адаптер для этой версии сервера '" + version.name() + "'.";
         throw new UnsupportedServerVersionException(message);
      } else {
         this.damagerList.add(damager);
         return (Damager)damager;
      }
   }

   public void delete(@NotNull Damager damager) {
      this.damagerList.remove(damager);
   }

   public void destruct() {
      this.damagerList.clear();
   }

   @NotNull
   private UUID generateUniqueDamagerPlayerUuid() {
      UUID uuid;
      for(uuid = UUID.randomUUID(); this.findDamagerByUuid(uuid) != null; uuid = UUID.randomUUID()) {
      }

      return uuid;
   }

   @Nullable
   private Damager findDamagerByUuid(@NotNull UUID uuid) {
      Iterator var2 = this.damagerList.iterator();

      Damager damager;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         damager = (Damager)var2.next();
      } while(!damager.getUniquePlayerUuid().equals(uuid));

      return damager;
   }

   @NotNull
   private String generateUniqueDamagerPlayerName() {
      String playerName;
      for(playerName = Password.generateRandomPassword(16); this.findDamagerByPlayerName(playerName) != null; playerName = Password.generateRandomPassword(16)) {
      }

      return playerName;
   }

   @Nullable
   private Damager findDamagerByPlayerName(@NotNull String playerName) {
      Iterator var2 = this.damagerList.iterator();

      Damager damager;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         damager = (Damager)var2.next();
      } while(!damager.getUniquePlayerName().equals(playerName));

      return damager;
   }

   @NotNull
   @Generated
   public List<Damager> getDamagerList() {
      return this.damagerList;
   }
}

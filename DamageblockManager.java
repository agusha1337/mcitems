package su.mcdev.minecraft.spigot.mcitems.item.component.damageblock;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import me.socrum.advanced.ini.Initer;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DamageblockManager implements Initer {
   @NotNull
   private File databaseYamlFile;
   @NotNull
   private YamlConfiguration databaseConfig;
   @NotNull
   private List<Damageblock> damageblockList;

   public DamageblockManager(@NotNull File databaseYamlFile) {
      this.databaseYamlFile = databaseYamlFile;
      this.databaseConfig = YamlConfiguration.loadConfiguration(this.databaseYamlFile);
      this.damageblockList = this.databaseConfig.getList("damageblock_list", new ArrayList());

      while(this.damageblockList.contains((Object)null)) {
         this.damageblockList.remove((Object)null);
      }

      Iterator var2 = this.damageblockList.iterator();

      while(var2.hasNext()) {
         Damageblock damageblock = (Damageblock)var2.next();
         damageblock.recreateEmitterAfterRestart();
      }

   }

   public void destroy() {
      try {
         this.databaseConfig.set("damageblock_list", this.damageblockList);
         this.databaseConfig.save(this.databaseYamlFile);
      } catch (Throwable var2) {
         throw var2;
      }
   }

   public void create(@NotNull Damageblock damageBlock, @NotNull Player initiator) {
      damageBlock.place(initiator);
      this.damageblockList.add(damageBlock);
   }

   public void delete(@NotNull Player destroyer, @NotNull Damageblock damageBlock) {
      GameMode gameMode = destroyer.getGameMode();
      damageBlock.removeBlock(destroyer, gameMode != GameMode.CREATIVE);
      this.damageblockList.remove(damageBlock);
   }

   @NotNull
   @Generated
   public File getDatabaseYamlFile() {
      return this.databaseYamlFile;
   }

   @NotNull
   @Generated
   public YamlConfiguration getDatabaseConfig() {
      return this.databaseConfig;
   }

   @NotNull
   @Generated
   public List<Damageblock> getDamageblockList() {
      return this.damageblockList;
   }

   @Generated
   public void setDatabaseYamlFile(@NotNull File databaseYamlFile) {
      this.databaseYamlFile = databaseYamlFile;
   }

   @Generated
   public void setDatabaseConfig(@NotNull YamlConfiguration databaseConfig) {
      this.databaseConfig = databaseConfig;
   }

   @Generated
   public void setDamageblockList(@NotNull List<Damageblock> damageblockList) {
      this.damageblockList = damageblockList;
   }
}

package su.mcdev.minecraft.spigot.mcitems;

import java.io.File;
import me.socrum.advanced.ini.Ini;
import me.socrum.minecraft.checker.Checker;
import me.socrum.minecraft.spigot.plugin.utilm.McDevJavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.EmitterModule;

public class Main extends McDevJavaPlugin {
   public static Main instance;
   public static Ini ini;
   public static EmitterModule emitterModule;

   public void onEnable() {
      int mcdevResourceId = 256;
      String pluginLicenceKey = new String(new byte[]{87, 52, 65, 55, 45, 85, 51, 66, 75, 45, 52, 79, 65, 77, 45, 82, 48, 53, 55});
      if (Checker.areChecksEnabled()) {
         if (!Checker.isLicenseValid(this, pluginLicenceKey, "sRx9iFk67fkJB1MR0K52XtZoQLIh9H6rGHQ9")) {
            Bukkit.getScheduler().cancelTasks(this);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
         }

         Checker.checkForUpdates(this, mcdevResourceId, "lEwscqLIwD35ekqrw-yjzb32okGLNtC9");
      }

      instance = this;
      ini = new Ini();
      this.refresh();
   }

   public void onDisable() {
      if (emitterModule != null) {
         emitterModule.destruct();
      }

      if (ini != null) {
         ini.destroy();
      }

   }

   public void refresh() {
      Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
      Bukkit.getServer().getOnlinePlayers();
      File var10002 = this.getDataFolder();
      File file = new File(var10002 + File.separator + "config.yml");
      if (!file.exists()) {
         this.saveDefaultConfig();
      }

      this.reloadConfig();
      HandlerList.unregisterAll(this);
      (new BukkitRunnable() {
         public void run() {
            Main.emitterModule = new EmitterModule(Main.this);
            Main.ini.destroy();
            Main.ini.put(new Bootstrap(Main.this));
            Main.ini.init();
         }
      }).runTask(instance);
   }
}

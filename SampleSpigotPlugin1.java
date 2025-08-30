package su.mcdev.minecraft.spigot.plugin.mcmenu.commons.examples;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import su.mcdev.minecraft.spigot.plugin.mcmenu.commons.McPlugin;

public class SampleSpigotPlugin1 extends JavaPlugin {
   public void onEnable() {
      String pluginName = this.getName();
      PluginDescriptionFile pluginDescriptionFile = this.getDescription();
      String version = pluginDescriptionFile.getVersion();
      File absoluteDataFolder = this.getDataFolder().getAbsoluteFile();
      McPlugin mcPlugin = new McPlugin(pluginName, version, absoluteDataFolder, -1);
      if (!mcPlugin.isSuccessfullyLoaded()) {
         Bukkit.getPluginManager().disablePlugin(this);
      }
   }
}

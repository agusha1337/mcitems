package su.mcdev.minecraft.spigot.plugin.mcmenu.commons.examples;

import java.io.File;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import su.mcdev.minecraft.spigot.plugin.mcmenu.commons.McPlugin;

public class SampleBungeeCordPlugin1 extends Plugin {
   public void onEnable() {
      PluginDescription pluginDescription = this.getDescription();
      String pluginName = pluginDescription.getName();
      String version = pluginDescription.getVersion();
      File absoluteDataFolder = this.getDataFolder().getAbsoluteFile();
      McPlugin mcPlugin = new McPlugin(pluginName, version, absoluteDataFolder, -1);
      if (mcPlugin.isSuccessfullyLoaded()) {
         ;
      }
   }
}

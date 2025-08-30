package su.mcdev.minecraft.spigot.mcitems.commons;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.List;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.Region;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.WorldGuardM;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class ItemMDisableWorldListComponent extends ItemMComponent {
   @NotNull
   private ConfigurationSection config;
   @NotNull
   private ConfigurationSection messageConfig;
   @NotNull
   private Boolean enableDenyWorlds;
   @NotNull
   private List<String> denyWorldNameList;
   @NotNull
   private List<String> denyWorldGuardRegionNameList;

   public ItemMDisableWorldListComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfig = this.config.getConfigurationSection("message");
      this.enableDenyWorlds = this.config.getBoolean("enable_deny_worlds");
      this.denyWorldNameList = this.config.getStringList("deny_world_name_list");
      this.denyWorldGuardRegionNameList = this.config.getStringList("deny_world_guard_region_name_list");
   }

   public void sendUseDenyWorldMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfig, player, "use_deny_world");
   }

   public boolean isWorldAllowed(@NotNull World world) {
      if (!this.enableDenyWorlds) {
         return true;
      } else {
         String worldName = world.getName();
         return this.denyWorldNameList.stream().noneMatch((denyWorldName) -> {
            return denyWorldName.equals(worldName);
         });
      }
   }

   public void sendIsIntersectingWithOneOfWorldGuardDenyRegionMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfig, player, "is_intersecting_with_one_of_worldguard_deny_region");
   }

   private boolean isPluginInstalled(@NotNull String pluginName) {
      PluginManager pluginManager = Bukkit.getPluginManager();
      Plugin plugin = pluginManager.getPlugin(pluginName);
      return plugin != null;
   }

   public boolean isIntersectingWithOneOfWorldGuardDenyRegion(@NotNull Region affectedRegion) {
      if (!this.isPluginInstalled("WorldGuard")) {
         return false;
      } else {
         List<ProtectedRegion> protectedRegionList = WorldGuardM.getProtectedRegionListIntersectsWithRegion(affectedRegion);
         return protectedRegionList.stream().anyMatch((it) -> {
            return this.denyWorldGuardRegionNameList.contains(it.getId());
         });
      }
   }

   @NotNull
   @Generated
   public ConfigurationSection getConfig() {
      return this.config;
   }

   @NotNull
   @Generated
   public ConfigurationSection getMessageConfig() {
      return this.messageConfig;
   }

   @NotNull
   @Generated
   public Boolean getEnableDenyWorlds() {
      return this.enableDenyWorlds;
   }

   @NotNull
   @Generated
   public List<String> getDenyWorldNameList() {
      return this.denyWorldNameList;
   }

   @NotNull
   @Generated
   public List<String> getDenyWorldGuardRegionNameList() {
      return this.denyWorldGuardRegionNameList;
   }

   @Generated
   public void setConfig(@NotNull ConfigurationSection config) {
      this.config = config;
   }

   @Generated
   public void setMessageConfig(@NotNull ConfigurationSection messageConfig) {
      this.messageConfig = messageConfig;
   }

   @Generated
   public void setEnableDenyWorlds(@NotNull Boolean enableDenyWorlds) {
      this.enableDenyWorlds = enableDenyWorlds;
   }

   @Generated
   public void setDenyWorldNameList(@NotNull List<String> denyWorldNameList) {
      this.denyWorldNameList = denyWorldNameList;
   }

   @Generated
   public void setDenyWorldGuardRegionNameList(@NotNull List<String> denyWorldGuardRegionNameList) {
      this.denyWorldGuardRegionNameList = denyWorldGuardRegionNameList;
   }
}

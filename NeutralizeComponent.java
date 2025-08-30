package su.mcdev.minecraft.spigot.mcitems.item.component.neutralize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMDisableWorldListComponent;

public class NeutralizeComponent extends ItemMDisableWorldListComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   @NotNull
   protected List<PotionEffectType> neutralizablePotionEffectTypeList;

   public NeutralizeComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = config.getConfigurationSection("message");
      this.neutralizablePotionEffectTypeList = new ArrayList();
      List<String> neutralizablePotionEffectList = config.getStringList("neutralizable_potion_effect_list");
      Iterator var3 = neutralizablePotionEffectList.iterator();

      while(var3.hasNext()) {
         String neutralizablePotionEffect = (String)var3.next();
         String neutralizablePotionEffectUpperCase = neutralizablePotionEffect.toUpperCase();
         PotionEffectType potionEffectType = PotionEffectType.getByName(neutralizablePotionEffectUpperCase);
         this.neutralizablePotionEffectTypeList.add(potionEffectType);
      }

   }

   public void sendNeutralizedMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "neutralized");
   }

   @NotNull
   @Generated
   public ConfigurationSection getConfig() {
      return this.config;
   }

   @NotNull
   @Generated
   public ConfigurationSection getMessageConfigurationSection() {
      return this.messageConfigurationSection;
   }

   @NotNull
   @Generated
   public List<PotionEffectType> getNeutralizablePotionEffectTypeList() {
      return this.neutralizablePotionEffectTypeList;
   }

   @Generated
   public void setConfig(@NotNull ConfigurationSection config) {
      this.config = config;
   }

   @Generated
   public void setMessageConfigurationSection(@NotNull ConfigurationSection messageConfigurationSection) {
      this.messageConfigurationSection = messageConfigurationSection;
   }

   @Generated
   public void setNeutralizablePotionEffectTypeList(@NotNull List<PotionEffectType> neutralizablePotionEffectTypeList) {
      this.neutralizablePotionEffectTypeList = neutralizablePotionEffectTypeList;
   }
}

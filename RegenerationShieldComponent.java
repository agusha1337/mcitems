package su.mcdev.minecraft.spigot.mcitems.item.component.regeneration_shield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import me.socrum.advanced.util.UtilA;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMDisableWorldListComponent;
import su.mcdev.minecraft.spigot.mcitems.commons.PotionEffectWrapper;

public class RegenerationShieldComponent extends ItemMDisableWorldListComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   protected double abilityChancePercent;
   @NotNull
   protected List<PotionEffectWrapper> potionEffectWrapperList;

   public RegenerationShieldComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = this.config.getConfigurationSection("message");
      this.abilityChancePercent = this.config.getDouble("ability_chance_percent");
      this.potionEffectWrapperList = new ArrayList();
      ConfigurationSection potionEffectWrapperListConfig = this.config.getConfigurationSection("ability_potion_effect_list");
      Set<String> potionEffectWrapperIdSet = potionEffectWrapperListConfig.getKeys(false);
      Iterator var4 = potionEffectWrapperIdSet.iterator();

      while(var4.hasNext()) {
         String potionEffectWrapperId = (String)var4.next();
         ConfigurationSection potionEffectWrapperConfig = potionEffectWrapperListConfig.getConfigurationSection(potionEffectWrapperId);
         PotionEffectWrapper potionEffectWrapper = new PotionEffectWrapper(potionEffectWrapperConfig);
         this.potionEffectWrapperList.add(potionEffectWrapper);
      }

   }

   public void sendRegenerationHitMessage(@NotNull Player player) {
      Map<String, String> placeholders = new HashMap();
      placeholders.put("%ability_chance_percent%", UtilA.number(this.abilityChancePercent, 16));
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "regeneration_hit", placeholders);
   }

   public void addAllDisorientationEffectsToLivingEntity(@NotNull LivingEntity livingEntity) {
      Iterator var2 = this.potionEffectWrapperList.iterator();

      while(var2.hasNext()) {
         PotionEffectWrapper potionEffectWrapper = (PotionEffectWrapper)var2.next();
         PotionEffect potionEffect = potionEffectWrapper.createPotionEffect();
         livingEntity.addPotionEffect(potionEffect);
      }

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

   @Generated
   public double getAbilityChancePercent() {
      return this.abilityChancePercent;
   }

   @NotNull
   @Generated
   public List<PotionEffectWrapper> getPotionEffectWrapperList() {
      return this.potionEffectWrapperList;
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
   public void setAbilityChancePercent(double abilityChancePercent) {
      this.abilityChancePercent = abilityChancePercent;
   }

   @Generated
   public void setPotionEffectWrapperList(@NotNull List<PotionEffectWrapper> potionEffectWrapperList) {
      this.potionEffectWrapperList = potionEffectWrapperList;
   }
}

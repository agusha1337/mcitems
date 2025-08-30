package su.mcdev.minecraft.spigot.mcitems.item.component.flash_snowball;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.SoundAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMUseDelayComponent;
import su.mcdev.minecraft.spigot.mcitems.commons.PotionEffectWrapper;

public class FlashSnowballComponent extends ItemMUseDelayComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   @NotNull
   protected List<PotionEffectWrapper> potionEffectWrapperList;

   public FlashSnowballComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = this.config.getConfigurationSection("message");
      this.potionEffectWrapperList = new ArrayList();
      ConfigurationSection potionEffectWrapperListConfig = this.config.getConfigurationSection("potion_effect_list");
      Set<String> potionEffectWrapperIdSet = potionEffectWrapperListConfig.getKeys(false);
      Iterator var4 = potionEffectWrapperIdSet.iterator();

      while(var4.hasNext()) {
         String potionEffectWrapperId = (String)var4.next();
         ConfigurationSection potionEffectWrapperConfig = potionEffectWrapperListConfig.getConfigurationSection(potionEffectWrapperId);
         PotionEffectWrapper potionEffectWrapper = new PotionEffectWrapper(potionEffectWrapperConfig);
         this.potionEffectWrapperList.add(potionEffectWrapper);
      }

   }

   public void sendSuccessHitMessage(@NotNull Player player) {
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, player, "success_hit");
   }

   public void playThrowSound(@NotNull Location location) {
      ConfigurationSection config = this.config.getConfigurationSection("sound.throw");
      SoundAdvanced soundAdvanced = UtilM.getSoundAdvancedFromConfigurationPath(config);
      soundAdvanced.play(location);
   }

   public void playSuccessHitSound(@NotNull Player player) {
      ConfigurationSection config = this.config.getConfigurationSection("sound.success_hit");
      SoundAdvanced soundAdvanced = UtilM.getSoundAdvancedFromConfigurationPath(config);
      soundAdvanced.play(player);
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
   public void setPotionEffectWrapperList(@NotNull List<PotionEffectWrapper> potionEffectWrapperList) {
      this.potionEffectWrapperList = potionEffectWrapperList;
   }
}

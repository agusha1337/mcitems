package su.mcdev.minecraft.spigot.mcitems.item.component.poison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.commons.ItemMDisableWorldListComponent;
import su.mcdev.minecraft.spigot.mcitems.commons.PotionEffectWrapper;

public class PoisonComponent extends ItemMDisableWorldListComponent {
   @NotNull
   protected ConfigurationSection config;
   @NotNull
   protected ConfigurationSection messageConfigurationSection;
   protected double triggerChance;
   @NotNull
   protected List<PotionEffectWrapper> potionEffectWrapperList;

   public PoisonComponent(@NotNull ConfigurationSection config) {
      super(config);
      this.config = config;
      this.messageConfigurationSection = config.getConfigurationSection("message");
      this.triggerChance = this.config.getDouble("trigger_chance");
      this.potionEffectWrapperList = new ArrayList();
      ConfigurationSection potionEffectWrapperListConfig = this.config.getConfigurationSection("poison_potion_effect_list");
      Set<String> potionEffectWrapperIdSet = potionEffectWrapperListConfig.getKeys(false);
      Iterator var4 = potionEffectWrapperIdSet.iterator();

      while(var4.hasNext()) {
         String potionEffectWrapperId = (String)var4.next();
         ConfigurationSection potionEffectWrapperConfig = potionEffectWrapperListConfig.getConfigurationSection(potionEffectWrapperId);
         PotionEffectWrapper potionEffectWrapper = new PotionEffectWrapper(potionEffectWrapperConfig);
         this.potionEffectWrapperList.add(potionEffectWrapper);
      }

   }

   public void addAllDisorientationEffectsToLivingEntity(@NotNull LivingEntity livingEntity) {
      Iterator var2 = this.potionEffectWrapperList.iterator();

      while(var2.hasNext()) {
         PotionEffectWrapper potionEffectWrapper = (PotionEffectWrapper)var2.next();
         PotionEffect potionEffect = potionEffectWrapper.createPotionEffect();
         livingEntity.addPotionEffect(potionEffect);
      }

   }

   public void sendYouPoisonedPlayerMessage(@NotNull Player attackerPlayer, @NotNull Player defenderPlayer) {
      String defenderPlayerName = defenderPlayer.getName();
      Map<String, String> placeholders = new HashMap();
      placeholders.put("%player_name%", defenderPlayerName);
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, attackerPlayer, "you_poisoned_player", placeholders);
   }

   public void sendYouWerePoisonedMessage(@NotNull Player attackerPlayer, @NotNull Player defenderPlayer) {
      String attackerPlayerName = attackerPlayer.getName();
      Map<String, String> placeholders = new HashMap();
      placeholders.put("%player_name%", attackerPlayerName);
      UtilM.sendMessageFromConfigurationPath(this.messageConfigurationSection, defenderPlayer, "you_were_poisoned", placeholders);
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
   public double getTriggerChance() {
      return this.triggerChance;
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
   public void setTriggerChance(double triggerChance) {
      this.triggerChance = triggerChance;
   }

   @Generated
   public void setPotionEffectWrapperList(@NotNull List<PotionEffectWrapper> potionEffectWrapperList) {
      this.potionEffectWrapperList = potionEffectWrapperList;
   }
}

package su.mcdev.minecraft.spigot.mcitems.item.component.damageblock;

import java.util.Map;
import lombok.Generated;
import me.socrum.minecraft.spigot.plugin.utilm.BlockM;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.ItemM;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.mcdev.minecraft.spigot.mcitems.Main;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.EmitterApi;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.Emitter;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.object.EmitterData;

@SerializableAs("mcItems_Damageblock")
public class Damageblock implements ConfigurationSerializable {
   @NotNull
   private String creatorPlayerName;
   @NotNull
   private Location location;
   @NotNull
   private String itemmItemIdUsedToCreateThisDamageblock;
   @NotNull
   private EmitterApi emitterApi;
   @NotNull
   private DamageblockComponent damageblockComponent;
   @Nullable
   private Emitter emitter;

   public Damageblock(@NotNull String creatorPlayerName, @NotNull Location location, @NotNull String itemmItemIdUsedToCreateThisDamageblock, @NotNull EmitterApi emitterApi) {
      this.creatorPlayerName = creatorPlayerName;
      this.location = location;
      this.itemmItemIdUsedToCreateThisDamageblock = itemmItemIdUsedToCreateThisDamageblock;
      this.emitterApi = emitterApi;
      this.damageblockComponent = this.calcDamageblockComponent();
      EmitterData emitterData = this.damageblockComponent.getEmitterData();
      if (emitterData != null) {
         double emitterSpawnOffsetX = this.damageblockComponent.getEmitterSpawnOffsetX();
         double emitterSpawnOffsetY = this.damageblockComponent.getEmitterSpawnOffsetY();
         double emitterSpawnOffsetZ = this.damageblockComponent.getEmitterSpawnOffsetZ();
         Location emitterSpawnLocation = location.clone().add(emitterSpawnOffsetX, emitterSpawnOffsetY, emitterSpawnOffsetZ);
         this.emitter = emitterData.createEmitter(emitterSpawnLocation);
      }

   }

   @Nullable
   public DamageblockComponent calcDamageblockComponent() {
      ItemM itemm = ItemMAPI.getOrNull(this.itemmItemIdUsedToCreateThisDamageblock);
      return itemm == null ? null : (DamageblockComponent)ItemMAPI.getFirstItemMComponent(itemm.getItemStack(), DamageblockComponent.class, false);
   }

   protected void recreateEmitterAfterRestart() {
      if (this.emitter != null) {
         this.emitterApi.attachEmitter(this.emitter);
      }

   }

   public void place(@NotNull Player initiator) {
      ItemM itemm = ItemMAPI.getOrNull(this.itemmItemIdUsedToCreateThisDamageblock);
      if (itemm != null) {
         ItemStack itemStack = itemm.getItemStack();
         DamageblockComponent damageblockComponent = (DamageblockComponent)ItemMAPI.getFirstItemMComponent(itemStack, DamageblockComponent.class, false);
         if (damageblockComponent != null) {
            BlockM toPlaceBlockM = damageblockComponent.getToPlaceBlockM();
            Block block = this.location.getBlock();
            toPlaceBlockM.place(block);
            damageblockComponent.sendPlacedMessage(initiator);
            if (this.emitter != null) {
               this.emitterApi.attachEmitter(this.emitter);
            }

         }
      }
   }

   private void removeBlock() {
      Block block = this.location.getBlock();
      block.setType(Material.AIR);
   }

   private void dropItemUsedToCreateThisDamageblock() {
      ItemM itemm = ItemMAPI.getOrNull(this.itemmItemIdUsedToCreateThisDamageblock);
      if (itemm != null) {
         ItemStack itemStackToDrop = itemm.getItemStack();
         World world = this.location.getWorld();
         Location dropLocation = this.location.clone();
         world.dropItemNaturally(dropLocation, itemStackToDrop);
      }
   }

   public void removeBlock(@NotNull Player initiator, boolean dropItem) {
      if (this.emitter != null) {
         this.emitterApi.detachEmitter(this.emitter);
      }

      this.removeBlock();
      if (dropItem) {
         this.dropItemUsedToCreateThisDamageblock();
      }

      ItemM itemm = ItemMAPI.getOrNull(this.itemmItemIdUsedToCreateThisDamageblock);
      if (itemm != null) {
         ItemStack itemStack = itemm.getItemStack();
         DamageblockComponent damageblockComponent = (DamageblockComponent)ItemMAPI.getFirstItemMComponent(itemStack, DamageblockComponent.class, false);
         if (damageblockComponent != null) {
            damageblockComponent.sendBrokenMessage(initiator);
         }
      }
   }

   @NotNull
   public static Damageblock deserialize(@NotNull Map<String, Object> stringObjectMap) {
      EmitterApi emitterApi = Main.emitterModule.getEmitterApi();
      return new Damageblock(String.valueOf(stringObjectMap.getOrDefault("creator_player_name", (Object)null)), (Location)stringObjectMap.getOrDefault("location", (Object)null), String.valueOf(stringObjectMap.getOrDefault("itemm_item_id_used_to_create_this_damageblock", (Object)null)), emitterApi);
   }

   public Map<String, Object> serialize() {
      return Map.of("creator_player_name", this.creatorPlayerName, "location", this.location, "itemm_item_id_used_to_create_this_damageblock", this.itemmItemIdUsedToCreateThisDamageblock);
   }

   @NotNull
   @Generated
   public String getCreatorPlayerName() {
      return this.creatorPlayerName;
   }

   @NotNull
   @Generated
   public Location getLocation() {
      return this.location;
   }

   @NotNull
   @Generated
   public String getItemmItemIdUsedToCreateThisDamageblock() {
      return this.itemmItemIdUsedToCreateThisDamageblock;
   }

   @NotNull
   @Generated
   public EmitterApi getEmitterApi() {
      return this.emitterApi;
   }

   @NotNull
   @Generated
   public DamageblockComponent getDamageblockComponent() {
      return this.damageblockComponent;
   }

   @Nullable
   @Generated
   public Emitter getEmitter() {
      return this.emitter;
   }

   @Generated
   public void setCreatorPlayerName(@NotNull String creatorPlayerName) {
      this.creatorPlayerName = creatorPlayerName;
   }

   @Generated
   public void setLocation(@NotNull Location location) {
      this.location = location;
   }

   @Generated
   public void setItemmItemIdUsedToCreateThisDamageblock(@NotNull String itemmItemIdUsedToCreateThisDamageblock) {
      this.itemmItemIdUsedToCreateThisDamageblock = itemmItemIdUsedToCreateThisDamageblock;
   }

   @Generated
   public void setEmitterApi(@NotNull EmitterApi emitterApi) {
      this.emitterApi = emitterApi;
   }

   @Generated
   public void setDamageblockComponent(@NotNull DamageblockComponent damageblockComponent) {
      this.damageblockComponent = damageblockComponent;
   }

   @Generated
   public void setEmitter(@Nullable Emitter emitter) {
      this.emitter = emitter;
   }
}

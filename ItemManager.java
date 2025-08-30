package su.mcdev.minecraft.spigot.mcitems.item;

import java.io.File;
import lombok.Generated;
import me.socrum.advanced.ini.Initer;
import me.socrum.minecraft.spigot.plugin.utilm.Version;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.component.ItemMComponentListener;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import su.mcdev.minecraft.spigot.mcitems.Main;
import su.mcdev.minecraft.spigot.mcitems.item.component.arise.AriseComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.arise.AriseComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.bait.BaitComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.bait.BaitComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.chunkloaderblock.ChunkloaderblockComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.damageblock.DamageblockComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.disorientation.DisorientationComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.disorientation.DisorientationComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.explosion_damage_reducer.ExplosionDamageReducerComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.explosion_damage_reducer.ExplosionDamageReducerComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.flash.FlashComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.flash.FlashComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.flash_snowball.FlashSnowballComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.flash_snowball.FlashSnowballComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.freezing.FreezingComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.freezing.FreezingComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.kamikaze.KamikazeComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.kamikaze.KamikazeComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.magic_snowball.MagicSnowballComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.magic_snowball.MagicSnowballComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.neutralize.NeutralizeComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.neutralize.NeutralizeComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.plast.PlastComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.plast.PlastComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.poison.PoisonComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.poison.PoisonComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.regeneration_shield.RegenerationShieldComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.regeneration_shield.RegenerationShieldComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.slow.SlowComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.slow.SlowComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.stealing_experience.StealingExperienceComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.stealing_experience.StealingExperienceComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.trap_chest.TrapChestComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.trap_chest.TrapChestComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.trapka.TrapkaComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.trapka.TrapkaComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.unbreakable_building.UnbreakableBuildingComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.unbreakable_building.UnbreakableBuildingComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.unbreakable_ward.UnbreakableWardComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.unbreakable_ward.UnbreakableWardComponentListener;
import su.mcdev.minecraft.spigot.mcitems.item.component.unfallable.SafeFallComponent;
import su.mcdev.minecraft.spigot.mcitems.item.component.unfallable.SafeFallComponentListener;
import su.mcdev.minecraft.spigot.mcitems.particle.emitter.EmitterApi;
import su.mcdev.minecraft.spigot.mcitems.util.DummyItemmComponentListener;

public class ItemManager implements Initer {
   @NotNull
   private EmitterApi emitterApi;

   public ItemManager(@NotNull EmitterApi emitterApi) {
      this.emitterApi = emitterApi;
      Server server = Bukkit.getServer();
      Version serverVersion = Version.getServerVersion(server);
      File dataFolder = Main.instance.getDataFolder();
      Main.instance.saveDirectory("items", false);
      Main.instance.saveDirectory("schematics", false);
      ItemMAPI.setup(true, false);
      ItemMAPI.attach(new File(dataFolder, "items"));
      ItemMAPI.attach("trapka_component", TrapkaComponent.class, new ItemMComponentListener[]{new TrapkaComponentListener(this.emitterApi)});
      ItemMAPI.attach("unbreakable_building_component", UnbreakableBuildingComponent.class, new ItemMComponentListener[]{new UnbreakableBuildingComponentListener()});
      ItemMAPI.attach("disorientation_component", DisorientationComponent.class, new ItemMComponentListener[]{new DisorientationComponentListener()});
      ItemMAPI.attach("unbreakable_ward_component", UnbreakableWardComponent.class, new ItemMComponentListener[]{new UnbreakableWardComponentListener()});
      ItemMAPI.attach("magic_snowball_component", MagicSnowballComponent.class, new ItemMComponentListener[]{new MagicSnowballComponentListener()});
      ItemMAPI.attach("safe_fall_component", SafeFallComponent.class, new ItemMComponentListener[]{new SafeFallComponentListener()});
      ItemMAPI.attach("plast_component", PlastComponent.class, new ItemMComponentListener[]{new PlastComponentListener()});
      ItemMAPI.attach("freezing_component", FreezingComponent.class, new ItemMComponentListener[]{new FreezingComponentListener()});
      ItemMAPI.attach("neutralize_component", NeutralizeComponent.class, new ItemMComponentListener[]{new NeutralizeComponentListener()});
      ItemMAPI.attach("kamikaze_component", KamikazeComponent.class, new ItemMComponentListener[]{new KamikazeComponentListener()});
      ItemMAPI.attach("bait_component", BaitComponent.class, new ItemMComponentListener[]{new BaitComponentListener()});
      ItemMAPI.attach("poison_component", PoisonComponent.class, new ItemMComponentListener[]{new PoisonComponentListener()});
      ItemMAPI.attach("trap_chest_component", TrapChestComponent.class, new ItemMComponentListener[]{new TrapChestComponentListener()});
      ItemMAPI.attach("explosion_damage_reducer_component", ExplosionDamageReducerComponent.class, new ItemMComponentListener[]{new ExplosionDamageReducerComponentListener()});
      ItemMAPI.attach("stealing_experience_component", StealingExperienceComponent.class, new ItemMComponentListener[]{new StealingExperienceComponentListener()});
      ItemMAPI.attach("damageblock_component", DamageblockComponent.class, new ItemMComponentListener[]{new DummyItemmComponentListener()});
      ItemMAPI.attach("chunkloaderblock_component", ChunkloaderblockComponent.class, new ItemMComponentListener[]{new DummyItemmComponentListener()});
      if (serverVersion.isNewerOrSameThan(Version.v1_12_R1)) {
         ItemMAPI.attach("slow_component", SlowComponent.class, new ItemMComponentListener[]{new SlowComponentListener(Main.instance)});
         ItemMAPI.attach("regeneration_shield_component", RegenerationShieldComponent.class, new ItemMComponentListener[]{new RegenerationShieldComponentListener()});
      }

      if (serverVersion.isNewerOrSameThan(Version.v1_16_R1)) {
         ItemMAPI.attach("flash_component", FlashComponent.class, new ItemMComponentListener[]{new FlashComponentListener()});
         ItemMAPI.attach("arise_component", AriseComponent.class, new ItemMComponentListener[]{new AriseComponentListener()});
         ItemMAPI.attach("flash_snowball_component", FlashSnowballComponent.class, new ItemMComponentListener[]{new FlashSnowballComponentListener()});
      }

      ItemMAPI.texture();
   }

   @NotNull
   @Generated
   public EmitterApi getEmitterApi() {
      return this.emitterApi;
   }

   @Generated
   public void setEmitterApi(@NotNull EmitterApi emitterApi) {
      this.emitterApi = emitterApi;
   }
}

package su.mcdev.chunk_loader.v1_16_R3;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.lang.reflect.Field;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.server.v1_16_R3.Advancement;
import net.minecraft.server.v1_16_R3.AdvancementDataPlayer;
import net.minecraft.server.v1_16_R3.AdvancementDataWorld;
import net.minecraft.server.v1_16_R3.AdvancementProgress;
import net.minecraft.server.v1_16_R3.AxisAlignedBB;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumGamemode;
import net.minecraft.server.v1_16_R3.EnumProtocolDirection;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.NetworkManager;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_16_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_16_R3.PacketPlayInChat;
import net.minecraft.server.v1_16_R3.PacketPlayInFlying;
import net.minecraft.server.v1_16_R3.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_16_R3.PacketPlayInTransaction;
import net.minecraft.server.v1_16_R3.PacketPlayInUpdateSign;
import net.minecraft.server.v1_16_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.SavedFile;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.mcdev.chunk_loader.DummyChannel;
import su.mcdev.chunk_loader.NpcChunkLoader;

public final class NpcChunkLoader_v1_16_R3 extends EntityPlayer implements NpcChunkLoader {
   @NotNull
   private final UUID npcUuid;
   @NotNull
   private final String npcName;
   private final AxisAlignedBB boundingBox;
   private final AdvancementDataPlayer advancements;
   private boolean dieCall = false;

   public NpcChunkLoader_v1_16_R3(@NotNull Location location, @NotNull UUID npcUuid, @NotNull String npcName) {
      super(((CraftServer)Bukkit.getServer()).getServer(), ((CraftWorld)location.getWorld()).getHandle(), new GameProfile(npcUuid, npcName), new PlayerInteractManager(((CraftWorld)location.getWorld()).getHandle()));
      this.npcUuid = npcUuid;
      this.npcName = npcName;
      this.boundingBox = new AxisAlignedBB(new BlockPosition(location.getX(), location.getY(), location.getZ()));
      this.playerConnection = new NpcChunkLoader_v1_16_R3.DummyPlayerConnection(this.server, this);
      this.advancements = new NpcChunkLoader_v1_16_R3.DummyPlayerAdvancements(this.server, this);
      this.playerInteractManager.setGameMode(EnumGamemode.CREATIVE);
      this.fallDistance = 0.0F;
      this.fauxSleeping = true;
      this.clientViewDistance = 0;

      try {
         this.affectsSpawning = true;
      } catch (Throwable var5) {
      }

      this.spawnIn(this.world);
      this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
      ((WorldServer)this.world).addPlayerJoin(this);
      super.a(this.boundingBox);
   }

   public AxisAlignedBB getBoundingBox() {
      return this.boundingBox;
   }

   public void die() {
      if (!this.dieCall) {
         this.dieCall = true;
         this.getWorldServer().removePlayer(this);
         this.dieCall = false;
      } else {
         super.die();
      }

   }

   public Location getLocation() {
      return this.getBukkitEntity().getLocation();
   }

   public Player getPlayer() {
      return this.getBukkitEntity();
   }

   public AdvancementDataPlayer getAdvancementData() {
      return this.advancements;
   }

   @NotNull
   @Generated
   public UUID getNpcUuid() {
      return this.npcUuid;
   }

   @NotNull
   @Generated
   public String getNpcName() {
      return this.npcName;
   }

   @Generated
   public AdvancementDataPlayer getAdvancements() {
      return this.advancements;
   }

   @Generated
   public boolean isDieCall() {
      return this.dieCall;
   }

   private static class DummyPlayerAdvancements extends AdvancementDataPlayer {
      DummyPlayerAdvancements(MinecraftServer server, EntityPlayer entityPlayer) {
         super(server.getDataFixer(), server.getPlayerList(), server.getAdvancementData(), getAdvancementsFile(server, entityPlayer), entityPlayer);
      }

      private static File getAdvancementsFile(MinecraftServer server, EntityPlayer entityPlayer) {
         File advancementsDir = server.a(SavedFile.ADVANCEMENTS).toFile();
         return new File(advancementsDir, entityPlayer.getUniqueID() + ".json");
      }

      public void a(EntityPlayer owner) {
      }

      public void a() {
      }

      public void a(AdvancementDataWorld advancementLoader) {
      }

      public void b() {
      }

      public boolean grantCriteria(Advancement advancement, String criterionName) {
         return false;
      }

      public boolean revokeCritera(Advancement advancement, String criterionName) {
         return false;
      }

      public void b(EntityPlayer player) {
      }

      public void a(@Nullable Advancement advancement) {
      }

      public AdvancementProgress getProgress(Advancement advancement) {
         return new AdvancementProgress();
      }
   }

   public static class DummyPlayerConnection extends PlayerConnection {
      DummyPlayerConnection(MinecraftServer minecraftServer, EntityPlayer entityPlayer) {
         super(minecraftServer, new NpcChunkLoader_v1_16_R3.DummyNetworkManager(), entityPlayer);
      }

      public void a(PacketPlayInWindowClick packetPlayInWindowClick) {
      }

      public void a(PacketPlayInTransaction packetPlayInTransaction) {
      }

      public void a(PacketPlayInFlying packetPlayInFlying) {
      }

      public void a(PacketPlayInUpdateSign packetPlayInUpdateSign) {
      }

      public void a(PacketPlayInBlockDig packetPlayInBlockDig) {
      }

      public void a(PacketPlayInBlockPlace packetPlayInBlockPlace) {
      }

      public void disconnect(String s) {
      }

      public void a(PacketPlayInHeldItemSlot packetPlayInHeldItemSlot) {
      }

      public void a(PacketPlayInChat packetPlayInChat) {
      }

      public void sendPacket(Packet packet) {
      }
   }

   public static class DummyNetworkManager extends NetworkManager {
      private static Field channelField;
      private static Field socketAddressField;

      DummyNetworkManager() {
         super(EnumProtocolDirection.SERVERBOUND);
         this.updateFields();
      }

      private void updateFields() {
         try {
            if (channelField != null) {
               channelField.set(this, new DummyChannel());
            }

            if (socketAddressField != null) {
               socketAddressField.set(this, (Object)null);
            }
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      }

      static {
         try {
            channelField = NetworkManager.class.getDeclaredField("channel");
            channelField.setAccessible(true);
            socketAddressField = NetworkManager.class.getDeclaredField("socketAddress");
            socketAddressField.setAccessible(true);
         } catch (Exception var1) {
            var1.printStackTrace();
         }

      }
   }
}

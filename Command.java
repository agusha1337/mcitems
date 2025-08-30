package su.mcdev.minecraft.spigot.mcitems.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import me.socrum.minecraft.spigot.plugin.utilm.advanced.CommandExecutorAdvanced;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.ItemMAPI;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.gui.ItemMGui;
import me.socrum.minecraft.spigot.plugin.utilm.itemm.itemm.ItemM;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import su.mcdev.minecraft.spigot.mcitems.Main;

public class Command implements CommandExecutorAdvanced, Listener {
   public List<Chunk> getChunksInRadius(Block block, int radius) {
      List<Chunk> chunks = new ArrayList();
      if (radius <= 0) {
         chunks.add(block.getChunk());
         return chunks;
      } else {
         World world = block.getWorld();
         Chunk centerChunk = block.getChunk();
         int centerX = centerChunk.getX();
         int centerZ = centerChunk.getZ();

         for(int x = centerX - radius; x <= centerX + radius; ++x) {
            for(int z = centerZ - radius; z <= centerZ + radius; ++z) {
               Chunk chunk = world.getChunkAt(x, z);
               chunks.add(chunk);
            }
         }

         return chunks;
      }
   }

   public void setGoldBlockInChunkCenter(Chunk chunk) {
      World world = chunk.getWorld();
      int centerX = chunk.getX() * 16 + 8;
      int centerZ = chunk.getZ() * 16 + 8;
      int y = 5;
      Block centerBlock = world.getBlockAt(centerX, y, centerZ);
      centerBlock.setType(Material.GOLD_BLOCK);
   }

   public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
      FileConfiguration config = Main.instance.getConfig();
      if (strings.length == 1 && strings[0].equals("list")) {
         if (!commandSender.hasPermission(config.getString("permission.list"))) {
            UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.permission_denied");
            return true;
         } else if (!(commandSender instanceof Player)) {
            return true;
         } else {
            Player player = (Player)commandSender;
            (new ItemMGui(player, "Список предметов", 54)).open(player);
            return true;
         }
      } else if (strings.length == 1 && strings[0].equalsIgnoreCase("help")) {
         UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.help");
         return true;
      } else {
         String targetPlayerName;
         String itemId;
         Player targetPlayer;
         ItemM itemM;
         ItemStack itemMItemStack;
         Map placeholders;
         if (strings.length == 4 && strings[0].equalsIgnoreCase("item") && strings[2].equalsIgnoreCase("give")) {
            if (!commandSender.hasPermission(config.getString("permission.give"))) {
               UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.permission_denied");
               return true;
            } else {
               targetPlayerName = strings[1];
               itemId = strings[3];
               targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
               if (targetPlayer != null && targetPlayer.isOnline()) {
                  itemM = ItemMAPI.getOrNull(itemId);
                  if (itemM == null) {
                     placeholders = Map.of("%item_id%", itemId);
                     UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.specified_item_id_does_not_exist", placeholders);
                     return true;
                  } else {
                     itemMItemStack = itemM.getItemStack();
                     UtilM.addItem(targetPlayer, itemMItemStack);
                     UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.success");
                     return true;
                  }
               } else {
                  UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.player_is_not_online");
                  return true;
               }
            }
         } else if (strings.length == 4 && strings[0].equalsIgnoreCase("item") && strings[2].equalsIgnoreCase("take")) {
            if (!commandSender.hasPermission(config.getString("permission.take"))) {
               UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.permission_denied");
               return true;
            } else {
               targetPlayerName = strings[1];
               itemId = strings[3];
               targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
               if (targetPlayer != null && targetPlayer.isOnline()) {
                  itemM = ItemMAPI.getOrNull(itemId);
                  if (itemM == null) {
                     placeholders = Map.of("%item_id%", itemId);
                     UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.specified_item_id_does_not_exist", placeholders);
                     return true;
                  } else {
                     itemMItemStack = itemM.getItemStack();
                     if (!UtilM.hasItem(targetPlayer, itemMItemStack)) {
                        UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.player_does_not_have_specified_item");
                        return true;
                     } else {
                        UtilM.delItem(targetPlayer, itemMItemStack);
                        UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.success");
                        return true;
                     }
                  }
               } else {
                  UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.player_is_not_online");
                  return true;
               }
            }
         } else {
            UtilM.sendMessageFromConfigurationPath(Main.instance, commandSender, "message.help");
            return true;
         }
      }
   }

   public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
      List itemMIdList;
      if (strings.length == 1) {
         itemMIdList = (List)(new ArrayList(List.of("help", "list", "item"))).stream().sorted().collect(Collectors.toList());
         return itemMIdList;
      } else if (strings.length == 2) {
         itemMIdList = (List)Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).sorted().collect(Collectors.toList());
         return itemMIdList;
      } else if (strings.length == 3) {
         itemMIdList = (List)(new ArrayList(List.of("give", "take"))).stream().sorted().collect(Collectors.toList());
         return itemMIdList;
      } else if (strings.length == 4) {
         itemMIdList = (List)ItemMAPI.map().keySet().stream().sorted().collect(Collectors.toList());
         return itemMIdList;
      } else {
         return Collections.emptyList();
      }
   }
}

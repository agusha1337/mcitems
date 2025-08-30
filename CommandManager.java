package su.mcdev.minecraft.spigot.mcitems.command;

import me.socrum.advanced.ini.Initer;
import me.socrum.minecraft.spigot.plugin.utilm.UtilM;
import su.mcdev.minecraft.spigot.mcitems.Main;

public class CommandManager implements Initer {
   public CommandManager() {
      Command command = new Command();
      UtilM.registerClassAsCommand(Main.instance, "mcitems", (Command)Main.ini.put(command));
      UtilM.registerClassAsCommand(Main.instance, "items", (Command)Main.ini.put(command));
      UtilM.registerClassAsCommand(Main.instance, "item", (Command)Main.ini.put(command));
   }
}

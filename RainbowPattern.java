package su.mcdev.minecraft.spigot.mcitems.lib.com.iridium.iridiumcolorapi.patterns;

import java.util.regex.Matcher;
import su.mcdev.minecraft.spigot.mcitems.lib.com.iridium.iridiumcolorapi.IridiumColorAPI;

public class RainbowPattern implements Pattern {
   java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<RAINBOW([0-9]{1,3})>(.*?)</RAINBOW>");

   public String process(String string) {
      String saturation;
      String content;
      for(Matcher matcher = this.pattern.matcher(string); matcher.find(); string = string.replace(matcher.group(), IridiumColorAPI.rainbow(content, Float.parseFloat(saturation)))) {
         saturation = matcher.group(1);
         content = matcher.group(2);
      }

      return string;
   }
}

package su.mcdev.minecraft.spigot.mcitems.lib.com.iridium.iridiumcolorapi;

import com.google.common.collect.ImmutableMap;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import su.mcdev.minecraft.spigot.mcitems.lib.com.iridium.iridiumcolorapi.patterns.GradientPattern;
import su.mcdev.minecraft.spigot.mcitems.lib.com.iridium.iridiumcolorapi.patterns.Pattern;
import su.mcdev.minecraft.spigot.mcitems.lib.com.iridium.iridiumcolorapi.patterns.RainbowPattern;
import su.mcdev.minecraft.spigot.mcitems.lib.com.iridium.iridiumcolorapi.patterns.SolidPattern;

public class IridiumColorAPI {
   private static final int VERSION = getVersion();
   private static final boolean SUPPORTS_RGB;
   private static final List<String> SPECIAL_COLORS;
   private static final Map<Color, ChatColor> COLORS;
   private static final List<Pattern> PATTERNS;

   @Nonnull
   public static String process(@Nonnull String string) {
      Pattern pattern;
      for(Iterator var1 = PATTERNS.iterator(); var1.hasNext(); string = pattern.process(string)) {
         pattern = (Pattern)var1.next();
      }

      return ChatColor.translateAlternateColorCodes('&', string);
   }

   @Nonnull
   public static List<String> process(@Nonnull Collection<String> strings) {
      return (List)strings.stream().map(IridiumColorAPI::process).collect(Collectors.toList());
   }

   @Nonnull
   public static String color(@Nonnull String string, @Nonnull Color color) {
      ChatColor var10000 = SUPPORTS_RGB ? ChatColor.of(color) : getClosestColor(color);
      return var10000 + string;
   }

   @Nonnull
   public static String color(@Nonnull String string, @Nonnull Color start, @Nonnull Color end) {
      ChatColor[] colors = createGradient(start, end, withoutSpecialChar(string).length());
      return apply(string, colors);
   }

   @Nonnull
   public static String rainbow(@Nonnull String string, float saturation) {
      ChatColor[] colors = createRainbow(withoutSpecialChar(string).length(), saturation);
      return apply(string, colors);
   }

   @Nonnull
   public static ChatColor getColor(@Nonnull String string) {
      return SUPPORTS_RGB ? ChatColor.of(new Color(Integer.parseInt(string, 16))) : getClosestColor(new Color(Integer.parseInt(string, 16)));
   }

   @Nonnull
   public static String stripColorFormatting(@Nonnull String string) {
      return string.replaceAll("<#[0-9A-F]{6}>|[&§][a-f0-9lnokm]|<[/]?[A-Z]{5,8}(:[0-9A-F]{6})?[0-9]*>", "");
   }

   @Nonnull
   private static String apply(@Nonnull String source, ChatColor[] colors) {
      StringBuilder specialColors = new StringBuilder();
      StringBuilder stringBuilder = new StringBuilder();
      int outIndex = 0;

      for(int i = 0; i < source.length(); ++i) {
         char currentChar = source.charAt(i);
         if (('&' == currentChar || 167 == currentChar) && i + 1 < source.length()) {
            char nextChar = source.charAt(i + 1);
            if ('r' != nextChar && 'R' != nextChar) {
               specialColors.append(currentChar).append(nextChar);
            } else {
               specialColors.setLength(0);
            }

            ++i;
         } else {
            stringBuilder.append(colors[outIndex++]).append(specialColors).append(currentChar);
         }
      }

      return stringBuilder.toString();
   }

   @Nonnull
   private static String withoutSpecialChar(@Nonnull String source) {
      String workingString = source;
      Iterator var2 = SPECIAL_COLORS.iterator();

      while(var2.hasNext()) {
         String color = (String)var2.next();
         if (workingString.contains(color)) {
            workingString = workingString.replace(color, "");
         }
      }

      return workingString;
   }

   @Nonnull
   private static ChatColor[] createRainbow(int step, float saturation) {
      ChatColor[] colors = new ChatColor[step];
      double colorStep = 1.0D / (double)step;

      for(int i = 0; i < step; ++i) {
         Color color = Color.getHSBColor((float)(colorStep * (double)i), saturation, saturation);
         if (SUPPORTS_RGB) {
            colors[i] = ChatColor.of(color);
         } else {
            colors[i] = getClosestColor(color);
         }
      }

      return colors;
   }

   @Nonnull
   private static ChatColor[] createGradient(@Nonnull Color start, @Nonnull Color end, int step) {
      step = Math.max(step, 2);
      ChatColor[] colors = new ChatColor[step];
      int stepR = Math.abs(start.getRed() - end.getRed()) / (step - 1);
      int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step - 1);
      int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step - 1);
      int[] direction = new int[]{start.getRed() < end.getRed() ? 1 : -1, start.getGreen() < end.getGreen() ? 1 : -1, start.getBlue() < end.getBlue() ? 1 : -1};

      for(int i = 0; i < step; ++i) {
         Color color = new Color(start.getRed() + stepR * i * direction[0], start.getGreen() + stepG * i * direction[1], start.getBlue() + stepB * i * direction[2]);
         if (SUPPORTS_RGB) {
            colors[i] = ChatColor.of(color);
         } else {
            colors[i] = getClosestColor(color);
         }
      }

      return colors;
   }

   @Nonnull
   private static ChatColor getClosestColor(Color color) {
      Color nearestColor = null;
      double nearestDistance = 2.147483647E9D;
      Iterator var4 = COLORS.keySet().iterator();

      while(var4.hasNext()) {
         Color constantColor = (Color)var4.next();
         double distance = Math.pow((double)(color.getRed() - constantColor.getRed()), 2.0D) + Math.pow((double)(color.getGreen() - constantColor.getGreen()), 2.0D) + Math.pow((double)(color.getBlue() - constantColor.getBlue()), 2.0D);
         if (nearestDistance > distance) {
            nearestColor = constantColor;
            nearestDistance = distance;
         }
      }

      return (ChatColor)COLORS.get(nearestColor);
   }

   private static int getVersion() {
      if (!classExists("org.bukkit.Bukkit") && classExists("net.md_5.bungee.api.ChatColor")) {
         return -1;
      } else {
         String version = Bukkit.getVersion();
         int index = version.lastIndexOf("MC:");
         if (index != -1) {
            version = version.substring(index + 4, version.length() - 1);
         } else if (version.endsWith("SNAPSHOT")) {
            index = version.indexOf(45);
            version = version.substring(0, index);
         }

         int lastDot = version.lastIndexOf(46);
         if (version.indexOf(46) != lastDot) {
            version = version.substring(0, lastDot);
         }

         return Integer.parseInt(version.substring(2));
      }
   }

   private static boolean classExists(String path) {
      try {
         Class.forName(path);
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }

   static {
      SUPPORTS_RGB = VERSION >= 16 || VERSION == -1;
      SPECIAL_COLORS = Arrays.asList("&l", "&n", "&o", "&k", "&m", "§l", "§n", "§o", "§k", "§m");
      COLORS = ImmutableMap.builder().put(new Color(0), ChatColor.getByChar('0')).put(new Color(170), ChatColor.getByChar('1')).put(new Color(43520), ChatColor.getByChar('2')).put(new Color(43690), ChatColor.getByChar('3')).put(new Color(11141120), ChatColor.getByChar('4')).put(new Color(11141290), ChatColor.getByChar('5')).put(new Color(16755200), ChatColor.getByChar('6')).put(new Color(11184810), ChatColor.getByChar('7')).put(new Color(5592405), ChatColor.getByChar('8')).put(new Color(5592575), ChatColor.getByChar('9')).put(new Color(5635925), ChatColor.getByChar('a')).put(new Color(5636095), ChatColor.getByChar('b')).put(new Color(16733525), ChatColor.getByChar('c')).put(new Color(16733695), ChatColor.getByChar('d')).put(new Color(16777045), ChatColor.getByChar('e')).put(new Color(16777215), ChatColor.getByChar('f')).build();
      PATTERNS = Arrays.asList(new GradientPattern(), new SolidPattern(), new RainbowPattern());
   }
}

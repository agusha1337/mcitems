package su.mcdev.minecraft.spigot.plugin.mcmenu.commons;

import io.github.g00fy2.versioncompare.Version;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.File;
import su.mcdev.minecraft.spigot.plugin.mcmenu.commons.updater.DevUpdater;
import su.mcdev.minecraft.spigot.plugin.mcmenu.commons.util.Util;

public class McPlugin {
   private String pluginName;
   private String version;
   private File absoluteDataFolder;
   private int mcDevResourceId;
   private boolean successfullyLoaded;

   public McPlugin(String pluginName, String version, File absoluteDataFolder, int mcDevResourceId) {
      this.pluginName = pluginName;
      this.version = version;
      this.absoluteDataFolder = absoluteDataFolder;
      this.mcDevResourceId = mcDevResourceId;
      this.successfullyLoaded = false;
      this.printEula();
      this.unpackEulaTxtToPluginDirectory();
      String actualVersionOfThisPlugin = this.makeRequestAndGetActualVersionOfThisPlugin();
      if (actualVersionOfThisPlugin == null) {
         this.printRequestError();
      } else {
         boolean outdate = this.isOutdate(this.version, actualVersionOfThisPlugin);
         if (outdate) {
            this.printOutdate(actualVersionOfThisPlugin);
         } else {
            this.printRelevant();
            this.successfullyLoaded = true;
         }
      }
   }

   public void printEula() {
      String white = "\u001b[1;37m";
      String black = "\u001b[30m";
      String blue = "\u001b[1;34m";
      String reset = "\u001b[0m";
      String bracketedPluginName = String.format("[%s] ", this.pluginName);
      System.out.println(white + bracketedPluginName + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + blue + "G55G" + white + "@@@@@@@@@@@@@@@@@@@@@" + reset);
      System.out.println(white + bracketedPluginName + black + "&&&&&&&&" + white + "@" + black + "&&&&&" + white + "@@@@@" + black + "&&&&&&" + white + "@@@@@@" + black + "&&###&&" + blue + "B55P&" + white + "@" + black + "&&&&&&" + white + "@@@" + black + "&&&&" + white + "@@@" + black + "&&&&" + reset);
      System.out.println(white + bracketedPluginName + black + "####&#####&###&" + white + "@@" + black + "&########&" + white + "@@" + black + "&#######" + blue + "G55P&" + white + "@" + black + "###" + white + "&&" + black + "####" + white + "@@" + black + "###&" + white + "@" + black + "&###&" + reset);
      System.out.println(white + bracketedPluginName + black + "###&" + white + "@@" + black + "###" + white + "@@" + black + "&###" + white + "@" + black + "&###" + white + "@@@@&&&" + white + "@@" + black + "#######" + blue + "G55P&" + white + "@" + black + "####&&&###&" + white + "@@" + black + "###&###&" + white + "@" + reset);
      System.out.println(white + bracketedPluginName + black + "###&" + white + "@@" + black + "###" + white + "@@" + black + "&###" + white + "@@" + black + "###&&&&##&" + white + "@@" + black + "&#####" + blue + "B555#" + white + "@@" + black + "&###" + white + "&&&###@@@" + black + "&#####&" + white + "@@" + reset);
      System.out.println(white + bracketedPluginName + black + "###&" + white + "@@" + black + "###" + white + "@@" + black + "&###" + white + "@@@" + black + "&######&" + white + "@@@@" + black + "&&##" + blue + "BP55B" + white + "@@@@" + black + "&&#####&" + white + "@@@@@" + black + "&###&" + white + "@@@" + reset);
      System.out.println(white + bracketedPluginName + "----------------------------------------------------------------" + reset);
      System.out.println(white + bracketedPluginName + "*** ПЛАГИН " + blue + this.pluginName + " " + white + "ТОЛЬКО НА " + blue + "https://mcdev.su " + white + "***" + reset);
      System.out.println(white + bracketedPluginName + "----------------------------------------------------------------" + reset);
   }

   public void unpackEulaTxtToPluginDirectory() {
      this.absoluteDataFolder.mkdirs();
      Util.copyJarFile(this.getClass(), "/eula.txt", new File(this.absoluteDataFolder, "eula.txt"));
   }

   public String getMcDevResourceUrl() {
      return String.format("https://mcdev.su/resources/%d", this.mcDevResourceId);
   }

   public boolean isOutdate(@Nonnull String currentVersion, @Nonnull String actualVersion) {
      return (new Version(currentVersion)).isLowerThan(actualVersion);
   }

   @Nullable
   public String makeRequestAndGetActualVersionOfThisPlugin() {
      try {
         DevUpdater devUpdater = (DevUpdater)DevUpdater.builder().apiKey("lEwscqLIwD35ekqrw-yjzb32okGLNtC9").resourceId(this.mcDevResourceId).build();
         su.mcdev.minecraft.spigot.plugin.mcmenu.commons.updater.common.Version version = devUpdater.currentVersion();
         return version == null ? null : version.asRaw();
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public void printRequestError() {
      String white = "\u001b[1;37m";
      String blue = "\u001b[1;34m";
      String reset = "\u001b[0m";
      String yellow = "\u001b[1;33m";
      String red = "\u001b[1;31m";
      String bracketedPluginName = String.format("[%s] ", this.pluginName);
      System.out.println(white + bracketedPluginName + "----------------------------------------------------------------" + reset);
      System.out.println(white + bracketedPluginName + "*** " + yellow + "Не удалось проверить наличие обновления!" + white + " ***" + reset);
      System.out.println(white + bracketedPluginName + "*** Прямая ссылка: " + blue + this.getMcDevResourceUrl() + white + " ***" + reset);
      System.out.println(white + bracketedPluginName + "----------------------------------------------------------------" + reset);
   }

   public void printOutdate(@Nonnull String newVersion) {
      String white = "\u001b[1;37m";
      String blue = "\u001b[1;34m";
      String reset = "\u001b[0m";
      String red = "\u001b[1;31m";
      String green = "\u001b[1;32m";
      String bracketedPluginName = String.format("[%s] ", this.pluginName);
      System.out.println(white + bracketedPluginName + "----------------------------------------------------------------" + reset);
      System.out.println(white + bracketedPluginName + "*** Найдено обновление: " + blue + this.pluginName + green + " [ОБНОВЛЕНИЕ " + newVersion + "]" + white + " ***" + reset);
      System.out.println(white + bracketedPluginName + "*** Скачать: " + blue + this.getMcDevResourceUrl() + white + " ***" + reset);
      System.out.println(white + bracketedPluginName + "----------------------------------------------------------------" + reset);
   }

   public void printRelevant() {
      String white = "\u001b[1;37m";
      String reset = "\u001b[0m";
      String bracketedPluginName = String.format("[%s] ", this.pluginName);
      System.out.println(white + bracketedPluginName + "Используется актуальная версия." + reset);
   }

   public String getPluginName() {
      return this.pluginName;
   }

   public String getVersion() {
      return this.version;
   }

   public File getAbsoluteDataFolder() {
      return this.absoluteDataFolder;
   }

   public int getMcDevResourceId() {
      return this.mcDevResourceId;
   }

   public boolean isSuccessfullyLoaded() {
      return this.successfullyLoaded;
   }

   public void setPluginName(String pluginName) {
      this.pluginName = pluginName;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public void setAbsoluteDataFolder(File absoluteDataFolder) {
      this.absoluteDataFolder = absoluteDataFolder;
   }

   public void setMcDevResourceId(int mcDevResourceId) {
      this.mcDevResourceId = mcDevResourceId;
   }

   public void setSuccessfullyLoaded(boolean successfullyLoaded) {
      this.successfullyLoaded = successfullyLoaded;
   }
}

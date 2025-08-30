package su.mcdev.minecraft.spigot.plugin.mcmenu.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class AdvancedLicense {
   private String licenseKey;
   private Plugin plugin;
   private String validationServer;
   private AdvancedLicense.LogType logType;
   private String securityKey;
   private boolean debug;

   public AdvancedLicense(String licenseKey, String validationServer, Plugin plugin) {
      this.logType = AdvancedLicense.LogType.NORMAL;
      this.securityKey = "sRx9iFk67fkJB1MR0K52XtZoQLIh9H6rGHQ9";
      this.debug = false;
      this.licenseKey = licenseKey;
      this.plugin = plugin;
      this.validationServer = validationServer;
   }

   private static String xor(String s1, String s2) {
      StringBuilder result = new StringBuilder();

      for(int i = 0; i < Math.min(s1.length(), s2.length()); ++i) {
         result.append(Byte.parseByte("" + s1.charAt(i)) ^ Byte.parseByte(s2.charAt(i) + ""));
      }

      return result.toString();
   }

   public AdvancedLicense setSecurityKey(String securityKey) {
      this.securityKey = securityKey;
      return this;
   }

   public AdvancedLicense setConsoleLog(AdvancedLicense.LogType logType) {
      this.logType = logType;
      return this;
   }

   public AdvancedLicense debug() {
      this.debug = true;
      return this;
   }

   public boolean register() {
      this.log(0, "[]==========[License-System]==========[]");
      this.log(0, "Connecting to License-Server...");
      AdvancedLicense.ValidationType vt = this.isValid();
      if (vt == AdvancedLicense.ValidationType.VALID) {
         this.log(1, "License valid!");
         this.log(0, "[]==========[License-System]==========[]");
         return true;
      } else {
         this.log(1, "License is NOT valid!");
         this.log(1, "Failed as a result of " + vt.toString());
         this.log(1, "Disabling plugin!");
         this.log(0, "[]==========[License-System]==========[]");
         Bukkit.getScheduler().cancelTasks(this.plugin);
         Bukkit.getPluginManager().disablePlugin(this.plugin);
         return false;
      }
   }

   public boolean isValidSimple() {
      return this.isValid() == AdvancedLicense.ValidationType.VALID;
   }

   private String requestServer(String v1, String v2) throws IOException {
      URL url = new URL(this.validationServer + "?v1=" + v1 + "&v2=" + v2 + "&pl=" + this.plugin.getName());
      HttpURLConnection con = (HttpURLConnection)url.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("User-Agent", "Mozilla/5.0");
      int responseCode = con.getResponseCode();
      if (this.debug) {
         System.out.println("\nSending 'GET' request to URL : " + url);
         System.out.println("Response Code : " + responseCode);
      }

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

      String var9;
      try {
         StringBuilder response = new StringBuilder();

         while(true) {
            String inputLine;
            if ((inputLine = in.readLine()) == null) {
               var9 = response.toString();
               break;
            }

            response.append(inputLine);
         }
      } catch (Throwable var11) {
         try {
            in.close();
         } catch (Throwable var10) {
            var11.addSuppressed(var10);
         }

         throw var11;
      }

      in.close();
      return var9;
   }

   public AdvancedLicense.ValidationType isValid() {
      String rand = this.toBinary(UUID.randomUUID().toString());
      String sKey = this.toBinary(this.securityKey);
      String key = this.toBinary(this.licenseKey);

      try {
         String response = this.requestServer(xor(rand, sKey), xor(rand, key));
         if (!response.startsWith("<")) {
            try {
               return AdvancedLicense.ValidationType.valueOf(response);
            } catch (IllegalArgumentException var7) {
               String respRand = xor(xor(response, key), sKey);
               return rand.substring(0, respRand.length()).equals(respRand) ? AdvancedLicense.ValidationType.VALID : AdvancedLicense.ValidationType.WRONG_RESPONSE;
            }
         } else {
            this.log(1, "The License-Server returned an invalid response!");
            this.log(1, "In most cases this is caused by:");
            this.log(1, "1) Your Web-Host injects JS into the page (often caused by free hosts)");
            this.log(1, "2) Your ValidationServer-URL is wrong");
            this.log(1, "SERVER-RESPONSE: " + (response.length() >= 150 && !this.debug ? response.substring(0, 150) + "..." : response));
            return AdvancedLicense.ValidationType.PAGE_ERROR;
         }
      } catch (IOException var8) {
         if (this.debug) {
            var8.printStackTrace();
         }

         return AdvancedLicense.ValidationType.PAGE_ERROR;
      }
   }

   private String toBinary(String s) {
      byte[] bytes = s.getBytes();
      StringBuilder binary = new StringBuilder();
      byte[] var4 = bytes;
      int var5 = bytes.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         byte b = var4[var6];
         int val = b;

         for(int i = 0; i < 8; ++i) {
            binary.append((val & 128) == 0 ? 0 : 1);
            val <<= 1;
         }
      }

      return binary.toString();
   }

   private void log(int type, String message) {
      if (this.logType != AdvancedLicense.LogType.NONE && (this.logType != AdvancedLicense.LogType.LOW || type != 0)) {
         System.out.println(message);
      }
   }

   public static enum LogType {
      NORMAL,
      LOW,
      NONE;

      // $FF: synthetic method
      private static AdvancedLicense.LogType[] $values() {
         return new AdvancedLicense.LogType[]{NORMAL, LOW, NONE};
      }
   }

   public static enum ValidationType {
      WRONG_RESPONSE,
      PAGE_ERROR,
      URL_ERROR,
      KEY_OUTDATED,
      KEY_NOT_FOUND,
      NOT_VALID_IP,
      INVALID_PLUGIN,
      VALID;

      // $FF: synthetic method
      private static AdvancedLicense.ValidationType[] $values() {
         return new AdvancedLicense.ValidationType[]{WRONG_RESPONSE, PAGE_ERROR, URL_ERROR, KEY_OUTDATED, KEY_NOT_FOUND, NOT_VALID_IP, INVALID_PLUGIN, VALID};
      }
   }
}

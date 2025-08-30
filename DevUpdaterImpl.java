package su.mcdev.minecraft.spigot.plugin.mcmenu.commons.updater;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import su.mcdev.minecraft.spigot.plugin.mcmenu.commons.updater.common.Version;
import su.mcdev.minecraft.spigot.plugin.mcmenu.commons.updater.request.HTTPRequest;

final class DevUpdaterImpl implements DevUpdater {
   private static final Version EMPTY_VERSION = Version.ofRaw("0.0.1");
   private final Version cachedVersion;

   DevUpdaterImpl(@Nonnull HTTPRequest request) {
      this.cachedVersion = (Version)request.readElementAsync().thenApply((element) -> {
         if (element == null) {
            return null;
         } else {
            JsonObject obj = element.getAsJsonObject();
            if (obj == null) {
               return null;
            } else {
               JsonObject resource = obj.getAsJsonObject("resource");
               if (resource != null && !resource.isJsonNull() && resource.isJsonObject()) {
                  JsonElement version = resource.getAsJsonPrimitive("version");
                  if (version.isJsonNull()) {
                     return null;
                  } else {
                     String versionString = version.getAsString();
                     return Version.ofRaw(versionString);
                  }
               } else {
                  return null;
               }
            }
         }
      }).join();
   }

   @Nullable
   public Version currentVersion() {
      return this.cachedVersion;
   }

   static final class BuilderImpl implements DevUpdater.Builder {
      private int productId;
      private String apiKey;

      public DevUpdater.Builder resourceId(int id) {
         this.productId = id;
         return this;
      }

      public DevUpdater.Builder apiKey(@Nonnull String apiKey) {
         this.apiKey = apiKey;
         return this;
      }

      @Nonnull
      public DevUpdater build() {
         return new DevUpdaterImpl((HTTPRequest)HTTPRequest.get().url("https://mcdev.su/api/resources/" + this.productId + '/').basicAuth(this.apiKey).header("User-Agent", "Mozilla/5.0").build());
      }
   }
}

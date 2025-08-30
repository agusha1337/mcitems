package su.mcdev.minecraft.spigot.plugin.mcmenu.commons.updater.common;

import jakarta.annotation.Nonnull;

public interface Version extends Comparable<Version> {
   static Version ofRaw(@Nonnull String raw) {
      return new Version.Impl(raw);
   }

   String asRaw();

   default int compareTo(@Nonnull Version o) {
      return this.asRaw().compareTo(o.asRaw());
   }

   public static class Impl implements Version {
      private final String raw;

      public Impl(@Nonnull String raw) {
         this.raw = raw;
      }

      public String asRaw() {
         return this.raw;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (!(o instanceof Version)) {
            return false;
         } else {
            Version impl = (Version)o;
            return this.raw != null ? this.raw.equals(impl.asRaw()) : impl.asRaw() == null;
         }
      }

      public String toString() {
         return this.asRaw();
      }
   }
}

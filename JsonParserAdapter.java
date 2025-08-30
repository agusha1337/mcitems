package su.mcdev.minecraft.spigot.plugin.mcmenu.commons.updater.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jakarta.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public final class JsonParserAdapter {
   private static final boolean SUPPORTS_STATIC;
   private static final MethodHandle PARSER_STRING_HANDLE;
   private static final Object INSTANCE;

   private JsonParserAdapter() {
      throw new AssertionError();
   }

   @Nonnull
   public static JsonElement parseString(@Nonnull String string) {
      try {
         MethodHandle handle;
         if (SUPPORTS_STATIC) {
            handle = PARSER_STRING_HANDLE;
         } else {
            handle = PARSER_STRING_HANDLE.bindTo(INSTANCE);
         }

         return handle.invoke(string);
      } catch (Throwable var2) {
         throw new RuntimeException(var2);
      }
   }

   @Nonnull
   private static Method resolveMethod() {
      try {
         return JsonParser.class.getMethod("parseString", String.class);
      } catch (NoSuchMethodException var3) {
         try {
            return JsonParser.class.getMethod("parse", String.class);
         } catch (NoSuchMethodException var2) {
            throw new RuntimeException(var2);
         }
      }
   }

   static {
      try {
         Method method = resolveMethod();
         SUPPORTS_STATIC = method.getName().equals("parseString");
         if (SUPPORTS_STATIC) {
            INSTANCE = null;
         } else {
            INSTANCE = new JsonParser();
         }

         PARSER_STRING_HANDLE = MethodHandles.lookup().unreflect(method);
      } catch (IllegalAccessException var1) {
         throw new RuntimeException(var1);
      }
   }
}

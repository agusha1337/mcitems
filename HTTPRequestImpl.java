package su.mcdev.minecraft.spigot.plugin.mcmenu.commons.updater.request;

import com.google.gson.JsonElement;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import su.mcdev.minecraft.spigot.plugin.mcmenu.commons.updater.adapter.JsonParserAdapter;

final class HTTPRequestImpl implements HTTPRequest {
   private final HttpURLConnection connection;

   HTTPRequestImpl(HTTPRequestImpl.BuilderImpl builder) {
      Object conn;
      try {
         conn = (HttpURLConnection)builder.url.openConnection();
         if (builder.get) {
            ((HttpURLConnection)conn).setRequestMethod("GET");
         }

         Iterator var3 = builder.headers.entrySet().iterator();

         while(var3.hasNext()) {
            Entry<String, String> entry = (Entry)var3.next();
            ((HttpURLConnection)conn).setRequestProperty((String)entry.getKey(), (String)entry.getValue());
         }
      } catch (IOException var5) {
         conn = HTTPRequestImpl.DummyHttpURLConnection.INSTANCE;
      }

      this.connection = (HttpURLConnection)conn;
   }

   private static String readLines(InputStream inputStream) throws IOException {
      StringBuilder response = new StringBuilder();
      BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

      String inputLine;
      try {
         while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
         }
      } catch (Throwable var6) {
         try {
            in.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      in.close();
      return response.toString();
   }

   @Nullable
   public JsonElement readElement() throws IOException {
      return this.connection instanceof HTTPRequestImpl.DummyHttpURLConnection ? null : JsonParserAdapter.parseString(readLines(this.connection.getInputStream()));
   }

   @Nonnull
   public CompletableFuture<JsonElement> readElementAsync() {
      return CompletableFuture.supplyAsync(() -> {
         try {
            return this.readElement();
         } catch (IOException var2) {
            return null;
         }
      });
   }

   public int statusCode() throws IOException {
      return this.connection.getResponseCode();
   }

   @Nonnull
   public String statusMessage() throws IOException {
      return this.connection.getResponseMessage();
   }

   static final class BuilderImpl implements HTTPRequest.Builder {
      private final Map<String, String> headers = new HashMap();
      private URL url;
      private boolean get = false;

      public HTTPRequest.Builder url(@Nonnull String url) {
         try {
            this.url = new URL(url);
            return this;
         } catch (MalformedURLException var3) {
            throw new RuntimeException(var3);
         }
      }

      public HTTPRequest.Builder get() {
         this.get = true;
         return this;
      }

      public HTTPRequest.Builder header(@Nonnull String key, @Nonnull String value) {
         this.headers.put(key, value);
         return this;
      }

      @Nonnull
      public HTTPRequest build() {
         return new HTTPRequestImpl(this);
      }
   }

   private static final class DummyHttpURLConnection extends HttpURLConnection {
      static final HTTPRequestImpl.DummyHttpURLConnection INSTANCE = new HTTPRequestImpl.DummyHttpURLConnection();
      private static final InputStream NULL_INPUT_STREAM = new InputStream() {
         public int read() throws IOException {
            return -1;
         }
      };

      private DummyHttpURLConnection() {
         super((URL)null);
      }

      public void disconnect() {
      }

      public boolean usingProxy() {
         return false;
      }

      public void connect() throws IOException {
      }

      public InputStream getInputStream() throws IOException {
         return NULL_INPUT_STREAM;
      }

      public int getResponseCode() throws IOException {
         return 404;
      }

      public String getResponseMessage() throws IOException {
         return "???";
      }
   }
}

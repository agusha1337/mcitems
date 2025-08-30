package su.mcdev.chunk_loader;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.EventLoop;
import io.netty.channel.AbstractChannel.AbstractUnsafe;
import java.net.SocketAddress;

public final class DummyChannel extends AbstractChannel {
   public DummyChannel() {
      super((Channel)null);
   }

   protected AbstractUnsafe newUnsafe() {
      return null;
   }

   protected boolean isCompatible(EventLoop eventLoop) {
      return false;
   }

   protected SocketAddress localAddress0() {
      return null;
   }

   protected SocketAddress remoteAddress0() {
      return null;
   }

   protected void doBind(SocketAddress socketAddress) {
   }

   protected void doDisconnect() {
   }

   protected void doClose() {
   }

   protected void doBeginRead() {
   }

   protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) {
   }

   public ChannelConfig config() {
      return null;
   }

   public boolean isOpen() {
      return false;
   }

   public boolean isActive() {
      return false;
   }

   public ChannelMetadata metadata() {
      return null;
   }
}

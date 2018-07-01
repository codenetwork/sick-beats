package au.org.codenetwork.sickbeats.socket;

import au.org.codenetwork.sickbeats.SickBeats;
import au.org.codenetwork.sickbeats.StreamingService;
import au.org.codenetwork.sickbeats.Track;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class SocketHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Gson gson = new GsonBuilder().create();

    private SickBeats sickBeats;
    private ChannelHandlerContext handlerContext;

    public SocketHandler(SickBeats sickBeats) {
        this.sickBeats = sickBeats;
    }

    public void sendMessage(String msgToSend) {
        if (handlerContext != null) {
            var cf = handlerContext.writeAndFlush(Unpooled.copiedBuffer(msgToSend, CharsetUtil.UTF_8));
            if (!cf.isSuccess()) {
                System.out.println("Failed to send: " + cf.cause());
            }
        } else {
            System.out.println("Failed to send message. No context available.");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.handlerContext = ctx;

        var connectMessage = new JsonObject();
        connectMessage.addProperty("type", "connect");
        connectMessage.addProperty("clientId", this.sickBeats.getConfiguration().getClientId());
        connectMessage.addProperty("serverSecret", this.sickBeats.getConfiguration().getServerSecret());
        sendMessage(gson.toJson(connectMessage));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        var message = msg.toString(Charset.forName("UTF-8"));
        System.out.println(message);
        var object = gson.fromJson(message, JsonElement.class).getAsJsonObject();
        if (object.get("type") == null) {
            System.out.println("Malformed packet");
            ctx.close();
            return;
        }
        var type = object.get("type").getAsString();
        switch (type) {
            case "close":
                System.out.println("Connection closed by remote host");
                ctx.close();
                return;
            case "platform":
                var platform = object.get("platform").getAsString();
                this.sickBeats.initialiseInterface(StreamingService.valueOf(platform.toUpperCase()));
                break;
            case "play":
                var identifier = object.get("identifier").getAsString();
                var title = object.get("title").getAsString();
                var artist = object.get("artist").getAsString();
                var runtime = object.get("runtime").getAsLong();
                this.sickBeats.getInterface().playTrack(new Track(identifier, title, artist, runtime, Lists.newArrayList()));
                break;
        }
    }
}

package au.org.codenetwork.sickbeats.socket;

import au.org.codenetwork.sickbeats.SickBeats;
import au.org.codenetwork.sickbeats.StreamingService;
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
            ChannelFuture cf = handlerContext.writeAndFlush(Unpooled.copiedBuffer(msgToSend, CharsetUtil.UTF_8));
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

        JsonObject connectMessage = new JsonObject();
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
        String message = msg.toString(Charset.forName("UTF-8"));
        System.out.println(message);
        JsonObject object = gson.fromJson(message, JsonElement.class).getAsJsonObject();
        if (object.get("type") == null) {
            System.out.println("Malformed packet");
            ctx.close();
            return;
        }
        String type = object.get("type").getAsString();
        switch (type) {
            case "close":
                System.out.println("Connection closed by remote host");
                ctx.close();
                return;
            case "platform":
                String platform = object.get("platform").getAsString();
                this.sickBeats.initialiseInterface(StreamingService.valueOf(platform.toUpperCase()));
                break;
        }
    }
}

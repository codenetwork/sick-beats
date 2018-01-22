package au.org.codenetwork.sickbeats.socket;

import au.org.codenetwork.sickbeats.SickBeats;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class SocketHandler extends SimpleChannelInboundHandler<Packet> {
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

        sendMessage("{\"message\":\"test message\"}");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        String message = new String(msg.message, Charset.forName("UTF-8"));
        JsonElement element = gson.fromJson(message, JsonElement.class);

    }
}

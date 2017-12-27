package au.org.codenetwork.sickbeats.socket;

import au.org.codenetwork.sickbeats.SickBeats;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class SocketHandler extends SimpleChannelInboundHandler<Packet> {
    private static final Gson gson = new GsonBuilder().create();

    private SickBeats sickBeats;

    public SocketHandler(SickBeats sickBeats) {
        this.sickBeats = sickBeats;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        String message = new String(msg.message, Charset.forName("UTF-8"));
        JsonElement element = gson.fromJson(message, JsonElement.class);

    }
}

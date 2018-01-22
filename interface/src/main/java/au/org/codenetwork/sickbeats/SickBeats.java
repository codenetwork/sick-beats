package au.org.codenetwork.sickbeats;

import au.org.codenetwork.sickbeats.socket.SocketHandler;
import au.org.codenetwork.sickbeats.spotify.SpotifyInterface;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SickBeats {
    private Configuration configuration;
    private BaseInterface playInterface;
    private StreamingService streamingService = StreamingService.NONE;

    private SocketHandler socketHandler;

    public SickBeats() {
        this.configuration = new Configuration();
        this.configuration.load();

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final SickBeats self = this;
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(socketHandler = new SocketHandler(self));
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(configuration.getHost(), configuration.getHostPort()).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public BaseInterface getInterface() {
        if (this.playInterface == null) {
            throw new IllegalStateException("Haven't received valid branding yet.");
        }
        return this.playInterface;
    }

    public void initialiseInterface(StreamingService service) {
        if (service == this.streamingService) {
            return; // Already setup.
        }
        if (this.playInterface != null) {
            this.playInterface.dispose();
        }
        this.streamingService = service;
        switch (service) {
            case NONE:
                this.playInterface = null;
                break;
            case SPOTIFY:
                this.playInterface = new SpotifyInterface();
                break;
        }
    }

    public static void main(String[] args) {
        SickBeats sickBeats = new SickBeats();
//        sickBeats.initialiseInterface(StreamingService.SPOTIFY);
//        sickBeats.getInterface().playTrack(new Track("spotify:track:66L8V84XCjOpgjoLuI6GC7", "", "", 0, List.of("")));
    }
}

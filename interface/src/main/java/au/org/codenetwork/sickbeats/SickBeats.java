package au.org.codenetwork.sickbeats;

import au.org.codenetwork.sickbeats.spotify.SpotifyInterface;

import java.util.List;

public class SickBeats {
    private Configuration configuration;
    private BaseInterface playInterface;

    public SickBeats() {
        this.configuration = new Configuration();
        this.configuration.load();
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

    public static void main(String[] args) {
        SickBeats sickBeats = new SickBeats();
        sickBeats.playInterface = new SpotifyInterface();
        sickBeats.getInterface().playTrack(new Track("spotify:track:66L8V84XCjOpgjoLuI6GC7", "", "", 0, List.of("")));
    }
}

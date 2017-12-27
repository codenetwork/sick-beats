package au.org.codenetwork.sickbeats.spotify;

import au.org.codenetwork.sickbeats.Configuration;
import au.org.codenetwork.sickbeats.SickBeats;
import au.org.codenetwork.sickbeats.Track;

import java.util.List;

public class SpotifySickBeats implements SickBeats<SpotifyInterface> {
    private SpotifyInterface spotifyInterface;
    private Configuration configuration;

    public SpotifySickBeats() {
        this.configuration = new Configuration();
        this.configuration.load();
    }

    @Override
    public SpotifyInterface getInterface() {
        if (this.spotifyInterface == null) {
            this.spotifyInterface = new SpotifyInterface();
        }
        return this.spotifyInterface;
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    public static void main(String[] args) {
        SpotifySickBeats spotifySickBeats = new SpotifySickBeats();
        spotifySickBeats.getInterface().playTrack(new Track("spotify:track:66L8V84XCjOpgjoLuI6GC7", "", "", 0, List.of("")));
    }
}

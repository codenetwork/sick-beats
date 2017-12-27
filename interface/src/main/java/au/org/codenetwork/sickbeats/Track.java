package au.org.codenetwork.sickbeats;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Represents a playable music track.
 */
public class Track {
    private String title;
    private String artist;
    private long runtime;
    private List<String> tags;
    /**
    This is used to play the song. May be a URL.
     */
    private String identifier;

    public Track(String identifier, String title, String artist, long runtime, List<String> tags) {
        this.identifier = identifier;
        this.title = title;
        this.artist = artist;
        this.runtime = runtime;
        this.tags = tags;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public long getRuntime() {
        return this.runtime;
    }

    public ImmutableList<String> getTags() {
        return ImmutableList.copyOf(this.tags);
    }
}

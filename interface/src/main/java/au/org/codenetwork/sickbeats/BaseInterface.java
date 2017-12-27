package au.org.codenetwork.sickbeats;

public interface BaseInterface {

    boolean playTrack(Track track);

    default void dispose() {}
}

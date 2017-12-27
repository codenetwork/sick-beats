package au.org.codenetwork.sickbeats;

public interface SickBeats<T extends BaseInterface> {

    Configuration getConfiguration();

    T getInterface();
}

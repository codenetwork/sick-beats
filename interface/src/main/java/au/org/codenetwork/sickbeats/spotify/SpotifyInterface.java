package au.org.codenetwork.sickbeats.spotify;

import au.org.codenetwork.sickbeats.BaseInterface;
import au.org.codenetwork.sickbeats.Track;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpotifyInterface implements BaseInterface {

    @Override
    public boolean playTrack(Track track) {
        try {
            String[] args = {
                    "osascript",
                    "-e",
                    "tell app \"Spotify\" to play track \"{}\"".replace("{}", track.getIdentifier())
            };
            Process process = Runtime.getRuntime().exec(args);
            process.waitFor();
            if (process.exitValue() != 0) {
                InputStreamReader isr = new InputStreamReader(process.getErrorStream());
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                return false;
            }
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
package au.org.codenetwork.sickbeats.spotify;

import au.org.codenetwork.sickbeats.BaseInterface;
import au.org.codenetwork.sickbeats.Track;
import au.org.codenetwork.sickbeats.util.PlatformUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpotifyInterface implements BaseInterface {

    @Override
    public boolean playTrack(Track track) {
        try {
            String[] args = null;
            switch (PlatformUtil.getOS()) {
                case WINDOWS:
                    args = new String[] {
                            "explorer",
                            track.getIdentifier()
                    };
                    break;
                case MACOS:
                    args = new String[] {
                            "osascript",
                            "-e",
                            "tell app \"Spotify\" to play track \"{}\"".replace("{}", track.getIdentifier())
                    };
                    break;
                case LINUX:
                    args = new String[] {
                            "spotify",
                            track.getIdentifier()
                    };
                    break;
            }
            var process = Runtime.getRuntime().exec(args);
            process.waitFor();
            if (process.exitValue() != 0) {
                var isr = new InputStreamReader(process.getErrorStream());
                var br = new BufferedReader(isr);
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
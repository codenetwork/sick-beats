package au.org.codenetwork.sickbeats.util;

public class PlatformUtil {
    private static final String OS_NAME = System.getProperty("os.name");

    public enum OSType {
        WINDOWS,
        MACOS,
        LINUX
    }

    public static OSType getOS() {
        if (OS_NAME.contains("Windows")) {
            return OSType.WINDOWS;
        } else if (OS_NAME.contains("Mac") || OS_NAME.contains("Darwin")) {
            return OSType.MACOS;
        } else {
            // If it's not Windows or macOS, assume Linux-like
            return OSType.LINUX;
        }
    }
}

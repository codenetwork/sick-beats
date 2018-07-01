package au.org.codenetwork.sickbeats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class Configuration {
    private static final Gson gson = new GsonBuilder().create();
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_HOST_PORT = 57438;

    private String serverSecret; // Basically App ID - Tells it which instance to connect to.
    private String clientId;

    private String host = DEFAULT_HOST;
    private int hostPort = DEFAULT_HOST_PORT;

    public void load() {
        var file = new File("config.json");
        if (!file.exists()) {
            clientId = UUID.randomUUID().toString();
            save();
        }

        try (var reader = new BufferedReader(new FileReader(file))) {
            var object = gson.fromJson(reader, JsonObject.class);
            this.clientId = object.get("clientId").getAsString();
            var serverSecretElement = object.get("serverSecret");
            this.serverSecret = serverSecretElement == null ? null : serverSecretElement.getAsString(); // Please add null coalescing operator to Java
            var hostElement = object.get("host");
            this.host = hostElement == null ? DEFAULT_HOST : hostElement.getAsString(); // Please add null coalescing operator to Java
            var hostPortElement = object.get("hostPort");
            this.hostPort = hostPortElement == null ? DEFAULT_HOST_PORT : hostPortElement.getAsInt(); // Please add null coalescing operator to Java
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        var object = new JsonObject();
        object.addProperty("serverSecret", serverSecret);
        object.addProperty("clientId", clientId);
        if (!host.equals(DEFAULT_HOST)) {
            object.addProperty("host", host);
        }
        if (hostPort != DEFAULT_HOST_PORT) {
            object.addProperty("hostPort", hostPort);
        }
        var file = new File("config.json");
        try (var writer = new PrintWriter(file)) {
            gson.toJson(object, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getServerSecret() {
        return this.serverSecret;
    }

    public String getHost() {
        return this.host;
    }

    public int getHostPort() {
        return this.hostPort;
    }
}

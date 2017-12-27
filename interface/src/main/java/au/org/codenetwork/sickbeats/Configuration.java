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

    private String serverSecret; // Basically App ID - Tells it which instance to connect to.
    private String clientId;

    public void load() {
        File file = new File("config.json");
        if (!file.exists()) {
            clientId = UUID.randomUUID().toString();
            save();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            JsonObject object = gson.fromJson(reader, JsonObject.class);
            this.clientId = object.get("clientId").getAsString();
            JsonElement serverSecretElement = object.get("serverSecret");
            this.serverSecret = serverSecretElement == null ? null : serverSecretElement.getAsString(); // Please add null coalescing operator to Java
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        JsonObject object = new JsonObject();
        object.addProperty("serverSecret", serverSecret);
        object.addProperty("clientId", clientId);
        File file = new File("config.json");
        try (PrintWriter writer = new PrintWriter(file)) {
            gson.toJson(object, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

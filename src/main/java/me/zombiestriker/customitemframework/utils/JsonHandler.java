package me.zombiestriker.customitemframework.utils;

import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHandler {

    private JsonWriter writer;

    public JsonHandler(File file) {
        try {
            writer = new JsonWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJsonStream(HashMap<String, Object> messages) throws IOException {
        writer.setIndent("    ");
        writeMessagesArray(messages);
        writer.close();
    }

    public void writeMessagesArray(HashMap<String, Object> messages) throws IOException {
        writer.beginObject();
        for (Map.Entry<String, Object> message : messages.entrySet()) {
            writeObjects(message);
        }
        writer.endObject();
    }

    private void writeObjects(Map.Entry<String, Object> message) throws IOException {
        System.out.println(message.getKey() + "  " + message.getValue());
        if (message.getValue() instanceof String) {
            writer.name(message.getKey()).value((String) message.getValue());
        } else if (message.getValue() instanceof Integer) {
            writer.name(message.getKey()).value((Integer) message.getValue());
        } else if (message.getValue() instanceof Boolean) {
            writer.name(message.getKey()).value((Boolean) message.getValue());
        } else if (message.getValue() instanceof List) {
            writer.name(message.getKey());
            writer.beginArray();
            for (Object val : (List<Object>) message.getValue()) {
                if (val instanceof HashMap) {
                    writeMessagesArray((HashMap<String, Object>) val);
                } else if (val instanceof Boolean) {
                    writer.value((Boolean) val);
                } else if (val instanceof Integer) {
                    writer.value((Integer) val);
                } else {
                    writer.value((String) val);
                }
            }
            writer.endArray();
        } else if (message.getValue() instanceof HashMap) {
            writer.name(message.getKey());
            writeMessagesArray((HashMap<String, Object>) message.getValue());

        }
    }
    /*
    public void writeMessage(JsonWriter writer, String message) throws IOException {
        writer.beginObject();
        writer.name("id").value(message.getId());
        writer.name("text").value(message.getText());
        if (message.getGeo() != null) {
            writer.name("geo");
            writeDoublesArray(writer, message.getGeo());
        } else {
            writer.name("geo").nullValue();
        }
        writer.name("user");
        writeUser(writer, message.getUser());
        writer.endObject();
    }

    public void writeUser(JsonWriter writer, User user) throws IOException {
        writer.beginObject();
        writer.name("name").value(user.getName());
        writer.name("followers_count").value(user.getFollowersCount());
        writer.endObject();
    }
    */

    public void writeDoublesArray(JsonWriter writer, List<Double> doubles) throws IOException {
        writer.beginArray();
        for (Double value : doubles) {
            writer.value(value);
        }
        writer.endArray();
    }
}

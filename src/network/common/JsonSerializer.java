package network.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonSerializer {
    private static Gson GSON = new Gson();

    public static JsonObject toJson(Message message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", message.getClass().getName());
        jsonObject.addProperty("data", GSON.toJson(message));
        return jsonObject;
    }

    public static Message fromJson(JsonObject jsonObject){
        String type = jsonObject.get("type").getAsString();
        String data = jsonObject.get("data").getAsString();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (Message) GSON.fromJson(data, clazz);
    }

}

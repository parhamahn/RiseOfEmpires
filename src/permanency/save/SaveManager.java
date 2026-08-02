package permanency.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import model.state.GameState;
import permanency.SlotID;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.*;

public class SaveManager {

    public static final int VERSION = 1;
    public static final Path SAVE_DIR = Paths.get(System.getProperty("user.dir"), "saves");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void writeMetadata(MetaDataSave metaDataSave) throws Exception {
        try {
            Files.createDirectories(SAVE_DIR);
            Path target = SAVE_DIR.resolve(metaDataSave.getSlotId() + ".meta.json");
            Path tmp = target.resolveSibling(target.getFileName() + ".tmp");

            try (JsonWriter writer = GSON.newJsonWriter(Files.newBufferedWriter(tmp))) {
                GSON.toJson(metaDataSave, MetaDataSave.class, writer);
            }

            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new Exception("Failed to write save metadata for slot " + metaDataSave.getSlotId(), e);
        }
    }

    public static void writeData(GameState gameState, MetaDataSave metaDataSave) throws Exception {
        try {
            Files.createDirectories(SAVE_DIR);
            Path target = SAVE_DIR.resolve(metaDataSave.getDataFileName());
            Path tmp = target.resolveSibling(target.getFileName() + ".tmp");

            try(OutputStream os = Files.newOutputStream(tmp)) {
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(gameState);
            }
            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        }catch (IOException e) {
            throw new Exception("Failed to write save data for slot " + metaDataSave.getDataFileName(), e);
        }
    }
}
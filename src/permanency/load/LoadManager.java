package permanency.load;

import com.google.gson.Gson;
import model.state.GameState;
import permanency.save.MetaDataSave;
import permanency.save.SaveManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LoadManager {

    private static final Gson GSON = new Gson();

    public static boolean isTooNew(MetaDataSave metaDataSave) {
        return metaDataSave != null && metaDataSave.getSaveVersion() > SaveManager.VERSION;
    }

    public static boolean needsMigration(MetaDataSave metaDataSave) {
        return metaDataSave != null && metaDataSave.getSaveVersion() < SaveManager.VERSION;
    }

    public static List<MetaDataSave> loadAllMetaData() throws Exception{

        List<MetaDataSave> metaDataSaves = new ArrayList<MetaDataSave>();

        try{

            if(!Files.isDirectory(SaveManager.SAVE_DIR)){
                return metaDataSaves;
            }else{
                try(DirectoryStream<Path> stream = Files.newDirectoryStream(SaveManager.SAVE_DIR, "*.meta.json")){
                    for(Path path : stream){
                        try(BufferedReader reader = Files.newBufferedReader(path)){
                            metaDataSaves.add(GSON.fromJson(reader, MetaDataSave.class));
                        }
                    }
                }
            }

        }catch(IOException e){
            throw new Exception("Failed to load meta data from "+ SaveManager.SAVE_DIR, e);
        }
        return metaDataSaves;
    }

    public static GameState loadData(MetaDataSave metaDataSave) throws Exception{
        Path source = SaveManager.SAVE_DIR.resolve(metaDataSave.getDataFileName());
        try(InputStream inputStream = Files.newInputStream(source);){
            ObjectInputStream reader = new ObjectInputStream(inputStream);
            Object readObject = reader.readObject();
            if(!(readObject instanceof GameState gameState)){
                throw new Exception("Save data in " + source + " is corrupted: not a valid game state");
            }
            return gameState;
        }catch(IOException | ClassNotFoundException e){
            throw new Exception("Save data in " + source + " is corrupted", e);
        }
    }

    public static void deleteData(MetaDataSave metaDataSave) throws Exception{
        Path source = SaveManager.SAVE_DIR.resolve(metaDataSave.getDataFileName());
        Files.deleteIfExists(source);
        Files.deleteIfExists(SaveManager.SAVE_DIR.resolve(metaDataSave.getSlotId()+".meta.json"));
    }

}

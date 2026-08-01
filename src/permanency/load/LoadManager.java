package permanency.load;

import com.google.gson.Gson;
import permanency.save.MetaDataSave;
import permanency.save.SaveManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LoadManager {

    private static final Gson GSON = new Gson();

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


}

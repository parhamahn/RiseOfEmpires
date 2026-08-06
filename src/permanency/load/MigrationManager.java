package permanency.load;

import permanency.save.MetaDataSave;
import permanency.save.SaveManager;

public class MigrationManager {

    public static boolean migrate(MetaDataSave metaDataSave) throws Exception {
        if (!LoadManager.needsMigration(metaDataSave)) {
            return false;
        }

        metaDataSave.upgradeToCurrentVersion();
        SaveManager.writeMetadata(metaDataSave);
        return true;
    }
}

package permanency.load;

import permanency.save.MetaDataSave;
import permanency.save.SaveManager;

public class MigrationManager {

    private static final int OLDEST_MIGRATABLE_VERSION = 2;

    public static boolean migrate(MetaDataSave metaDataSave) throws Exception {
        if (!LoadManager.needsMigration(metaDataSave)) {
            return false;
        }
        if (metaDataSave.getSaveVersion() < OLDEST_MIGRATABLE_VERSION) {
            throw new Exception("Saves from before version " + OLDEST_MIGRATABLE_VERSION
                    + " are incompatible with the multiplayer refactor. Please start a new game.");
        }
        metaDataSave.upgradeToCurrentVersion();
        SaveManager.writeMetadata(metaDataSave);
        return true;
    }
}

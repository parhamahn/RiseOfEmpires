package permanency.save;

import permanency.SlotID;

public class MetaDataSave {

    SlotID slotId;
    int saveVersion;
    String saveName;
    int turnNumber;
    String season;
    String savedAt;
    String townHallLevel;
    String summary;
    String dataFileName;

    public MetaDataSave(SlotID slotId, int saveVersion, String saveName, int turnNumber, String season, String townHallLevel, String summary, String dataFileName, String savedAt) {
        this.slotId = slotId;
        this.saveVersion = saveVersion;
        this.saveName = saveName;
        this.turnNumber = turnNumber;
        this.season = season;
        this.townHallLevel = townHallLevel;
        this.summary = summary;
        this.dataFileName = dataFileName;
        this.savedAt = savedAt;
    }

    public SlotID getSlotId() {
        return slotId;
    }

    public int getSaveVersion() {
        return saveVersion;
    }

    public String getSaveName() {
        return saveName;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public String getSeason() {
        return season;
    }

    public String getSavedAt() {
        return savedAt;
    }

    public String getTownHallLevel() {
        return townHallLevel;
    }

    public String getSummary() {
        return summary;
    }

    public String getDataFileName() {
        return dataFileName;
    }

}

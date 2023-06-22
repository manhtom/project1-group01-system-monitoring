package resource.storage.Win;

import java.util.List;

import oshi.hardware.HWDiskStore;
import resource.storage.Partition;
import resource.storage.Storage;
import resource.storage.Volume;

public class WinStorage extends Storage{
    private List<WinPartition> pw;
    public WinStorage(HWDiskStore d, List<Volume> listVol){
        super(d,listVol);
    }

}

package resource.storage.Linux;

import java.util.List;

import oshi.hardware.HWDiskStore;
import resource.storage.Storage;
import resource.storage.Volume;

public class StorageLinux extends Storage{
    public StorageLinux(HWDiskStore d, List<Volume> listVol){
        super(d,listVol);
    }
    
}

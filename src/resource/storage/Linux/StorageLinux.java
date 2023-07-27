package resource.storage.Linux;

import java.util.List;

import oshi.software.os.OSFileStore;
import resource.storage.Volume;

public class StorageLinux extends VolumeLinux{
    public StorageLinux(OSFileStore d, List<Volume> listVolume){
        super(d,listVolume);
    }


    
}

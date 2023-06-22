package resource.storage.Linux;

import oshi.software.os.OSFileStore;
import resource.storage.Volume;

public class VolumeLinux extends Volume {
    public VolumeLinux(OSFileStore o){
        super(o);
    }

}

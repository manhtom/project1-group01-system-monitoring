package resource.storage.Linux;

import java.util.List;

import oshi.software.os.OSFileStore;
import resource.storage.Volume;

public class VolumeLinux extends Volume {

    public VolumeLinux(OSFileStore o, List<Volume> listVol){
        super(o);
        this.name = o.getName();
        this.fileSys = o.getType();
        this.volume = o.getVolume();
        this.spaceAvailable = o.getUsableSpace();
        this.spaceTotal = o.getTotalSpace();
    }




}

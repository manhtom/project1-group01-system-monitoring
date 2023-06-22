package system;

import java.util.ArrayList;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.software.os.OSFileStore;
import resource.storage.Partition;
import resource.storage.Volume;

public class SystemIO {
    public List<Volume> listVolume; // ignore this in normal cases - only used for disk object init
    public SystemIO(SystemInfo s, OS o) {
        listVolume = new ArrayList<Volume>();
        for (OSFileStore of : o.getOS().getFileSystem().getFileStores()) {
            listVolume.add(new Volume(of));
        }
    }

}

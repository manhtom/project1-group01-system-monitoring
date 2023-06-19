package system;

import java.util.ArrayList;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.software.os.OSFileStore;
import resource.storage.*;
import system.software.OS;

public class SystemIO {
    public List<Storage> listDisk;
    public List<Volume> listVolume; // ignore this in normal cases - only used for disk object init

    public SystemIO(SystemInfo s, OS o) {
        listVolume = new ArrayList<Volume>();
        listDisk = new ArrayList<Storage>();
        List<HWDiskStore> diskStore = s.getHardware().getDiskStores();
        for (OSFileStore of : o.getOS().getFileSystem().getFileStores()) {
            listVolume.add(new Volume(of));
        }

        for (HWDiskStore i : diskStore) {
            listDisk.add(new Storage(i, listVolume));
            }
        }

}

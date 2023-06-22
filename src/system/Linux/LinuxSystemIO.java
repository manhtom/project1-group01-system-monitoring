package system.Linux;

import java.util.ArrayList;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.software.os.OSFileStore;
import resource.storage.Linux.StorageLinux;
import resource.storage.Linux.VolumeLinux;
import system.OS;
import system.SystemIO;

public class LinuxSystemIO extends SystemIO {
    public List<StorageLinux> listDiskLinux;

    public LinuxSystemIO(SystemInfo s, OS os) {
        super(s,os);
        listDiskLinux = new ArrayList<StorageLinux>();
        List<HWDiskStore> diskStore = s.getHardware().getDiskStores();
        for (HWDiskStore i: diskStore){
            listDiskLinux.add(new StorageLinux(i, listVolume));
        }
    }

}

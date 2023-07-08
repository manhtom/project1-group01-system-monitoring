package system.Linux;

import java.util.ArrayList;
import java.util.List;

import oshi.SystemInfo;
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
        List<OSFileStore> osFileStores = s.getOperatingSystem().getFileSystem().getFileStores();
        for (OSFileStore i: osFileStores){
            listDiskLinux.add(new StorageLinux(i, listVolume));
        }
    }

}

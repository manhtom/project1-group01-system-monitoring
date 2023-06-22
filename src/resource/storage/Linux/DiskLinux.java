package resource.storage.Linux;

import java.util.List;

import oshi.hardware.HWDiskStore;
import resource.storage.Disk;
import resource.storage.Volume;

public class DiskLinux extends Disk {
    private long spaceAvailableLinux;
    public DiskLinux(HWDiskStore d, List<Volume> list) {
        super(d, list);
        //TODO Auto-generated constructor stub
    }
    @Override
    public void updateSpace(List<Volume> list){
        for (Volume v: list){
            spaceAvailableLinux = v.getSpaceAvailable();
        }
    }

    public long getSpaceAvailable(){
        return spaceAvailableLinux;
    }


}

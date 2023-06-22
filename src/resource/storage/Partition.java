package resource.storage;

import java.util.List;

import oshi.hardware.HWPartition;
import oshi.software.os.OSFileStore;

public class Partition{
    private List<Volume> listvol;    
    private String mountPoint;
    private HWPartition part;
    private long freeSpace;
    private String uuid;
    public Partition(HWPartition p, List<Volume> listvol) {
        this.part = p;
        mountPoint = p.getMountPoint();
        this.listvol = listvol;
    }
    public String getMountPoint() {
        return mountPoint;
    }

    public HWPartition getPartition() {
        return part;
    }


    public void updateSpace(List<Volume> listVol){
        for (Volume v: listVol){
            this.freeSpace = v.getSpaceAvailable();
            this.uuid = v.getUUID();
        }
    }
    public double getPercentageFreeSpace(){
        return ((double)freeSpace / (double)getPartition().getSize());
    }

    public long getFreeSpace(){
        return freeSpace;
    }

    public String getUUID(){
        return uuid;
    }

}

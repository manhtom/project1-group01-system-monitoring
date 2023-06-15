package resource.storage;

import oshi.hardware.HWPartition;

public class Partition {
        
    private String mountPoint;
    private HWPartition part;

    public Partition(HWPartition p) {
        this.part = p;
        mountPoint = p.getMountPoint();
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public HWPartition getPartition() {
        return part;
    }
}

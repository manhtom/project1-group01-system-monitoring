package resource.storage.Linux;

import java.util.List;

import oshi.hardware.HWPartition;
import resource.storage.Partition;
import resource.storage.Volume;


public class PartitionLinux extends Partition{
    private String mountPoint;
    private HWPartition part;
    public PartitionLinux(HWPartition p, List<Volume> listvol){
        super(p, listvol);
        this.mountPoint = p.getMountPoint();
    }
    public String getMountPoint() {
        return mountPoint;
    }

    public HWPartition getPartition() {
        return part;
    }
}
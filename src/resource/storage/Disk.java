package resource.storage;

import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;

import java.util.*;

public class Disk {
    private String name;
    private HWDiskStore disk;
    private long size;
    private long spaceTotal;
    private long spaceAvailable;
    private List<Volume> listVol;

    void initVolume(List<Volume> listVol){
        this.listVol = new ArrayList<Volume>(0);
        for (HWPartition p : disk.getPartitions()) {

            for (Volume v : listVol) {

                if (p.getMountPoint().equals(v.getMountPoint())) {
                    this.listVol.add(v);
                }
            }
        }
    }  
    
    public Disk(HWDiskStore d, List<Volume> list){
        this.disk = d;
        this.name = disk.getModel();
        this.size = disk.getSize();
        initVolume(list);
        updateSpace(this.listVol);
    }

    public void updateSpace(List<Volume> list) {
        spaceTotal = 0;
        spaceAvailable = 0;
        for (Volume v : list) {
            spaceAvailable += v.getSpaceAvailable();
        }
    }

    public HWDiskStore getDisk() {
        return disk;
    }

    public long getSize(){
        return (size);
    }

    public long getSpaceAvailable(){
        return (spaceAvailable);
    }

    public String getName(){
        return name;
    }
}

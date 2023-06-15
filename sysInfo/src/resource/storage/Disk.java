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
        listVol = new ArrayList<Volume>(0);
        for (HWPartition p : disk.getPartitions()) {
            for (Volume v : listVol) {
                if (p.getUuid().equals(v.getUUID())) {
                    listVol.add(v);
                }
            }
        }
    }  
    public Disk(HWDiskStore d, List<Volume> listVol){
        this.disk = d;
        this.name = disk.getName();
        this.size = disk.getSize();
        initVolume(listVol);
        updateSpace(this.listVol);
    }

    public void updateSpace(List<Volume> listVol) {
        spaceTotal = 0;
        spaceAvailable = 0;
        for (Volume v : listVol) {
            spaceTotal += v.getSpaceTotal();
            spaceAvailable += v.getSpaceAvailable();
        }
    }

    public HWDiskStore getDisk() {
        return disk;
    }

    public long getSpaceTotal(){
        return (spaceTotal);
    }

    public long getSpaceAvailable(){
        return (spaceAvailable);
    }

    public String getName(){
        return name;
    }
}

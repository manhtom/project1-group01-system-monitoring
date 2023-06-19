package resource.storage;

import oshi.hardware.HWDiskStore;

import java.util.*;

public class Storage extends Disk {
    private float totalDataRead;
    private float totalDataWrite;
    private long IORead;
    private long IOWrite;

    void updateData() {
        totalDataRead = getDisk().getReadBytes();
        totalDataWrite= getDisk().getWriteBytes();
        IORead = getDisk().getReads();
        IOWrite = getDisk().getWrites();
    }

    public Storage(HWDiskStore d, List<Volume> listVol) {
        super(d, listVol);
        totalDataRead = d.getReadBytes();
        totalDataWrite= d.getWriteBytes();
        IORead = d.getReads();
        IOWrite = d.getWrites();
    }

    public float getTotalDataRead(){
        return totalDataRead;
    }
    public float getTotalDataWritten(){
        return totalDataWrite;
    }
    public long getIORead(){
        return IORead;
    }
    public long getIOWrite(){
        return IOWrite;
    }
    
}

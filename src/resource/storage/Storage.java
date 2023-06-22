package resource.storage;

import oshi.hardware.HWDiskStore;
import resource.storage.Linux.PartitionLinux;

import java.util.*;

public class Storage extends Disk {
    private long totalDataRead;
    private long totalDataWrite;
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

    public long getTotalDataRead(){
        return totalDataRead;
    }
    public long getTotalDataWritten(){
        return totalDataWrite;
    }
    public long getIORead(){
        return IORead;
    }
    public long getIOWrite(){
        return IOWrite;
    }



}

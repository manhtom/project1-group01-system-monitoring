package system.Win;

import java.util.ArrayList;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import resource.storage.Win.WinPartition;
import resource.storage.Win.WinStorage;
import system.OS;
import system.SystemIO;

public class WinSystemIO extends SystemIO{
    public List<WinStorage> listDiskWin;
    public List<WinPartition> listPartitionWin;
    public WinSystemIO(SystemInfo s, OS os) {
        super(s,os);
        listDiskWin = new ArrayList<WinStorage>();
        listPartitionWin = new ArrayList<WinPartition>();
        List<HWDiskStore> diskStore = s.getHardware().getDiskStores();
        
        for (HWDiskStore i : diskStore) {
            listDiskWin.add(new WinStorage(i, listVolume));
            for (HWPartition j: i.getPartitions()){
                    listPartitionWin.add(new WinPartition(j, listVolume));
                }
            }
        
        }

     }

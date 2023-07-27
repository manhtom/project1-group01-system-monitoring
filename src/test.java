import oshi.hardware.HWDiskStore;

import oshi.util.FormatUtil;
import oshi.hardware.HWPartition;
import oshi.hardware.LogicalVolumeGroup;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.util.List;


import oshi.SystemInfo;

public class test {
    public static void main(String... args) {
        SystemInfo systemInfo = new SystemInfo();

        List<HWDiskStore> hwDiskStore = systemInfo.getHardware().getDiskStores();
        List<LogicalVolumeGroup> vol = systemInfo.getHardware().getLogicalVolumeGroups();

        FileSystem fileSystem = systemInfo.getOperatingSystem().getFileSystem();
        List<OSFileStore> osFileStores = fileSystem.getFileStores();
        for(HWDiskStore hw : hwDiskStore) {
            System.out.println("Name: " + hw.getName());
            System.out.println("Model: " + hw.getModel());
            System.out.println("Serial: " + hw.getSerial());
            System.out.printf("Size: %s %n" , hw.getSize() > 0 ? FormatUtil.formatBytesDecimal(hw.getSize()) : "?");
            System.out.println("Read: " + hw.getReads());
            System.out.println("Read bytes: " + FormatUtil.formatBytes(hw.getReadBytes()));
            System.out.println("Write: " + hw.getWrites());
            System.out.println("Write bytes: " + FormatUtil.formatBytes(hw.getWriteBytes()));
            System.out.printf("Transfer times : %s ms%n", hw.getTransferTime());
            System.out.println();
            for (HWPartition hp: hw.getPartitions()){
                System.out.println("Identification: "+hp.getIdentification());
                System.out.println("MountPoint: " + hp.getMountPoint());
                System.out.println("Name: " + hp.getName());
                System.out.println("Size: " + FormatUtil.formatBytes(hp.getSize()));
                System.out.println("Type: " + hp.getType());
                System.out.println("UUID: " + hp.getUuid());
                System.out.println();
            }
        }
        for (LogicalVolumeGroup v: vol){
            System.out.println("Name: " + v.getName());
            System.out.println("Physical Volume: " + v.getPhysicalVolumes());
            System.out.println("Logical Volumes: " + v.getLogicalVolumes());
        }

        for(OSFileStore fileStore : osFileStores) {
            System.out.println("Description: " + fileStore.getDescription());
            System.out.println("Label: " + fileStore.getLabel());
            System.out.println("Logical Volume: " + fileStore.getLogicalVolume());
            System.out.println("Mount: " + fileStore.getMount());
            System.out.println("Name: " + fileStore.getName());
            System.out.println("Options: " + fileStore.getOptions());
            System.out.println("Type: " + fileStore.getType());
            System.out.println("UUID: " + fileStore.getUUID());
            System.out.println("Volume: " + fileStore.getVolume());
            System.out.println("Free Space: " + FormatUtil.formatBytes(fileStore.getFreeSpace()));
            System.out.println("Total Space: " + FormatUtil.formatBytes(fileStore.getTotalSpace()));
            System.out.println("Usable Space: " + FormatUtil.formatBytes(fileStore.getUsableSpace()));
            System.out.println();
        }
    }
}

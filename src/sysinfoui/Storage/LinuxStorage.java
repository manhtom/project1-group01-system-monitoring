package sysinfoui.Storage;


import oshi.util.FormatUtil;
import resource.storage.Linux.StorageLinux;
import system.Linux.*;
public class LinuxStorage {
    private static void pressToContinue() { 
        System.out.println("Press Enter key to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
    }

    public static void showDetail(LinuxSystemIO s) {
        int k = 0;
        for (StorageLinux i: s.listDiskLinux){
            k++;
            System.out.printf("======================== Disk %d ========================%n",k);
            System.out.printf("Disk :%s%n", i.getName());
            System.out.printf("Total space: %s %n",FormatUtil.formatBytes(i.getSpaceTotal()));
            System.out.printf("Available space: %s %n",FormatUtil.formatBytes(i.getSpaceAvailable()));
            //System.out.printf("Used Space: %d bytes%n",i.getDisk());
            }
        
        pressToContinue();
        return;
        }
 
}

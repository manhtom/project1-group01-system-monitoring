package sysinfoui.Storage;

import oshi.SystemInfo;
import system.SystemIO;
import system.OS;
import system.Linux.*;
import resource.storage.Linux.*;
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
        for (resource.storage.Linux.StorageLinux i: s.listDiskLinux){
            k++;
            System.out.printf("======================== Disk %d ========================%n",k);
            System.out.printf("Disk :%s%n", i.getName());
            System.out.printf("Total space: %d bytes%n",i.getSize());
            System.out.printf("Available space: %d bytes%n",i.getSpaceAvailable());
            //System.out.printf("Used Space: %d bytes%n",i.getDisk());
            }
        
        pressToContinue();
        return;
        }
 
}

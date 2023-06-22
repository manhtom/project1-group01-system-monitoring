package sysinfoui.Storage;

import system.Win.WinSystemIO;
import resource.storage.Disk;
import sysinfoui.SysInfoUI;
public class WinStorage {
        private static void pressToContinue() { 
        System.out.println("Press Enter key to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
    }

    public static void showDetail(WinSystemIO s) {
        int k = 0;
        for (resource.storage.Win.WinStorage i: s.listDiskWin){
            k++;
            System.out.printf("======================== Disk %d ========================%n",k);
            System.out.printf("Disk: %s%n",i.getName());
            System.out.printf("Total space: "+SysInfoUI.formatBytes(i.getSize()) + "%n");
            System.out.printf("Available space: " + SysInfoUI.formatBytes(i.getSpaceAvailable()) + "%n");
            //System.out.printf("Used Space: %d bytes%n",i.get);
            }
        System.out.printf("%n");
        for (resource.storage.Win.WinPartition j: s.listPartitionWin){
            System.out.printf("Volume: %s%n",j.getMountPoint());
            System.out.printf("Physical disc: %s%n",j.getPartition().getIdentification());
            System.out.printf("Capacity: "+SysInfoUI.formatBytes(j.getPartition().getSize()) + "%n");
            System.out.printf("Free space: "+SysInfoUI.formatBytes(j.getFreeSpace()) + "%n");
            //System.out.printf("% Free: %d%n",Math.round(j.getPercentageFreeSpace()));
            System.out.printf("%n");
            //System.out.printf("Free space: %s%n",j.getPartition().)
        }
        
        pressToContinue();
        return;
        }

 
}

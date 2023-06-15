package sysinfoui;

import system.SystemIO;

public class Storage {
    private static void pressToContinue() { 
        System.out.println("Press Enter key to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
    }

    public static void showDetail(SystemIO s) {
        int k = 0;
        for (resource.storage.Storage i : s.listDisk) {
            k++;
            System.out.printf("======================== Disk %d ========================%n", k);
            System.out.printf("Disk: %s%n", i.getName());
            System.out.printf("Total space: %d bytes%n", i.getSize());
            System.out.printf("Available space: %d bytes%n", i.getSpaceAvailable());
            System.out.printf("Data read: %.2f bytes%n", i.getTotalDataRead());
            System.out.printf("Data written: %.2f bytes%n", i.getTotalDataWritten());
            System.out.printf("Read I/O operations: %d%n", i.getIORead());
            System.out.printf("Write I/O operations: %d%n", i.getIOWrite());

            System.out.println("=========================================================");
        }

        pressToContinue();
        return;
    }
}
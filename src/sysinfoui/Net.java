package sysinfoui;

import resource.network.Adapter;
import system.SystemNetwork;

public class Net {
    private static void pressToContinue() { 
        System.out.println("Press Enter key to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
    }

    public static void showDetail(SystemNetwork s) {
        int k = 0;
        for (Adapter i : s.getAdapters()) {
            k++;
            System.out.printf("======================== Adapter %d ========================%n", k);
            System.out.printf("Name: %s%n", i.getName());
            System.out.printf("Type: %s%n", i.getType());
            System.out.printf("Data received: %d bytes %n", (double)i.getDataReceived());
            System.out.printf("Data sent: %d bytes %n", (double)i.getDataSent());

            System.out.println("=========================================================");
        }

        pressToContinue();
        return;
    }
}



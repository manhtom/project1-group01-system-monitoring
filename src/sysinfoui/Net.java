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

    public static void showDetail(SystemNetwork net) {
        int k = 0;
        for (Adapter i : net.adapterList) {
            k++;
            System.out.printf("======================== Adapter %d ========================%n", k);
            System.out.printf("Name: %s%n", i.getName());
            System.out.printf("Type: %s%n", i.getType());
            System.out.printf("IPv4 Address: %s%n", i.getIPv4Address());
            System.out.printf("IPV6 Address: %s%n", i.getIPv6Address());
            System.out.printf("MAC Address: %s%n",i.getMacAddress());
            //System.out.printf(i.getIftypeOfficial()+"%n");
            System.out.printf("Data received: %d bytes %n", i.getDataReceived());
            System.out.printf("Data sent: %d bytes %n", i.getDataSent());

            System.out.println("=========================================================");
        }

        pressToContinue();
        return;
    }
}



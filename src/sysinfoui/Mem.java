package sysinfoui;

import system.SystemMemory;

public class Mem {
     private static void pressToContinue() { 
        System.out.println("Press any key to return to previous menu...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
    }

    public static void showDetail(SystemMemory m) {
        System.out.println("");
        System.out.println("================== Memory ==================");
        System.out.printf("Current memory usage: %.2f %%%n", (float)((double)m.getPhysicalUsed()/m.getPhysicalTotal())*100);
        System.out.println("");

        System.out.println("======== Real-time statistics ===========");
        System.out.printf("In use: %.2f MB %n", m.getPhysicalUsed()/((float)1000000));
        System.out.printf("Available: %.2f MB %n", m.getPhysicalAvailable()/((float)1000000));
        System.out.printf("Swap used %.2f MB %n", m.getSwapUsed()/((float)1000000));
        System.out.println("");

        System.out.println("========= Memory information ============");

        System.out.printf("Installed memory: %.2f MB%n", m.getPhysicalTotal()/((float)1000000));
        System.out.printf("Swap space total: %.2f MB%n", m.getSwapTotal()/((float)1000000));

        System.out.println("");
        
        pressToContinue();
        return;

    }
}

package sysinfoui;

import system.SystemCPU;
import system.software.*;

public class CPU {
     private static void pressToContinue() { 
        System.out.println("Press Enter key to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
    }

    public static void showDetail(SystemCPU c, OS o) {
        System.out.println("");
        System.out.println("================== CPU ==================");
        System.out.printf("Current CPU utilization: %.2f %%%n", c.getUtilization()*100);
        System.out.println("");

        System.out.println("======== Real-time statistics ===========");
        System.out.printf("Clock speed: %.2f GHz %n", c.getSpeed()/((float)1000000000));
        System.out.printf("Number of processes: %d%n", o.getnbProcess());
        System.out.printf("Number of threads %d%n", o.getnbThread());
        System.out.println("");

        System.out.println("=========== CPU information= =============");

        System.out.printf("CPU name: %s%n", c.getName());
        System.out.printf("Base speed: %.2f GHz%n", c.getFreq()/((float)1000000000));
        System.out.printf("Architecture: %s%n", c.getArch());
        System.out.printf("Sockets: %d%n", c.getPackage());
        System.out.printf("Logical processors: %d%n", c.getLogicalProcessor());
        System.out.printf("Cores: %d%n", c.getCore());
        System.out.println("");
        
        pressToContinue();
        return;

    }
}

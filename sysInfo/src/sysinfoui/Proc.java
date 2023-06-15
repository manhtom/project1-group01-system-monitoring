package sysinfoui;

import system.software.*;
import java.util.*;
import process.Process;
import process.*;

public class Proc {
    public static void showDetail(OS o) {
        System.out.println("Please enter the PID of the process you want: ");
        Scanner s = new Scanner(System.in);
        int pid = s.nextInt();
        for (Process p : o.getProcesses()) {
            if (Integer.compare(pid, p.getPID()) == 0) {
                System.out.printf("======================== PID %d ========================%n", pid);
                System.out.printf("Process name: %s%n", p.getName());
                System.out.printf("PID: %d%n", p.getPID());
                System.out.printf("Process path: %s%n", p.getPath());
                System.out.printf("Architecture: %d%n", p.getBitness());
                System.out.printf("User: %s%n", p.getUser());
                System.out.printf("Status: %s%n", p.getStatus().toString());
                System.out.printf("CPU usage: %.2f %%%n", p.getUtilization()*100);
                System.out.printf("Kernel time: %s s%n", p.getKernelTime());
                System.out.printf("User time: %s s%n", p.getUserTime());
                System.out.printf("Data Read: %d bytes%n", p.getDataRead());
                System.out.printf("Data Written: %d bytes %n", p.getDataWritten());
                System.out.printf("Virtual Memory Size: %.1f MB%n", (double)p.getVMSize()/1000000);
                System.out.printf("Working Set: %.1f MB%n", (double)p.getWorkingSet()/1000000);


                System.out.println("=========================================================");
                break;
            }
        }
    }
}

package sysinfoui;

import oshi.*;
import oshi.software.os.OSProcess;
import system.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.util.*;
import process.Process;

public class SysInfoUI {
    static SystemInfo si;
    static Sys s;
    static Scanner input;
    static int o;
    public static void main(String[] args) {
        System.out.println("Initializing...");
        si = new SystemInfo();
        s = new Sys(si);
        mainMenu();
    }

    public static String formatBytes(long bytes){
        double kilobytes = (bytes / 1024.0);
        double megabytes = (kilobytes / 1024.0);
        double gigabytes = (megabytes / 1024.0);
        if (gigabytes >= 1) return String.format("%.2f GB", gigabytes);
        else if (megabytes >= 1) return String.format("%.2f MB", megabytes);
        else if (kilobytes >= 1) return String.format("%.2f KB", kilobytes);
        else return String.format("%d",bytes);
    }

    public static void mainMenu() {
        System.out.println("");

        // for the future GUI - process objects will also be initialized - but for now it will be only initialized on demand
        
        System.out.println("================== Overview ==================");
        System.out.printf("Current CPU usage: %.2f %%%n", s.cpu.getUtilization()*100);
        System.out.printf("Memory usage: %.2f %% %n", (float)s.mem.getPhysicalUsed()/(float)s.mem.getPhysicalTotal()*100);
        System.out.println("");
        System.out.println("============= System information =============");
        System.out.printf("OS: %s%n", s.os.getVersion()); // to be filled
        System.out.printf("Architecture: %d bit %n", s.os.getBitness());
        System.out.printf("Processor: %s%n", s.cpu.getName());
        System.out.printf("Installed memory: %d MB%n", s.mem.getPhysicalTotal()/1000000);
        System.out.println("");
        System.out.println("============ System configuration ============");
        System.out.printf("System time: %s%n", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()));// to be filled
        System.out.printf("Uptime: %s s %n", s.os.getUptime()); // to be filled
        System.out.printf("Hostname: %s%n", s.os.getComputerName()); // to be filled
        System.out.printf("Current user: %s%n", s.os.getUser()); // to be filled
        System.out.println("");

        System.out.println("1. View resource details");
        System.out.println("2. View process details");
        System.out.println("0. Exit");
        System.out.println("==============================================");

        input = new Scanner(System.in);
        o = input.nextInt();
        input.nextLine();

        if (o == 1) {
            resourceMenu();
            mainMenu();
        }

        else if (o == 2) {
            processMenu();
            mainMenu();
        }

        else {
            return;
        }
    }

    public static void resourceMenu() {
        System.out.println("");
        System.out.println("================== Resources ==================");
        System.out.println("1. View CPU details");
        System.out.println("2. View memory details");
        System.out.println("3. View storage details");
        System.out.println("4. View network details");
        System.out.println("0. Return to previous menu");
        System.out.println("===============================================");

        o = input.nextInt();
        input.nextLine();

        if (o == 1) {
            CPU.showDetail(s.cpu, s.os);
        }

        else if (o == 2) {
            Mem.showDetail(s.mem);
        }

        else if (o == 3) {
            Storage.showDetail(s.io);
        }

        else if (o == 4) {
            Net.showDetail(s.net);
        }

        else {
            return;
        }
    }

    public static void processMenu() {
        System.out.println("");
        System.out.println("================== Processes ==================");
        System.out.println("1. List all the processes on the system (Process Name + ID)");
        System.out.println("2. View details about a process");
        System.out.println("0. Return to previous menu");

        o = input.nextInt();
        input.nextLine();

        if (o == 1) {
        System.out.println("");
        System.out.println("==================== List of processes ====================");
        System.out.printf("%1$-48s %2$10s%n", "Name", "PID");
        System.out.println("-----------------------------------------------------------");

            for (Process i : s.os.getProcesses()) {
                System.out.printf("%1$-48s %2$10d%n", i.getName(), i.getPID());
            }
        System.out.println("");
        System.out.println("1. View details about a process");
        System.out.println("0. Return to previous menu");  
        o = input.nextInt();
        input.nextLine();

        if (o == 1) {Proc.showDetail(s.os);}
        else {return;}
        
        }

        else if (o == 2) {
            Proc.showDetail(s.os);
        }

        else {
            return;
        }
    }
}

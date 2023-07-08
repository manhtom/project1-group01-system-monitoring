package system;

import oshi.SystemInfo;
import system.Linux.Linux;
import system.Linux.LinuxSystemIO;
import system.Win.Win;
import system.Win.WinSystemIO;


public class Sys {
    public SystemCPU cpu;
    public SystemMemory mem;
    public WinSystemIO wio;
    public LinuxSystemIO lio;
    public SystemNetwork net;
    public OS os;

    public Sys(SystemInfo s) {
        cpu = new SystemCPU(s);
        mem = new SystemMemory(s);
        if (s.getOperatingSystem().getFamily().equals("Windows")) {
            os = (OS)new Win(s);    
            wio = new WinSystemIO(s, os);
        }
        else {
            os = (OS)new Linux(s);
            lio = new LinuxSystemIO(s, os);
        }
        net = new SystemNetwork(s);
    }
}

package system;

import oshi.SystemInfo;
import system.Linux.Linux;
import system.Linux.LinuxSystemIO;


public class Sys {
    public SystemCPU cpu;
    public SystemMemory mem;
    public LinuxSystemIO io;
    public SystemNetwork net;
    public OS os;

    public Sys(SystemInfo s) {
        cpu = new SystemCPU(s);
        mem = new SystemMemory(s);
        //if (s.getOperatingSystem().getFamily().equals("Windows")) {
        //    os = (OS)new Win(s);    
        //    io = new WinSystemIO(s, os);
        //}
        //else {
            os = (OS)new Linux(s);
            io = new LinuxSystemIO(s, os);
        //}
        net = new SystemNetwork(s);
    }
}

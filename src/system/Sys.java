package system;

import oshi.SystemInfo;
import system.software.*;

public class Sys {
    public SystemCPU cpu;
    public SystemMemory mem;
    public SystemIO io;
    public SystemNetwork net;
    public OS os;

    public Sys(SystemInfo s) {
        cpu = new SystemCPU(s);
        mem = new SystemMemory(s);
        os = new OS(s);
        io = new SystemIO(s, os);
        net = new SystemNetwork(s);
    }
}

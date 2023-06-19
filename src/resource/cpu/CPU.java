package resource.cpu;

import oshi.hardware.CentralProcessor.*;
import oshi.hardware.CentralProcessor;
import oshi.SystemInfo;
import java.util.*;

public abstract class CPU {
    protected CentralProcessor cpu;
    private ProcessorIdentifier cpuid;
    private String name;
    private boolean arch;
    private long freq;
    private long nbCore;
    private long nbLogicalProcessor;
    private long nbPackage;
    List<ProcessorCache> cache;

    public CPU(SystemInfo s) {
        cpu = s.getHardware().getProcessor();
        cpuid = cpu.getProcessorIdentifier();
        name = cpuid.getName();
        arch = cpuid.isCpu64bit();
        freq = cpuid.getVendorFreq();
        cache = cpu.getProcessorCaches();
        nbCore = cpu.getPhysicalProcessorCount();
        nbPackage = cpu.getPhysicalPackageCount();
        nbLogicalProcessor = cpu.getLogicalProcessorCount();
    }

    public String getName(){
        return name;
    }

    public String getArch() {
        if (arch) {return "64-bit";}
        else {return "32-bit";}
    }

    public long getFreq() {
        return freq;
    }

    public String getCache() {
        return cache.toString();
    }

    public long getCore() {
        return nbCore;
    }

    public long getPackage() {
        return nbPackage;
    }

    public long getLogicalProcessor() {
        return nbLogicalProcessor;
    }
}
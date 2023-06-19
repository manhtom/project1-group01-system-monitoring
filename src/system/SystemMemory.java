package system;

import resource.memory.*;

import oshi.SystemInfo;

public class SystemMemory extends Memory {
    private long physicalUsed;
    private long swapUsed;

    public SystemMemory(SystemInfo s) {
        super(s);

        physicalUsed = this.getPhysicalTotal()-gm.getAvailable();
        swapUsed = vm.getSwapUsed();
    }

    public long getPhysicalUsed() {
        return (physicalUsed);  
    }

    public long getSwapUsed() {
        return (swapUsed);
    }
}
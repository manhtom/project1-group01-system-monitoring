package system;

import resource.memory.*;

import oshi.SystemInfo;

public class SystemMemory extends Memory {
    private long physicalUsed;
    private long swapUsed;
    private long swapAvailable;
    private long physicalAvailable;


    public SystemMemory(SystemInfo s) {
        super(s);
        physicalUsed = this.getPhysicalTotal()-gm.getAvailable();
        swapUsed = vm.getSwapUsed();
    }

    public long getPhysicalUsed() {
        physicalUsed = this.getPhysicalTotal()-gm.getAvailable();
        return (physicalUsed);  
    }

    public long getSwapUsed() {
        swapUsed = vm.getSwapUsed();
        return (swapUsed);
    }

    public long getSwapAvailable(){
        swapAvailable = getSwapTotal() - vm.getSwapUsed();
        return (swapAvailable);
    }

    public long getPhysicalAvailable(){
        physicalAvailable = gm.getAvailable();
        return (physicalAvailable);
    }
}
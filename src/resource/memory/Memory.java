package resource.memory;

import oshi.hardware.GlobalMemory;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.VirtualMemory;
import oshi.SystemInfo;

public abstract class Memory {
    protected GlobalMemory gm;
    protected VirtualMemory vm;
    protected PhysicalMemory pm;
    private long physicalTotal;
    private long swapTotal;
    private String type;
    private long speed;


    public Memory(SystemInfo s) {
        gm = s.getHardware().getMemory();
        vm = gm.getVirtualMemory();
        pm = gm.getPhysicalMemory().get(0); //assuming only one memory bank is installed
        physicalTotal = gm.getTotal();
        swapTotal = vm.getSwapTotal();
        type = pm.getMemoryType();
        speed = pm.getClockSpeed();
    }

    public long getPhysicalTotal() {
        physicalTotal = gm.getTotal(); 
        return (physicalTotal);
    }

    public long getSwapTotal() {
        swapTotal = vm.getSwapTotal();
        return (swapTotal);
    }

    public String getType() {
        return type;
    }

    public long getSpeed() {
        return speed;
    }
}
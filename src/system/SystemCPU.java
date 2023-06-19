package system;

import resource.cpu.*;
import oshi.SystemInfo;

public class SystemCPU extends CPU {
    double utilization;
    float speed;
    public SystemCPU(SystemInfo s) {
        super(s);
    }

    public double getUtilization() {
        utilization = cpu.getSystemCpuLoad(((long)1000));
        return utilization; // get real time cpu load per 1 sec
    }

    public float getSpeed() {
        speed = 0;
        for (long i: cpu.getCurrentFreq()) {
            speed += (float)i;
        }
        speed = speed / getLogicalProcessor();
        return speed;
    }
}
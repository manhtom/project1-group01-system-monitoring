package process;

import oshi.software.os.OSProcess;
import oshi.software.os.OSProcess.State;
import system.software.OS;

public class Process {
    OSProcess process;
    private int pid;
    private String name;
    private State status;
    private int priority;
    private String user;
    private String path;
    private double utilization;
    private int bitness;
    private long kernelTime;
    private long userTime;
    private long dataRead;
    private long dataWritten;
    private long vmSize;
    private long workingSet;

    public Process(int pid, OSProcess o) {
        this.pid = pid;
        process = o;
        name = process.getName();
        priority = process.getPriority();
        status = (process.getState());

        user = process.getUser();
        path = process.getPath();

        kernelTime = this.process.getKernelTime();
        userTime = this.process.getUserTime();
        utilization = this.process.getProcessCpuLoadBetweenTicks(o);
        bitness = this.process.getBitness();

        dataRead = this.process.getBytesRead();
        dataWritten = this.process.getBytesWritten();

        vmSize = this.process.getVirtualSize();
        workingSet = this.process.getResidentSetSize();
    }

    public int getPID() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public String getUser() {
        return user;
    }

    public String getPath() {
        return path;
    }

    public State getStatus() {
        return status;
    }

    public double getUtilization() {
        return utilization;
    }

    public int getBitness() {
        return bitness;
    }

    public long getKernelTime() {
        return kernelTime;
    }

    public long getUserTime() {
        return userTime;
    }

    public long getDataRead() {
        return dataRead;
    }

    public long getDataWritten() {
        return dataWritten;
    }

    public long getVMSize() {
        return vmSize;
    }

    public long getWorkingSet() {
        return workingSet;
    }
}
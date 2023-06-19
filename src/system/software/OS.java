package system.software;

import oshi.SystemInfo;
import oshi.software.os.*;
import resource.storage.Volume;
import process.Process;

import java.util.*;

public class OS {
    private OperatingSystem os;
    private int bitness;
    private String build;
    private String version;
    private String family;
    private long uptime;
    private OSSession user;
    private int nbProcess;
    private int nbThread;
    private String pcName;
    private List<Process> listProcesses;
    private List<Volume> listVol;

    public OS(SystemInfo s) {
        listVol = new ArrayList<Volume>();
        os = s.getOperatingSystem();
        bitness = os.getBitness();
        build = os.getVersionInfo().getBuildNumber();
        version = os.getVersionInfo().getVersion();
        family = os.getFamily();
        uptime = os.getSystemUptime();
        nbProcess = os.getProcessCount();
        nbThread = os.getThreadCount();
        user = os.getSessions().get(0);
        pcName = os.getNetworkParams().getHostName();
        for (OSFileStore o : os.getFileSystem().getFileStores()) {
            listVol.add(new Volume(o));
        }
        listProcesses = new ArrayList<Process>();
        for (OSProcess p :os.getProcesses(null, OperatingSystem.ProcessSorting.PID_ASC, 0)) {
            listProcesses.add(new Process(p.getProcessID(),p));
        }
    }

    public OperatingSystem getOS() {
        return os;
    }

    public int getBitness() {
        return bitness;
    }

    public long getUptime() {
        return uptime;
    }

    public String getUser() {
        return user.getUserName();
    }

    public String getVersion() {
        return String.format ("%s %s (Build %s)", family, version, build);
    }

    public int getnbProcess() {
        return nbProcess;
    }

    public List<Process> getProcesses() {
        return listProcesses;
    }

    public int getnbThread() {
        return nbThread;
    }

    public String getComputerName() {
        return pcName;
    }
}

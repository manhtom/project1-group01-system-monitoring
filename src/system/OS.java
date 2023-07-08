package system;

import java.util.ArrayList;
import java.util.List;

import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OSSession;
import oshi.software.os.OperatingSystem;
import process.Process;


public class OS {

    protected static String family;
    protected OperatingSystem os;
    private int bitness;
    private String build;
    private String version;
    private long uptime;
    private OSSession user;
    private int nbProcess;
    private int nbThread;
    private String pcName;
    private List<Process> listProcesses;

    public OS(SystemInfo s) {

        os = s.getOperatingSystem();
        this.family = os.getFamily();
        bitness = os.getBitness();
        build = os.getVersionInfo().getBuildNumber();
        version = os.getVersionInfo().getVersion();
        family = os.getFamily();
        uptime = os.getSystemUptime();
        nbProcess = os.getProcessCount();
        nbThread = os.getThreadCount();
        user = os.getSessions().get(2);
        pcName = os.getNetworkParams().getHostName();

        listProcesses = new ArrayList<Process>();
        for (OSProcess p :os.getProcesses(null, OperatingSystem.ProcessSorting.PID_ASC, 0)) {
            listProcesses.add(new Process(p.getProcessID(),p));
        }

    }

    public static String getFamily() {
        return family;
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
        nbProcess = os.getProcessCount();
        return nbProcess;
    }


    public List<Process> getProcesses() {
        for (OSProcess p :os.getProcesses(null, OperatingSystem.ProcessSorting.PID_ASC, 0)) {
            listProcesses.add(new Process(p.getProcessID(),p));
        }

        return listProcesses;
    }

    public int getnbThread() {
        nbThread = os.getThreadCount();

        return nbThread;
    }

    public String getComputerName() {
        return pcName;
    }
}

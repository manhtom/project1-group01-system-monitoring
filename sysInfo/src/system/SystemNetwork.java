package system;

import resource.network.Network;
import oshi.SystemInfo;

public class SystemNetwork extends Network{
    public SystemNetwork(SystemInfo s) {
        super(s);
    }
}

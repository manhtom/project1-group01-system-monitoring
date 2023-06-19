package resource.network;

import java.util.*;

import oshi.SystemInfo;
import oshi.hardware.NetworkIF;

public abstract class Network {
    List <Adapter> adapterList = new ArrayList<Adapter>();
    public Network(SystemInfo s) {
        for (NetworkIF n : s.getHardware().getNetworkIFs()) {
            adapterList.add(new Adapter(n));
        }
    }

    public List<Adapter> getAdapters() {
        return adapterList;
    }
}

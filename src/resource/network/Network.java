package resource.network;

import oshi.SystemInfo;
import oshi.hardware.NetworkIF;

import java.util.ArrayList;
import java.util.List;

public class Network {
    public List<Adapter> adapterList;
    public Network(SystemInfo s) {
        adapterList = new ArrayList<Adapter>();
        for (NetworkIF n : s.getHardware().getNetworkIFs(false)) {
            adapterList.add(new Adapter(n));
        }      
    }

    public List<Adapter> getAdapters() {
        return adapterList;
    }
}

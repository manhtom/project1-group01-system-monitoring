package resource.network;
import java.util.*;
import oshi.hardware.NetworkIF;
import system.Sys;
import oshi.SystemInfo;
public abstract class Network {
    private List<Adapter> ad;
    public Network(){
        ad = new ArrayList<>();
    }

    public void collectAdapterData(SystemInfo si){
        List<NetworkIF> networkIFs = si.getHardware().getNetworkIFs();
        
        for (NetworkIF net: networkIFs){
            Adapter adapter = new Adapter(net.getIPv4addr(), net.getName(), net.getDisplayName(), net.getBytesSent(),net.getBytesRecv());
            ad.add(adapter);
        }
    }

    public List<Adapter> getAdapters(){
        return ad;
    }
}

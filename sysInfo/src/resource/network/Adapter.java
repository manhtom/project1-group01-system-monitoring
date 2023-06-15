package resource.network;

import java.util.Arrays;
import oshi.hardware.NetworkIF;
public class Adapter {
    private String[] ipAddress;
    private String name;
    private String type;
    private float dataSent;
    private float dataReceived;
    

    public Adapter(NetworkIF net){
        this.ipAddress = net.getIPv4addr();
        this.name = net.getName();
        this.type = net.getDisplayName();
        this.dataReceived = net.getBytesRecv();
        this.dataSent = net.getBytesSent();
    }
    public Adapter(String[] strings, String name, String type, float dataSent, float dataReceived){
        this.ipAddress = strings;
        this.name = name;
        this.type = type;
        this.dataSent = dataSent;
        this.dataReceived = dataReceived;
    }
    
    public Adapter(String[] ipAddress) {
        this.ipAddress = ipAddress;
    }
    public Adapter(String[] ipAddress, String name) {
        this.ipAddress = ipAddress;
        this.name = name;
    }
    
    public Adapter(String[] ipAddress, String name, String type) {
        this.ipAddress = ipAddress;
        this.name = name;
        this.type = type;
    }
    
    public Adapter(String[] ipAddress, String name, String type, float dataSent) {
        this.ipAddress = ipAddress;
        this.name = name;
        this.type = type;
        this.dataSent = dataSent;
    }
    public String[] getIpAddress() {
        return ipAddress;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public float getDataSent() {
        return dataSent;
    }
    public float getDataReceived() {
        return dataReceived;
    }

}

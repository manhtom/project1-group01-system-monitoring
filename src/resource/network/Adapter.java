package resource.network;

import oshi.hardware.NetworkIF;
import system.OS;
public class Adapter {
    private String[] ipAddressV4;
    private String[] ipAddressV6;
    private String name;
    private String type;
    private long dataSent;
    private long dataReceived;
    private String macAddr;
    private int IfType;
    public NetworkIF net;
 
    public Adapter(NetworkIF net){
        this.net = net;
        this.ipAddressV4 = net.getIPv4addr();
        this.ipAddressV6 = net.getIPv6addr();
        this.name = net.getDisplayName();
        this.type = net.getName();
        this.dataReceived = net.getBytesRecv();
        this.dataSent = net.getBytesSent();
        this.macAddr = net.getMacaddr();
        this.IfType = net.getIfType();
    }
    
    public String getIPv4Address() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ipAddressV4.length; i++){
            sb.append(ipAddressV4[i]);
        }
        String IPv4 = sb.toString();
        return IPv4;
    }

    public String getIPv6Address(){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ipAddressV6.length; i++){
            sb.append(ipAddressV6[i]);
        }
        String IPv6 = sb.toString();
        return IPv6;
    }
    public String getName() {          
        return name;
    }
    public String getType() {
        return type;
    }
    public long getDataSent() {
        return dataSent;
    }
    public long getDataReceived() {
        return dataReceived;
    }
    public String getMacAddress(){
        return macAddr;
    }
    public int getIfType(){
        return IfType;
    }


    public long updateUpSpeed(){
        return net.getBytesSent();
    }

    public long updateDownSpeed(){

        return (net.getBytesRecv());
    }

}   

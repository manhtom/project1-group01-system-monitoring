package resource.storage;

import oshi.software.os.OSFileStore;

public class Volume {
    private OSFileStore vol;
    private String name;
    private String fileSys;
    private long spaceTotal;
    private long spaceAvailable;
    private String uuid;
    private String mountPoint;

    public Volume(OSFileStore o){
        this.vol = o;
        this.name = o.getLabel();
        this.fileSys = o.getType();
        this.spaceTotal = o.getTotalSpace();
        this.spaceAvailable = o.getUsableSpace(); // or getUsableSpace?
        this.uuid = o.getUUID();
        this.mountPoint = o.getMount();
        }
    
    public void updateSpace(){
        this.spaceTotal =  vol.getTotalSpace();
        this.spaceAvailable = vol.getUsableSpace(); // rem to fix
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    //public String getFilesys() {
    //    return fileSys;
    //}

    public String getfileSys(){
        return fileSys;
    }

    public long getSpaceTotal() {
        return spaceTotal;
    }

    public long getSpaceAvailable() {
        return spaceAvailable;
    }

    public String getUUID() {
        return uuid;
    }

    public String getMountPoint() {
        return mountPoint;
    }
}

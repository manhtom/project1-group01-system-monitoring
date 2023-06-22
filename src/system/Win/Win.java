package system.Win;

import oshi.SystemInfo;
import oshi.software.os.OSFileStore;
import system.OS;
import resource.storage.Volume;

import java.util.*;

public class Win extends OS {
    private List<Volume> listVol;

    public Win(SystemInfo s) {
        super(s);
        listVol = new ArrayList<Volume>();

        for (OSFileStore o : os.getFileSystem().getFileStores()) {
            listVol.add(new Volume(o));
        }

    }
}

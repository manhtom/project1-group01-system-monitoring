package system.Linux;

import oshi.SystemInfo;
import oshi.software.os.*;
import resource.storage.Volume;
import system.OS;

import java.util.*;

public class Linux extends OS {

    private List<Volume> listVol;

    public Linux(SystemInfo s) {
        super(s);
        listVol = new ArrayList<Volume>();
        for (OSFileStore o : os.getFileSystem().getFileStores()) {
            listVol.add(new Volume(o));
        }

    }

    public static void showDetail(LinuxSystemIO io) {
    }
}

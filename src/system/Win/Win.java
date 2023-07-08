package system.Win;

import oshi.SystemInfo;
import oshi.software.os.OSFileStore;
import system.OS;
import resource.storage.Win.WinVolume;

import java.util.*;

public class Win extends OS {
    private List<WinVolume> listVol;

    public Win(SystemInfo s) {
        super(s);
        listVol = new ArrayList<WinVolume>();

        for (OSFileStore o : os.getFileSystem().getFileStores()) {
            listVol.add(new WinVolume(o));
        }

    }
}

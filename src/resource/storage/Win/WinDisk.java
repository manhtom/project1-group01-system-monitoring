package resource.storage.Win;

import oshi.hardware.HWDiskStore;
import resource.storage.*;

import java.util.*;

public class WinDisk extends Disk {
    public WinDisk(HWDiskStore d, List<Volume> list) {
        super(d,list);
    }
}
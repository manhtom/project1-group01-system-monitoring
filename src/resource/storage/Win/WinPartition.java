package resource.storage.Win;

import java.util.List;

import oshi.hardware.HWPartition;
import resource.storage.*;


public class WinPartition extends Partition {
    public WinPartition(HWPartition p, List<Volume> listvol) {
        super(p,listvol);
    }

}
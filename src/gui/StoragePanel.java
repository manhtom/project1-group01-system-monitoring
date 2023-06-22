package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;

import resource.network.Adapter;
import resource.storage.Volume;
import resource.storage.Linux.DiskLinux;
import resource.storage.Linux.StorageLinux;
import resource.storage.Win.WinStorage;
import system.Sys;

public class StoragePanel extends JPanel {
    Sys s;
    DynamicTimeSeriesCollection sendData;
    DynamicTimeSeriesCollection receiveData;
    JTextArea statText;
DefaultTableModel diskModel;
    private static final String SEND = "";
    private static final String RECEIVE = "Read";
    private static final String STAT = "";
    private static final String VOL_INFO = "Volume info";
    private static final String DISK_INFO = "Disk info";
    private static final String[] COL = {"Name", "File system", "Mount point", "Available", "Total"};

    public StoragePanel(Sys s) {
        super();
        this.s = s;
        init(s);
    }

    public void init(Sys s) {
        JPanel netPanel = new JPanel();
        Font s1 = new Font("S1", Font.PLAIN, 14);
        Font s2 = new Font("S2", Font.BOLD, 14);
        netPanel.setFont(s1);
        netPanel.setLayout(new BorderLayout());

        // Create the quad grid panel
        JPanel quadGridPanel = new JPanel();
        quadGridPanel.setLayout(new GridLayout(1, 1));

        // Create the four sub-panels for the quad grid
        JPanel VolumePanel = createGridPanel(VOL_INFO);

        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        diskModel = new DefaultTableModel((Object[])COL, 1);

        JTable table = new JTable(diskModel);

        diskModel.setRowCount(16);

                // Update existing rows with random data

        VolumePanel.add(table);
        getVolInfo();
        table.setPreferredSize(new Dimension(1100,1100));
        VolumePanel.setPreferredSize(new Dimension(1100,1100));

 
        // Add the sub-panels to the quad grid panel
        quadGridPanel.add(VolumePanel);

        // Add the quad grid panel to the content panel
        netPanel.add(quadGridPanel, BorderLayout.CENTER);
        netPanel.setPreferredSize(new Dimension(1100,1100));

        add(netPanel);
        
    }

    private JPanel createGridPanel(String name) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(name));
        panel.setBackground(Color.WHITE);
        return panel;
    }       
 
    private String format(long l) {
        double kb = (double)l/1024;
        double mb = (double)l/1048576;
        double gb = (double)l/1048576/1024;

        if (gb >=1 ) {
            return String.format("%.2f GB", gb);
        }
        else if (mb >= 1) {
            return String.format("%.2f MB", mb);
        }
        else if (kb >=1) {
            return String.format("%.2f KB", kb);

        }
        else {
            return String.format("%d bytes", l);

        }


    }

    private void getVolInfo() {
        int c = 0;
            for (StorageLinux j : s.io.listDiskLinux) { 
                for (Volume i : j.listVol) {
                    diskModel.setValueAt((Object)i.getName(), c, 0);
                    diskModel.setValueAt((Object)i.getfileSys(), c, 1);
                    diskModel.setValueAt((Object)i.getMountPoint(), c, 2);
                    diskModel.setValueAt((Object)format(i.getSpaceAvailable()), c, 3);
                    diskModel.setValueAt((Object)format(i.getSpaceTotal()), c, 4);
                    c++;

            }
        }
    }
}

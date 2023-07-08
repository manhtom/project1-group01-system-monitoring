package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.*;

import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import system.Sys;

public class MemoryPanel extends Panel {

    Sys s;
    JTable table;
    DefaultTableModel processModel;
    DynamicTimeSeriesCollection memData;
    JTextArea statText;
    //private static final String OVERVIEW = "";
    private static final String MEM_INFO = "About the memory";
    private static final String STAT = "Real-time stats";
    private static final String TOP_PROC = "Top Processes";

    public MemoryPanel(Sys s) {
        super();
        this.s = s;
        init(s);
    }

    private void init(Sys s) {
        // Create the main content panel
        JPanel memPanel = new JPanel();
        Font s1 = new Font("S1", Font.PLAIN, 14);
        memPanel.setFont(s1);
        memPanel.setLayout(new BorderLayout());

        // Create the quad grid panel
        JPanel quadGridPanel = new JPanel();
        quadGridPanel.setLayout(new GridLayout(2, 2));

        // Create the four sub-panels for the quad grid
        JPanel memUsage = createGridPanel("");
        JPanel topProc = createGridPanel(TOP_PROC);
        JPanel stat = createGridPanel(STAT);
        JPanel info = createGridPanel(MEM_INFO);

        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        memData = new DynamicTimeSeriesCollection(1, 60, new Second());
        memData.setTimeBase(new Second(date));
        memData.addSeries(getUsage((double)s.mem.getPhysicalUsed()/s.mem.getPhysicalTotal()), 0, "Memory Usage");

        JFreeChart memChart = ChartFactory.createTimeSeriesChart(
                "", "", "Memory usage (%)", memData);


        
        XYPlot plot = (XYPlot) memChart.getPlot();

        ValueAxis domain = plot.getDomainAxis();

        ValueAxis range = plot.getRangeAxis();

        domain.setVisible(false);
        range.setRange(0d,100d);
        range.setVerticalTickLabels(false);
        ChartPanel memChartPanel = new ChartPanel(memChart);
        memChartPanel.setPreferredSize(new Dimension(400,200));

        memUsage.add(memChartPanel);



        statText = new JTextArea(0,0);
        statText.setText(getStats());
        statText.setFont(s1);
        stat.add(statText);

        JTextArea infoText = new JTextArea(0,0);
        infoText.setText(getInfo());
        infoText.setFont(s1);
        info.add(infoText);

        createTable();
        topProc.add(table);
        table.setPreferredSize(new Dimension(400, 400));

            
        // Add the sub-panels to the quad grid panel
        quadGridPanel.add(memUsage);
        quadGridPanel.add(topProc);
        quadGridPanel.add(stat);
        quadGridPanel.add(info);

        // Add the quad grid panel to the content panel
        memPanel.add(quadGridPanel, BorderLayout.CENTER);
        // memPanel.add(SysInfoGUI.sidebarPanel, BorderLayout.WEST);

        add(memPanel);

        Timer timer = new Timer(Config.RSLOW, e -> updateGraph());
        timer.start();
        
    }

    private JPanel createGridPanel(String name) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(name));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private String getStats() { // fix formatting
        return String.format("In use: %s%n Available: %s%nSwap used: %s%n", format(s.mem.getPhysicalUsed()), format(s.mem.getPhysicalAvailable()), format(s.mem.getSwapUsed()));
    }

    private String getInfo() {
        return String.format("Usable installed memory: %s%nSwap total: %s%n%n", 
        format(s.mem.getPhysicalTotal()), format(s.mem.getSwapTotal()) );
    }

    private void updateGraph() {
        memData.advanceTime();
        memData.appendData(getUsage((double)s.mem.getPhysicalUsed()/s.mem.getPhysicalTotal()));
        statText.setText(getStats());
        updateTable();

    }

    private static float[] getUsage(double d) {
        float[] memUsage = new float[1];
        memUsage[0] = (float)d * 100;
        return memUsage;
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

    public void createTable() {

        final String[] COL = {"Name", "PID", "RSS"};

        processModel = new DefaultTableModel((Object[])COL, 5);

        table = new JTable(processModel);


    }

    public void updateTable() {
            int i = 0;
                    // Update existing rows with random data
                for (OSProcess proc : s.os.getOS().getProcesses(null, OperatingSystem.ProcessSorting.RSS_DESC, 0)) {
                        processModel.setValueAt((Object)proc.getName(), i, 0);
                        processModel.setValueAt((Object)proc.getProcessID(), i, 1);
                        processModel.setValueAt((Object)format(proc.getResidentSetSize()), i, 2);
                        i++;
                        if (i == 5) {break;}
                }
    }
}

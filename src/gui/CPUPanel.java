package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYSeries;

import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import system.Sys;
import system.SystemCPU;

public class CPUPanel extends Panel {

    Sys s;
    DynamicTimeSeriesCollection cpuData;
    DefaultTableModel processModel;
    JTable table;
    JTextArea statText;
    private static final String OVERVIEW = "";
    private static final String CPU_INFO = "About the CPU";
    private static final String STAT = "Real-time stats";
    private static final String TOP_PROC = "Top Processes";

    public CPUPanel(Sys s) {
        super();
        this.s = s;
        init(s);
    }

    private void init(Sys s) {
        // Create the main content panel
        JPanel cpuPanel = new JPanel();
        Font s1 = new Font("S1", Font.PLAIN, 14);
        Font s2 = new Font("S2", Font.BOLD, 14);
        cpuPanel.setFont(s1);
        cpuPanel.setLayout(new BorderLayout());

        // Create the quad grid panel
        JPanel quadGridPanel = new JPanel();
        quadGridPanel.setLayout(new GridLayout(2, 2));

        // Create the four sub-panels for the quad grid
        JPanel cpuUsage = createGridPanel("");
        JPanel topProc = createGridPanel(TOP_PROC);
        JPanel stat = createGridPanel(STAT);
        JPanel info = createGridPanel(CPU_INFO);

        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        cpuData = new DynamicTimeSeriesCollection(1, 60, new Second());
        cpuData.setTimeBase(new Second(date));
        cpuData.addSeries(getUsage(s.cpu.getUtilization()), 0, "CPU Usage");

        JFreeChart cpuChart = ChartFactory.createTimeSeriesChart(
                "", "", "CPU Utilization (%)", cpuData);

        
        XYPlot plot = (XYPlot) cpuChart.getPlot();

        ValueAxis domain = plot.getDomainAxis();

        ValueAxis range =  plot.getRangeAxis();

        domain.setVisible(false);
        range.setRange(0d,100d);
        range.setVerticalTickLabels(false);
        ChartPanel cpuChartPanel = new ChartPanel(cpuChart);
        cpuChartPanel.setPreferredSize(new Dimension(200,200));

        cpuUsage.add(cpuChartPanel);

        statText = new JTextArea(0,0);    DefaultTableModel processModel;
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
        quadGridPanel.add(cpuUsage);
        quadGridPanel.add(topProc);
        quadGridPanel.add(stat);
        quadGridPanel.add(info);

        // Add the quad grid panel to the content panel
        cpuPanel.add(quadGridPanel, BorderLayout.CENTER);
        cpuPanel.add(SysInfoGUI.sidebarPanel, BorderLayout.WEST);

        add(cpuPanel);

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
        return String.format("Clock speed: %.2f GHz %nProcesses: %d%nThreads: %d%n", s.cpu.getSpeed()/1048576/1024, s.os.getnbProcess(), s.os.getnbThread());
    }

    private String getInfo() {
        return String.format("Name: %s%nBase frequency: %s%nSockets: %d%nCores: %d%nLogical processors: %d%n", 
        s.cpu.getName(), format(s.cpu.getFreq()), s.cpu.getPackage(),s.cpu.getCore(), s.cpu.getLogicalProcessor() );
    }

    private void updateGraph() {
        cpuData.advanceTime();
        cpuData.appendData(getUsage(s.cpu.getUtilization()));
        statText.setText(getStats());
        updateTable();
    }

    private static float[] getUsage(double d) {
        float[] cpuUsage = new float[1];
        cpuUsage[0] = (float)d * 100;
        return cpuUsage;
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

    private String format(long l) {
        double kb = (double)l/1024;
        double mb = (double)l/1048576;
        double gb = (double)l/1048576/1024;

        if (gb >=1 ) {
            return String.format("%.2f GHz", gb);
        }
        else if (mb >= 1) {
            return String.format("%.2f MHz", mb);
        }
        else if (kb >=1) {
            return String.format("%.2f KHz", kb);

        }
        else {
            return String.format("%d Hz", l);
        }
    }
}

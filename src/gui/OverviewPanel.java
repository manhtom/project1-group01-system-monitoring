package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    


//import oshi.util.FormatUtil;
import system.Sys;
import java.awt.*;

public class OverviewPanel extends JPanel {
    Sys s;
    DefaultPieDataset cpuData;
    DefaultPieDataset memData;
    JTextArea configText;
    JTable table;
    DefaultTableModel processModel;
    private static final String OVERVIEW = "What matters most at a glance";
    private static final String SYS_INFO = "System Information";
    private static final String SYS_CONFIG = "System Configuration";
    private static final String TOP_PROC = "Top Processes";



    public OverviewPanel(Sys s) {
        super();
        this.s = s;
        init(s);
    }

    private void init(Sys s) {
        // Create the main content panel
        JLabel overviewLabel = new JLabel(OVERVIEW);
        JPanel overviewPanel = new JPanel();
        Font s1 = new Font("S1", Font.PLAIN, 14);
        Font s2 = new Font("S2", Font.BOLD, 14);
        overviewLabel.setFont(s2);
        overviewPanel.setFont(s1);
        overviewPanel.setLayout(new BorderLayout());

        // Create the quad grid panel
        JPanel quadGridPanel = new JPanel();
        quadGridPanel.setLayout(new GridLayout(2, 2));

        // Create the four sub-panels for the quad grid
        JPanel resource = createGridPanel("");
        JPanel topProc = createGridPanel(TOP_PROC);
        JPanel general = createGridPanel(SYS_INFO);
        JPanel config = createGridPanel(SYS_CONFIG);

        // Create the pie dataset
        cpuData= new DefaultPieDataset();
        double util = s.cpu.getUtilization()*100;
        cpuData.setValue("Used", util);
        cpuData.setValue("Available", 100-util);


               // Create the pie dataset
        memData = new DefaultPieDataset();
        double memutil = (double)s.mem.getPhysicalUsed()/s.mem.getPhysicalTotal()*100;

        memData.setValue("Used", memutil);
        memData.setValue("Available", 100-memutil);


        // Create the pie chart
        JFreeChart cpuChart = ChartFactory.createPieChart("CPU",cpuData,false,false,false);
        ChartPanel cpuChartPanel = new ChartPanel(cpuChart);
        cpuChartPanel.setPreferredSize(new Dimension(200,200));
        resource.add(cpuChartPanel);




        JFreeChart memChart = ChartFactory.createPieChart("Memory", memData,false,false,false);
        ChartPanel memChartPanel = new ChartPanel(memChart);
        memChartPanel.setPreferredSize(new Dimension(200,200));

        resource.add(memChartPanel);

        JTextArea generalText = new JTextArea(0,0);
        generalText.setText(getGeneralInfo());
        generalText.setFont(s1);
        general.add(generalText);

        configText = new JTextArea(0,0);
        configText.setText(getConfigInfo());
        configText.setFont(s1);
        config.add(configText);
        createTable();
        topProc.add(table);
        table.setPreferredSize(new Dimension(400, 400));
        // Add the sub-panels to the quad grid panel
        quadGridPanel.add(resource);
        quadGridPanel.add(topProc);
        quadGridPanel.add(general);
        quadGridPanel.add(config);


        // Add the quad grid panel to the content panel
        overviewPanel.add(overviewLabel, BorderLayout.NORTH);
        overviewPanel.add(SysInfoGUI.sidebarPanel, BorderLayout.WEST);
        overviewPanel.add(quadGridPanel, BorderLayout.CENTER);

        add(overviewPanel);

        Timer timer = new Timer(Config.RSLOW, e -> updateGraph());
        timer.start();
        
    }

    private JPanel createGridPanel(String name) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(name));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private String getGeneralInfo() { // fix formatting
        return String.format("OS: %s%nCPU: %s%nInstalled memory: %s%n", s.os.getVersion(), s.cpu.getName(), format(s.mem.getPhysicalTotal()));
    }

    private String getConfigInfo() {
        return String.format("Uptime: %s%nSystem time: %s%nCurrent user: %s%nComputer name: %s%n", FormatUtil.formatElapsedSecs(s.os.getOS().getSystemUptime()),DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()), s.os.getUser(), s.os.getComputerName());
    }

    private void updateGraph() {
        double util = s.cpu.getUtilization()*100;
        cpuData.setValue("Used", util);
        cpuData.setValue("Available", 100-util);
        double memutil = (double)s.mem.getPhysicalUsed()/s.mem.getPhysicalTotal()*100;
        memData.setValue("Used", memutil);
        memData.setValue("Available", 100-memutil);
        configText.setText(getConfigInfo());
        updateTable();
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

        final String[] COL = {"Name", "PID", "CPU %"};

        processModel = new DefaultTableModel((Object[])COL, 5);

        table = new JTable(processModel);


    }

    public void updateTable() {
            int i = 0;
                    // Update existing rows with random data
                for (OSProcess proc : s.os.getOS().getProcesses(null, OperatingSystem.ProcessSorting.CPU_DESC, 0)) {
                        processModel.setValueAt((Object)proc.getName(), i, 0);
                        processModel.setValueAt((Object)proc.getProcessID(), i, 1);
                        processModel.setValueAt((Object)String.format("%.2f", proc.getProcessCpuLoadCumulative()), i, 2);
                        i++;
                        if (i == 5) {break;}
                }
    }
}

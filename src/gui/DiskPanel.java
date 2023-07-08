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
import javax.swing.JTextArea;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;

import resource.storage.Volume;
import resource.storage.Win.WinStorage;

public class DiskPanel extends JPanel {
    WinStorage s;
    DynamicTimeSeriesCollection sendData;
    DynamicTimeSeriesCollection receiveData;
    JTextArea statText;

    private static final String STAT = "";
    private static final String VOL_INFO = "Volume info";
    private static final String DISK_INFO = "Disk info";
    public DiskPanel(WinStorage s) {
        super();
        this.s = s;
        init(s);
    }

    public void init(WinStorage s) {
        JPanel netPanel = new JPanel();
        Font s1 = new Font("S1", Font.PLAIN, 14);
        netPanel.setFont(s1);
        netPanel.setLayout(new BorderLayout());

        // Create the quad grid panel
        JPanel quadGridPanel = new JPanel();
        quadGridPanel.setLayout(new GridLayout(4, 1));

        // Create the four sub-panels for the quad grid
        JPanel graphPanel = createGridPanel("");
        JPanel stat = createGridPanel(STAT);
        JPanel VolumePanel = createGridPanel(VOL_INFO);
        JPanel info = createGridPanel(DISK_INFO);

        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        sendData = new DynamicTimeSeriesCollection(1, 60, new Second());
        sendData.setTimeBase(new Second(date));
        sendData.addSeries(getData(s.getTotalDataWritten()), 0, "");

        JFreeChart sendChart = ChartFactory.createTimeSeriesChart("", "", "Write data (GB)", sendData);

        receiveData = new DynamicTimeSeriesCollection(1, 60, new Second());
        receiveData.setTimeBase(new Second(date));
        receiveData.addSeries(getData(s.getTotalDataRead()), 0, "");

        JFreeChart receiveChart = ChartFactory.createTimeSeriesChart("", "", "Read data (GB)", receiveData);


        XYPlot plot = (XYPlot) sendChart.getPlot();

        ValueAxis domain = plot.getDomainAxis();

        ValueAxis range =  plot.getRangeAxis();

        domain.setVisible(false);
        range.setRange(0d,100d);
        range.setVerticalTickLabels(false);

        ChartPanel sendChartPanel = new ChartPanel(sendChart);
        graphPanel.add(sendChartPanel);
        sendChartPanel.setPreferredSize(new Dimension(200,200));

        plot = (XYPlot) receiveChart.getPlot();

        domain = plot.getDomainAxis();

        range =  plot.getRangeAxis();

        domain.setVisible(false);
        range.setRange(0d,100d);
        range.setVerticalTickLabels(false);
        ChartPanel receiveChartPanel = new ChartPanel(receiveChart);
        graphPanel.add(receiveChartPanel);
        receiveChartPanel.setPreferredSize(new Dimension(200,200));


        statText = new JTextArea(0,0);
        statText.setText(getStats());
        statText.setFont(s1);
        stat.add(statText);

        JTextArea volText = new JTextArea(0,0);
        volText.setText(getVolInfo());
        volText.setFont(s1);
        VolumePanel.add(volText);

        JTextArea infoText = new JTextArea(0,0);
        infoText.setText(getInfo());
        infoText.setFont(s1);
        info.add(infoText);
            
        // Add the sub-panels to the quad grid panel
        quadGridPanel.add(graphPanel);
        quadGridPanel.add(stat);
        quadGridPanel.add(VolumePanel);
        quadGridPanel.add(info);

        // Add the quad grid panel to the content panel
        netPanel.add(quadGridPanel, BorderLayout.CENTER);

        add(netPanel);

        Timer timer = new Timer(Config.RFAST, e -> updateGraph());
        timer.start();
        
    }

    private JPanel createGridPanel(String name) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(name));
        panel.setBackground(Color.WHITE);
        return panel;
    }       
 
    private void updateGraph() {
        sendData.advanceTime();
        sendData.appendData(getData(s.getTotalDataWritten()));
        receiveData.advanceTime();
        receiveData.appendData(getData(s.getTotalDataRead()));
        statText.setText(getStats());

    }

    private float[] getData(double d) {
            float[] data = new float[1];
            data[0] = (float)d / 1048576 / 1024;
            return data;

    }

    private String getStats() { // fix formatting
        return String.format("Written data: %s%nRead data: %s%n", format(s.getTotalDataWritten()), format(s.getTotalDataRead()));
    }


    private String getVolInfo() {
        String res = ("");
        res = res+(String.format("%1$-48s %2$20s %3$20s %4$20s %5$20s%n", "Name", "File system", "Mount point", "Available", "Total"));
        res = res+"\n";
            for (Volume i : s.listVol) {
                res+=(String.format("%1$-48s %2$20s %3$20s %4$20.2f GB %5$20.2f GB%n", i.getName(), i.getfileSys(), i.getMountPoint(), (double)i.getSpaceAvailable()/1073741824, (double)i.getSpaceTotal()/1073741824));
            }

        return res;
    }

    private String getInfo() {
        return String.format("Name: %s%nSize: %.2f GB%n", s.getName(), (double)s.getSize()/1073741824);
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
}

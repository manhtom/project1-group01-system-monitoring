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

import resource.network.Adapter;

public class AdapterPanel extends JPanel {
    Adapter a;
    DynamicTimeSeriesCollection sendData;
    DynamicTimeSeriesCollection receiveData;
    JTextArea statText;
    private static final String SEND = "Send";
    private static final String RECEIVE = "Receive";
    private static final String STAT = "Real-time stats";
    private static final String NET_INFO = "Adapter info";
    private long sendSpeed;
    private long receiveSpeed;

    public AdapterPanel(Adapter a) {
        super();
        this.a = a;
        init(a);
    }

    public void init(Adapter a) {
        JPanel netPanel = new JPanel();
        Font s1 = new Font("S1", Font.PLAIN, 14);
        netPanel.setFont(s1);
        netPanel.setLayout(new BorderLayout());

        // Create the quad grid panel
        JPanel quadGridPanel = new JPanel();
        quadGridPanel.setLayout(new GridLayout(2, 2));

        // Create the four sub-panels for the quad grid
        JPanel sendPanel = createGridPanel(SEND);
        JPanel receivePanel = createGridPanel(RECEIVE);
        JPanel stat = createGridPanel(STAT);
        JPanel info = createGridPanel(NET_INFO);

        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        sendData = new DynamicTimeSeriesCollection(1, 60, new Second());
        sendData.setTimeBase(new Second(date));
        sendData.addSeries(getData(0), 0, "");

        JFreeChart sendChart = ChartFactory.createTimeSeriesChart("", "", "Send speed (Mbps)", sendData);

        receiveData = new DynamicTimeSeriesCollection(1, 60, new Second());
        receiveData.setTimeBase(new Second(date));
        receiveData.addSeries(getData(0), 0, "");

        JFreeChart receiveChart = ChartFactory.createTimeSeriesChart("", "", "Receive speed (Mbps)", receiveData);


        XYPlot plot = (XYPlot) sendChart.getPlot();

        ValueAxis domain = plot.getDomainAxis();

        ValueAxis range =  plot.getRangeAxis();

        domain.setVisible(false);
        range.setRange(0d, 200d);
        range.setVerticalTickLabels(false);

        ChartPanel sendChartPanel = new ChartPanel(sendChart);
        sendPanel.add(sendChartPanel);
        sendChartPanel.setPreferredSize(new Dimension(200,200));

        plot = (XYPlot) receiveChart.getPlot();

        domain = plot.getDomainAxis();

        range =  plot.getRangeAxis();

        domain.setVisible(false);
        range.setRange(0d, 200d);
        range.setVerticalTickLabels(false);
        ChartPanel receiveChartPanel = new ChartPanel(receiveChart);
        receivePanel.add(receiveChartPanel);
        receiveChartPanel.setPreferredSize(new Dimension(200,200));


        statText = new JTextArea(0,0);
        statText.setText(getStats());
        statText.setFont(s1);
        stat.add(statText);

        JTextArea infoText = new JTextArea(0,0);
        infoText.setText(getInfo());
        infoText.setFont(s1);
        info.add(infoText);
            
        // Add the sub-panels to the quad grid panel
        quadGridPanel.add(sendPanel);
        quadGridPanel.add(receivePanel);
        quadGridPanel.add(stat);
        quadGridPanel.add(info);

        // Add the quad grid panel to the content panel
        netPanel.add(quadGridPanel, BorderLayout.CENTER);

        add(netPanel);

        Timer timer = new Timer(Config.RSLOW, e -> updateGraph());
        sendSpeed = a.updateUpSpeed();
        receiveSpeed = a.updateDownSpeed();

        timer.start();
        
    }

    private JPanel createGridPanel(String name) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(name));
        panel.setBackground(Color.WHITE);
        return panel;
    }       
 
    private void updateGraph() {
        a.net.updateAttributes();
        sendData.advanceTime();
        sendData.appendData(getData((double)(a.updateUpSpeed()-sendSpeed)/Config.RSLOW*1000));
        receiveData.advanceTime();
        receiveData.appendData(getData((double)(a.updateDownSpeed()-receiveSpeed)/Config.RSLOW*1000));
        statText.setText(getStats());
        sendSpeed = a.updateUpSpeed();
        receiveSpeed = a.updateDownSpeed();

    }

    private float[] getData(double d) {
            float[] data = new float[1];
            data[0] = (float)d/1024576*8;
            return data;

    }

    private String getStats() { // fix formatting
        return String.format("Sent data: %s%nReceived data: %s%n", format(a.updateUpSpeed()), format(a.updateDownSpeed()));
    }

    private String getInfo() {
        return String.format("Name: %s%nIdentifier: %s%nIPv4 address: %s%nIPv6 address: %s%nMAC Address: %s%n", a.getName(), a.getType(), a.getIPv4Address(), a.getIPv6Address(), a.getMacAddress());
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

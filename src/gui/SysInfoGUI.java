package gui;

import javax.swing.*;

import oshi.SystemInfo;
import system.Sys;

import java.awt.*;


public class SysInfoGUI extends JFrame {
    public static JPanel sidebarPanel;
    private JFrame mainFrame;
    private JButton cpu;
    private JButton mem;
    private JButton overview;
    private SystemInfo si;
    private Sys s;


    public SysInfoGUI() {
        si = new SystemInfo();
        s = new Sys(si);

        mainFrame = new JFrame(Config.TITLE);
        mainFrame.setSize(Config.WIDTH,Config.HEIGHT);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setLocationByPlatform(true);
        mainFrame.setLayout(new BorderLayout());


        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(Color.LIGHT_GRAY);
        sidebarPanel.setPreferredSize(new Dimension(120, getHeight()));

        JButton resources = new JButton("Resources");

        JPanel subresources = new JPanel();
        subresources.setLayout(new BoxLayout(subresources, BoxLayout.Y_AXIS));
        cpu = createJButton(String.format("CPU     %.1f%%", s.cpu.getUtilization()*100), 'C', new CPUPanel(s));
        mem = createJButton(String.format("Memory  %.1f%%", (double)s.mem.getPhysicalUsed()/s.mem.getPhysicalTotal()*100), 'M', new MemoryPanel(s));
        subresources.add(cpu);
        subresources.add(mem);


        resources.addActionListener(e -> {
            subresources.setVisible(!subresources.isVisible());
            pack();
        });

        mainFrame.setContentPane(new OverviewPanel(s));


        // Add items to the sidebar panel
        overview = createJButton("Overview", 'O', new OverviewPanel(s));
        sidebarPanel.add(overview);
        sidebarPanel.add(resources);
        sidebarPanel.add(subresources);
        sidebarPanel.add(createJButton("Storage", 'S', new StoragePanel(s)));
        sidebarPanel.add(createJButton("Networking", 'N', new NetworkPanel(s)));
        sidebarPanel.add(createJButton("Processes", 'O', new ProcessPanel(s)));

        mainFrame.add(sidebarPanel, BorderLayout.WEST);

        Timer timer = new Timer(Config.RSLOW, e -> updateInfo());
        timer.start();
    }

    private JButton createJButton(String title, char mnemonic, JPanel panel) {
        JButton button = new JButton(title);
        button.setMnemonic(mnemonic);
        button.addActionListener(e -> {
            Container contentPane = this.mainFrame.getContentPane();
            if (contentPane.getComponents().length <= 0 || contentPane.getComponent(0) != panel) {
                resetMainGui();
                this.mainFrame.getContentPane().add(panel);
                refreshMainGui();
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SysInfoGUI gui = new SysInfoGUI();
            gui.mainFrame.setVisible(true);
        });
    }

    private void resetMainGui() {
        this.mainFrame.getContentPane().removeAll();
        mainFrame.add(sidebarPanel, BorderLayout.WEST);

        Timer timer = new Timer(Config.RSLOW, e -> updateInfo());
        timer.start();
    }

    private void refreshMainGui() {
        this.mainFrame.revalidate();
        this.mainFrame.repaint();
    }

    private void updateInfo() {
        cpu.setText(String.format("%1$-1s %2$5.1f%%%n", "CPU", s.cpu.getUtilization()*100));
        mem.setText(String.format("%1$-1s %2$5.1f%%%n", "Memory", (double)s.mem.getPhysicalUsed()/s.mem.getPhysicalTotal()*100));

    }
}
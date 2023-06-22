package gui;

import javax.swing.*;
import java.awt.*;

public class test extends JFrame {
    public test() {
        setTitle("Quad Grid Layout Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Create the main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Create the quad grid panel
        JPanel quadGridPanel = new JPanel();
        quadGridPanel.setLayout(new GridLayout(2, 2));

        // Create the four sub-panels for the quad grid
        JPanel panel1 = createGridPanel("");
        JPanel panel2 = createGridPanel("");
        JPanel panel3 = createGridPanel("");
        JPanel panel4 = createGridPanel("");

        // Add the sub-panels to the quad grid panel
        quadGridPanel.add(panel1);
        quadGridPanel.add(panel2);
        quadGridPanel.add(panel3);
        quadGridPanel.add(panel4);

        // Add the quad grid panel to the content panel
        contentPanel.add(quadGridPanel, BorderLayout.CENTER);

        // Set the content pane of the frame
        setContentPane(contentPanel);
    }

    private JPanel createGridPanel(String name) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(name));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            test example = new test();
            example.setVisible(true);
        });
    }
}

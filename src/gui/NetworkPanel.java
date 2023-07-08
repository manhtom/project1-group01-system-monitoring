package gui;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import system.Sys;

import java.util.List;
import resource.network.Adapter;

public class NetworkPanel extends Panel {


    Sys s;

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private List<Adapter> adapterList;
    //private static final String OVERVIEW = "";


    public NetworkPanel(Sys s) {
        super();
        adapterList = s.net.getAdapters();
        this.s = s;
        init(s);
    }



    private void init(Sys s) {
        JPanel networkPanel = new JPanel();
        Font s1 = new Font("S1", Font.PLAIN, 14);
        networkPanel.setFont(s1);
        networkPanel.setLayout(new BorderLayout());

        String[] menuItems = new String[32]; // max 32 adapters
        int k = 0;

        // Create the card layout and panel to hold the different panels
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        // Create the dropdown menu
        for (Adapter a : adapterList) {
            menuItems[k] = a.getName();
            JPanel panel = createPanel(menuItems[k], Color.WHITE, a);
            cardsPanel.add(panel, menuItems[k]);
            k++;
        }
        
        JComboBox<String> dropdown = new JComboBox<>(menuItems);



        // Add action listener to the dropdown
        dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) dropdown.getSelectedItem();
                cardLayout.show(cardsPanel, selectedItem);
            }
        });

        // Add the dropdown and cards panel to the content panel
        networkPanel.add(dropdown, BorderLayout.NORTH);
        networkPanel.add(cardsPanel, BorderLayout.CENTER);
        // networkPanel.add(SysInfoGUI.sidebarPanel, BorderLayout.WEST);

        // Set the content pane of the frame
        add(networkPanel);
    }

    private JPanel createPanel(String name, Color color, Adapter a) {
        return new AdapterPanel(a);
    };
}
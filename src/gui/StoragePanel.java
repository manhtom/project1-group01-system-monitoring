package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import system.Sys;

import java.util.List;
import resource.storage.Win.WinStorage;

public class StoragePanel extends Panel {


    Sys s;

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private List<WinStorage> adapterList;
    //private static final String OVERVIEW = "";


    public StoragePanel(Sys s) {
        super();
        adapterList = s.io.listDiskWin;
        this.s = s;
        init(s);
    }



    private void init(Sys s) {
        JPanel storagePanel = new JPanel();
        Font s1 = new Font("S1", Font.PLAIN, 14);
        storagePanel.setFont(s1);
        storagePanel.setLayout(new BorderLayout());

        String[] menuItems = new String[32]; // max 32 adapters
        int k = 0;

        // Create the card layout and panel to hold the different panels
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        // Create the dropdown menu
        for (WinStorage a : adapterList) {
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
        storagePanel.add(dropdown, BorderLayout.NORTH);
        storagePanel.add(cardsPanel, BorderLayout.CENTER);

        // Set the content pane of the frame
        add(storagePanel);
        
    }

    private JPanel createPanel(String name, Color color, WinStorage a) {
        return new DiskPanel(a);
    }
}


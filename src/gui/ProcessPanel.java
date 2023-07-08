package gui;


import oshi.util.FormatUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import system.Sys;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ProcessPanel extends Panel {

    private DefaultTableModel processModel;

    Sys s;
    JTextArea statText;
    TableRowSorter<TableModel> sorter;
    //private static final String OVERVIEW = "";
    private static final String[] COL = {"Name", "PID", "Path", "User", "Status", "CPU %", " Kernel time", "VM Total", "Working Set", "Data read",
            "Data written", "Architecture"};

    public ProcessPanel(Sys s) {
        super();
        this.s = s;
        init(s);
    }

    private void init(Sys s) {
        // Create the main content panel
        JPanel processPanel = new JPanel();
        Font s1 = new Font("S1", Font.PLAIN, 14);
        processPanel.setFont(s1);
        processPanel.setLayout(new BorderLayout());

        processModel = new DefaultTableModel((Object[])COL, 1);

        JTable table = new JTable(processModel);

        sorter = new TableRowSorter<>(table.getModel());
        sorter.setComparator(0, new SizeComparator());
        sorter.setComparator(1, new SizeComparator());
        sorter.setComparator(2, new SizeComparator());
        sorter.setComparator(3, new SizeComparator());
        sorter.setComparator(4, new SizeComparator());
        sorter.setComparator(5, new SizeComparator());
        sorter.setComparator(6, new SizeComparator());
        sorter.setComparator(7, new SizeComparator());
        sorter.setComparator(8, new SizeComparator());
        sorter.setComparator(9, new SizeComparator());
        sorter.setComparator(10, new SizeComparator());
        sorter.setComparator(11, new SizeComparator());

        sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(11, SortOrder.DESCENDING)));

        table.setRowSorter(sorter);

        // Create a timer to update the table
        Timer timer = new Timer(Config.RSLOW, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });
        timer.start();
        processPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        // processPanel.add(SysInfoGUI.sidebarPanel, BorderLayout.WEST);

        // Set the content pane of the frame
        add(processPanel);
    }

    private void updateTable() {
        int i = 0;
        List<process.Process> procList = s.os.getProcesses();
        processModel.setRowCount(procList.size());

                // Update existing rows with random data
            for (process.Process proc : procList) {
                    processModel.setValueAt((Object)proc.getName(), i, 0);
                    processModel.setValueAt((Object)proc.getPID(), i, 1);
                    processModel.setValueAt((Object)proc.getPath(), i, 2);
                    processModel.setValueAt((Object)proc.getUser(), i, 3);

                    processModel.setValueAt((Object)proc.getStatus(), i, 4);
                    processModel.setValueAt((Object)String.format("%.2f",proc.getUtilization()), i, 5);
                    processModel.setValueAt((Object)FormatUtil.formatElapsedSecs(proc.getKernelTime()), i, 6);
                    processModel.setValueAt((Object)format(proc.getVMSize()), i, 7);
                    processModel.setValueAt((Object)format(proc.getWorkingSet()), i, 8);
                    processModel.setValueAt((Object)format(proc.getDataRead()), i, 9);
                    processModel.setValueAt((Object)format(proc.getDataWritten()), i, 10);
                    processModel.setValueAt((Object)proc.getBitness(), i, 11);
                    i++;

            }
            List<? extends RowSorter.SortKey> sortKeys = sorter.getSortKeys();
            sorter.setModel(processModel);
            sorter.setComparator(0, new SizeComparator());
            sorter.setComparator(1, new SizeComparator());
            sorter.setComparator(2, new SizeComparator());
            sorter.setComparator(3, new SizeComparator());
            sorter.setComparator(4, new SizeComparator());
            sorter.setComparator(5, new SizeComparator());
            sorter.setComparator(6, new SizeComparator());
            sorter.setComparator(7, new SizeComparator());
            sorter.setComparator(8, new SizeComparator());
            sorter.setComparator(9, new SizeComparator());
            sorter.setComparator(10, new SizeComparator());
            sorter.setComparator(11, new SizeComparator());

            sorter.setSortKeys(sortKeys);
            sorter.sort();
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

private static class SizeComparator implements Comparator<Object> {

    public int compare(Object o1, Object o2) {
        return o1.toString().compareTo(o2.toString());
    }
}

}
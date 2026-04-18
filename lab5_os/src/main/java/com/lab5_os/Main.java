package com.lab5_os;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.awt.*;
    

public class Main extends JFrame{
    private JTextField blockInput, processInput;
    private JTable resultTable;
    private ResultTableModel tableModel;
    private JTextArea logArea;

    private List<MemoryBlock> memoryBlocks = new ArrayList<>();
    private List<Process> processJobs = new ArrayList<>();

    //Main Class Initiator
    public Main(){

        //GUI Initialization
        setTitle("First Fit Memory Allocation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850,550);
        setLayout(new BorderLayout(10,10));

        //Setting the UI
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){}

        //Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4,3,10,10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //Block Size Input
        inputPanel.add(new JLabel("Memory Block Sizes (example: 100, 500, 200)"));
        blockInput = new JTextField("");
        inputPanel.add(blockInput);

        //Process Size Input
        inputPanel.add(new JLabel("Process Request Sizes (example: 123, 456)"));
        processInput = new JTextField("");
        inputPanel.add(processInput);

        //Run and ErsetButton
        JButton runBtn = new JButton("Run Allocation");
        runBtn.setBackground(new Color(56,205,232));
        runBtn.setFocusPainted(false);
        runBtn.addActionListener(e -> runSimulation());
        JButton resetBtn = new JButton("Restart/Clear");
        runBtn.setBackground(new Color(56,205,232));
        resetBtn.addActionListener(e -> resetSimulation());
        inputPanel.add(resetBtn);
        inputPanel.add(runBtn);
        inputPanel.add(new JLabel(""));

        //Table Panel
        tableModel = new ResultTableModel();
        resultTable = new JTable(tableModel);
        resultTable.setFillsViewportHeight(true);

        //Log Panel
        logArea = new JTextArea(10,20);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

    }

    //Logic for running the simulation

    private void runSimulation(){
        try{
            //Creating blocks of memory 
            memoryBlocks.clear();
            int [] blockSizes = parseInput(blockInput.getText());

            //Creating list of jobs
            processJobs.clear();
            int [] processSizes = parseInput(processInput.getText());

            //Run the firstFit algorithm
            firstFit(blockSizes, processSizes);

            //Update the UI
            tableModel.updateData(processJobs);
            updateLog();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Enter valid numbers seperated by commas!");
        }
    }

    //Logic for the Reset Button

    private void resetSimulation() {

        //Clear Text Fields
        blockInput.setText("");
        processInput.setText("");

        //Clear the Log
        logArea.setText("");

        //Clear the table by passing an empty list
        tableModel.updateData(new ArrayList<>());
        updateLog();
    }

    //Logic for Parsing the Input 
    private int[] parseInput(String text) {
        if (text.trim().isEmpty()) {
            return new int[0];
        }

        String[] parts = text.split(",");
        int [] nums = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            nums[i] = Integer.parseInt(parts[i].trim());
        }
        return nums;
    }

    //Logic for the Simulation Log
    private void updateLog() {
        StringBuilder sb = new StringBuilder("-----Final Block States-----\n");

        for (MemoryBlock b: memoryBlocks){
            sb.append(String.format("Block %d | Total: %d | Left: %d\n", b.blockId, b.totalSize, b.remainingSize));
        }
        logArea.setText(sb.toString());
    }

    //Classes for the Table Model, Result and Logic 
    
    //Object for a block in memory
    public static class MemoryBlock{
        int blockId;
        int totalSize;
        int remainingSize;

        public MemoryBlock(int ID, int size){
            this.blockId = ID;
            this.totalSize = size;
            this.remainingSize = size;
        }
    }

    //Object for jobs
    public static class Process{
        int processId;
        int size;
        int allocatedBlockId;

        public Process(int id, int size) {
            this.processId = id;
            this.size = size;
            this.allocatedBlockId = -1;
        }
    }
    //First Fit Allocation Algorithm
    private void firstFit(int[] blockSizes, int[] processSizes) {
        memoryBlocks.clear();
        processJobs.clear();
        //Initialize memory blocks
        for (int i = 0; i < blockSizes.length; i++){
            memoryBlocks.add(new MemoryBlock(i+1, blockSizes[i]));
        }

        //Try to allocate each process
        for (int i = 0; i < processSizes.length; i++) {
            Process process = new Process(i + 1, processSizes[i]);
            processJobs.add(process);

            for (MemoryBlock block : memoryBlocks) {
                if (block.remainingSize >= process.size) {
                    block.remainingSize -= process.size;
                    process.allocatedBlockId = block.blockId;
                    break;
                }
            }
        }
    }
    //Object for displaying results
    class ResultTableModel extends AbstractTableModel {
        private List<Process> jobs = new ArrayList<>();
        private final String[] columns = {"Process ID", "Size", "Block ID", "Status"};

        public void updateData(List<Process> newJobs) {
            this.jobs = newJobs;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount(){
            return jobs.size();
        }

        @Override
        public int getColumnCount(){
            return columns.length;
        }

        @Override
        public String getColumnName(int c) {
            return columns[c];
        }

        @Override
        public Object getValueAt(int r, int c) {
            Process j = jobs.get(r);

            return switch(c) {
                case 0 -> "Job " + j.processId;
                case 1 -> j.size;
                case 2 -> j.allocatedBlockId != -1 ? "Block " + j.allocatedBlockId : "N/A";
                case 3 -> j.allocatedBlockId != -1 ? "ALLOCATED" : "WAITING";
                default -> null;
            };
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }

}
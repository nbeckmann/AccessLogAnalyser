package de.beckmann.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.beckmann.services.AccessLogAnalyserService;

public class AccessLogAnalyserGui {

    private JTextField inputFilePathTF;
    private JTextField outputFilePathTF;
    private JTextArea consoleTA;

    public void showGui() {
        JFrame frame = new JFrame("AccessLogAnalyser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        JPanel fileSelectPanel = getFileSelectPanel();
        JPanel actionButtonPanel = getActionButtonPanel();
        JPanel consolePanel = getConsolePanel();
        mainPanel.add(fileSelectPanel);
        mainPanel.add(actionButtonPanel);
        mainPanel.add(consolePanel);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel getActionButtonPanel() {
        JPanel actionButtonPanel = new JPanel(new FlowLayout());
        JButton requestsPerSecondButton = new JButton("Request per Second");
        requestsPerSecondButton.addActionListener(e -> {
            analyseRequestsPerSecond();
        });
        actionButtonPanel.add(requestsPerSecondButton);
        return actionButtonPanel;
    }

    private JPanel getConsolePanel() {
        JPanel consolePanel = new JPanel(new FlowLayout());
        consoleTA = new JTextArea(20, 100);
        consolePanel.add(consoleTA);
        return consolePanel;
    }

    private JPanel getFileSelectPanel() {
        JPanel inputFilePanel = createInputFilePanel();
        JPanel outputFilePanel = createOutputFilePanel();
        JPanel fileSelectPanel = new JPanel(new GridLayout(0, 1));
        fileSelectPanel.add(inputFilePanel);
        fileSelectPanel.add(outputFilePanel);
        return fileSelectPanel;
    }

    private JPanel createInputFilePanel() {
        JPanel inputFilePanel = new JPanel(new FlowLayout());
        inputFilePanel.setBorder(BorderFactory.createTitledBorder("Input file"));
        inputFilePathTF = new JTextField(30);
        JButton browseButton = new JButton("Browse");

        browseButton.addActionListener(e -> {
            selectFile(inputFilePathTF);
        });

        inputFilePanel.add(inputFilePathTF);
        inputFilePanel.add(browseButton);
        return inputFilePanel;
    }

    private JPanel createOutputFilePanel() {
        JPanel outputFilePanel = new JPanel(new FlowLayout());
        outputFilePanel.setBorder(BorderFactory.createTitledBorder("Output file"));
        outputFilePathTF = new JTextField(30);
        JButton browseButton = new JButton("Browse");

        browseButton.addActionListener(e -> {
            selectFile(outputFilePathTF);
        });

        outputFilePanel.add(outputFilePathTF);
        outputFilePanel.add(browseButton);
        return outputFilePanel;
    }

    private void selectFile(JTextField fileNameTextField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            fileNameTextField.setText(filePath);
        }
    }

    private void analyseRequestsPerSecond() {
        try {
            String inputFilePath = inputFilePathTF.getText();
            String outputFilePath = outputFilePathTF.getText();
            AccessLogAnalyserService accessLogAnalyserService = new AccessLogAnalyserService();
            accessLogAnalyserService.analyseRequestsPerSecond(inputFilePath, outputFilePath);
            displayResult(outputFilePath);
        } catch (Exception e) {
            consoleTA.setText(e.toString());
            e.printStackTrace();
        }
    }

    private void displayResult(String outputFilePath)
    {
        String resultString = "Test finished!\nResult saved under %s";
        consoleTA.setText(String.format(resultString, outputFilePath));
    }
}

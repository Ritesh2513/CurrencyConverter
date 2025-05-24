package com.Springboot.CurrencyConverter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class CurrencyConverterGUI extends JFrame {
    private JComboBox<String> fromCurrency, toCurrency;
    private JTextField amountField, resultField;
    private JButton convertButton, historyButton;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    public CurrencyConverterGUI() {
        setTitle("Currency Converter");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        setLayout(new BorderLayout(10, 10));

        // Colors
        Color primaryColor = new Color(58, 123, 213);     // Blue
        Color secondaryColor = new Color(34, 45, 65);     // Dark Blue
        Color accentColor = new Color(255, 193, 7);       // Yellow
        Color backgroundColor = new Color(245, 245, 245); // Light Gray
        Color panelColor = new Color(230, 240, 250);      // Soft Blue Gray

        // Main Panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(panelColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel title = new JLabel("Currency Converter", JLabel.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 26));
        title.setForeground(primaryColor);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        // From Currency
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel fromLabel = new JLabel("From Currency:");
        fromLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        fromLabel.setForeground(secondaryColor);
        panel.add(fromLabel, gbc);

        fromCurrency = new JComboBox<>(new String[]{ "INR", "USD", "EUR", "GBP", "JPY", "CAD"});
        fromCurrency.setFont(new Font("Tahoma", Font.PLAIN, 14));
        fromCurrency.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(fromCurrency, gbc);

        // To Currency
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel toLabel = new JLabel("To Currency:");
        toLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        toLabel.setForeground(secondaryColor);
        panel.add(toLabel, gbc);

        toCurrency = new JComboBox<>(new String[]{"USD", "INR", "EUR", "GBP", "JPY", "CAD"});
        toCurrency.setFont(new Font("Tahoma", Font.PLAIN, 14));
        toCurrency.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(toCurrency, gbc);

        // Amount Input
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        amountLabel.setForeground(secondaryColor);
        panel.add(amountLabel, gbc);

        amountField = new JTextField();
        amountField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(amountField, gbc);

        // Convert Button
        convertButton = new JButton("Convert");
        convertButton.setFont(new Font("Arial", Font.BOLD, 15));
        convertButton.setBackground(primaryColor);
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(convertButton, gbc);

        // Converted Amount
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel resultLabel = new JLabel("Converted Amount:");
        resultLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        resultLabel.setForeground(secondaryColor);
        panel.add(resultLabel, gbc);

        resultField = new JTextField();
        resultField.setEditable(false);
        resultField.setFont(new Font("Tahoma", Font.BOLD, 14));
        resultField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(resultField, gbc);

        // History Button
        historyButton = new JButton("Show History");
        historyButton.setFont(new Font("Arial", Font.BOLD, 15));
        historyButton.setBackground(accentColor);
        historyButton.setForeground(Color.BLACK);
        historyButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(historyButton, gbc);

        // Table for Conversion History
        tableModel = new DefaultTableModel(new String[]{"From", "To", "Amount", "Converted", "Timestamp"}, 0);
        historyTable = new JTable(tableModel);
        historyTable.setFont(new Font("Tahoma", Font.PLAIN, 13));
        historyTable.setRowHeight(22);
        historyTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        historyTable.setBackground(Color.WHITE);
        historyTable.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(primaryColor), "Conversion History",
                0, 0, new Font("Arial", Font.BOLD, 14), primaryColor));
        scrollPane.getViewport().setBackground(backgroundColor);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button Click Event for Conversion
        convertButton.addActionListener(e -> convertCurrency());

        // Button Click Event for History
        historyButton.addActionListener(e -> fetchHistory());

        setVisible(true);
    }


    private void convertCurrency() {
        try {
            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();
            String amount = amountField.getText();

            // API Call
            String urlStr = "http://localhost:8080/api/convert?from=" + from + "&to=" + to + "&amount=" + amount;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            reader.close();

            resultField.setText(response);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Conversion Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchHistory() {
        try {
            String urlStr = "http://localhost:8080/api/history";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            reader.close();

            // Parse JSON response
            JSONArray jsonArray = new JSONArray(response);
            tableModel.setRowCount(0); // Clear table

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                tableModel.addRow(new Object[]{
                        obj.getString("fromCurrency"),
                        obj.getString("toCurrency"),
                        obj.getDouble("amount"),
                        obj.getDouble("convertedAmount"),
                        obj.getString("timestamp")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching history: " + ex.getMessage(), "History Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CurrencyConverterGUI::new);
    }
}

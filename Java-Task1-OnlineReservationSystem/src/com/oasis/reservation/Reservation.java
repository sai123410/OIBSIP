package com.oasis.reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Reservation extends JFrame {

    JTextField passengerField;
    JTextField trainNumberField;
    JTextField trainNameField;
    JComboBox<String> classBox;
    JTextField dateField;
    JTextField sourceField;
    JTextField destinationField;

    public Reservation() {

        setTitle("Online Reservation System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

        passengerField = new JTextField();
        trainNumberField = new JTextField();
        trainNameField = new JTextField();

        classBox = new JComboBox<>(
                new String[]{"Sleeper", "AC", "First Class"}
        );

        dateField = new JTextField();
        sourceField = new JTextField();
        destinationField = new JTextField();

        JButton bookButton = new JButton("Book Ticket");
        JButton cancelButton = new JButton("Cancel Booking");

        panel.add(new JLabel("Passenger Name:"));
        panel.add(passengerField);

        panel.add(new JLabel("Train Number:"));
        panel.add(trainNumberField);

        panel.add(new JLabel("Train Name:"));
        panel.add(trainNameField);

        panel.add(new JLabel("Class Type:"));
        panel.add(classBox);

        panel.add(new JLabel("Journey Date (yyyy-MM-dd):"));
        panel.add(dateField);

        panel.add(new JLabel("Source Station:"));
        panel.add(sourceField);

        panel.add(new JLabel("Destination Station:"));
        panel.add(destinationField);

        panel.add(bookButton);
        panel.add(cancelButton);

        add(panel);

        bookButton.addActionListener(e -> bookTicket());

        cancelButton.addActionListener(e -> {
            new Cancellation().setVisible(true);
            dispose();
        });
    }

    private void bookTicket() {

        String passengerName = passengerField.getText();
        String trainNumberText = trainNumberField.getText();
        String trainName = trainNameField.getText();
        String classType = (String) classBox.getSelectedItem();
        String journeyDate = dateField.getText();
        String source = sourceField.getText();
        String destination = destinationField.getText();

        // Basic validation
        if (passengerName.isEmpty() ||
            trainNumberText.isEmpty() ||
            trainName.isEmpty() ||
            journeyDate.isEmpty() ||
            source.isEmpty() ||
            destination.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all required fields."
            );
            return;
        }

        int trainNumber;

        try {
            trainNumber = Integer.parseInt(trainNumberText);
        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Train number must be numeric."
            );
            return;
        }

        // Validate date
        try {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd");

            sdf.setLenient(false);
            sdf.parse(journeyDate);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid date. Use yyyy-MM-dd."
            );
            return;
        }

        // Generate PNR
        String pnr = "PNR" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        String sql =
                "INSERT INTO reservations " +
                "(pnr, passenger_name, train_number, train_name, " +
                "class_type, journey_date, source_station, destination_station) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con =
                     DatabaseConnection.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, pnr);
            ps.setString(2, passengerName);
            ps.setInt(3, trainNumber);
            ps.setString(4, trainName);
            ps.setString(5, classType);
            ps.setString(6, journeyDate);
            ps.setString(7, source);
            ps.setString(8, destination);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Reservation Successful!\n\n" +
                    "PNR: " + pnr + "\n" +
                    "Passenger: " + passengerName + "\n" +
                    "Train: " + trainName + "\n" +
                    "Journey Date: " + journeyDate + "\n" +
                    "From: " + source + "\n" +
                    "To: " + destination
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Reservation failed!"
            );

            e.printStackTrace();
        }
    }
}
